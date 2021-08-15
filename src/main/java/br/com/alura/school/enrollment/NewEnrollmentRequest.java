package br.com.alura.school.enrollment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alura.school.support.validation.ExistsEntity;

class NewEnrollmentRequest {

	@ExistsEntity(classpath = "user.User", field = "username")
	@Size(max = 20)
	@NotBlank
	@JsonProperty
	private final String username;
	
	@Deprecated
	NewEnrollmentRequest() {
		this.username = "";}
	
	NewEnrollmentRequest(String username) {
		this.username = username;
	}

	String getUsername() {
		return username;
	}

	Enrollment toEntity(String code) {
		return new Enrollment(this.username, code);
	}

}
