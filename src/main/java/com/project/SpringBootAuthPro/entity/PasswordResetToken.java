package com.project.SpringBootAuthPro.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    private String token;

    private String email;

    private Date expiryDate;

}
