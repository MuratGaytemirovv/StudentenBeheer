package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.Course;
import org.example.domain.Gender;
import org.example.domain.Student;
import org.example.repository.CourseRepository;
import org.example.repository.StudentRepository;
import org.example.request.StudentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private final String baseUrl = "/students";

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Course> courses;
    private List<Student> students;

    @BeforeEach
    void setUp() {
        students = new ArrayList<>();
        students.add(new Student("John", "Johnson", LocalDate.now().minusMonths(3).minusYears(20).minusDays(1), Gender.M));
        students.add(new Student("Mark", "Michaelson", LocalDate.now().minusMonths(9).minusYears(18).minusDays(27), Gender.M));
        students.add(new Student("Mei", "Cheng", LocalDate.now().minusMonths(1).minusYears(23).minusDays(3), Gender.V));

        courses = new ArrayList<>();
        courses.add(new Course("Backend Programming", 3));
        courses.add(new Course("iOS", 3));
        courses.add(new Course("Android", 2));
        courses.add(new Course("Database concepts", 1));
        courses.add(new Course("Analyse and design", 2));

        Student john = students.get(0);
        for (Course course : courses.subList(0, 2)) {
            john.addCourse(course);
        }
    }

    @Test
    void findAllCourses() throws Exception {
        Page page = new PageImpl(students);
        PageRequest of = PageRequest.of(0, 20);
        when(studentRepository.findAll(of)).thenReturn(page);

        mvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].name", equalTo("Johnson John")))
                .andExpect(jsonPath("$.content[1].name", equalTo("Michaelson Mark")))
                .andExpect(jsonPath("$.content[2].name", equalTo("Cheng Mei")));

    }

    @Test
    void findById() throws Exception {
        Student john = students.get(0);
        when(studentRepository.findById(22L)).thenReturn(Optional.of(john));
        mvc.perform(get(baseUrl+"/22"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Johnson John")))
                .andExpect(jsonPath("$.birthDate", equalTo("2002-09-06")))
                .andExpect(jsonPath("$.gender", equalTo("M")));
    }

    @Test
    void findByIdNotFound() throws Exception {
        when(studentRepository.findById(2247L)).thenReturn(Optional.empty());
        mvc.perform(get(baseUrl+"/2247"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



    @Test
    void createStudent() throws Exception {
        Student jill = new Student("Jill", "Jackson", LocalDate.of(1995, 4, 17), Gender.V);
        jill.setId(96L);
        when(studentRepository.save(any(Student.class))).thenReturn(jill);

        StudentRequest request = new StudentRequest();
        request.setFirstName("Jill");
        request.setLastName("Jackson");
        request.setGender("V");
        request.setGsmNumber("77445521");
        request.setBirthDate(LocalDate.of(1995, 4, 17));

        mvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost/students/96"));
    }

    @Test
    void createStudentWithValidationError() throws Exception {

        StudentRequest request = new StudentRequest();
        request.setGender("Female");
        request.setBirthDate(LocalDate.of(1990, 4, 17));

        mvc.perform(post(baseUrl)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentRepository);

    }


    @Test
    void updateStudent() throws Exception {
        Long studentId = 96L;

        Student jill = new Student("Jill", "Jackson", LocalDate.of(1995, 4, 17), Gender.V);
        jill.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(jill));


        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArguments()[0]);

        StudentRequest request = new StudentRequest();
        request.setFirstName("Jill");
        request.setLastName("Jackson");
        request.setGender("V");
        request.setGsmNumber("77445521");
        request.setBirthDate(LocalDate.of(1995, 4, 17));

        mvc.perform(put(baseUrl+"/"+studentId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudentNotFound() throws Exception {
        Long studentId = 961L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        verifyNoMoreInteractions(studentRepository);

        StudentRequest request = new StudentRequest();
        request.setFirstName("Jill");
        request.setLastName("Jackson");
        request.setGender("V");
        request.setGsmNumber("77445521");
        request.setBirthDate(LocalDate.of(1995, 4, 17));

        mvc.perform(put(baseUrl+"/"+studentId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }


    @Test
    void updateStudentWithValidationErrors() throws Exception {
        Long studentId = 96L;

        StudentRequest request = new StudentRequest();
        request.setGender("Female");

        mvc.perform(put(baseUrl+"/"+studentId)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentRepository);
    }

    @Test
    void deleteStudent() throws Exception {
        Long studentId = 96L;

        Student jill = new Student("Jill", "Jackson", LocalDate.of(1995, 4, 17), Gender.V);
        jill.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(jill));

        mvc.perform(delete(baseUrl+"/"+studentId))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteStudentNotFound() throws Exception {
        Long studentId = 96L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        mvc.perform(delete(baseUrl+"/"+studentId))
                .andExpect(status().isNotFound());

    }


}
