package familyhealth.configuration;

import familyhealth.common.Expertise;
import familyhealth.common.Gender;
import familyhealth.common.Relation;
import familyhealth.common.UserType;
import familyhealth.model.Doctor;
import familyhealth.model.Role;
import familyhealth.model.User;
import familyhealth.repository.DoctorRepository;
import familyhealth.repository.RoleRepository;
import familyhealth.repository.UserRepository;
import familyhealth.service.IDoctorEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final IDoctorEmbeddingService doctorEmbeddingService;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>> START INIT DATABASE");

        initRoles();
        initAdminAccount();
        initSampleDoctors();

        log.info(">>> INIT DATABASE COMPLETED");
    }

    private void initRoles() {
        long count = roleRepository.count();
        if (count > 0) {
            log.info("Roles already initialized.");
            return;
        }

        for (UserType type : UserType.values()) {
            if (!roleRepository.existsByName(type)) {
                Role role = new Role();
                role.setName(type);
                roleRepository.save(role);
                log.info("✔ Created role: {}", type);
            }
        }
    }
    
    private void initAdminAccount() {
        String adminPhone = "0123456789";
        
        if (userRepository.existsByPhone(adminPhone)) {
            log.info("Admin account already exists.");
            return;
        }
        
        Role adminRole = roleRepository.findByName(UserType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        
        User admin = User.builder()
                .phone(adminPhone)
                .password(passwordEncoder.encode("admin123"))
                .role(adminRole)
                .isActive(true)
                .build();
        
        userRepository.save(admin);
        log.info("Created admin account - Phone: {}, Password: admin123", adminPhone);
    }
    
    private void initSampleDoctors() {
        if (doctorRepository.count() > 0) {
            log.info("Doctors already initialized. Re-indexing for vector store...");
            doctorEmbeddingService.indexAllDoctors();
            return;
        }
        
        Role doctorRole = roleRepository.findByName(UserType.DOCTOR)
                .orElseThrow(() -> new RuntimeException("Doctor role not found"));
        
        // Bác sĩ Tim mạch
        createDoctor("0901234501", "Nguyễn Văn Minh", Gender.MALE, "nguyenvanminh@hospital.com",
                Expertise.TIM_MACH, 
                "Chuyên gia tim mạch với 15 năm kinh nghiệm. Chuyên điều trị các bệnh về tim, huyết áp cao, rối loạn nhịp tim, suy tim, đau thắt ngực, nhồi máu cơ tim.",
                doctorRole);
        
        // Bác sĩ Hô hấp
        createDoctor("0901234502", "Trần Thị Lan", Gender.FEMALE, "tranthilan@hospital.com",
                Expertise.HO_HAP,
                "Bác sĩ chuyên khoa hô hấp, điều trị hen suyễn, viêm phổi, viêm phế quản, ho kéo dài, khó thở, COPD, lao phổi và các bệnh về phổi.",
                doctorRole);
        
        // Bác sĩ Tiêu hóa
        createDoctor("0901234503", "Lê Hoàng Nam", Gender.MALE, "lehoangnam@hospital.com",
                Expertise.TIEU_HOA,
                "Chuyên gia tiêu hóa, điều trị đau dạ dày, viêm loét dạ dày, trào ngược dạ dày, đau bụng, tiêu chảy, táo bón, viêm gan, xơ gan.",
                doctorRole);
        
        // Bác sĩ Thần kinh
        createDoctor("0901234504", "Phạm Thị Hương", Gender.FEMALE, "phamthihuong@hospital.com",
                Expertise.THAN_KINH,
                "Bác sĩ thần kinh, chuyên trị đau đầu, chóng mặt, mất ngủ, đau nửa đầu, rối loạn lo âu, trầm cảm, Parkinson, Alzheimer, đột quỵ.",
                doctorRole);
        
        // Bác sĩ Tai mũi họng
        createDoctor("0901234505", "Hoàng Văn Đức", Gender.MALE, "hoangvanduc@hospital.com",
                Expertise.TAI_MUI_HONG,
                "Chuyên khoa tai mũi họng, điều trị viêm họng, viêm amidan, viêm xoang, viêm tai giữa, ù tai, chảy máu cam, nghẹt mũi, đau họng.",
                doctorRole);
        
        // Bác sĩ Mắt
        createDoctor("0901234506", "Vũ Thị Mai", Gender.FEMALE, "vuthimai@hospital.com",
                Expertise.MAT,
                "Bác sĩ nhãn khoa, chuyên trị cận thị, viễn thị, loạn thị, đục thủy tinh thể, tăng nhãn áp, viêm kết mạc, khô mắt, mỏi mắt.",
                doctorRole);
        
        // Bác sĩ Da liễu
        createDoctor("0901234507", "Đỗ Minh Tuấn", Gender.MALE, "dominhtuan@hospital.com",
                Expertise.DA_LIEU,
                "Chuyên gia da liễu, điều trị mụn trứng cá, viêm da, chàm, vẩy nến, nổi mề đay, dị ứng da, nấm da, zona thần kinh.",
                doctorRole);
        
        // Bác sĩ Phụ khoa
        createDoctor("0901234508", "Ngô Thị Thanh", Gender.FEMALE, "ngothithanh@hospital.com",
                Expertise.PHU_KHOA,
                "Bác sĩ phụ khoa, chuyên khám và điều trị các bệnh phụ khoa, viêm nhiễm, rối loạn kinh nguyệt, u xơ tử cung, thai sản.",
                doctorRole);
        
        log.info("✔ Created 8 sample doctors");
        
        // Index all doctors for vector search
        doctorEmbeddingService.indexAllDoctors();
    }
    
    private void createDoctor(String phone, String fullname, Gender gender, String email,
                              Expertise expertise, String bio, Role doctorRole) {
        User user = User.builder()
                .phone(phone)
                .password(passwordEncoder.encode("doctor123"))
                .role(doctorRole)
                .isActive(true)
                .build();
        userRepository.save(user);
        
        Doctor doctor = Doctor.builder()
                .fullname(fullname)
                .gender(gender)
                .email(email)
                .expertise(expertise)
                .bio(bio)
                .user(user)
                .build();
        doctorRepository.save(doctor);
    }

    private String convertToVietnamese(Relation r) {
        return switch (r) {
            case CHU_HO -> "Chủ hộ";
            case VO -> "Vợ";
            case CHONG -> "Chồng";
            case CON -> "Con";
            case BO -> "Bố";
            case ME -> "Mẹ";
        };
    }
}
