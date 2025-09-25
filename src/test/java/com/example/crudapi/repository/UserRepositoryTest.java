package com.example.crudapi.repository;

import com.example.crudapi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void findByEmail_returnsUser() {
		User u = new User(null, "A", "a@e.com");
		u = userRepository.save(u);
		Optional<User> found = userRepository.findByEmail("a@e.com");
		assertThat(found).isPresent();
		assertThat(found.get().getId()).isEqualTo(u.getId());
	}

	@Test
	void uniqueEmail_constraint_enforced() {
		userRepository.save(new User(null, "A", "dup@e.com"));
		userRepository.flush();
		assertThatThrownBy(() -> {
			userRepository.saveAndFlush(new User(null, "B", "dup@e.com"));
		}).isInstanceOf(DataIntegrityViolationException.class);
	}
}


