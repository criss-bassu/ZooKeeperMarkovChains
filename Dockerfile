FROM openjdk:17
WORKDIR /
ADD prPractica3.jar prPractica3.jar
# Crea el directorio para el fichero medidas.txt
RUN mkdir /fichero
# Define environment variable
ENV NAME PRACTICA2_HOST
ENV NAME ZOOKEEPER_HOST
CMD java -jar prPractica3.jar