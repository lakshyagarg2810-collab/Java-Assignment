package com.kiet.employeemanagement.controller;

import com.kiet.employeemanagement.entity.Employee;
import com.kiet.employeemanagement.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    // (b) Add a new employee
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    // (c) Retrieve all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // (c) Retrieve employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable int id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // (d) Update employee details
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable int id,
            @RequestBody Employee newEmployee) {

        return repository.findById(id)
                .map(employee -> {

                    employee.setName(newEmployee.getName());
                    employee.setDepartment(newEmployee.getDepartment());
                    employee.setSalary(newEmployee.getSalary());

                    Employee updated = repository.save(employee);

                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // (e) Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable int id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);

            return ResponseEntity.ok("Employee deleted successfully!");
        }

        return ResponseEntity.notFound().build();
    }
}