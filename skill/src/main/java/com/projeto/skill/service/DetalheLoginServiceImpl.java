package com.projeto.skill.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.projeto.skill.data.DetalheLoginData;
import com.projeto.skill.model.Login;
import com.projeto.skill.repository.LoginRepository;

@Component
public class DetalheLoginServiceImpl implements UserDetailsService{
	
	private final LoginRepository loginRepository;
	
	public DetalheLoginServiceImpl(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<Login> loginOptional = loginRepository.findByLogin(username);
		if(loginOptional.isEmpty()) {
			throw new UsernameNotFoundException("Login [" + username + "], não encontrado");
		}
		return new DetalheLoginData(loginOptional);
	}

}
