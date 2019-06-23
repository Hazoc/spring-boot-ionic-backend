package com.hazoc.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazoc.cursomc.domain.Cliente;
import com.hazoc.cursomc.domain.Pedido;
import com.hazoc.cursomc.repositories.PedidoRepository;
import com.hazoc.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado! id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
}
