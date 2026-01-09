package com.project.SpringBootAuthPro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.SpringBootAuthPro.entity.user;

@Repository
public interface UserRepository extends JpaRepository<user ,Long>{

	public Optional<user> findByUsername(String username);

    public Optional<user> findByEmail(String email);
}
