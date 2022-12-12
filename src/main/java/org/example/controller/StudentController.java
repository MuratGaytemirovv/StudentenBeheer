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
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StudentController {


    private final StudentService studentService;
    private final CourseRepository courseRepository;

    public StudentController(StudentService studentService, CourseRepository courseRepository) {
        this.studentService = studentService;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public Page<StudentResponse> findAllStudents(Pageable pageable){
        return studentService.findAllStudents(pageable);
    }
    @GetMapping("/{studentId}")
    public StudentResponse retrieveStudentById(@PathVariable(name = "studentId") Long studentId) {
        return studentService.retrieveStudentById(studentId);
    }

    @GetMapping("/{studentId}/courses")
    public StudentWithCoursesResponse retrieveStudentByIdWithCourses(@PathVariable(name = "studentId") Long studentId) {
        return studentService.retrieveStudentByIdWithCourses(studentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return studentService.createStudent(studentRequest);
    }

    @PutMapping("/{studentId}")
    public StudentResponse putStudent(@PathVariable(name = "studentId") Long studentId,
                                      @RequestBody @Valid StudentRequest studentRequest) {
        return studentService.putStudent(studentId,studentRequest);
    }
    @PatchMapping("/{studentId}")
    public StudentResponse patchStudent(@PathVariable("studentId") Long studentId,
                                        @RequestBody StudentRequest studentRequest) {
        return studentService.patchStudent(studentId,studentRequest);
    }
    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
       studentService.deleteStudent(studentId);
    }

    @PostMapping("/{studentId}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCourseToStudent(@PathVariable(name = "studentId") Long studentId, @RequestBody @Valid CourseRequest courseRequest){
        studentService.addCourseToStudent(studentId,courseRequest);
    }


    @DeleteMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourseOfStudent(@PathVariable(name = "studentId") Long studentId,
                                     @PathVariable(name = "courseId") Long courseId) {
        studentService.deleteCourseOfStudent(studentId,courseId);
    }


}
