package com.projeto.skill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.skill.dto.LoginDTO;
import com.projeto.skill.dto.LoginInserirDTO;
import com.projeto.skill.model.Login;
import com.projeto.skill.repository.LoginRepository;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginRepository;
	
    private final PasswordEncoder encoder;
	
	public LoginService(PasswordEncoder encoder) {
		this.encoder = encoder;
	}
	
	public List<LoginDTO> obterTodos(){
		List<Login> logins = loginRepository.findAll();
		List<LoginDTO> loginDTOs = new ArrayList<>();
		
		for(Login login : logins) {
			loginDTOs.add(new LoginDTO(login));
		}
		return loginDTOs;
	}
	
	public LoginDTO obterPorId(Integer id) throws LoginException{
		Optional<Login> loginOptional = loginRepository.findById(id);
		try {
			if(loginOptional.isEmpty()) {
				throw new LoginException("Login não existe!");
			}
		}
		catch(LoginException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		return new LoginDTO(loginOptional.get());
	}
	
	public Login cadastrar(LoginInserirDTO loginDTO) throws LoginException {
		String login = loginDTO.getLogin();
		String senha = loginDTO.getSenha();
		String confirmarSenha = loginDTO.getConfirmarSenha();
		
		if(!senha.equals(confirmarSenha)) {
			throw new LoginException("Senha e confirmação de senha não são igulais.");
		}
		
		Optional<Login> loginOptional = loginRepository.findByLogin(login);
		try {
			if(!loginOptional.isEmpty()) {
				throw new LoginException("Login já existe!");
			}
		}
		catch(LoginException ex){
			System.out.println(ex.getMessage());
			return null;
		}
		Login novoLogin = new Login();
		novoLogin.setLogin(login);
		novoLogin.setSenha(encoder.encode(senha));
		
		//login.setSenha(encoder.encode(login.getSenha()));
		return loginRepository.save(novoLogin);
	}
	
	public boolean validaSenha(String login, String senha) {
		Optional<Login> optionalLogin = loginRepository.findByLogin(login);
		if(optionalLogin.isEmpty()) {
			return false;
		}
		Login loginModel = optionalLogin.get();
		
		return encoder.matches(senha, loginModel.getSenha());
	}
}
