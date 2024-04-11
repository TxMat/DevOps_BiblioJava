FROM openjdk:21

COPY ./target/*.jar /biblio/

WORKDIR /biblio/

CMD ["java","InittoDelete"]