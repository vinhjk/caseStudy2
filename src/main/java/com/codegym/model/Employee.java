package com.codegym.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String address;
    private String gender;
    private String avatar;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee(){}

    public Employee(String name, LocalDate birthDate, String address, String gender, String avatar, Double salary) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.gender = gender;
        this.avatar = avatar;
        this.salary = salary;
    }

    public Employee(String name, LocalDate birthDate, String address, String gender, String avatar, Double salary, Department department) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.gender = gender;
        this.avatar = avatar;
        this.salary = salary;
        this.department = department;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }



    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", salary=" + salary +
                ", department=" + department +
                '}';
    }
}
