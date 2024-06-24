package com.crud.demo;


public class Pessoa {
  private String name;      // Nome ou Razao social
  private String cpf_cnpj;  // CPF ou CNPJ (UNICO)
  private String email;     // Email
  private String[] address; // Endereco (0 ou N enderecos)


  public Pessoa(String name, String cpf_cnpj, String email, String[] address) {
    this.name = name;
    this.cpf_cnpj = cpf_cnpj;
    this.email = email;
    this.address = address;
  }
  
  @Override
  public String toString() {
    return String.format("Nome: %s, CPF/CNPJ: %s, Email: %s, Endereco: %s", name, cpf_cnpj, email, address);
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getCpf_cnpj() {
    return cpf_cnpj;
  }

  public void setCpf_cnpj(String cpf_cnpj) {
    this.cpf_cnpj = cpf_cnpj;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String[] getAddress() {
    return address;
  }

  public void setAddress(String[] address) {
    this.address = address;
  }  

}