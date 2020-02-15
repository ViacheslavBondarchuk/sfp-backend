FROM openjdk:11
ADD target/sfp-backend.jar sfp-backend.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dep","-jar","sfp-backend.jar"]
EXPOSE 8080