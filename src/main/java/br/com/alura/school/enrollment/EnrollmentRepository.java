package br.com.alura.school.enrollment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

	Optional<Enrollment> findByIdUsernameAndIdCourseCode(String username, String courseCode);

}
