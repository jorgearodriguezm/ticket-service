package com.examination.ticket.controller;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examination.ticket.entity.Ticket;
import com.examination.ticket.service.TicketService;

@RestController
@RequestMapping("tickets")
public class TicketController {

	private final Map<String, String> creationFailed = Stream
			.of(new String[][] { { "message", "Could not create the ticket" } })
			.collect(Collectors.toMap(data -> data[0], data -> data[1]));
	private final Map<String, String> notFound = Stream.of(new String[][] { { "message", "Ticket was not found." } })
			.collect(Collectors.toMap(data -> data[0], data -> data[1]));

	@Autowired
	private TicketService ticketService;

	@PostMapping("/create")
	public ResponseEntity<Object> add(@RequestBody Ticket ticket) {
		Ticket createdTicket = ticketService.add(ticket);
		if (createdTicket != null && createdTicket.getItineraryID() != null) {
			return ResponseEntity.ok(createdTicket);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(creationFailed);
		}

	}

	@GetMapping("/get/{itineraryID}")
	public ResponseEntity<Object> get(@PathVariable Long itineraryID) {

		Ticket foundTicket = ticketService.get(itineraryID);

		if (foundTicket != null) {
			return ResponseEntity.ok().body(foundTicket);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
		}

	}
}
