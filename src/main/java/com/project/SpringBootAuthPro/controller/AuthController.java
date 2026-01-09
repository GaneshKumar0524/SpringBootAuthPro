package com.project.SpringBootAuthPro.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.SpringBootAuthPro.entity.PasswordResetToken;
import com.project.SpringBootAuthPro.repository.PasswordResetTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SpringBootAuthPro.entity.user;
import com.project.SpringBootAuthPro.exception.UserNotFoundException;
import com.project.SpringBootAuthPro.repository.UserRepository;
import com.project.SpringBootAuthPro.service.CustomUserDetailsService;
import com.project.SpringBootAuthPro.utility.JwtUtil;

import jakarta.validation.Valid;

import java.util.Date;
import com.project.SpringBootAuthPro.service.EmailService;
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

	private static final  Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	PasswordEncoder PasswordEncoder;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	JwtUtil JwtUtil;
	@Autowired
	UserRepository UserRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepo;

    @Autowired
    private  EmailService emailService;


	//Register the new User into the server
	@PostMapping("/register")
	public ResponseEntity<user> addUsers(@Valid @RequestBody user user) {
		user.setPassword(PasswordEncoder.encode(user.getPassword())); 
		user createdUser = UserRepository.save(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);		
	}

	//Showing List of active users in the DataBase
	@GetMapping("/userList")
	public ResponseEntity<List<user>> GetUsers() {
		List<user> list = UserRepository.findAll();
		return ResponseEntity.ok(list);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String,String>> login(@RequestBody user authRequest) {
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			String accessToken = JwtUtil.generateToken(authRequest.getUsername() ,1000 * 60 * 10);//10 Minutes

			String refreshToken = JwtUtil.generateToken(authRequest.getUsername(), 1000 * 60 * 60 * 24 * 7); // 7 days

			Map<String, String> response = Map.of(
					"accessToken", accessToken,
					"refreshToken", refreshToken
					);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new UserNotFoundException("Invalid username or password.");
		}
		//return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {

		String refreshToken = request.get("refreshToken");

		try {
			String username = JwtUtil.extractUsername(refreshToken);

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (JwtUtil.validateToken(refreshToken ,userDetails)) {
				String newAccessToken = JwtUtil.generateToken(username, 1000 * 60 * 60 *24*7);// 7 days
				return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
			}
		}
		catch(Exception e) 
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");	
		} 
	}
	//{
	//"username": "Alien",
	//"password": "Ganesh@123"
	//}
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        Optional<user> existingUser = UserRepository.findByEmail(email);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found!");
        }

        String token = java.util.UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); // 15 mins

        passwordResetTokenRepo.save(resetToken);

        String link = "http://localhost:9090/auth/reset-password?token=" + token;

        emailService.sendEmail(email, "Reset Password", "Click here to reset password:\n" + link);

        return ResponseEntity.ok("Reset password link sent to email!");
    }

}