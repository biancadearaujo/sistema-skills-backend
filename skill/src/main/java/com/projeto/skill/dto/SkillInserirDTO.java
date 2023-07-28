package com.projeto.skill.dto;

public class SkillInserirDTO {
	private String skill;
	private String level;
	
	public SkillInserirDTO() {
		
	}
	
	public SkillInserirDTO(String skill, String level) {
		this.skill = skill;
		this.level = level;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
