package br.com.alura.school.enrollment;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.sun.istack.NotNull;

@Entity
public class Enrollment {

	@NotNull
	@EmbeddedId
	private EnrollmentId id;
	
    @Column(name = "enroll_date", nullable = false, unique = false)
	private LocalDate date = LocalDate.now();

	@Deprecated
	protected Enrollment() {}

	Enrollment(String username, String courseCode) {
		this.id = new EnrollmentId(username, courseCode);
	}

	EnrollmentId getId() {
		return id;
	}

	String getUsername() {
		return this.id.getUsername();
	}

	String getCode() {
		return this.id.getCourseCode();
	}
	
	LocalDate getDate() {
		return date;
	}

}
