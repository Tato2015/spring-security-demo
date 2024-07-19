package pe.mrtato.spring.security.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.mrtato.spring.security.demo.model.entity.UserEntity;
import pe.mrtato.spring.security.demo.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
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
	
	
	

}
