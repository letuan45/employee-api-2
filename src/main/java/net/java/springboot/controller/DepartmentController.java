package net.java.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.java.springboot.models.Department;
import net.java.springboot.services.DepartmentService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class DepartmentController {	
	@Autowired
	private DepartmentService departmentService;
	
	//Get all Departments
	@GetMapping("/departments")
	public List<Department> getAllDepartment() {
		return departmentService.getAllDepartment();
	}
	
	//Get Department by ID
	@GetMapping("/departments/{id}")
	public ResponseEntity<Department> getDepartmentById(@PathVariable long id) {
		Department departmentFound = departmentService.getDepartmentById(id);
		return ResponseEntity.ok(departmentFound);
	}
	
	//Add department to repository 
	@PostMapping("/departments")
	public Department createDepartment(@RequestBody Department department) {
		return departmentService.createDepartment(department);
	}
	
	//Update department by id
	@PutMapping("/departments/{id}")
	public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
		Department departmentUpdated = departmentService.updateDepartment(id, departmentDetails);
		return ResponseEntity.ok(departmentUpdated);
	}
	
	//Delete Depart by id
	@DeleteMapping("/departments/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteDepartment(@PathVariable Long id) {
		String responseMessage = departmentService.deleteDepartment(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put(responseMessage, Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	//Delete all Departments (Use for testing)
	@DeleteMapping("/deleteAllDepartment")
	public ResponseEntity<Map<String, Boolean>> deleteAllDepartment() {
		String responseMessage = departmentService.deleteAllDepartment();
			
		Map<String, Boolean> response = new HashMap<>();
		response.put(responseMessage, Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}