FROM openjdk:21

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Copy the .env file into the image
COPY .env .env

ENTRYPOINT ["java","-jar","/app.jar"]