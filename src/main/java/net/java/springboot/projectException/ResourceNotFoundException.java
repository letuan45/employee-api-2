package net.java.springboot.projectException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	//Constructor with a message -> use to return when resource not found
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
