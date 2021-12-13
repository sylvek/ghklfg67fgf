FROM maven as builder

COPY src /src
COPY pom.xml /

RUN mvn -f /pom.xml clean package

FROM openjdk

COPY --from=builder /target/foobartory-1.0-SNAPSHOT.jar /foobartory-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/foobartory-1.0-SNAPSHOT.jar"]