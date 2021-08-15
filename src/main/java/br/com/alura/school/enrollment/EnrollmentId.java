package br.com.alura.school.enrollment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

@Embeddable
class EnrollmentId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 20)
	@NotNull
	@Column(nullable = false, unique = false)
	private String username;

	@Size(max = 10)
	@NotNull
	@Column(name = "course_code", nullable = false, unique = false)
	private String courseCode;
	
	@Deprecated
	protected EnrollmentId() {	}

	EnrollmentId(String username, String courseCode) {
		this.username = username;
		this.courseCode = courseCode;
	}

	String getUsername() {
		return username;
	}

	String getCourseCode() {
		return courseCode;
	}

}
