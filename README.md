Utilizando Java 22, mais especificamente "Liberica Standard JDK 22.0.1+12 x86 64 for Linux".
Gradlew para compilar e rodar.
Spring boot Initialzr para geração dos arquivos base.
Spring versão 3.3.1 para criação das APIs


Para rodar diretamente do gradlew basta "./gradlew bootRun"
Para criar uma build jar basta rodar "./gradlew build". O arquivo .jar será criado em "build/libs", certifique de que o arquivo "preload.csv" se encontra no mesmo diretório do arquivo .jar
No Windows basta rodar "./gradlew.bat build". Foi testado no Windows 11 com JDK 22.0.1.


O projeto também conta com um alguns testes para exemplificar, todos contidos no arquivo dentro na pasta ./src/test. Para rodar a bateria de testes basta usar "./gradlew test".


Este repositório ja conta com um arquivo .jar com nome de "final.jar" compilado usando as configurações citadas nas primeiras linhas.


GET:
  Busca no bando de dados, clientes que possuam as informações passadas no parametro name e/ou email. Trabalha filtrando os registro pelo nome e depois por email, retornando ao usuário um objeto JSON com todos os registros que batam as informações. Caso nenhum parametro for passado, retorna todos os usuários do banco.
  
  Ex: curl http://localhost:8080/records
  Retorna todos os registros do banco.


POST: 
  Recebe no formato JSON no body da requisição, um objeto Pessoa, com todas as informacoes para ser diretamente adicionado ao banco de dados. O objeto JSON precisa necessariamente conter 1 CPJ/CNPJ único e diferente dos que já contém no banco de dados, um nome/razão social que pode ser igual a algum no banco de dados, um email que pode ser igual a algum registro já no banco de dados e uma lista de endereços vazia ou com N endereços.
  
  EX: curl -X POST http://localhost:8080/records -H "Content-Type: application/json" -d '{"name": "nomeaqui", "cpf_cnpj":"12345678901", "email":"emailaqui", "address":["ende", "reco"]}'
  Adiciona um registro com os campos passados.


PUT: 
  Recebe 1 JSON contendo, sendo o cpf/cnpj obrigatorio, os outros campos do JSON podem ser vazios, sendo assim, considerando que não serão modificados, os campos que estiverem com algum valor serão considerados como novos valores para o regristro.

  Ex: curl -X PUT http://localhost:8080/records -H "Content-Type: application/json" -d '{"name": "nomealterado", "cpf_cnpj":"<cpfoucnpj>", "email":"emailalterado", "address":["um", "dois"]}'
  Modifica um registro de cpf/cnpj = <cpfoucnpj> com os campos passados


DELETE: 
  Recebe apeans cpf/cnpj e deleta o registro do banco de dados.
  Ex: curl -X DELETE http://localhost:8080/records/<cpfoucnpj>
  Deleta o registro de cpf/cnpj passado