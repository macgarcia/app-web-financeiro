# Use uma imagem base do Apache Tomcat 8 (a versão 8 é compatível com o Java 8)
FROM tomcat:8-jre8

# Copie o seu arquivo WAR para o diretório de implantação do Tomcat
COPY target/pessoal-financeiro-jsf-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

RUN mv /usr/local/tomcat/webapps/pessoal-financeiro-jsf-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/contas.war

# Exponha a porta 8080, que é a porta padrão do Tomcat
EXPOSE 8080
