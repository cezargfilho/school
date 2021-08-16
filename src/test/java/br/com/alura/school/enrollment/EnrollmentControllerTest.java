package br.com.alura.school.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EnrollmentControllerTest {
	
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
	private MockMvc mockMvc;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Test
    void should_add_new_enrollment() throws Exception {
    	NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("alex");
		
    	 mockMvc.perform(post("/courses/java-1/enroll")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                 .andExpect(status().isCreated());
    }
    
    @Test
    void should_validate_bad_enroll_request_course_code_not_found() throws Exception {
    	NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("alex");
    	
    	mockMvc.perform(post("/courses/java-555/enroll")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
				.andExpect(status().isBadRequest());
    }
    
    @Test
    void should_validate_bad_enroll_request_user_not_found() throws Exception {
    	NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("paulo");
    	
    	mockMvc.perform(post("/courses/java-1/enroll")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
				.andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_duplication_of_enrollment() throws Exception {
    	enrollmentRepository.save(new Enrollment("alex", "java-1"));

		NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("alex");

    	mockMvc.perform(post("/courses/java-1/enroll")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
				.andExpect(status().isBadRequest());
    }
    
    @Test
    void should_list_report_from_users_enrollment() throws Exception {
    	enrollmentRepository.save(new Enrollment("alex", "java-1"));
    	enrollmentRepository.save(new Enrollment("alex", "java-2"));
    	enrollmentRepository.save(new Enrollment("ana", "java-1"));
    	
    	mockMvc.perform(get("/courses/enroll/report")
    			.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$.length()", is(2)))
        		.andExpect(jsonPath("$[0].email", is("alex@email.com")))
        		.andExpect(jsonPath("$[0].quantidade_matriculas", is(2)))
        		.andExpect(jsonPath("$[1].email", is("ana@email.com")))
        		.andExpect(jsonPath("$[1].quantidade_matriculas", is(1)));
    }
    
    @Test
    void no_content_when_users_reports_is_empty() throws Exception{
    	 mockMvc.perform(get("/courses/enroll/report")
                 .accept(MediaType.APPLICATION_JSON))
                 .andExpect(status().isNoContent());
    }
}
