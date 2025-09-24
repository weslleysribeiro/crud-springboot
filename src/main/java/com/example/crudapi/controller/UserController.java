package com.example.crudapi.controller;

import com.example.crudapi.model.User; //Importação do modelo de usuário
import com.example.crudapi.service.UserService; //Importação do serviço de usuários
import org.springframework.http.ResponseEntity; //Importação do ResponseEntity
import org.springframework.http.HttpStatus; //Importação do HttpStatus
import org.springframework.web.bind.annotation.*; //Importação do RestController e RequestMapping
import jakarta.validation.Valid; //Importação da validação

import java.util.List; //Importação da lista

@RestController //Anotação para indicar que a classe é um controlador
@RequestMapping("/api/users") //Anotação para indicar o caminho da API
public class UserController { //Classe que implementa os métodos do controlador de usuários

    private final UserService userService;

    public UserController(UserService userService) { //Construtor que recebe o serviço de usuários
        this.userService = userService;
    }

    // Create
    @PostMapping //Anotação para indicar que o método é um POST
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) { //Método que cria um usuário
        User saved = userService.createUser(user); //Cria o usuário
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); //Retorna o usuário criado
    }

    // Read all
    @GetMapping //Anotação para indicar que o método é um GET
    public List<User> getAllUsers() { //Método que retorna todos os usuários
        return userService.getAllUsers(); //Retorna todos os usuários
    }

    // Read by id
    @GetMapping("/{id}") //Anotação para indicar que o método é um GET
    public ResponseEntity<User> getUserById(@PathVariable Long id) { //Método que retorna um usuário por id
        User user = userService.getUserById(id); //Retorna o usuário por id
        return ResponseEntity.ok(user); //Retorna o usuário encontrado
    }

    // Update
    @PutMapping("/{id}") //Anotação para indicar que o método é um PUT
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {  //Método que atualiza um usuário
        User updated = userService.updateUser(id, userDetails); //Atualiza o usuário
        return ResponseEntity.ok(updated); //Retorna o usuário atualizado
    }

    // Delete
    @DeleteMapping("/{id}") //Anotação para indicar que o método é um DELETE
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) { //Método que deleta um usuário
        userService.deleteUser(id); //Deleta o usuário
        return ResponseEntity.noContent().build(); //Retorna o usuário deletado
    }
}
