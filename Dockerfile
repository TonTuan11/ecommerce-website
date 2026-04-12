
# Packaged as a Docker Image
#1 cop src code
#2 build project
#3 creat file jar
#4 target/xxx.jar
FROM maven:3.9-eclipse-temurin-21 AS build

#create folder app
WORKDIR /app

#copy all source code local to container
COPY . .


#build maven -> .jar
RUN mvn clean package -DskipTests


# runtime image
#copy .jar ->image

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

#container run, run java, -jar, app,jar
# read 1.application.yml, 2.application-{profile}.yml

ENTRYPOINT ["java","-jar","app.jar"]