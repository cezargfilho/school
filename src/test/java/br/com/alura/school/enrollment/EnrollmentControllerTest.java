package br.com.alura.school.enrollment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    void should_validate_bad_enroll_request() throws Exception {
    	NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("alex");
    	
    	mockMvc.perform(post("/courses/java-555/enroll")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
				.andExpect(status().isBadRequest());
    }
    
    @Test
    void should_validate_bad_enroll_request_not_found() throws Exception {
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
}
