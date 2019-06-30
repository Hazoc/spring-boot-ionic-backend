package com.hazoc.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.hazoc.cursomc.domain.Cidade;
import com.hazoc.cursomc.domain.Cliente;
import com.hazoc.cursomc.domain.Endereco;
import com.hazoc.cursomc.domain.enums.TipoCliente;
import com.hazoc.cursomc.dto.ClienteDTO;
import com.hazoc.cursomc.dto.ClienteNewDTO;
import com.hazoc.cursomc.repositories.ClienteRepository;
import com.hazoc.cursomc.repositories.EnderecoRepository;
import com.hazoc.cursomc.services.exceptions.DataIntegrityException;
import com.hazoc.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository repoEnd;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		repoEnd.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não e possive excluir a entidades relacionadas.");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());

	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDTO) {

		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(),TipoCliente.toEnum(objDTO.getTipo()));
		Cidade cid = new Cidade(objDTO.getCidadeId(),null,null);
		Endereco end = new Endereco(null,objDTO.getLogradouro(),objDTO.getNumero(),objDTO.getComplemento(),objDTO.getBairro(),objDTO.getCep(),cli,cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2() != null) {
			cli.getTelefones().add(objDTO.getTelefone2());	
		}
		if(objDTO.getTelefone3() != null) {
			cli.getTelefones().add(objDTO.getTelefone3());	
		}
		return cli;
	}
}
