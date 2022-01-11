package net.java.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.java.springboot.models.Employee;
import net.java.springboot.services.EmployeeService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	
	//Get all employees
	@GetMapping("/employees")
	public List<Employee> getAllEmployee() {
		return employeeService.getAllEmployee();
	}
	
	//Get employee by id
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
		return ResponseEntity.ok(employeeService.getEmployeeById(id));
	}
	
	//Add employee to repository
	@PostMapping("/employees")
	public Employee createEmployee(@Validated @RequestBody Employee employee) {
		return employeeService.createEmployee(employee);
	}
	
	//Add employee to department
	@PutMapping("/addEmployeeToDepart/{departId}/{employeeId}")
	public ResponseEntity<Employee> addEmployee(@PathVariable Long departId,@PathVariable Long employeeId) {
		return ResponseEntity.ok(employeeService.addEmployee(departId, employeeId));
	}
	
	//Remove employee from department
	@PutMapping("/removeEmployeeFromDepart/{departId}/{employeeId}")
	public ResponseEntity<Employee> removeEmployee(@PathVariable Long departId,@PathVariable Long employeeId) {
		return ResponseEntity.ok(employeeService.removeEmployee(departId, employeeId));
	}
	
	//Set an employee to manager
	@PutMapping("/setToManager/{departId}/{employeeId}")
	public ResponseEntity<Employee> setToManager(@PathVariable Long departId,@PathVariable Long employeeId) {
		return ResponseEntity.ok(employeeService.setToManager(departId, employeeId));
	}
	
	//Set a manager to employee
	@PutMapping("/setToStaff/{departId}/{employeeId}")
	public ResponseEntity<Employee> setToStaff(@PathVariable Long departId,@PathVariable Long employeeId) {
		return ResponseEntity.ok(employeeService.setToStaff(departId, employeeId));
	}
	
	//Update employee by id
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Validated @RequestBody Employee employeeDetails) {
		return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDetails));
	}
	
	//Delete an employee by id
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		String responseMessage = employeeService.deleteEmployee(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put(responseMessage , Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	//Delete all employee in repository (Use for testing)
	@DeleteMapping("/employees")
	public ResponseEntity<Map<String, Boolean>> deleteAllEmployee() {
		Map<String, Boolean> response = new HashMap<>();
		String responseMessage = employeeService.deleteAllEmployee();
		response.put(responseMessage , Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}