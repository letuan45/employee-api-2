package net.java.springboot.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Employee")
public class Employee {
	@Id
	@Column(name = "id")
	protected long id;
	
	@Column(name = "first_name", length = 25, nullable = false)
	@Pattern(regexp = "[a-zA-Z]*", message = "FirstName is not valid")
	protected String firstName;
	
	@Column(name = "last_name", length = 50, nullable = false)
	@Pattern(regexp = "[a-zA-Z ]*", message = "LastName is not valid")
	protected String lastName;
	
	@Column(name = "gender", nullable = false)
	protected String gender;
	
	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Email is not valid")
	protected String email;
	
	@Column(name = "phone", unique = true, nullable = false)
	@Pattern(regexp = "0[0-9]{9}", message = "Phone is not valid")
	protected String phone;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/mm/yyyy")
	@Column(name = "dayOfBirth", nullable = false)
	protected Date birthDay;
	
	@Column(name = "role")
	protected String role = "Staff";
	
	@Column(name = "salary", nullable = false)
	protected double salary;
	
	@Column(name = "depart")
	protected long depart = 0;
	
	//Default Constructor
	public Employee() {
		super();
	}
	
	//Constructor
	public Employee(long id, String firstName, String lastName, String gender, String email, String phone, Date birthDay,
			double salary) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;		
		this.email = email;
		this.phone = phone;
		this.birthDay = birthDay;
		this.salary = salary;
	}
	
	//constructor with employee
	public Employee(Employee employee) {
		super();
		this.id = employee.getId();
		this.firstName = employee.getFirstName();
		this.lastName = employee.getLastName();
		this.gender = employee.getGender();		
		this.email = employee.getEmail();
		this.phone = employee.getPhone();
		this.birthDay = employee.getBirthDay();
		this.salary = employee.getSalary();
		this.depart = employee.getDepart();
	}
		
	public void setToManager(double factor) {
		setSalary(getSalary() * factor);
		setRole("Manager");
	}
	
	public void setToStaff(double factor) {
		setSalary(getSalary() / factor);
		setRole("Staff");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public long getDepart() {
		return depart;
	}

	public void setDepart(long depart) {
		this.depart = depart;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", email=" + email + ", phone=" + phone + ", birthDay=" + birthDay + ", role=" + role + ", salary="
				+ salary + ", depart=" + depart + "]";
	}
}
