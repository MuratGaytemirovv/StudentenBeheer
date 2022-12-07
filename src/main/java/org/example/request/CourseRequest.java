package org.example.request;

import javax.validation.constraints.NotNull;

public class CourseRequest {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
