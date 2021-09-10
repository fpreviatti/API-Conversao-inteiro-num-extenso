# Aplicativo para realizar a conversão do path de um número inteiro retornando o valor escrito por extenso. Ex: localhost:8080/1000  -> Retorna um arquivo JSON com chave "extenso" e conteúdo "mil"

Realize o git clone do projeto e execute os seguintes comandos no git bash aberto:

1)mvn spring-boot:build-image (criar a imagem docker do projeto spring, precisa estar na pasta raiz do projeto)

2)docker run -d -p 8080:8080 conversor:0.0.1-SNAPSHOT (rodar a aplicação na porta 8080, irá gerar um container listar com o comando docker ps)

3)docker ps (listar os containers)

4)docker kill (encerrar container / aplicação)

Testes:

Com um software de testes instalado na máquina e com a aplicação rodando, rode um get para o endereço http://localhost:8080/ inserindo o numero no final da URL, conforme o exemplo abaixo feito utilizando o software de testes Postman:

<img src="https://github.com/fpreviatti/API-Conversao-inteiro-num-extenso/blob/main/postman.png" width="400px" height="auto">
