package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable long id) {
        String message = String.format("Employee with id: %s was not found", id);
        Employee foundEmployee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
        return ResponseEntity.ok(foundEmployee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getId())
                .toUri();

        return ResponseEntity.created(uri).body(employeeRepository.save(employee));
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
        String message = String.format("Employee with id: %s was not found", id);
        Employee updatedEmployee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));

        updatedEmployee.setFirstName(employee.getFirstName());
        updatedEmployee.setLastName((employee.getLastName()));
        updatedEmployee.setEmail((employee.getEmail()));
        employeeRepository.save(updatedEmployee);

        return  ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable long id) {
        String message = String.format("Employee with id: %s was not found", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
        employeeRepository.deleteById(id);

        return  ResponseEntity.noContent().build();
    }
}
