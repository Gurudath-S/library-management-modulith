package com.library.users;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            return ResponseEntity.ok().body("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(), 
                    loginDto.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User userPrincipal = (User) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userPrincipal);
            
            return ResponseEntity.ok(new JwtResponseDto(jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(), 
                userPrincipal.getEmail(),
                userPrincipal.getRole().name()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username/email or password");
        }
    }
}
