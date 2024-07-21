package pe.mrtato.spring.security.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
		@NotBlank String username,
		@NotBlank String password,
		AuthCreateRoleRequest roleRequest
		) {

	
	
	
	
}
