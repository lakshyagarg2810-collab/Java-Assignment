package com.kiet.hostelmanagement.repository;

import com.kiet.hostelmanagement.entity.HostelStudent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HostelStudentRepository
        extends JpaRepository<HostelStudent, Integer> {

}