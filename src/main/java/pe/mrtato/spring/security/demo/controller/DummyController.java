package pe.mrtato.spring.security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummy")
@PreAuthorize("denyAll()")
public class DummyController {
	
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('READ')")
	public String getSomething() {
		return "Hello World - GET";
	}
	
	@PostMapping("/post")
	@PreAuthorize("hasAuthority('CREATE')")
	public String postSomething() {
		return "Hello World - POST";
	}
	
	@PutMapping("/put")
	@PreAuthorize("hasAuthority('UPDATE')")
	public String putSomething() {
		return "Hello World - PUT";
	}
	
	@DeleteMapping("/delete")
	@PreAuthorize("hasAuthority('DELETE')")
	public String deleteSomething() {
		return "Hello World - DELETE";
	}

}
