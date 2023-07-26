package com.projeto.skill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.skill.model.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer>{
	public Optional<Login> findByLogin (String login);
}
