package org.example.controller;

import org.example.domain.Course;
import org.example.domain.Gender;
import org.example.domain.Student;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.CourseRepository;
import org.example.repository.StudentRepository;
import org.example.request.CourseRequest;
import org.example.request.StudentRequest;
import org.example.response.StudentResponse;
import org.example.response.StudentWithCoursesResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;




@RestController
@RequestMapping("/students")
@CrossOrigin("*")
public class MainController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public MainController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public Page<StudentResponse> findAllStudents(Pageable pageable){
        return studentRepository.findAll(pageable).map(StudentResponse::new);
    }

    @GetMapping("/{studentId}")
    public StudentResponse retrieveStudentById(@PathVariable(name = "studentId") Long studentId) {
        return new StudentResponse(studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student")));
    }

    @GetMapping("/{StudentId}/courses")
    public StudentWithCoursesResponse retrieveStudentByIdWithCourses(@PathVariable(name = "studentId") Long studentId) {
        return new StudentWithCoursesResponse(studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student")));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentRequest studentRequest) {
        Student student = new Student(
                studentRequest.getFirstName(),
                studentRequest.getLastName(),
                studentRequest.getBirthDate(),
                Gender.valueOf(studentRequest.getGender())
        );
        student.setGsmNumber(studentRequest.getGsmNumber());
        Student l = studentRepository.save(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(l.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{studentId}")
    public StudentResponse putStudent(@PathVariable(name = "studentId") Long studentId,
                                    @RequestBody @Valid StudentRequest studentRequest) {
        Student student = studentRepository.findById(Long.parseLong(studentId.toString())).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));

        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setBirthDate(studentRequest.getBirthDate());
        student.setGender(Gender.valueOf(studentRequest.getGender()));
        student.setGsmNumber(studentRequest.getGsmNumber());

        return new StudentResponse(studentRepository.save(student));
    }

    @PatchMapping("/{studentId}")
    public StudentResponse patchStudent(@PathVariable("studentId") Long studentId,
                                      @RequestBody StudentRequest StudentRequest) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));
        if (StudentRequest.getFirstName() != null) {
            student.setFirstName(StudentRequest.getFirstName());
        }
        if (StudentRequest.getLastName() != null) {
            student.setLastName(StudentRequest.getLastName());
        }
        if (StudentRequest.getBirthDate() != null) {
            student.setBirthDate(StudentRequest.getBirthDate());
        }
        if (StudentRequest.getGender() != null) {
            student.setGender(Gender.valueOf(StudentRequest.getGender()));
        }
        if (StudentRequest.getGsmNumber() != null) {
            student.setGsmNumber(StudentRequest.getGsmNumber());
        }

        return new StudentResponse(studentRepository.save(student));
    }

    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));

        try {
            studentRepository.deleteById(studentId);
        } catch (EmptyResultDataAccessException e) {
            // fine
        }
    }

    @PostMapping("/{studentId}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCourseToStudent(@PathVariable(name = "studentId") Long studentId, @RequestBody @Valid CourseRequest courseRequest){
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));
        Course course = courseRepository.findById(courseRequest.getId()).orElseThrow(() -> new ResourceNotFoundException(courseRequest.getId().toString(), "course"));
        student.addCourse(course);
        studentRepository.save(student);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourseOfStudent(@PathVariable(name = "studentId") Long studentId,
                                     @PathVariable(name = "courseId") Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(courseId.toString(), "course"));
        student.removeCourse(course);
        studentRepository.save(student);
    }


}
