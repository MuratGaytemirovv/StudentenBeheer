package org.example.repository;

import org.example.domain.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void simpleCrud(){
        Course backend = courseRepository.save(new Course("Backend Programming", 2));
        assertThat(backend.getId()).isNotNull();
        assertThat(courseRepository.findAll().size()).isEqualTo(1);

        assertThat(backend.getName()).isEqualTo("Backend Programming");
        assertThat(backend.getPhase()).isEqualTo(2);


        backend.setName("Java Backend Programming");
        backend.setPhase(3);
        backend = courseRepository.save(backend);

        assertThat(backend.getName()).isEqualTo("Java Backend Programming");
        assertThat(backend.getPhase()).isEqualTo(3);


        Course ios = courseRepository.save(new Course("iOS", 3));
        assertThat(ios.getId()).isNotNull();
        assertThat(courseRepository.findAll().size()).isEqualTo(2);

        Course courseWithId = courseRepository.findById(ios.getId()).get();
        assertThat(courseWithId.getId()).isEqualTo(ios.getId());
        assertThat(courseWithId.getName()).isEqualTo(ios.getName());

        courseRepository.delete(courseWithId);
        assertThat(courseRepository.findAll().size()).isEqualTo(1);
        assertThat(courseRepository.findById(ios.getId()).isEmpty()).isTrue();

    }

    @Test
    void findByName() {
        courseRepository.save(new Course("Backend Programming", 3));
        courseRepository.save(new Course("iOS", 3));
        courseRepository.save(new Course("Android", 2));

        assertThat(courseRepository.findAll().size()).isEqualTo(3);

        Course backend = courseRepository.findByName("Backend Programming").get();
        assertThat(backend.getName()).isEqualTo("Backend Programming");

        Course ios = courseRepository.findByName("iOS").get();
        assertThat(ios.getName()).isEqualTo("iOS");

        assertThat(courseRepository.findByName("----").isEmpty()).isTrue();
    }
}
