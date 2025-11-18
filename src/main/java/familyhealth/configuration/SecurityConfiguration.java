package familyhealth.configuration;

import familyhealth.common.UserType;
import familyhealth.component.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {



    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            // User: login, register, get
                            .requestMatchers("/api/v1/users/**").permitAll()
                            // Role
                            .requestMatchers("/api/v1/roles/**").hasRole(UserType.ADMIN.getValue())
                            // Member
                            .requestMatchers(POST, "/api/v1/members/**").hasAnyRole(UserType.ADMIN.getValue(),
                                    UserType.PATIENT_HOUSEHOLD.getValue())
                            // Doctor
                            .requestMatchers(POST, "/api/v1/doctors/**").hasRole(UserType.ADMIN.getValue())
                            .requestMatchers(DELETE, "/api/v1/doctors/**").hasRole(UserType.ADMIN.getValue())
                            .requestMatchers(PUT, "/api/v1/doctors/**").hasAnyRole(UserType.ADMIN.getValue(),
                                    UserType.DOCTOR.getValue())
                            // Appointment
                            .requestMatchers(POST, "/api/v1/appointments/**").hasAnyRole(UserType.ADMIN.getValue(),
                                    UserType.PATIENT.getValue(),
                                    UserType.PATIENT_HOUSEHOLD.getValue())
                            // Medical results
                            .requestMatchers(PUT, "/api/v1/medicalresults/**").hasRole(UserType.ADMIN.getValue())
                            .requestMatchers(DELETE, "/api/v1/medicalresults/**").hasRole(UserType.ADMIN.getValue())
                            // Household
                            .requestMatchers(POST, "/api/v1/households/**").hasAnyRole(UserType.ADMIN.getValue(),
                                    UserType.PATIENT_HOUSEHOLD.getValue())
                            .requestMatchers(PUT, "/api/v1/households/**").hasAnyRole(UserType.ADMIN.getValue(),
                                    UserType.PATIENT_HOUSEHOLD.getValue())
                            .requestMatchers(DELETE, "/api/v1/households/**").hasAnyRole(UserType.ADMIN.getValue())
                            // Any other
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}