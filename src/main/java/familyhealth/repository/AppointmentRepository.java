package familyhealth.repository;

import familyhealth.common.AppointmentStatus;
import familyhealth.model.Appointment;
import familyhealth.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.member IN :members")
    Page<Appointment> findAllByMembers(@Param("members") List<Member> members , Pageable pageable);

    Page<Appointment> findAllByDoctor_IdAndStatus(
            Long doctorId,
            AppointmentStatus status,
            Pageable pageable
    );

    @Query("""
    SELECT a
    FROM Appointment a
    JOIN FETCH a.medicalResult mr
    WHERE a.status = 'COMPLETED'
""")
    Page<Appointment> findAllCompletedWithMedicalResult(Pageable pageable);


    Page<Appointment> findAllByDoctor_Id(Long doctorId, Pageable pageable);
}
