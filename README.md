# Desafío Tenpo

## Requerimientos de la API
1. Debes desarrollar una API REST en Spring Boot con las siguientes funcionalidades:
   1. Sign up usuarios.
   2. Login usuarios.
   3. Sumar dos números. Este endpoint debe retornar el resultado de la suma y puede ser consumido solo por usuarios logueados.
   4. Historial de todos los llamados a todos los endpoint. Responder en Json, con data paginada.
   5. Logout usuarios.
   6. El historial y la información de los usuarios se debe almacenar en una database PostgreSQL.
   7. Incluir errores http. Mensajes y descripciones para la serie 4XX.


## Prueba funcional de la API - Swagger

Se utilizó Open Api como dependencia para generar la interface web de Swagger. Mediente la misma es posible probar los distintos endpoints.

Una vez que la aplicación se encuentra corriendo, se debe acceder a:
```
   http://localhost:8080/api-docs.html
```
**Importante:** a excepción del signup y login, todos los endpoints están segurizados. Por ello:
 primero deber crearse un usuario, ejecutar el login para obtener el token, y agregar al header
el token obtenido en como Authorization.

En Swagger se puede agregar una sola vez para todos los request (botón superior derecho **Authorize**).

Si se prueba desde Postman por ejemplo, sucede lo mismo pero se debe agregar al header una key
Autorization y como valor escribir **Bearer** [token obtenido]

## Ejecución de Test de la aplicación

Simplemente se debe ejecutar:
```
./mvnw clean test
```


## Ejecutar el código

### Ejecutando desde el código fuente

Es posible ejecutar el codigo clonádolo desde el presente repositorio. Para ello tener en cuenta lo siguiente:

1. Revisar la configuración en el archivo **aplication.properties**. 

Prestar especial atención a los datos de conexión a la base de datos. 
```
spring.datasource.url=jdbc:postgresql://localhost:5432/tenpo_challenge
spring.datasource.username=postgres
spring.datasource.password=admin
```

Es posible levantar un contendor docker con la siguiente sentencia:

```
   docker run --name postgres_img -e POSTGRES_PASSWORD=postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=tenpo-challenge -p 5432:5432 -d postgres   
```

Dicho comando baja la última versiónd de postgres de docker hub y la ejecuta. A su vez, se parametrizó para que 
cree una nueva base de datos llamada **tenpo_challenge** y al correr la aplicación el esquema ya exista.

Como alternativa, se puede crear un esquema en un servidor postgres pre-existente y configurar el archivo
application.properties para referenciar a dicho motor de base de datos. 

Por último, ejecutar el comando 
```
.\mvnw spring-boot:run
```

### Crear y ejecutar como imagen de docker

Los pasos referidos a la base de datos, son análogos al paso anterior. 
La aplicación cuenta con un Dockerfile configurado para la creación de la imagen. 
Por lo que solo basta situarse en el directorio raíz de la misma y ejecutar:
```
   docker build . -t tenpo_challenge.v1 
   docker run tenpo_challenge.v1
```

### Ejecutar con docker-compose

En el directorio raíz del proyecto se encuentra el archivo **docker-compose.yaml**

A su vez, en en **docker hub** ya existe una imagen del proyecto en

 https://hub.docker.com/repository/docker/arielmiglio/tenpo_challenge-v1

Situados en el directorio raíz del proyecto, es posible ejecutar:
```
   docker-compose up -d 
```

Automáticamente se descargará la imagen anterior y se ejecutará junto con una imagen de postgres.
La aplicación se encontrará corriendo. Se podrán probar los endopints tal como se describe en 
el apartado anterior de Swagger 



## Detalles de Implementación

### Stack Tecnológico
Java 16
Spring Boot 2.5.4
Postgres 
Swagger - Open API
H2 Memory Database para ejecución de Test
Controller advice
Junit

### División por capas de la API

**Controller**: definición de todos los endpoints de la API. Documentación a través de Open Api para generación de Swagger.

**Service**: capa intermedia que permite la interacción de los controllers con el modelo y respositorio e implementa la lógica de negocio.

**Repository**: abstracción del respositorio mediante la implementación de Spring Data.

**Model**: en este caso, solo se ha modelado el usuario y un objeto para persistir cierta información de los request http.

**Manejo de excepciones**: para el manejo de excepciones se implementó **Controller Advice**.
El modelo y servicios generan solo RuntimeExceptions las cuales son capturadas y manejadas por el aspecto para dar respuesta uniforme a los endpoints Http.


