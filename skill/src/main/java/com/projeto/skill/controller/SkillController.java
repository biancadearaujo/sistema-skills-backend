package com.projeto.skill.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projeto.skill.dto.LoginDTO;
import com.projeto.skill.dto.SkillAtualizarDTO;
import com.projeto.skill.dto.SkillDTO;
import com.projeto.skill.dto.SkillInserirDTO;
import com.projeto.skill.model.Skill;
import com.projeto.skill.repository.SkillRepository;
import com.projeto.skill.service.SkillService;

import exception.LoginException;
import exception.SkillException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/skill")
public class SkillController {
	private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    
    /**
     * Retorna uma lista de skills associadas a um determinado ID de login.
     * 
     * @param id O ID do login para o qual se deseja obter as skills.
     * @return Um objeto ResponseEntity contendo a lista de skills associadas ao ID de login especificado. Retorna status 200 (OK) se as skills forem encontradas e 404 (Not Found) caso contrário.
     */
    @GetMapping("/{id}")
    @ApiOperation(value="Retorna uma skill", notes="Skill")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna uma skill"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
    public ResponseEntity<List<Skill>> obterPorIdLogin(@PathVariable int id){
    	return ResponseEntity.status(HttpStatus.OK).body(skillService.obterPorIdLogin(id));
    }
	
    /**
     * Insere os dados de uma nova skill associada a um login específico.
     * 
     * @param loginId O ID do login ao qual a skill será associada.
     * @param skillDTO O objeto SkillDTO contendo os dados da skill a ser cadastrada.
     * @return Um objeto ResponseEntity contendo a skill cadastrada e o status da resposta. Retorna status 201 (Created) se a skill for cadastrada com sucesso, 404 (Not Found) se o login não for encontrado, ou 500 (Internal Server Error) se ocorrer um erro interno na aplicação.
     */
	@PostMapping("/{loginId}")
	@ApiOperation(value="Insere os dados de uma skill", notes="Inserir Skill")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Skill adicionada"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Erro interno"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<Skill> cadastrar(@PathVariable int loginId, @RequestBody SkillDTO skillDTO){
		try {
			Skill skill = skillService.cadastrar(loginId, skillDTO);
			URI uri = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(skillDTO.getId())
					.toUri();
			
			return ResponseEntity.created(uri).body(skill);
		}catch(LoginException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	/**
	 * Atualiza os dados de uma skill existente.
	 * 
	 * @param skillAtualizacaoDTO O objeto SkillAtualizarDTO contendo os novos dados da skill.
	 * @param id O ID da skill a ser atualizada.
	 * @return Um objeto ResponseEntity contendo a skill atualizada e o status da resposta. Retorna status 200 (OK) se a skill for atualizada com sucesso, 401 (Unauthorized) se o usuário não estiver autenticado, 403 (Forbidden) se o usuário não tiver permissão para acessar o recurso, 404 (Not Found) se a skill não for encontrada, ou 422 (Unprocessable Entity) se ocorrer um erro ao processar a entidade.
	 */
	@PutMapping("/{id}")
	@ApiOperation(value="Atualiza os dados de uma skill", notes="Atualizar")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Skill Atualizada"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=422, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<Skill> atualizar(@RequestBody SkillAtualizarDTO skillAtualizacaoDTO, @PathVariable int id){
		try {
			if(skillService.atualizar(id, skillAtualizacaoDTO) != null) {
				return ResponseEntity.status(HttpStatus.OK).body(skillService.atualizar(id, skillAtualizacaoDTO));
			}
		}catch(SkillException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
	}
	
	/**
	 * Remove uma skill existente.
	 * 
	 * @param id O ID da skill a ser removida.
	 * @return Um objeto ResponseEntity contendo uma mensagem de confirmação da exclusão e o status da resposta. Retorna status 200 (OK) se a skill for removida com sucesso, 401 (Unauthorized) se o usuário não estiver autenticado, 403 (Forbidden) se o usuário não tiver permissão para acessar o recurso, 404 (Not Found) se a skill não for encontrada, ou 500 (Internal Server Error) se ocorrer um erro interno na aplicação.
	 */
	@DeleteMapping("/{id}")
	@ApiOperation(value="Remove uma skill", notes="Remover Skill")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Skill Removida"),
			@ApiResponse(code=401, message="Erro de autenticação"),
			@ApiResponse(code=403, message="Não há premissão para acessar o recurso"),
			@ApiResponse(code=404, message="Recurso não encontrado"),
			@ApiResponse(code=505, message="Exceção interna da aplicação")
	})
	public ResponseEntity<String> deletar(@PathVariable int id){
		try {
			skillService.deletar(id);
			return ResponseEntity.ok("Skill excluída.");
			}catch(SkillException ex) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Skill não encontrada.");
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
	}
	}
}
