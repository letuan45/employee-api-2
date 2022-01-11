package net.java.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.java.springboot.models.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
	//find with department's name
	Department findByName(String name);
}
