package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.example.domain.Course;


import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

}
