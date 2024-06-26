Utilizando Java 22, mais especificamente "Liberica Standard JDK 22.0.1+12 x86 64 for Linux".
Gradlew para compilar e rodar.
Spring boot Initialzr para geração dos arquivos base.
Spring versão 3.3.1 para criação das APIs

Para rodar diretamente do gradlew basta ./gradlew bootRun
Para criar uma build jar basta rodar ./gradlew build



GET:
  Busca no bando de dados, clientes que possuam as informações passadas no parametro name. Aceitando apenas 2 parametros (name e email), os parametros trabalham
  juntos para a busca de registros que possuam ambas as informações exatas. Também trabalha recebendo apeans o nome ou apenas o email da pessoa. Caso não receba
  nenhum parametro, retorna ao usuário que nenhum parametro foi passado.

POST: 
  Recebe no formato JSON no body da requisição, um objeto Pessoa, com todas as informacoes para ser diretamente adicionado ao banco de dados. O objeto JSON precisa necessariamente
  conter 1 CPJ/CNPJ único e diferente diferente dos que já contém no banco de dados, um nome/razão social que pode ser igual a algum no banco de dados, um email que pode ser igual 
  a algum registro já no banco de dados e uma lista de endereços vazia ou com N endereços.

PUT: 
  Recebe 1 JSON contendo, sendo o cpf/cnpj obrigatorio, os outros campos do JSON podem ser vazios, sendo assim, considerando que não serão modificados, os campos que estiverem com algum valor serão considerados como novos valores para o regristro.

DELETE: 
  Recebe apeans cpf/cnpj e deleta o registro do banco de dados.
