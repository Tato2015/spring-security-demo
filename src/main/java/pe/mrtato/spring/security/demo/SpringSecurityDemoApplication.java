package pe.mrtato.spring.security.demo;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pe.mrtato.spring.security.demo.model.entity.PermissionEntity;
import pe.mrtato.spring.security.demo.model.entity.RoleEntity;
import pe.mrtato.spring.security.demo.model.entity.UserEntity;
import pe.mrtato.spring.security.demo.model.enums.RoleEnum;
import pe.mrtato.spring.security.demo.repository.UserRepository;

@SpringBootApplication
public class SpringSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		
		return args -> {
			//CREATE PERMISSION
			PermissionEntity createPermission = PermissionEntity.builder()
					.name("CREATE")
					.build();
			PermissionEntity readPermission = PermissionEntity.builder()
					.name("READ")
					.build();
			PermissionEntity updatePermission = PermissionEntity.builder()
					.name("UPDATE")
					.build();
			PermissionEntity deletePermission = PermissionEntity.builder()
					.name("DELETE")
					.build();
			PermissionEntity refactorPermission = PermissionEntity.builder()
					.name("REFACTOR")
					.build();
			
			//CREATE ROLES
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissions(Set.of(createPermission,readPermission,updatePermission,deletePermission))
					.build();
			RoleEntity roleUser = RoleEntity.builder()
					.roleEnum(RoleEnum.USER)
					.permissions(Set.of(createPermission,readPermission))
					.build();
			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissions(Set.of(readPermission))
					.build();
			RoleEntity roleDeveloper = RoleEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissions(Set.of(createPermission,readPermission,updatePermission,deletePermission,refactorPermission))
					.build();
			
			//CREATE USERS
			UserEntity userGeraldo = UserEntity.builder()
					.username("Geraldo")
					.password("$2a$10$ikWEiax1G0kX3pUSppaqdO70dKwhnGPgDKA4cBHmB2uvQsJDgtGAK")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();
			
			UserEntity userFelipe = UserEntity.builder()
					.username("Felipe")
					.password("$2a$10$ikWEiax1G0kX3pUSppaqdO70dKwhnGPgDKA4cBHmB2uvQsJDgtGAK")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleUser))
					.build();
			
			UserEntity userJose = UserEntity.builder()
					.username("Jose")
					.password("$2a$10$ikWEiax1G0kX3pUSppaqdO70dKwhnGPgDKA4cBHmB2uvQsJDgtGAK")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();
			
			UserEntity userRenzo = UserEntity.builder()
					.username("Renzo")
					.password("$2a$10$ikWEiax1G0kX3pUSppaqdO70dKwhnGPgDKA4cBHmB2uvQsJDgtGAK")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();
			
			userRepository.saveAll(List.of(userGeraldo,userFelipe,userJose,userRenzo));
		};
		
	}

}
