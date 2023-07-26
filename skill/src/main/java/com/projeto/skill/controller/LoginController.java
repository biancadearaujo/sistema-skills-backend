package com.projeto.skill.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projeto.skill.dto.LoginDTO;
import com.projeto.skill.dto.LoginInserirDTO;
import com.projeto.skill.dto.ValidarLoginSenhaDTO;
import com.projeto.skill.model.Login;
import com.projeto.skill.repository.LoginRepository;
import com.projeto.skill.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	LoginService loginService;
	
	@GetMapping
	public ResponseEntity<List<LoginDTO>> obterTodos(){
		return new ResponseEntity<>(loginService.obterTodos(),
				HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<LoginDTO> obterPorId(@PathVariable Integer id) throws LoginException{		
		if(loginService.obterPorId(id) != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(loginService.obterPorId(id));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<Login>salvar(@RequestBody LoginInserirDTO loginDTO) throws LoginException{
		//login.setSenha(encoder.encode(login.getSenha()));
		try {
			if(loginService.cadastrar(loginDTO) != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(loginService.cadastrar(loginDTO));
			}
		}catch(LoginException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
	}
	
	@GetMapping("/validar-senha")
	public ResponseEntity<Boolean> validarSenha(@RequestBody ValidarLoginSenhaDTO validarLoginSenhaDTO){
		String login = validarLoginSenhaDTO.getLogin();
		String senha = validarLoginSenhaDTO.getSenha();
		
		boolean valid = loginService.validaSenha(login, senha);
		
		HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		
		return ResponseEntity.status(status).body(valid);
	}
}
