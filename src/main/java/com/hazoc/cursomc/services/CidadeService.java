package com.hazoc.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazoc.cursomc.domain.Cidade;
import com.hazoc.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> findByEstado(Integer estado_id) {
		return cidadeRepository.findCidades(estado_id) ; 
	}
	
}
