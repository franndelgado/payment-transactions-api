# API de transacciones de pago

## Descripción 

> Este proyecto es una API RESTful para la gestión segura y eficiente de transacciones de pago. Proporciona endpoints para crear transacciones, ver su estado y consultar el historial de transacciones aprobadas.

---

## Tabla de Contenidos

1. [Tecnologías y Herramientas](#tecnologías-y-herramientas)  
2. [Servicios Involucrados](#servicios-involucrados)  
3. [Instalación](#instalación)  
4. [Base de datos H2](#Base-de-datos-H2)
5. [Endpoints](#endpoints)  
6. [Ejemplos de Peticiones y Respuestas](#ejemplos-de-peticiones-y-respuestas)
7. [Documentación Swagger](#documentación-swagger)

---

## Tecnologías y Herramientas

- **Lenguaje de programación:** Java  
- **Frameworks:** Spring Boot 
- **Base de datos:** H2 en memoria 
- **Librerías adicionales:** Lombok

---

## Servicios Involucrados

- **transaction-service:** Creación y obtención de transacciones
- **currency-service:** Manejo de múltiples monedas
- **Base de datos principal:** H2

---

## Instalación

Clona el repositorio:  

```bash
git clone https://github.com/franndelgado/payment-transactions-api
```
```bash
cd payment-transactions-api
```

Luego abrir el proyecto en un Entorno de Desarrollo (IDE) como VS Code y ejecutarlo.

---

## Base de datos H2
Para visualizar la base de datos se debe:
1. Ingresar a http://localhost:8080/h2-console
2. Colocar "jdbc:h2:mem:paymentsdb" en la sección JDBC URL.
3. Colocar "user" en User name.
4. Hacer click en Connect.

### Endpoints

A continuación, se describen los principales endpoints de la API, incluyendo el método HTTP, la URL, la descripción y un ejemplo de la petición y respuesta esperada.

#### `POST /api/transactions`

- **Descripción:** Recibe un Transaction Request con los valores de la transacción, valida que los datos esten presentes y correctos. Una vez validado, crea una nueva transaccion asignandole un estado inicial y devuelve un Transaction Response con el código 201 CREATED.
- **Método:** `POST`

**Cuerpo de la Solicitud (Request Body)**

Este endpoint espera un objeto JSON con los siguientes campos para crear una transacción.

Ejemplo:
```json
{ 
    "user_id": "113411", 
    "amount": "23487932", 
    "currency": "EUR",
    "bank_code": "BANK123", 
    "recipient_account": "DE343543543534543" 
}
```
---

#### `GET /api/{transactionId}/status`

- **Descripción:** Recibe un Transaction Id por path variable y retorna el estado de la transación con el código 200 OK.
- **Método:** `GET`

#### `GET /api/approved`

- **Descripción:** 
- **Método:** `GET

---
## Documentación Swagger

Una vez levantado el proyecto se puede visualizar su documentación Swagger en el link: http://localhost:8080/swagger-ui/index.html#