package org.example.response;

import org.example.domain.Course;
public class CourseResponse {

    private Long id;
    private String name;
    private int phase;

    public CourseResponse(Course course){
        this.id = course.getId();
        this.name = course.getName();
        this.phase = course.getPhase();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPhase() {
        return phase;
    }
}
