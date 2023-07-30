package com.projeto.skill.dto;

public class SkillDTO {
	private int id;
	private String skill;
	private String level;
	
	public SkillDTO() {
		
	}
	
	public SkillDTO(int id, String skill, String level) {
		super();
		this.id = id;
		this.skill = skill;
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
