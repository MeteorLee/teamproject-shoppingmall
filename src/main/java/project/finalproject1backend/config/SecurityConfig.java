package project.finalproject1backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import project.finalproject1backend.jwt.JwtAccessDeniedHandler;
import project.finalproject1backend.jwt.JwtAuthenticationEntryPoint;
import project.finalproject1backend.jwt.JwtAuthenticationFilter;
import project.finalproject1backend.jwt.JwtTokenProvider;

@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //    @Bean
//    AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
//        return builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()).and().build();
//    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("----------------configure-------------");
        return http
                .httpBasic().disable()
                .formLogin().disable()
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
//                .requestMatchers("/user/**").authenticated()
//                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                .antMatchers("/", "/signup", "/login","/upload/*","/upload/view/*",
                        "/upload/remove/*","/sendEmail","/sendEmail/confirm","/findUserIdByManagerName","/findUserIdByManagerName/confirm",
                        "/setRandomPassword/confirm","/setRandomPassword","/signup/checkId").permitAll()

                .antMatchers("/account/**").hasAnyRole("STANDBY","USER", "ADMIN")

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //  401 UNAUTHORIZED
                .accessDeniedHandler(jwtAccessDeniedHandler) // 403 FORBIDDEN
                .and().build();
    }

    // 필터에서 제외시킬 url 등록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("-------------web configure-------------");
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        return (web) -> web.ignoring().antMatchers(
//                "/**"                 // 임시로 모든 보안 해제시 셋팅!
                "/api-docs/**",
                "/swagger-ui/**",
                "/test"
        );

    }
}