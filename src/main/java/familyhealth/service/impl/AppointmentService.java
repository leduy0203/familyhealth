package familyhealth.service.impl;

import familyhealth.common.AppointmentStatus;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.mapper.AppointmentMapper;
import familyhealth.model.*;
import familyhealth.model.dto.AppointmentDTO;
import familyhealth.model.dto.response.AppointmentResponse;
import familyhealth.model.dto.response.PageResponse;
import familyhealth.repository.AppointmentRepository;
import familyhealth.repository.MemberRepository;
import familyhealth.service.IAppointmentService;
import familyhealth.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService implements IAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final MemberService memberService;
    private final UserService userService;
    private final MemberRepository memberRepository;

    @Override
    public PageResponse<List<AppointmentResponse>> getAllAppointmentService(String[] search, Pageable pageable) {


        String currentPhone = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        User currentUser = userService.getUserByPhone(currentPhone);


        Member currentUserMember = this.memberRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        Long householdId = Optional.ofNullable(currentUserMember.getHousehold())
                .map(Household::getId)
                .orElseThrow(() -> new AppException(ErrorCode.HOUSEHOLD_NOT_FOUND));

        Page<Member> members = this.memberRepository.findActiveMembersByHousehold(householdId , pageable);

        if (members.isEmpty()) {
            return PageResponse.<List<AppointmentResponse>>builder()
                    .meta(new PageResponse.Meta())
                    .result(List.of())
                    .build();
        }

        Page<Appointment> appointments = this.appointmentRepository.findAllByMembers(members.getContent(), pageable);


        PageResponse.Meta meta = new PageResponse.Meta();
        meta.setPage(appointments.getNumber());
        meta.setPageSize(appointments.getSize());
        meta.setPages(appointments.getTotalPages());
        meta.setTotal(appointments.getTotalElements());

        return PageResponse.<List<AppointmentResponse>>builder()
                .meta(meta)
                .result(appointments.getContent().stream()
                        .map(AppointmentMapper::convertToAppointmentResponse).toList())
                .build();
    }


    @Override
    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
    }

    @Override
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Doctor doctor = doctorService.getDoctor(appointmentDTO.getDoctorId());
        Member member = memberService.getMember(appointmentDTO.getMemberId());
        Appointment appointment = AppointmentMapper.convertToAppointment(appointmentDTO, doctor, member);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Appointment appointment = getAppointment(id);
        Appointment updateAppointment = AppointmentMapper.convertToAppointment(appointmentDTO, appointment.getDoctor(), appointment.getMember());
        updateAppointment.setId(appointment.getId());
        return appointmentRepository.save(updateAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointment(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }
}
