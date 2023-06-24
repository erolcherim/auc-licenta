FROM openjdk:11

COPY ./build/libs/auc-licenta-0.0.1-SNAPSHOT.jar /auc-licenta/libs/auc-licenta.jar

WORKDIR /auc-licenta/libs/

CMD ["java", "-jar","/auc-licenta/libs/auc-licenta.jar"]
