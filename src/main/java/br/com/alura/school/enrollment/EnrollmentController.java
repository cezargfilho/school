package br.com.alura.school.enrollment;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.UserReportResponse;

@RestController
class EnrollmentController {

	private EnrollmentRepository enrollmentRepository;
	private CourseRepository courseRepository;

	EnrollmentController(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
		this.enrollmentRepository = enrollmentRepository;
		this.courseRepository = courseRepository;
	}

	@PostMapping("/courses/{courseCode}/enroll")
	ResponseEntity<Void> newEnrollment(@PathVariable("courseCode") String courseCode,
			@RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {
		
		Optional<?> course = courseRepository.findByCode(courseCode);
		Optional<Enrollment> enrollment = enrollmentRepository.findByIdUsernameAndIdCourseCode(newEnrollmentRequest.getUsername(), courseCode);
		
		if (course.isPresent() && !enrollment.isPresent()) {
			enrollmentRepository.save(newEnrollmentRequest.toEntity(courseCode));
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/courses/enroll/report")
	ResponseEntity<List<UserReportResponse>> newEnrollment() {
		List<UserReportResponse> usersReports = enrollmentRepository.countTotalEnrollmentsByUser();
		if (!usersReports.isEmpty()) {
			return ResponseEntity.ok(usersReports);
		}
		return ResponseEntity.noContent().build();
	}
}
