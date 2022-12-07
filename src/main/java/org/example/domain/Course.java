package org.example.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;;

@Entity public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int phase;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses")
    private List<Student> students = new ArrayList<>();


    public Course() {
    }

    public Course(String name, int phase) {
        this.name = name;
        this.phase = phase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public List<Student> getLectors() {
        return students;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theory=" +
                '}';
    }
}
