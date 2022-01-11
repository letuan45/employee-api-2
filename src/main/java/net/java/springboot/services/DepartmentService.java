package net.java.springboot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.java.springboot.models.Department;
import net.java.springboot.models.Employee;
import net.java.springboot.projectException.ResourceNotFoundException;
import net.java.springboot.repositories.DepartmentRepository;
import net.java.springboot.repositories.EmployeeRepository;

@Service
public class DepartmentService {
	//Variables 
	double factor = 1.5;
			
	@Autowired
	private DepartmentRepository departmentRepository;
		
	@Autowired
	private EmployeeRepository employeeRepository;
	
	//Get all Departments
	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}
		
	//Get Department by ID, throw an exception if could not find Department with given id
	public Department getDepartmentById(long id) {
		Department departmentFound = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not exist with id: " + id));
		return departmentFound;
	}
	
	//Add department to repository
	public Department createDepartment(Department department) {
		if(department.getMaxEmployees() <= 0) {
			throw new IllegalArgumentException("maxEmployee must > 0");
		}
		//Unique department.name
		if(departmentRepository.findByName(department.getName()) != null) {
			throw new IllegalArgumentException("This department's name have already exist");
		}
		return departmentRepository.save(department);
	}
	
	//Update department with id, throw an exception if could not find Department with given id
	public Department updateDepartment(Long id, Department departmentDetails) {
		Department departmentFound = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not exist with id: " + id));
		//Only allow user set Max employee more than old Max employee
		if(departmentDetails.getMaxEmployees() < departmentFound.getMaxEmployees()) {
			throw new IllegalArgumentException("Can not set maxEmployee fewer than old maxEmployee");
		}
		//Unique department.name
		if(!departmentFound.getName().equals(departmentDetails.getName()) && departmentRepository.findByName(departmentDetails.getName()) != null) {
			throw new IllegalArgumentException("This department's name have already exist");
		}
		
		departmentFound.setName(departmentDetails.getName());
		departmentFound.setMaxEmployees(departmentDetails.getMaxEmployees());
		
		departmentRepository.save(departmentFound);
		return departmentFound;
	}
	
	//Delete Depart, throw an exception if could not find Department with given id
	public String deleteDepartment(Long id) {
		Department departmentFound = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not exist with id: " + id));
		List<Employee> employees = departmentFound.getEmployee();
		List<Employee> employeesNew = new ArrayList<Employee>();
		//Add employee to Temporary List
		for(int i = 0; i < employees.size(); i++) {
			Employee employeeTemp = new Employee(employees.get(i));
			employeeTemp.setDepart(0);
			employeesNew.add(employeeTemp);
		}
		//Add Manager as Staff
		if(departmentFound.getManager() != null) {
			Employee employeeTemp = new Employee(departmentFound.getManager());
			employeeTemp.setDepart(0);
			employeeTemp.setToStaff(factor);
			employeesNew.add(employeeTemp);
		}
		//Add temporary List to repository
		departmentRepository.delete(departmentFound);
		for(int i = 0; i < employeesNew.size(); i++) { //This for add Employees to repository
			employeeRepository.save(employeesNew.get(i));
		}
		
		String responseMessage = "deleted Department with id: " + departmentFound.getId();
		return responseMessage;
	}
	
	//Delete all Departments (Use for testing)
	public String deleteAllDepartment() {
		departmentRepository.deleteAll();
			
		String responseMessage = "deleted all departments";
		return responseMessage;
	}
}
