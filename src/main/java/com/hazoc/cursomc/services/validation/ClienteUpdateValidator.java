package com.hazoc.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.hazoc.cursomc.domain.Cliente;
import com.hazoc.cursomc.dto.ClienteDTO;
import com.hazoc.cursomc.repositories.ClienteRepository;
import com.hazoc.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void initialize(ClienteUpdate ann) {
		
	}
	
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		Integer uriID = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null && !aux.getId().equals(uriID)) {
			list.add(new FieldMessage("email", "email ja existente"));
		}
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
	

}
