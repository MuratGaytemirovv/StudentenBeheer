package org.example.response;


import org.example.domain.Student;
import org.example.domain.Course;

import java.util.List;
import java.util.stream.Collectors;

public class StudentWithCoursesResponse extends StudentResponse {

    private List<Course> courses;

    public StudentWithCoursesResponse(Student student) {
        super(student);
        this.courses = student.getCourses();
    }

    public List<CourseResponse> getCourses() {
        return courses.stream().map(CourseResponse::new).collect(Collectors.toList());
    }
}
