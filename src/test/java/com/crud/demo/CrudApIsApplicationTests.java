package com.crud.demo;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;


import static org.assertj.core.api.Assertions.assertThat;





@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CrudApisApplicationTests {

	//@Test
	//void contextLoads() {
	//}

	@LocalServerPort	
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;



	@Test
	public void testPost() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Pessoa pessoaObj = new Pessoa("claudio", "1", "aaa", new String[]{"um", "dois"});
		HttpEntity<String> request = new HttpEntity<String>(pessoaObj.toJson(), headers);
		
		System.out.println(pessoaObj.toJson());

		assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/records", request, String.class)).contains("Pessoa de nome/razao social claudio adicionada!");
	}


	@Test
	public void testDelete() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Pessoa pessoaObj = new Pessoa("claudio", "1", "aaa", new String[]{"um", "dois"});
		HttpEntity<String> request = new HttpEntity<String>(pessoaObj.toJson(), headers);
		
		System.out.println(pessoaObj.toJson());

		assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/records", request, String.class)).contains("CPF/CNPJ ja cadastrado!");

		assertThat(this.restTemplate.exchange("http://localhost:" + port + "/records/1", org.springframework.http.HttpMethod.DELETE, request, String.class).getBody()).contains("Registro com CPF/CNPJ 1 deletado!");
	}

	@Test
	public void testGetAll() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/records", String.class)).contains("Nenhuma pessoa encontrada!");
	}

}
