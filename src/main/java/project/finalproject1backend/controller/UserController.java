package project.finalproject1backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.finalproject1backend.dto.UserSignUpRequestDTO;
import project.finalproject1backend.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpRequestDTO requestDTO,
                                    BindingResult bindingResult) throws Exception {
        return new ResponseEntity<>(userService.signUp(requestDTO), HttpStatus.OK);
    }
}
