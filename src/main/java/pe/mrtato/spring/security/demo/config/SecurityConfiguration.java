package pe.mrtato.spring.security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import pe.mrtato.spring.security.demo.config.filter.JwtTokenValidator;
import pe.mrtato.spring.security.demo.service.UserDetailServiceImpl;
import pe.mrtato.spring.security.demo.util.JwtUtils;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtUtils jwtUtils;
	
	   @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
	        return httpSecurity
	                .csrf(csrf -> csrf.disable())
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(http -> {
	                    // EndPoints publicos
	                    http.requestMatchers(HttpMethod.POST, "/jwt/**").permitAll();

	                    // EndPoints Privados
	                    http.requestMatchers(HttpMethod.GET, "/jwt/get").hasAuthority("READ");
	                    http.requestMatchers(HttpMethod.POST, "/jwt/post").hasAuthority("CREATE");
//	                    http.requestMatchers(HttpMethod.DELETE, "/method/delete").hasAuthority("DELETE");
//	                    http.requestMatchers(HttpMethod.PUT, "/method/put").hasAuthority("UPDATE");

	                    http.anyRequest().denyAll();
	                })
	                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
	                .build();
	    }
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//		return httpSecurity
//				.csrf(csrf -> csrf.disable())
//				.httpBasic(Customizer.withDefaults())
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
//				.build();
//	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailServiceImpl) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailServiceImpl);
		return provider;
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
		
}
