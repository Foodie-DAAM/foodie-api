FROM openjdk:13-alpine
EXPOSE 8080
COPY build/libs/foodie-*.jar /app/foodie.jar
WORKDIR /app
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=docker", "-jar", "foodie.jar"]