FROM openjdk:8-alpine

RUN apk update && apk add maven
COPY . /histogram
RUN  cd /histogram && mvn package

#this part for debug is moved into compose file, into "command":
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/project/target/demo-0.0.1-SNAPSHOT.jar"]