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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

// POST example
// curl -X POST http://localhost:8080/records -H "Content-Type: application/json" -d '{"name": "claudio", "cpf_cnpj":"1", "email":"aaa", "address":["um", "dois"]}'

@SpringBootApplication
@RestController
public class CrudApisApplication {

  // Para simular um banco de dados
  public static List<Pessoa> Database = new ArrayList<Pessoa>();

  // Carrega o banco de dados com alguns registros
  private static void PreloadDatabase() {
      // Assume que o arquivo preload.csv esta na mesma pasta que o arquivo .jar
      File file = new File("./preload.csv");

      try {
        // Inicializaz o leitor 
        Scanner Reader = new Scanner(file);
        // Enquanto houver linhas no arquivo, adiciona a pessoa no banco de dados

        // Pula a primeira linha, que contem os nomes das colunas
        Reader.nextLine();

        while (Reader.hasNextLine()) {
          String line = Reader.nextLine();
          String[] fields = line.split(";");

          String name = fields[1] + " " + fields[2];
          String cpf_cnpj = fields[4];
          String email = fields[3];
          
          Pessoa p = new Pessoa(name, cpf_cnpj, email, new String[0]);
          Database.add(p);
          //System.out.println(Reader.nextLine());
        }
        Reader.close();
      // Caso o arquivo nao seja encontrado
      } catch (FileNotFoundException e) {
        System.out.println("Arquivo nao encontrado!");
        e.printStackTrace();
      }
  }

  // Carrega o banco de dados e inicia a aplicacao
  public static void main(String[] args) {
    PreloadDatabase();  
    SpringApplication.run(CrudApisApplication.class, args);

  }

  // Metodo GET, recebe nome ou email do registro a ser buscado, retorna JSON com todos os matchs
  @GetMapping("/records")
  public String GetRecord(@RequestParam(value = "name", defaultValue = "") String name, 
                          @RequestParam(value = "email", defaultValue = "") String email) {
    
    // Caso nome nem email tenha sido passado, retorna todos os registros

    // Iniciliza a lista de resultado
    List<Pessoa> result = Database;

    // Se o nome foi passado, busca por nome
    if (name.length() != 0){
      result = SearchByNameIn(name, Database);
      if (result.size() == 0) {
        return "Nenhuma pessoa encontrada!\n";
      }
    }

    // Se o email foi passado, filtra a lista de pessoas encontradas baseada no email
    if (email.length() != 0) {
      result = SearchByEmailIn(email, result);
    }

    // Caso nao tenha encontrado nenhuma pessoa
    if (result.size() == 0) {
      return "Nenhuma pessoa encontrada!\n";
    }
    // Formata a lista de pessoas encontradas em JSON e retorna
    String response = "{\n\"records\": [";
    String people = result.get(0).toJson();

    for (int i = 1; i < result.size(); i++) {
      people += ", " + result.get(i).toJson();
    }

    response += people + "]}\n";
    return response;
  }

  // Metodo GET, recebe o CPF/CNPJ do registro a ser buscado, retorna string do registro, considera id como CPF ou CNPJ
  @GetMapping("/records/{id}")
  public String GetRecordByCpfCnpj(@PathVariable String id) {
    Pessoa p = SearchByCpfCnpjIn(id, Database);
    if (p == null) {
      return "Nenhuma pessoa encontrada!\n";
    }
    return p.toString() + "\n";
  }

  // Metodo POST, recebe JSON com os dados da pessoa a ser adicionada
  @PostMapping("/records")
    public String PostRecord(@RequestBody Pessoa pessoa){
    // Objeto Pessoa ja é criado automaticamente

    // Certifica que os campos obrigatorios nao estao vazios
    if (pessoa.getName().length() == 0) return "Nome/Razao social nao pode ser vazio!\n";
    if (pessoa.getCpf_cnpj().length() == 0) return "CPF/CNPJ nao pode ser vazio!\n";
    if (pessoa.getEmail().length() == 0) return "Email nao pode ser vazio!\n";

    // Caso o CPF/CNPJ ja exista no banco de dados
    if (SearchByCpfCnpjIn(pessoa.getCpf_cnpj(), Database) != null) return "CPF/CNPJ ja cadastrado!\n";
    
    // Adiciona a pessoa no banco de dados
    Database.add(pessoa);

    // Printa o banco de dados a fim de debug
    // for (Pessoa p : Database) {
    //  System.out.println(p.toString());
    // }
    // System.out.println();

    return String.format("Pessoa de nome/razao social %s adicionada!\n", pessoa.getName());
  }

  // Metodo PUT, recebe JSON com os dados da pessoa a ser atualizada, CPF/CNPJ obrigatorio, campos vazios nao sao atualizados
  @PutMapping("/records")
  public String PutRecord(@RequestBody Pessoa pessoa){
    // Caso o campo do CPF/CNPJ esteja vazio
    if (pessoa.getCpf_cnpj().length() == 0) return "CPF/CNPJ nao pode ser vazio!\n";

    // Caso todos os campos estejam vazios
    if (pessoa.getName().length() == 0
    && pessoa.getEmail().length() == 0
    && pessoa.getAddress().length == 0) return "Nenhum dado para atualizar!\n";

    // Caso nao exista pessoa com o CPF/CNPJ 
    Pessoa p = SearchByCpfCnpjIn(pessoa.getCpf_cnpj(), Database);
    if (p == null) return "Nenhuma pessoa encontrada!\n";

    // Atualiza os campos da pessoa
    if (pessoa.getName().length() != 0) p.setName(pessoa.getName());
    if (pessoa.getEmail().length() != 0) p.setEmail(pessoa.getEmail());
    if (pessoa.getAddress().length != 0) p.setAddress(pessoa.getAddress());

    return String.format("Cadastro de CPF/CNPJ %s atualizada!\n", pessoa.getCpf_cnpj());
  }

  // Metodo DELETE, recebe o CPF/CNPJ do registro a ser deletado
  @DeleteMapping("/records/{id}")
  public String DeleteRecord(@PathVariable String id) {
    if (RemoveByCpfCnpj(id) == 0) return "Nenhuma pessoa encontrada!\n";

    return String.format("Registro com CPF/CNPJ %s deletado!\n", id);
  }



  // Retorna todas as pessoas que possuem nome igual ao parametro name
  private List<Pessoa> SearchByNameIn(String name, List<Pessoa> database) {
    // Lista dinamica
    List<Pessoa> result = new ArrayList<Pessoa>();
    
    // Para cada pessoa no banco de dados, se o nome for igual ao parametro, adiciona na lista
    for (Pessoa p : database) {
      if (p.getName().equals(name)) {
        result.add(p);
      }
    }
    return result;
  }

  // Retorna a PF ou PJ que possui o CPF/CNPJ igual ao parametro cpf_cnpj
  private Pessoa SearchByCpfCnpjIn(String cpf_cnpj, List<Pessoa> database) {
    // Para cada pessoa no banco de dados, se o cpf_cnpj for igual ao parametro, retorna a pessoa
    for (Pessoa p : database) {
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

  // Retorna todas as pessoas que possuem email igual ao parametro email
  private List<Pessoa> SearchByEmailIn(String email, List<Pessoa> database) {
    // Lista dinamica
    List<Pessoa> result = new ArrayList<Pessoa>();
    
    // Para cada pessoa no banco de dados, se o email for igual ao parametro, adiciona na lista
    for (Pessoa p : database) {
      if (p.getEmail().equals(email)) {
        result.add(p);
      }
    }
    return result;
  }

}


