package com.projeto.skill.dto;

import com.projeto.skill.model.Login;

public class LoginDTO {
	private int id;
	private String login;
	private String senha;
	public LoginDTO() {
		
	}
	
	public LoginDTO(int id, String login, String senha) {
		this.id = id;
		this.login = login;
		this.senha = senha;
	}
	
	public LoginDTO(Login login) {
		this.id=login.getIdLogin();
		this.login=login.getLogin();
		this.senha=login.getSenha();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
