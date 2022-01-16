package net.java.springboot.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.java.springboot.models.Department;
import net.java.springboot.models.Employee;
import net.java.springboot.models.Manager;
import net.java.springboot.projectException.ResourceNotFoundException;
import net.java.springboot.repositories.DepartmentRepository;
import net.java.springboot.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	//Variables
	double factor = 1.5;
	private String genderArr[] = new String[] {"male", "female", "other"};
	
	@Autowired
	private EmployeeRepository employeeRepository;
		
	@Autowired
	private DepartmentRepository departmentRepository;
	
	//Check a string element is in String Array element
	boolean checkInArr(String [] arr, String testStr) {
		for(String i : arr) {
			if(i.equals(testStr)) return true;
		}
		return false;
	}
	
	//Get all employees
	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}
	
	public Employee getEmployeeById(long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
		return employee;
	}
	
	//Add employee to repository, *Note that this employee don't work for any department, depart attribute will be default: 0
	public Employee createEmployee(Employee employee) throws IllegalArgumentException {
		if(!checkInArr(genderArr, employee.getGender()))
			throw new IllegalArgumentException("Gender is not valid");
		Optional<Employee> employeeFound = employeeRepository.findById(employee.getId());
		//Check this employee has present in repository
		if(employeeFound.isPresent()) {
			throw new IllegalArgumentException("This employee has been created");
		}
		
		//Unique employee.email
		if(employeeRepository.findByEmail(employee.getEmail()) != null) {
			throw new IllegalArgumentException("This employee's email has been registered");
		}
		
		//Unique employee.phone
		if(employeeRepository.findByPhone(employee.getPhone()) != null) {
			throw new IllegalArgumentException("This employee's phone has been registered");
		}
		return employeeRepository.save(employee);
	}
	
	//Add employee to department
	public Employee addEmployee(Long departId, Long employeeId) {
		//Find department with given id, throw an exception if could not find Department with given id
		Department departmentFound = departmentRepository.findById(departId)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + departId));	
		
		//Find employee with given id, throw an exception if could not find Employee with given id
		Employee employeeFound = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + employeeId));
		
		//Avoid when employee has been created
		if(departmentFound.getEmployee().contains(employeeFound) || (departmentFound.getManager() != null && departmentFound.getManager().equals(employeeFound)))
			throw new IllegalArgumentException("This employee has been added");
		
		//Increase number of employee
		if(departmentFound.getNumberOfEmployees() < departmentFound.getMaxEmployees()) {
			departmentFound.setNumberOfEmployees(departmentFound.getNumberOfEmployees() + 1);
			//Change depart attribute
			employeeFound.setDepart(departmentFound.getId());
			//Add to depart
			departmentFound.getEmployee().add(employeeFound);
			departmentRepository.save(departmentFound);
		}
		else {
			throw new IllegalArgumentException("This department has no vacancy left");
		}
		return employeeFound;
	}
	
	//Remove employee from department
	public Employee removeEmployee(Long departId, Long employeeId) throws IllegalArgumentException {
		//Find department with given id, throw an exception if could not find Department with given id
		Department departmentFound = departmentRepository.findById(departId)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + departId));	
		
		//Find employee with given id, throw an exception if could not find Employee with given id
		Employee employeeFound = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + employeeId));
		
		if(departmentFound.getId() != employeeFound.getDepart()) {
			throw new ResourceNotFoundException("This employee is not in this department");
		}
		//The Employee is manager, throw an exception
		if(departmentFound.getManager() != null && departmentFound.getManager().getId() == employeeFound.getId()) {
			throw new IllegalArgumentException("This Employee is a Manager");
		}
		//Change depart attribute to 0
		employeeFound.setDepart(0);
			
		//Remove from depart
		departmentFound.getEmployee().remove(employeeFound);
		departmentFound.setNumberOfEmployees(departmentFound.getNumberOfEmployees() - 1);
		departmentRepository.save(departmentFound);
		return employeeFound;
	}
	
	public Manager setToManager(Long departId, Long employeeId) throws IllegalArgumentException{
		//Find department with given id, throw an exception if could not find Department with given id
		Department departmentFound = departmentRepository.findById(departId)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + departId));	
		
		//Find employee with given id, throw an exception if could not find Employee with given id
		Employee employeeFound = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + employeeId));
		
		//If this department already have a manager or this employee is not in the department
		if(departmentFound.getManager() != null || employeeFound.getDepart() != departmentFound.getId()) {
			throw new IllegalArgumentException("Can't set this employee to manager");
		}	
		//Set to manager
		Manager manager = new Manager(employeeFound, factor);
		//set Inauguration Date to current Date
		Date currentTime = Calendar.getInstance().getTime();
		manager.setInaugurationDate(currentTime);
		
		employeeRepository.deleteById(employeeId);
		departmentFound.setManager(manager);
		departmentRepository.save(departmentFound);	
		return manager;
	}
	
	//Set a manager to employee
	public Employee setToStaff(Long departId, Long employeeId) throws IllegalArgumentException {
		//Find department with given id, throw an exception if could not find Department with given id
		Department departmentFound = departmentRepository.findById(departId)
			.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + departId));	
			
		//Find employee with given id, throw an exception if could not find Employee with given id
		Employee employeeFound = employeeRepository.findById(employeeId)
			.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + employeeId));
			
		//If this employee is not manager
		if(departmentFound.getManager() == null || !departmentFound.getManager().equals(employeeFound)) {
			throw new IllegalArgumentException("This employee is not manager");
		}	
		//Set to staff, create new Employee with copy of manager, then delete the manager
		Employee newEmployee = new Employee(employeeFound);
		newEmployee.setToStaff(factor);
				
		departmentFound.setManager(null);
		employeeRepository.deleteById(employeeId);
		departmentFound.getEmployee().add(newEmployee);
		departmentRepository.save(departmentFound);
		return newEmployee;
	}
	
	//Update employee, throw an exception if could not find Employee with given id
	public Employee updateEmployee(Long id, Employee employeeDetails) throws IllegalArgumentException {
		Employee employeeFound = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
			
		//Unique employee.email
		if(!employeeFound.getEmail().equals(employeeDetails.getEmail()) && employeeRepository.findByEmail(employeeDetails.getEmail()) != null) {
			throw new IllegalArgumentException("This employee's email has been registered");
		}
		//Unique employee.phone
		if(!employeeFound.getPhone().equals(employeeDetails.getPhone()) && employeeRepository.findByPhone(employeeDetails.getPhone()) != null) {
			throw new IllegalArgumentException("This employee's phone has been registered");
		}
		
		employeeFound.setFirstName(employeeDetails.getFirstName());
		employeeFound.setLastName(employeeDetails.getLastName());
		employeeFound.setBirthDay(employeeDetails.getBirthDay());
		employeeFound.setEmail(employeeDetails.getEmail());
		employeeFound.setGender(employeeDetails.getGender());
		employeeFound.setPhone(employeeDetails.getPhone());
		employeeFound.setSalary(employeeDetails.getSalary());
		
		if(!employeeFound.getRole().equals(employeeDetails.getRole())) {
			throw new IllegalArgumentException("This employee's role has been changed");
		}
				
		Employee updatedEmployee = employeeRepository.save(employeeFound);
		return updatedEmployee;
	}
	
	//Delete an employee, throw an exception if could not find Employee with given id
	public String deleteEmployee(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
		//If this employee in any department
		if(employee.getDepart() > 0) {
			Department department = departmentRepository.findById(employee.getDepart())
					.orElseThrow(() -> new ResourceNotFoundException("Department not exist with Employee id: " + id));
			department.setNumberOfEmployees(department.getNumberOfEmployees() - 1);
			//If this employee is Manager
			if(employee.getRole().equals("Manager")) {
				department.setManager(null);
			}	
		}
		employeeRepository.delete(employee);
		
		String responseMessage = "deleted employee with id: " + employee.getId();
		return responseMessage;
	}
	
	
	public String deleteAllEmployee() {
		employeeRepository.deleteAll();
		List<Department> departments = departmentRepository.findAll();
		for(Department depart : departments) {
			depart.setNumberOfEmployees(0);
		}
		
		String responseMessage = "deleted all employees";
		return responseMessage;
	}
}
