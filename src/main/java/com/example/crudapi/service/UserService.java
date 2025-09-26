package com.example.crudapi.service;

import com.example.crudapi.exception.ResourceNotFoundException; //Importação da exceção ResourceNotFoundException (Usuário não encontrado)
import com.example.crudapi.model.User; //Importação do modelo de usuário
import com.example.crudapi.repository.UserRepository; //Importação do repositório de usuários
import org.springframework.stereotype.Service; //Importação do Service (anotação para indicar que a classe é um serviço)
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List; //Importação da lista (retorna uma lista de usuários)

@Service //Anotação para indicar que a classe é um serviço
public class UserService { //Classe que implementa os métodos do serviço de usuários

    private final UserRepository userRepository;
    private final Counter usersCreatedCounter;

    public UserService(UserRepository userRepository, MeterRegistry meterRegistry) { //Construtor que recebe o repositório de usuários
        this.userRepository = userRepository;
        this.usersCreatedCounter = meterRegistry.counter("users_created_total", "service", "UserService");
    }

    public User createUser(User user) { //Método que cria um usuário
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email já cadastrado"); //Exceção para indicar que o email já está cadastrado
        });
        User saved = userRepository.save(user);
        usersCreatedCounter.increment();
        return saved;
    }

    public List<User> getAllUsers() { //Método que retorna todos os usuários
        return userRepository.findAll();
    }

    public User getUserById(Long id) { //Método que retorna um usuário por id
        return userRepository.findById(id) 
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id " + id)); //Exceção para indicar que o usuário não foi encontrado
    }

    public User updateUser(Long id, User userDetails) { //Método que atualiza um usuário
        User user = getUserById(id);
        user.setName(userDetails.getName()); //Atualiza o nome do usuário
        user.setEmail(userDetails.getEmail()); //Atualiza o email do usuário
        return userRepository.save(user);
    }

    public void deleteUser(Long id) { //Método que deleta um usuário
        User user = getUserById(id);
        userRepository.delete(user); //Deleta o usuário
    }
}
