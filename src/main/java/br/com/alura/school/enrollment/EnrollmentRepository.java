package br.com.alura.school.enrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.school.user.UserReportResponse;

interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

	Optional<Enrollment> findByIdUsernameAndIdCourseCode(String username, String courseCode);
	
	@Query("SELECT new br.com.alura.school.user.UserReportResponse(u.email AS email, COUNT(*) AS amount) "
			+ "FROM Enrollment as e, User as u WHERE u.username = e.id.username "
			+ "GROUP BY u.email ORDER BY amount DESC")
	List<UserReportResponse> countTotalEnrollmentsByUser();

}
