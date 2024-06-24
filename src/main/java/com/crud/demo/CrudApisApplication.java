package com.crud.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//curl -X POST http://localhost:8080/client -H 'Content-Type: application/json' -d '{"name":"aaaaaa","cpf_cnpj":"10","email":"aaa","address":["as"]}'

@SpringBootApplication
@RestController
public class CrudApisApplication {

  public static List<Pessoa> Database = new ArrayList<Pessoa>();

  public static void main(String[] args) {
    SpringApplication.run(CrudApisApplication.class, args);

  }

  @GetMapping("/client")
  public String GetClient(@RequestParam(value = "name") String name) {
    List<Pessoa> result = SearchByName(name);
    if (result.size() == 0) {
      return "Nenhuma pessoa encontrada!";
    }

    // Formata a lista de pessoas encontradas
    String response = "Pessoas encontradas: ";
    for (Pessoa p : result) {
      response += p.toString() + ", ";
    }
    return response;
  }

  @PostMapping("/client")
    //public String PostClient(@RequestParam(value = "name") String name, @RequestParam(value = "cpf_cnpj") String cpf_cnpj, @RequestParam(value = "email") String email, @RequestParam(value = "address") String[] address){
    public String PostClient(@RequestBody Pessoa pessoa){

    // Tratar todos os dados e fazer a verificacao
    //...

    // Criar objeto pessoa
    //Pessoa p = new Pessoa(name, cpf_cnpj, email, address);
    
    // Adicionar a pessoa no banco de dados
    Database.add(pessoa);
    for (Pessoa p : Database) {
      System.out.println(p.toString());
    }
    return String.format("Pessoa de nome/razao social %s adicionada!", pessoa.getName());
  }

  @PutMapping("/client")
  public String PutClient(@RequestParam(value = "name") String name) {
    return String.format("PUT Hello %s!", name);
  }

  @DeleteMapping("/client")
  public String DeleteClient(@RequestParam(value = "name") String name) {
    return String.format("DELETE Hello %s!", name);
  }



  // Retorna todas as pessoas que possuem nome igual ao parametro name
  private List<Pessoa> SearchByName(String name) {
    // Lista dinamica
    List<Pessoa> result = new ArrayList<Pessoa>();
    
    // Para cada pessoa no banco de dados, se o nome for igual ao parametro, adiciona na lista
    for (Pessoa p : Database) {
      if (p.getName().equals(name)) {
        result.add(p);
      }
    }
    return result;
  }

}
