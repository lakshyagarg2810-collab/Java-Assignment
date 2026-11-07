package com.kiet.hostelmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HostelStudent")
public class HostelStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String studentName;
    private String rollNumber;
    private String hostelName;
    private String roomNumber;
    private double fee;

    public HostelStudent() {
    }

    public HostelStudent(String studentName, String rollNumber,
                         String hostelName, String roomNumber,
                         double fee) {

        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.hostelName = hostelName;
        this.roomNumber = roomNumber;
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}