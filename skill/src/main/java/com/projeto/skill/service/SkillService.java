package com.projeto.skill.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.skill.dto.LoginDTO;
import com.projeto.skill.dto.SkillAtualizarDTO;
import com.projeto.skill.dto.SkillDTO;
import com.projeto.skill.dto.SkillInserirDTO;
import com.projeto.skill.model.Login;
import com.projeto.skill.model.Skill;
import com.projeto.skill.repository.LoginRepository;
import com.projeto.skill.repository.SkillRepository;

import exception.LoginException;
import exception.SkillException;

@Service
public class SkillService {
	private final SkillRepository skillRepository;
    private final LoginRepository loginRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository, LoginRepository loginRepositor) {
        this.skillRepository = skillRepository;
        this.loginRepository = loginRepositor;
    }
    
    public List<Skill> obterPorIdLogin(int id){
    	List<Skill> skillList = skillRepository.findByidLogin(id);
    	
    	return skillList;
    }
    
    public Skill cadastrar(int loginId, SkillDTO skillDTO) throws LoginException{
    	Optional<Login> loginOptional = loginRepository.findById(loginId);
    	Login login = loginOptional.orElseThrow(() -> new LoginException("Login não encontrado."));
    	
    	Skill novaSkill = new Skill();
    	novaSkill.setSkill(skillDTO.getSkill());
    	novaSkill.setLevel(skillDTO.getLevel());
    	novaSkill.setIdLogin(loginId);
    	
    	return skillRepository.save(novaSkill);
    }
    
    public Skill atualizar(int id, SkillAtualizarDTO skillAtualizarDTO) {
    	Optional<Skill> skillOptional = skillRepository.findById(id);
    	
    		if(skillOptional.isEmpty()) {
    			throw new SkillException("Erro ao encontrar uma skill");
    		}
    		
    		Skill skill = skillOptional.get();
    		
    		String level = skillAtualizarDTO.getLevel();
    	    if (level == null || level.equals("")) {
    	        throw new SkillException("O campo level não pode ser vazio");
    	    }
    		
    		BeanUtils.copyProperties(skillAtualizarDTO, skillOptional.get());
    		
    	
    		return skillRepository.save(skillOptional.get());
    }
    
    public void deletar(int id) {
    	Optional<Skill> skillOptional = skillRepository.findById(id);
    	
		if(skillOptional.isEmpty()) {
			throw new SkillException("Skill não existe.");
		}
		skillRepository.deleteById(id);
    }
}
