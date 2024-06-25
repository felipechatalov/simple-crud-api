package com.crud.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//curl -X POST http://localhost:8080/client -H "Content-Type: application/json" -d '{"name": "claudio", "cpf_cnpj":"1", "email":"aaa", "address":["um", "dois"]}'

@SpringBootApplication
@RestController
public class CrudApisApplication {

  public static List<Pessoa> Database = new ArrayList<Pessoa>();

  public static void main(String[] args) {
    SpringApplication.run(CrudApisApplication.class, args);

  }

  @GetMapping("/client")
  public String GetClient(@RequestParam(value = "name", defaultValue = "") String name, 
                          @RequestParam(value = "email", defaultValue = "") String email) {
    if (name.length() == 0 && email.length() == 0) {
      return "Nenhum parametro de busca!\n";
    }

    List<Pessoa> result;

    if (name.length() != 0){
      result = SearchByName(name);
      if (result.size() == 0) {
        return "Nenhuma pessoa encontrada!\n";
      }
    }
    else{
      result = Database;
    }

    if (email.length() != 0) {
      List<Pessoa> result2 = new ArrayList<Pessoa>();
      for (Pessoa p : result) {
        if (p.getEmail().equals(email)) {
          result2.add(p);
        }
      }
      result = result2;
    }


    // Formata a lista de pessoas encontradas em JSON
    String response = "{\n\"records\": [";

    for (Pessoa p : result) {
      response += p.toJson() + ", ";
    }
    response += "]}\n";

    return response;
  }

  @GetMapping("/client/{id}")
  public String GetClientByCpfCnpj(@PathVariable String id) {
    Pessoa p = SearchByCpfCnpj(id);
    if (p == null) {
      return "Nenhuma pessoa encontrada!\n";
    }
    return p.toString() + "\n";
  }

  @PostMapping("/client")
    public String PostClient(@RequestBody Pessoa pessoa){
    // Objeto Pessoa ja é criado automaticamente

    // Tratar todos os dados e fazer a validação
    if (pessoa.getName().length() == 0) return "Nome/Razao social nao pode ser vazio!\n";
    if (pessoa.getCpf_cnpj().length() == 0) return "CPF/CNPJ nao pode ser vazio!\n";
    if (pessoa.getEmail().length() == 0) return "Email nao pode ser vazio!\n";

    if (SearchByCpfCnpj(pessoa.getCpf_cnpj()) != null) return "CPF/CNPJ ja cadastrado!\n";
    

    // Adicionar a pessoa no banco de dados
    Database.add(pessoa);

    for (Pessoa p : Database) {
      System.out.println(p.toString());
    }
    System.out.println();

    return String.format("Pessoa de nome/razao social %s adicionada!\n", pessoa.getName());
  }

  // Considera que o JSON está completo, sem nenhum campo faltando
  @PutMapping("/client")
  public String PutClient(@RequestBody Pessoa pessoa){
    // Caso o campo do CPF/CNPJ esteja vazio
    if (pessoa.getCpf_cnpj().length() == 0) return "CPF/CNPJ nao pode ser vazio!\n";

    // Caso todos os campos estejam vazios
    if (pessoa.getName().length() == 0
    && pessoa.getEmail().length() == 0
    && pessoa.getAddress().length == 0) return "Nenhum dado para atualizar!\n";

    // Caso nao exista pessoa com o CPF/CNPJ 
    Pessoa p = SearchByCpfCnpj(pessoa.getCpf_cnpj());
    if (p == null) return "Nenhuma pessoa encontrada!\n";

    // Atualiza os campos da pessoa
    if (pessoa.getName().length() != 0) p.setName(pessoa.getName());
    if (pessoa.getEmail().length() != 0) p.setEmail(pessoa.getEmail());
    if (pessoa.getAddress().length != 0) p.setAddress(pessoa.getAddress());

    return String.format("Cadastro de CPF/CNPJ %s atualizada!\n", pessoa.getCpf_cnpj());
  }

  @DeleteMapping("/client")
  public String DeleteClient(@RequestParam(value = "id") String cpf_cnpj) {
    if (RemoveByCpfCnpj(cpf_cnpj) == 0) return "Nenhuma pessoa encontrada!\n";
    return String.format("Registro com CPF/CNPJ %s deletado!\n", cpf_cnpj);
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

  // Retorna a PF ou PJ que possui o CPF/CNPJ igual ao parametro cpf_cnpj
  private Pessoa SearchByCpfCnpj(String cpf_cnpj) {
    // Para cada pessoa no banco de dados, se o cpf_cnpj for igual ao parametro, retorna a pessoa
    for (Pessoa p : Database) {
      if (p.getCpf_cnpj().equals(cpf_cnpj)) {
        return p;
      }
    }
    return null;
  }

  // Remove a PF ou PJ que possui o CPF/CNPJ igual ao parametro cpf_cnpj
  private int RemoveByCpfCnpj(String cpf_cnpj) {
    // Para cada pessoa no banco de dados, se o cpf_cnpj for igual ao parametro, remove a pessoa
    for (Pessoa p : Database) {
      if (p.getCpf_cnpj().equals(cpf_cnpj)) {
        Database.remove(p);
        return 1;
      }
    }
    return 0;
  }

  private int IsAddressesEqual(String[] a1, String[] a2) {
    if (a1.length != a2.length) return 0;
    for (int i = 0; i < a1.length; i++) {
      if (!a1[i].equals(a2[i])) return 0;
    }
    return 1;
  }
  
}


