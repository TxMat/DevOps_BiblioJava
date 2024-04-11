FROM openjdk:21

COPY *.jar /biblio/

WORKDIR /biblio/

CMD ["java","InittoDelete"]