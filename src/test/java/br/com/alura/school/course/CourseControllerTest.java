package br.com.alura.school.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void should_retrieve_course_by_code() throws Exception {
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

        mockMvc.perform(get("/courses/java-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("java-1")))
                .andExpect(jsonPath("$.name", is("Java OO")))
                .andExpect(jsonPath("$.shortDescription", is("Java and O...")));
    }

    @Test
    void should_retrieve_all_courses() throws Exception {
        courseRepository.save(new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC."));
        courseRepository.save(new Course("spring-2", "Spring Boot", "Spring Boot"));

        mockMvc.perform(get("/courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].code", is("spring-1")))
                .andExpect(jsonPath("$[0].name", is("Spring Basics")))
                .andExpect(jsonPath("$[0].shortDescription", is("Spring Cor...")))
                .andExpect(jsonPath("$[1].code", is("spring-2")))
                .andExpect(jsonPath("$[1].name", is("Spring Boot")))
                .andExpect(jsonPath("$[1].shortDescription", is("Spring Boot")));
    }

    @Test
    void should_add_new_course() throws Exception {
        NewCourseRequest newCourseRequest = new NewCourseRequest("java-2", "Java Collections", "Java Collections: Lists, Sets, Maps and more.");

        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourseRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-2"));
    }
    
    @Test
    void not_found_when_course_code_does_not_exist() throws Exception {
    	mockMvc.perform(get("/courses/java-555")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @ParameterizedTest
    @CsvSource({
    	", Spring Basic, an-course-description",
    	"'', Spring Basic, an-course-description",
    	"'    ', Spring Basic, an-course-description",
    	"spring-1, , an-course-description",
    	"spring-1, '', an-course-description",
    	"spring-1, '    ', an-course-description",
    	"spring-1, Spring-Basic, ",
    	"spring-1, Spring-Basic, ''",
    	"spring-1, Spring-Basic, '    '",
    	"an-long-long-long-course-code, Spring-Basic, an-course-description",
    	"spring-1, an-name-that-is-really-really-big, an-course-description"    	
    })
    void should_validate_bad_user_requests(String code, String name, String desription) throws Exception {
    	NewCourseRequest newCourse = new NewCourseRequest(code, name, desription);
		
		mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourse)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
	void should_not_allow_duplication_of_code() throws Exception {
		courseRepository.save(new Course("spring-1", "Spring Basic", "Spring Core, Spring MVC and more."));
		
		NewCourseRequest newCourse = new NewCourseRequest("spring-1", "Spring Boot", "Spring Core, Spring MVC and more.");
		mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourse)))
                .andDo(print())
                .andExpect(status().isBadRequest());
	}

    @Test
	void should_not_allow_duplication_of_name() throws Exception {
		courseRepository.save(new Course("spring-1", "Spring Basic", "Spring Core, Spring MVC and more."));

		NewCourseRequest newCourse = new NewCourseRequest("spring-2", "Spring Basic", "Spring Core, Spring MVC and more.");
		mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newCourse)))
                .andDo(print())
                .andExpect(status().isBadRequest());
	}

}