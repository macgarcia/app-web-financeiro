# Use uma imagem base do Apache Tomcat 8 (a versão 8 é compatível com o Java 8)
FROM tomcat:8-jre8

# Copie o seu arquivo WAR para o diretório de implantação do Tomcat
COPY target/pessoal-financeiro-jsf-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

RUN mv /usr/local/tomcat/webapps/pessoal-financeiro-jsf-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/contas.war

# Adicione as seguintes etapas para configurar o locale
RUN apt-get update && apt-get install -y locales
RUN sed -i -e 's/# pt_BR.UTF-8 UTF-8/pt_BR.UTF-8 UTF-8/' /etc/locale.gen
RUN dpkg-reconfigure --frontend=noninteractive locales
RUN update-locale LANG=pt_BR.UTF-8

# Exponha a porta 8080, que é a porta padrão do Tomcat
EXPOSE 8080

# Criando a imagem do docker
# docker build -t app-web-contas .

# Subindo o container para uso
# docker run --name app-contas -d -p 9090:8080 app-web-contas
