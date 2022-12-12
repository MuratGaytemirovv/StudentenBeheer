package org.example.service;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Page<StudentResponse> findAllStudents(Pageable pageable){
        return studentRepository.findAll(pageable).map(StudentResponse::new);
    }

    public StudentResponse retrieveStudentById(@PathVariable(name = "studentId") Long studentId) {
        return new StudentResponse(studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student")));
    }

    public StudentWithCoursesResponse retrieveStudentByIdWithCourses(@PathVariable(name = "studentId") Long studentId) {
        return new StudentWithCoursesResponse(studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student")));
    }

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

    public StudentResponse patchStudent(@PathVariable("studentId") Long studentId,
                                        @RequestBody StudentRequest studentRequest) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));
        if (studentRequest.getFirstName() != null) {
            student.setFirstName(studentRequest.getFirstName());
        }
        if (studentRequest.getLastName() != null) {
            student.setLastName(studentRequest.getLastName());
        }
        if (studentRequest.getBirthDate() != null) {
            student.setBirthDate(studentRequest.getBirthDate());
        }
        if (studentRequest.getGender() != null) {
            student.setGender(Gender.valueOf(studentRequest.getGender()));
        }
        if (studentRequest.getGsmNumber() != null) {
            student.setGsmNumber(studentRequest.getGsmNumber());
        }

        return new StudentResponse(studentRepository.save(student));
    }

    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));

        try {
            studentRepository.deleteById(studentId);
        } catch (EmptyResultDataAccessException e) {
            // fine
        }
    }

    public void addCourseToStudent(@PathVariable(name = "studentId") Long studentId, @RequestBody @Valid CourseRequest courseRequest){
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));
        Course course = courseRepository.findById(courseRequest.getId()).orElseThrow(() -> new ResourceNotFoundException(courseRequest.getId().toString(), "course"));
        student.addCourse(course);
        studentRepository.save(student);
    }

    public void deleteCourseOfStudent(@PathVariable(name = "studentId") Long studentId,
                                      @PathVariable(name = "courseId") Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException(studentId.toString(), "student"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException(courseId.toString(), "course"));
        student.removeCourse(course);
        studentRepository.save(student);
    }

}
