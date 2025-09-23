package com.example.crudapi.model; //pacote do modelo de usuário

import jakarta.persistence.*; //importação do jakarta (javax) - @entity, @table, @id, @generatedvalue, @column
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity //classe entidade que representa a tabela do banco de dados
@Table(name = "users") //tabela do banco de dados
public class User {

    @Id //chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gerar automaticamente o id
    private Long id; 

    @NotBlank(message = "Nome é obrigatório") //não pode ser vazio
    @Column(nullable = false) //não pode ser nulo
    private String name;

    @NotBlank(message = "Email é obrigatório") //não pode ser vazio
    @Email(message = "Email inválido") //email inválido
    @Column(nullable = false, unique = true) //não pode ser nulo e deve ser único
    private String email;

    public User() {} //construtor vazio

    public User(Long id, String name, String email) { //construtor com informações
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
