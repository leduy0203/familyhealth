package familyhealth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import familyhealth.exception.AppException;
import familyhealth.exception.ErrorCode;
import familyhealth.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceCustomizer implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        log.info("Loading user by phone: {}", phone);
        UserDetails user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("User found: {}, authorities: {}", phone, user.getAuthorities());
        return user;
    }
}
