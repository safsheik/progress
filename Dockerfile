# Build fatjar using maven.
FROM maven:3-jdk-11 as build

ADD settings.xml /root/.m2/settings.xml
ADD . /code
WORKDIR /code
#RUN mvn --settings ./settings.xml clean package  -Dmaven.test.failure.ignore=true -Dcheckstyle.skip
RUN mvn clean package -Dmaven.test.failure.ignore=true -Dcheckstyle.skip -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true

# Run the fatjar from the previous maven build stage.
FROM openjdk:11-slim

#TODO: Check if the following cert copy/import steps are necessary for BCM (these steps are directly copied from extracredit-rewards)
COPY .build/certs/ $JAVA_HOME/lib/security
RUN cd $JAVA_HOME/lib/security \
    && keytool -cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias cypher -file cypher.crt \
    && keytool -cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias elite -file elite.cer \
    && keytool -cacerts -storepass changeit -noprompt -trustcacerts -importcert -alias pgxca -file VWSL-PGXCA01.crt

COPY --from=build /code/bcm-web/target/bcm-web-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app

EXPOSE 8080

CMD java -jar app.jar -Dserver.port=$PORT -Duser.timezone=America/Denver
