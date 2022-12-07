package org.example.response;


import org.example.domain.Student;
import java.time.LocalDate;

public class StudentResponse {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private String gender;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getLastName() + " " + student.getFirstName();
        this.birthDate = student.getBirthDate();
        this.gender = student.getGender().name();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

}
