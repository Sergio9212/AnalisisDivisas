--Ejecutar primero

CREATE DATABASE Monedas;
 
--Para las siguientes instrucciones, se debe cambiar la conexión
 
/* Crear tabla MONEDA */

CREATE TABLE Moneda( 

	Id SERIAL PRIMARY KEY,

	Moneda VARCHAR(100) NOT NULL,

	Sigla VARCHAR(5) NOT NULL,

	Simbolo VARCHAR(5) NULL,

	Emisor VARCHAR(100) NULL,

	Imagen BYTEA NULL

	);
 
/* Crear indice para MONEDA

	ordenado por MONEDA */

CREATE UNIQUE INDEX ixMoneda

	ON Moneda(Moneda);
 