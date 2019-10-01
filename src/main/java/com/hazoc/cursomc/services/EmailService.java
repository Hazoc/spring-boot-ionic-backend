package com.hazoc.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.hazoc.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);
}
