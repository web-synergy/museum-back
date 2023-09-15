FROM openjdk:17
ADD target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]