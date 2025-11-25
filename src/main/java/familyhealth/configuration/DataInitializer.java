package familyhealth.configuration;

import familyhealth.common.Relation;
import familyhealth.common.UserType;
import familyhealth.model.Role;
import familyhealth.model.User;
import familyhealth.repository.RoleRepository;
import familyhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info(">>> START INIT DATABASE");

        initRoles();
        initAdminAccount();

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
