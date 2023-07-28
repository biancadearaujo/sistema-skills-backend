package com.projeto.skill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.skill.model.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer>{
	public Optional<Skill> findBySkill (String skill);
	List<Skill> findByidLogin(int idLogin);
}