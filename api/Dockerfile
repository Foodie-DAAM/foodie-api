FROM openjdk:13-alpine
EXPOSE 8080
COPY build/libs/api-*.jar /app/api.jar
WORKDIR /app
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=docker", "-jar", "api.jar"]