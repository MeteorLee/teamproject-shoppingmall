package project.finalproject1backend.config;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.exception.PaymentException;

import java.nio.file.AccessDeniedException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IllegalAccessException.class)
    public String handleIllegalAccessException(IllegalAccessException e) {
        return "redirect:/login";
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorDTO("400",e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        return new ResponseEntity<>(new ErrorDTO("403",e.getMessage()), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
        return new ResponseEntity<>(new ErrorDTO("401",e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 결제 오류 처리
     *
     * @return
     */
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ResponseDTO> PaymentExceptionHandler() {

        return new ResponseEntity<>(new ResponseDTO("400","fail"), HttpStatus.OK);
    }
}