package com.examination.ticket.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.examination.ticket.TicketServiceApplication;
import com.examination.ticket.entity.Ticket;
import com.examination.ticket.repository.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TicketServiceApplication.class)
@AutoConfigureMockMvc
public class TicketControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private TicketRepository repository;

	@Test
	public void postTicket_ByItinerary_Endpoint_Test() throws JsonProcessingException, Exception {
		// Given
		Ticket ticketToCreate = createTicketObject();

		// When
		ResultActions result = mockMvc.perform(post("/tickets/create").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(createTicketObject())));

		// Then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.itineraryID").exists());
		result.andExpect(jsonPath("$.itineraryID").isNotEmpty());
		result.andExpect(jsonPath("$.itineraryID").isNumber());
		result.andExpect(jsonPath("$.passengerName").value(ticketToCreate.getPassengerName()));

	}

	@Test
	public void getTicket_ByItinerary_ValidaItineraryID_Endpoint_Test() throws Exception {
		// Given itineraryID
		Ticket existentTicket = repository.save(createTicketObject());

		// When
		ResultActions result = mockMvc.perform(get("/tickets/get/{itineraryID}", existentTicket.getItineraryID()));

		// Then
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.itineraryID").exists());
		result.andExpect(jsonPath("$.itineraryID").isNotEmpty());
		result.andExpect(jsonPath("$.itineraryID").isNumber());
		result.andExpect(jsonPath("$.itineraryID").value(existentTicket.getItineraryID()));
		result.andExpect(jsonPath("$.passengerName").value(existentTicket.getPassengerName()));

	}

	@Test
	public void getTicket_ByItinerary_InvalidaItineraryID_Endpoint_Test() throws Exception {
		// Given not existent itineraryID
		Long invalidID = Long.parseLong("999");

		// When
		ResultActions result = mockMvc.perform(get("/tickets/get/{itineraryID}", invalidID));

		// Then
		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.message").exists());
		result.andExpect(jsonPath("$.itineraryID").doesNotExist());

	}

	public Ticket createTicketObject() {
		Ticket ticket = new Ticket(new Date(), new Date(), "Dallas", "Michigan", "Pierre", 32, false,
				new BigDecimal(26789), "09:00", "13:01");
		return ticket;
	}

}
