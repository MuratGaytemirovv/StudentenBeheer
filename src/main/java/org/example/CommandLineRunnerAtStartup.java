package org.example;

import org.example.domain.Gender;
import org.example.domain.Student;
import org.example.domain.Course;
import org.example.repository.CourseRepository;
import org.example.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component

public class CommandLineRunnerAtStartup implements CommandLineRunner {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public CommandLineRunnerAtStartup(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        Student john = studentRepository.save(new Student("John", "Johnson", LocalDate.now().minusMonths(3).minusYears(20).minusDays(13), Gender.M));
        Student mark = studentRepository.save(new Student("Mark", "Michaelson", LocalDate.now().minusMonths(9).minusYears(18).minusDays(27), Gender.M));
        Student mei = studentRepository.save(new Student("Mei", "Cheng", LocalDate.now().minusMonths(1).minusYears(23).minusDays(3), Gender.V));

        courseRepository.save(new Course("Backend Programming", 3));
        courseRepository.save(new Course("iOS", 3));
        courseRepository.save(new Course("Android", 2));
        courseRepository.save(new Course("Ethical Hacking", 2));

        Optional<Course> iOS = courseRepository.findByName("iOS");
        Optional<Course> android = courseRepository.findByName("Android");
        Optional<Course> hacking = courseRepository.findByName("Ethical Hacking");
        Optional<Course> backend = courseRepository.findByName("Backend Programming");

        john.addCourse(hacking.get());
        john.addCourse(backend.get());
        mei.addCourse(hacking.get());
        mark.addCourse(iOS.get());
        mark.addCourse(android.get());

        studentRepository.save(john);
        studentRepository.save(mei);
        studentRepository.save(mark);

        courseRepository.save(iOS.get());
        courseRepository.save(android.get());
        courseRepository.save(backend.get());
        courseRepository.save(hacking.get());





    }
}
