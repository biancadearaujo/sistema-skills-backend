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

@RestController
@RequestMapping("/skill")
public class SkillController {
	private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<List<Skill>> obterPorIdLogin(@PathVariable int id){
    	return ResponseEntity.status(HttpStatus.OK).body(skillService.obterPorIdLogin(id));
    }
	
	@PostMapping("/{loginId}")
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
	
	@PutMapping("/{id}")
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
	
	@DeleteMapping("/{id}")
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
