# Novahemjo blockchain

"Nova hemjo" es en esperanto "Nuevo Hogar".

Novahemjo blockchain es un servicio REST service para la interaccion de dispositivos moviles o pagina HTML que necesite guardar la informacion de los refugiados, tales como documentos de identificacion, imagenes, etc. y asegurar el claim del documento y poder ser revisado por una autoridad tercera que le den permisos para poder revisar y autentificar sus documentos.

Esta basada sobre la red ethereum y tambien la red p2p de IPFS para guardar los documentos en de los refugidos en la blockchain.

Se utilizan los contratos en Solidity de EthDoc y el estandar de ERC725 de identificacion y claims para poder guardar los documentos de los refugiados dentro de la carpeta de contracts.

La forma en que funciona es, se crea un wallet dentro de la blockchain permisionada para el refugiado, este envia una imagen hacia el endpoint de documentos en base 64, se guarda la imagen, se crea un registro en la blockchain del mismo, se guarda la imagen en el nodo de IPFS, se genera un claim del documento enviado usando la llave privada del wallet del refugiado anteriormente creada por el mismo refugiado y este claim puede ser despues reclamado por un tercero, debidamente registrado en la plataforma y asegurado su identidad, una vez que a traves de su llave publica de su wallet, se valida el claim y se intercambia las llaves para que la autoridad o tercero pueda accesar al documento.

### Prerequisitos

Java JDK 1.8
Maven

Ethereum blockchain permisionada.
IPFS nodo privado.

## Instruciones

Para poder compilar el proyecto se utiliza maven, en linea de comando se ejecuta lo sigiente en el directorio raiz del proyecto:

```
mvn clean compile assembly:single
```

Esto generara un archivo jar en el subdirectorio de target con el nombre de blockchain.rest.server-0.0.1-SNAPSHOT-jar-with-dependencies.jar

Este jar es autoejecutable, para correrlo se llama el comando en linea de:

```
java -jar blockchain.rest.server-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Por default buscara en el host actual por el nodo de la blockchain de ethereum en el puerto 7100 y por el nodo de IPFS privado local en el puerto 5100.
El servicio REST se publicara por default en el puerto 4567 con el swagger ya publicado para ser consumido en la raiz del URL.