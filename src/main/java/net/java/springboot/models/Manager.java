package net.java.springboot.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Manager")
public class Manager extends Employee {
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/mm/yyyy")
	protected Date inaugurationDate;
	
	public Manager() {
		setRole("Manager");
	}
	
	public Manager(Employee employee, double factor) {
		super(employee);
		setToManager(factor);
	}

	public Date getInaugurationDate() {
		return inaugurationDate;
	}

	public void setInaugurationDate(Date inaugurationDate) {
		this.inaugurationDate = inaugurationDate;
	}
}