package com.projeto.skill.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;

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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	LoginService loginService;
	
	/**
	 * Lista todos os logins.
	 * 
	 * @return Uma lista com todos os logins cadastrados.
	 */
	@GetMapping
	@ApiOperation(value="Lista todos os logins", notes="Listagem de Logins")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna todos os logins"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<List<LoginDTO>> obterTodos(){
		return new ResponseEntity<>(loginService.obterTodos(),
				HttpStatus.OK);
	}
	
	/**
	 * Retorna um login pelo seu ID.
	 * 
	 * @param id O ID do login a ser retornado.
	 * @return O login correspondente ao ID.
	 * @throws LoginException Caso o login não seja encontrado.
	 */
	@GetMapping("/{id}")
	@ApiOperation(value="Retorna um login", notes="Login")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna um login"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<LoginDTO> obterPorId(@PathVariable Integer id) throws LoginException{		
		if(loginService.obterPorId(id) != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(loginService.obterPorId(id));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	/**
	 * Insere os dados de um novo login.
	 * 
	 * @param loginDTO O objeto que contém os dados do login a ser inserido.
	 * @return Um objeto ResponseEntity com o login adicionado no corpo da resposta e status 201 (Created) se a operação for bem-sucedida.
	 * @throws LoginException Caso ocorra algum erro durante a inserção do login.
	 */
	@PostMapping("/salvar")
	@ApiOperation(value="Insere os dados de um login", notes="Inserir Login")
	@ApiResponses(value= {
			@ApiResponse(code=201, message="Login adicionado"),
			@ApiResponse(code=400, message="Dados inválidos ou incompletos"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=422, message="Entidade não processável"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<Login>salvar(@RequestBody LoginInserirDTO loginDTO) throws LoginException{
		try {
			if(loginService.cadastrar(loginDTO) != null) {
				URI uri = ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(loginDTO.getId())
						.toUri();
				
				return ResponseEntity.created(uri).body(loginService.cadastrar(loginDTO));
			}
		}catch(LoginException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
	}
	
	/**
	 * Valida a senha de um usuário.
	 * 
	 * @param validarLoginSenhaDTO O objeto contendo o nome de usuário (login) e senha a serem validados.
	 * @return Um objeto ResponseEntity contendo um booleano indicando se a senha é válida ou não. Retorna status 200 (OK) se a senha for válida e 401 (Unauthorized) caso contrário.
	 */
	@GetMapping("/validar-senha")
	@ApiOperation(value="Válida a senha", notes="Válida a senha")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Senha Validada"),
			@ApiResponse(code=400, message="Dados inválidos ou incompletos"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<Boolean> validarSenha(@RequestBody ValidarLoginSenhaDTO validarLoginSenhaDTO){
		String login = validarLoginSenhaDTO.getLogin();
		String senha = validarLoginSenhaDTO.getSenha();
		
		if (login == null || login.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	    }
		
		boolean valid = loginService.validaSenha(login, senha);
		
		HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		
		return ResponseEntity.status(status).body(valid);
	}
}
