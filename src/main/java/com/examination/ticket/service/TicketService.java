package com.examination.ticket.service;

import com.examination.ticket.entity.Ticket;

public interface TicketService {
	public Ticket add(Ticket newTicket);
	public Ticket get(Long id);
}
