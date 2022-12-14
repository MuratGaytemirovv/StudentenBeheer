package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/private/students")
@CrossOrigin("*")
public class StudentController {



    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @Operation(summary = "Get all students")
    @GetMapping
    public Page<StudentResponse> findAllStudents(Pageable pageable){
        return studentService.findAllStudents(pageable);
    }

    @Operation(summary = "Get a student by his/her id")
    @GetMapping("/{studentId}")
    public StudentResponse retrieveStudentById(@PathVariable(name = "studentId") Long studentId) {
        return studentService.retrieveStudentById(studentId);
    }

    @Operation(summary = "Get a student and his courses by student's id")
    @GetMapping("/{studentId}/courses")
    public StudentWithCoursesResponse retrieveStudentByIdWithCourses(@PathVariable(name = "studentId") Long studentId) {
        return studentService.retrieveStudentByIdWithCourses(studentId);
    }

    @Operation(summary = "Create new student")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return studentService.createStudent(studentRequest);
    }

    @Operation(summary = "Update student's data")
    @PutMapping("/{studentId}")
    public StudentResponse putStudent(@PathVariable(name = "studentId") Long studentId,
                                      @RequestBody @Valid StudentRequest studentRequest) {
        return studentService.putStudent(studentId,studentRequest);
    }

    @Operation(summary = "Update student's data")
    @PatchMapping("/{studentId}")
    public StudentResponse patchStudent(@PathVariable("studentId") Long studentId,
                                        @RequestBody StudentRequest studentRequest) {
        return studentService.patchStudent(studentId,studentRequest);
    }

    @Operation(summary = "Delete a specific student")
    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
       studentService.deleteStudent(studentId);
    }

    @Operation(summary = "Add a course to a student")
    @PostMapping("/{studentId}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCourseToStudent(@PathVariable(name = "studentId") Long studentId, @RequestBody @Valid CourseRequest courseRequest){
        studentService.addCourseToStudent(studentId,courseRequest);
    }

    @Operation(summary = "Remove course from student's list")
    @DeleteMapping("/{studentId}/courses/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourseOfStudent(@PathVariable(name = "studentId") Long studentId,
                                     @PathVariable(name = "courseId") Long courseId) {
        studentService.deleteCourseOfStudent(studentId,courseId);
    }


}
