package net.java.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.java.springboot.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	//find with email
	Employee findByEmail(String email);
	//find with phone number
	Employee findByPhone(String phone);
}
