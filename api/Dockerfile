# Stage 1: Build the application
FROM maven:3.8.1-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
COPY --from=build /home/app/target/*.jar /usr/local/lib/my-app.jar
# The application needs a uploads folder to store the uploaded files, so we create it next to the jar file
RUN mkdir /upload
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/my-app.jar"]