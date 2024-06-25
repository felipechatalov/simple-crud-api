Utilizando Java 22, mais especificamente "Liberica Standard JDK 22.0.1+12 x86 64 for Linux".
Gradlew para compilar e rodar.
Tambem foi utilizado Spring boot Initialzr para geração dos arquivos base
e java spring para a criação das apis
para criar uma build jar basta rodar ./gradlew build



GET:
  Busca no bando de dados, clientes que possuam as informações passadas nos parametros name, cpf/cnpj, email ou um dos endereços. Podendo ser passado 1 ou mais parametros.

POST: Recebe no formato JSON no body da requisição, um objeto Pessoa, com todas as informacoes para ser diretamente adicionado ao banco de dados.

PUT: Recebe ao menos 2 parametros, sendo o cpf/cnpj obrigatorio, os outros parametros recebidos serao usados para atualizar o cadastro do cpf/cnpj recebido.

DELETE: Recebe apeans cpf/cnpj e deleta o registro do banco de dados.
    