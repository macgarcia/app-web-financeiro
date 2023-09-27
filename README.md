# Software Financeiro
Projeto para fim pessoal lhe proporcionando o controle das finanças mensais.

Softwares utilizados:

    Java 1.8
    Apache Tomcat 8.5
    MySQL 8
    Java Server Faces 2
    Docker

## Utilização

```bash
# Clonar o repositório
$ git clone https://github.com/macgarcia/app-web-financeiro.git
```

## Configuração da base de dados
Configure de acordo com suas necessidades.

```xml
<property name="javax.persistence.jdbc.url"
    value="jdbc:mysql://seu-host:sua-porta/sua-base" />
<property name="javax.persistence.jdbc.user" value="seu-usuario" />
<property name="javax.persistence.jdbc.password"
    value="sua-senha" />
```

## Montagem de container
Se preferir pode montar uma imagem do projeto.
Certifique que configurou a conexão com o banco de dados corretamente.


Comandos para montar a imagem

```bash
# Esteja no diretório do projeto
$ cd app-web-financeiro

#Limpe o projeto
$ mvn clean

# Construa o projeto
$ mvn install -DskipTests

# Comando para criar a imagem para o projeto
$ docker build -t app-web-contas .

# Subindo o container para uso
$ docker run --name app-contas -d -p 9090:8080 app-web-contas
```

Depois desses passos acesse o navegador e digite:
    
    http://localhost:9090/contas

