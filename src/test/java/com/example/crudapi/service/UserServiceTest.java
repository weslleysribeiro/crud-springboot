package com.example.crudapi.service;

import com.example.crudapi.exception.ResourceNotFoundException;
import com.example.crudapi.model.User;
import com.example.crudapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    private UserService userService;

	private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "Alice", "alice@example.com");
        when(meterRegistry.counter("users_created_total", "service", "UserService")).thenReturn(counter);
        userService = new UserService(userRepository, meterRegistry);
    }

	@Test
	void createUser_saves_whenEmailNotExists() {
		when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenAnswer(inv -> {
			User u = inv.getArgument(0);
			u.setId(1L);
			return u;
		});

		User created = userService.createUser(new User(null, "Alice", "alice@example.com"));

		assertThat(created.getId()).isEqualTo(1L);
		assertThat(created.getName()).isEqualTo("Alice");
		verify(userRepository).save(any(User.class));
	}

	@Test
	void createUser_throws_whenEmailExists() {
		when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(sampleUser));

		assertThatThrownBy(() -> userService.createUser(new User(null, "Bob", "alice@example.com")))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Email");

		verify(userRepository, never()).save(any());
	}

	@Test
	void getAllUsers_returnsList() {
		when(userRepository.findAll()).thenReturn(Arrays.asList(
			new User(1L, "A", "a@e.com"),
			new User(2L, "B", "b@e.com")
		));

		List<User> users = userService.getAllUsers();
		assertThat(users).hasSize(2);
		assertThat(users.get(0).getName()).isEqualTo("A");
	}

	@Test
	void getUserById_returns_whenFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
		User user = userService.getUserById(1L);
		assertThat(user.getEmail()).isEqualTo("alice@example.com");
	}

	@Test
	void getUserById_throws_whenMissing() {
		when(userRepository.findById(99L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> userService.getUserById(99L))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("99");
	}

	@Test
	void updateUser_updatesFieldsAndSaves() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "Old", "old@e.com")));
		when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

		User details = new User(null, "New", "new@e.com");
		User updated = userService.updateUser(1L, details);

		assertThat(updated.getName()).isEqualTo("New");
		assertThat(updated.getEmail()).isEqualTo("new@e.com");

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		assertThat(captor.getValue().getId()).isEqualTo(1L);
	}

	@Test
	void deleteUser_deletesExisting() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
		userService.deleteUser(1L);
		verify(userRepository).delete(sampleUser);
	}
}


