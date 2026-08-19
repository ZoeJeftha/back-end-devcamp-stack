FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY ./src ./src

RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]