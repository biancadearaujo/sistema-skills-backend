package exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ResponseEntityExceptionHandler extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler{
	@ExceptionHandler(LoginException.class)
	protected ResponseEntity<Object> handleLoginException(LoginException ex){
		return ResponseEntity.unprocessableEntity().body(ex.getMessage());
	}
}
