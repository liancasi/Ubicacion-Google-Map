# Location Tracking System

Sistema web para gestionar personas y sus ubicaciones utilizando Google Maps. Permite registrar, actualizar y eliminar personas, así como visualizar sus ubicaciones de forma individual o grupal en el mapa.
Tecnologías

## Frontend:

* React.js
* Google Maps API

## Backend:

* Spring Boot
* MySQL

## Instalación
### Frontend
bashCopycd FrontEnd
* npm install
* npm start
### Backend

Configurar MySQL:

propertiesCopy# application.properties
* spring.datasource.url=jdbc:mysql://localhost:3306/db_location
* spring.datasource.username=root
* spring.datasource.password=your_password
* spring.jpa.hibernate.ddl-auto=update
* spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

Ejecutar el proyecto:

bashCopycd BackEnd
* mvn clean install
* mvn spring-boot:run
## Funcionalidades Principales

* Gestión CRUD de personas
* Registro de ubicaciones
* Visualización en mapa (individual/grupal)
* Actualización en tiempo real de ubicaciones

Variables de Entorno Necesarias

* REACT_APP_GOOGLE_MAPS_API_KEY: API key de Google Maps
* MYSQL_HOST: Host de MySQL
* MYSQL_DATABASE: Nombre de la base de datos
* MYSQL_USER: Usuario de MySQL
* MYSQL_PASSWORD: Contraseña de MySQL
