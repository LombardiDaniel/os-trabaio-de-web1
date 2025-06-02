# https://github.com/andreendo/DSW1/tree/master/Modulo02

FROM tomcat:11

RUN apt-get update && \
    apt-get -y upgrade && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /usr/local/tomcat

COPY tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
# COPY context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY target/*.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]