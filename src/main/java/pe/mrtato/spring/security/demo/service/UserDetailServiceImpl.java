package pe.mrtato.spring.security.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.mrtato.spring.security.demo.dto.AuthCreateUserRequest;
import pe.mrtato.spring.security.demo.dto.AuthLoginRequest;
import pe.mrtato.spring.security.demo.dto.AuthResponse;
import pe.mrtato.spring.security.demo.model.entity.RoleEntity;
import pe.mrtato.spring.security.demo.model.entity.UserEntity;
import pe.mrtato.spring.security.demo.repository.RoleRepository;
import pe.mrtato.spring.security.demo.repository.UserRepository;
import pe.mrtato.spring.security.demo.util.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findUserEntityByUsername(username)
				.orElseThrow( () -> new UsernameNotFoundException("User : " + username + " does not exist") );
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		userEntity.getRoles()
			.forEach( role -> authorities.add( new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())) ) );
		
		userEntity.getRoles().stream()
			.flatMap( role -> role.getPermissions().stream() )
			.forEach( permission -> authorities.add( new SimpleGrantedAuthority(permission.getName()) ) );
		
		return new User(userEntity.getUsername(),
					                userEntity.getPassword(),
					                userEntity.isEnabled(),
					                userEntity.isAccountNoExpired(),
					                userEntity.isCredentialNoExpired(),
					                userEntity.isAccountNoLocked(),
					                authorities);
	}
	
	
	public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
		String username = authLoginRequest.username();
		String password = authLoginRequest.password();
		
		Authentication authentication = this.authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtUtils.createToken(authentication);
		AuthResponse authResponse = new AuthResponse(username, "User loged successfully", accessToken, true);
		return authResponse;
	}

	
	public Authentication authenticate(String username,String password) {
		UserDetails userDetails = this.loadUserByUsername(username);
		
		if( userDetails == null ) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		
		if( !passwordEncoder.matches(password, userDetails.getPassword()) ) {
			throw new BadCredentialsException("Invalid password");
		}
		
		return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword() , userDetails.getAuthorities());
		
	}

	public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
		String username = authCreateUserRequest.username();
		String password = authCreateUserRequest.password();
		List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();
		
		
		Set<RoleEntity> roleEnitySet =  roleRepository.findRoleEntiesByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet());
		if( roleEnitySet.isEmpty() ) {
			throw new IllegalArgumentException("The roles specified does not exist.");
		}
		
		UserEntity userEntity = UserEntity.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.roles(roleEnitySet)
				.isEnabled(true)
				.accountNoLocked(true)
				.accountNoExpired(true)
				.credentialNoExpired(true)
				.build();
		UserEntity userCreated = userRepository.save(userEntity);
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
		
		userCreated.getRoles()
			.stream()
			.flatMap( role -> role.getPermissions().stream() )
			.forEach( permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName()))  );
		
		//SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userCreated.getPassword(),authorityList);
		
		String accessToken = jwtUtils.createToken(authentication);
		AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "User created successfully",accessToken,true);
		
		return authResponse;
	
	}
	
	
	
}
