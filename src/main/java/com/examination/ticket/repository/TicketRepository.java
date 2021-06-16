package com.examination.ticket.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.examination.ticket.entity.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {

}
