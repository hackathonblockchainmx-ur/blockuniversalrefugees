# Novahemjo blockchain

"Nova hemjo" es en esperanto "Nuevo Hogar".

Novahemjo blockchain es un servicio REST service para la interaccion de dispositivos moviles o pagina HTML que necesite guardar la informacion de los refugiados, tales como documentos de identificacion, imagenes, etc. y asegurar el claim del documento y poder ser revisado por una autoridad tercera que le den permisos para poder revisar y autentificar sus documentos.

Esta basada sobre la red ethereum y tambien la red p2p de IPFS para guardar los documentos en de los refugidos en la blockchain.

Se utilizan los contratos en Solidity de EthDoc y el estandar de ERC725 de identificacion y claims para poder guardar los documentos de los refugiados dentro de la carpeta de contracts.

La forma en que funciona es, se crea un wallet dentro de la blockchain permisionada para el refugiado, este envia una imagen hacia el endpoint de documentos en base 64, se guarda la imagen, se crea un registro en la blockchain del mismo, se guarda la imagen en el nodo de IPFS, se genera un claim del documento enviado usando la llave privada del wallet del refugiado anteriormente creada por el mismo refugiado y este claim puede ser despues reclamado por un tercero, debidamente registrado en la plataforma y asegurado su identidad, una vez que a traves de su llave publica de su wallet, se valida el claim y se intercambia las llaves para que la autoridad o tercero pueda accesar al documento. 