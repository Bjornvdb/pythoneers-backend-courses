# cat Dockerfile
FROM openjdk:13-oracle
VOLUME /tmp
EXPOSE 8000
RUN mvn clean install
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/team_15-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar"]