package br.com.alura.school.enrollment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alura.school.support.validation.ExistsEntity;

class NewEnrollmentRequest {

//	@UniqueEnrollment(entity = Enrollment.class, field = "username",pathVarName =
//	"courseCode", column = "course_code")
	@ExistsEntity(classpath = "user.User", field = "username")
	@Size(max = 20)
	@NotBlank
	@JsonProperty
	private String username;

	public NewEnrollmentRequest() {
	}

	NewEnrollmentRequest(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	Enrollment toEntity(String code) {
		return new Enrollment(this.username, code);
	}

}
