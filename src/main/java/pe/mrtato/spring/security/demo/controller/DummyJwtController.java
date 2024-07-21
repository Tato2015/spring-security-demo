package pe.mrtato.spring.security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.mrtato.spring.security.demo.dto.AuthCreateUserRequest;
import pe.mrtato.spring.security.demo.dto.AuthLoginRequest;
import pe.mrtato.spring.security.demo.dto.AuthResponse;
import pe.mrtato.spring.security.demo.service.UserDetailServiceImpl;

@RestController
@RequestMapping("/jwt")
//@PreAuthorize("denyAll()")
public class DummyJwtController {
	
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@PostMapping("/sign-up")
	public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest authCreateUser){
		return new ResponseEntity<>(this.userDetailServiceImpl.createUser(authCreateUser), HttpStatus.CREATED);
	}
	
	
	@PostMapping("/log-in")
	public ResponseEntity<AuthResponse> login( @RequestBody @Valid AuthLoginRequest userRequest){
		return new ResponseEntity<>(userDetailServiceImpl.loginUser(userRequest),HttpStatus.OK);
	}
	
	
	@GetMapping("/get")
	public String getSomething() {
		return "Hello world - GET with JWT Token";
	}
	
	@PostMapping("/post")
	public String postMapping() {
		return "Hello world - POST with JWT Token";
	}

}
