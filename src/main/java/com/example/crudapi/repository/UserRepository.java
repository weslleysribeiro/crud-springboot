package com.example.crudapi.repository; //pacote do repositório de usuários

import com.example.crudapi.model.User; //importação do modelo de usuário (entidade)
import org.springframework.data.jpa.repository.JpaRepository; //importação do JpaRepository (Metodos para o CRUD)
import java.util.Optional; //importação do Optional (retorna objeto ou nada, ao inves de null)

public interface UserRepository extends JpaRepository<User, Long> { //interface que extende o JpaRepository
    Optional<User> findByEmail(String email); //método que busca um usuário por email
}
