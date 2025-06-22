package com.aa2.GamePlatform.dto; // ESTE É O PACOTE CORRETO PARA A SUA CLASSE ADMINDTO

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdminDTO {

    private Long id;

    @NotBlank(message = "{validation.name.not_blank}") // Usando chave i18n
    @Size(min = 3, max = 100, message = "{validation.name.size}") // Usando chave i18n
    private String nome;

    @NotBlank(message = "{validation.email.not_blank}") // Usando chave i18n
    @Email(message = "{validation.email.invalid}") // Usando chave i18n
    @Size(max = 100, message = "{validation.email.size}") // Usando chave i18n
    private String email;

    // Senha é obrigatória apenas para novos registros (no controller)
    // No DTO, só validamos o tamanho mínimo se não estiver vazio
    @Size(min = 6, message = "{validation.password.size}") // Usando chave i18n
    private String senha;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}