package com.example.crudapi.controller;

import com.example.crudapi.model.User;
import com.example.crudapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	void createUser_returns201() throws Exception {
		User toCreate = new User(null, "Alice", "alice@example.com");
		User created = new User(1L, "Alice", "alice@example.com");
		given(userService.createUser(any(User.class))).willReturn(created);

		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(toCreate)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("Alice"));
	}

	@Test
	void getAllUsers_returnsList() throws Exception {
		given(userService.getAllUsers()).willReturn(Arrays.asList(
			new User(1L, "A", "a@e.com"),
			new User(2L, "B", "b@e.com")
		));

		mockMvc.perform(get("/api/users"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	void getUserById_returnsUser() throws Exception {
		given(userService.getUserById(1L)).willReturn(new User(1L, "A", "a@e.com"));

		mockMvc.perform(get("/api/users/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email").value("a@e.com"));
	}

	@Test
	void updateUser_returnsUpdated() throws Exception {
		User details = new User(null, "New", "new@e.com");
		User updated = new User(1L, "New", "new@e.com");
		given(userService.updateUser(eq(1L), any(User.class))).willReturn(updated);

		mockMvc.perform(put("/api/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(details)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("New"));
	}

	@Test
	void deleteUser_returns204() throws Exception {
		doNothing().when(userService).deleteUser(1L);

		mockMvc.perform(delete("/api/users/1"))
			.andExpect(status().isNoContent());
	}
}


