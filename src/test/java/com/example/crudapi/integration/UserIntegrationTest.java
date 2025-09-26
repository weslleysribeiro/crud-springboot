package com.example.crudapi.integration;

import com.example.crudapi.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private ObjectMapper objectMapper;

	private String baseUrl() { return "http://localhost:" + port + "/api/users"; }

	@Test
	void fullCrudFlow() throws Exception {
		RestTemplate rest = new RestTemplate();

		// Create
		User toCreate = new User(null, "Alice", "alice@it.com");
		ResponseEntity<User> createdResp = rest.postForEntity(baseUrl(), toCreate, User.class);
		assertThat(createdResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		User created = createdResp.getBody();
		assertThat(created).isNotNull();
		assertThat(created.getId()).isNotNull();

		Long id = created.getId();

		// Read by id
		User got = rest.getForObject(baseUrl() + "/" + id, User.class);
		assertThat(got.getEmail()).isEqualTo("alice@it.com");

		// Update
		User details = new User(null, "Alice Updated", "alice.updated@it.com");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(details), headers);
		ResponseEntity<User> updateResp = rest.exchange(baseUrl() + "/" + id, HttpMethod.PUT, entity, User.class);
		assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResp.getBody().getName()).isEqualTo("Alice Updated");

		// List
		ResponseEntity<String> listResp = rest.getForEntity(baseUrl(), String.class);
		assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<User> users = objectMapper.readValue(listResp.getBody(), new TypeReference<List<User>>(){});
		assertThat(users).isNotEmpty();

		// Delete
		rest.delete(baseUrl() + "/" + id);


		// Ensure 404 after delete
		try {
			rest.getForEntity(baseUrl() + "/" + id, String.class);
			throw new AssertionError("Expected 404 after delete");
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}
	}
}


