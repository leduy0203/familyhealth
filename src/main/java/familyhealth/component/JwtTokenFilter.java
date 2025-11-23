package familyhealth.component;

import familyhealth.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        //Nếu API được phép đi qua, không cần token
        if(isBypassToken(request)){
            filterChain.doFilter(request, response);
            return;
        }

        //Lấy token từ header
        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        final String token = authHeader.substring(7);

        final String phone = jwtTokenUtil.extractPhone(token);

        if(phone != null && SecurityContextHolder.getContext().getAuthentication() == null){

            User existingUser = (User) userDetailsService.loadUserByUsername(phone);

            if(jwtTokenUtil.validateToken(token,existingUser)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                existingUser,
                                null,
                                existingUser.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    //Những API này sẽ được đi qua
    private boolean isBypassToken(@NotNull HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/api/v1/users/register","POST"),
                Pair.of("/api/v1/users/login","POST"),
                Pair.of("/swagger-ui","GET"),
                Pair.of("/v3/api-docs","GET")

        );
        for(Pair<String, String> bypassToken : bypassTokens){
            if(request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())){
                return true;
            }
        }
        return false;
    }
}

