-- ===================================
-- DDL - Sistema de Análisis de Divisas
-- ===================================

-- Ejecutar primero
CREATE DATABASE Monedas;

-- Para las siguientes instrucciones, cambiar la conexión a la base de datos Monedas

-- ===================================
-- Tabla: MONEDA
-- ===================================

CREATE TABLE Moneda (
    Id SERIAL PRIMARY KEY,
    Moneda VARCHAR(100) NOT NULL,
    Sigla VARCHAR(5) NOT NULL,
    Simbolo VARCHAR(5) NULL,
    Emisor VARCHAR(100) NULL,
    Imagen BYTEA NULL
);

CREATE UNIQUE INDEX ixMoneda ON Moneda(Moneda);

-- ===================================
-- Tabla: PAIS
-- ===================================

CREATE TABLE Pais (
    Id SERIAL PRIMARY KEY,
    Pais VARCHAR(100) NOT NULL,
    CodigoAlfa2 VARCHAR(2) NOT NULL,
    CodigoAlfa3 VARCHAR(3) NOT NULL,
    IdMoneda INT NOT NULL,
    CONSTRAINT FK_Pais_Moneda FOREIGN KEY (IdMoneda) REFERENCES Moneda(Id)
);

CREATE UNIQUE INDEX ixPais ON Pais(Pais);
CREATE INDEX ixPaisMoneda ON Pais(IdMoneda);

-- ===================================
-- Tabla: CAMBIOMONEDA
-- ===================================

CREATE TABLE CambioMoneda (
    Id SERIAL PRIMARY KEY,
    IdMoneda INT NOT NULL,
    Fecha DATE NOT NULL,
    Cambio DECIMAL(18, 6) NOT NULL,
    CONSTRAINT FK_CambioMoneda_Moneda FOREIGN KEY (IdMoneda) REFERENCES Moneda(Id)
);

CREATE INDEX ixCambioMonedaFecha ON CambioMoneda(IdMoneda, Fecha);
CREATE UNIQUE INDEX ixCambioMonedaUnico ON CambioMoneda(IdMoneda, Fecha);

-- ===================================
-- Comentarios de las tablas
-- ===================================

COMMENT ON TABLE Moneda IS 'Catálogo de monedas del mundo';
COMMENT ON TABLE Pais IS 'Catálogo de países y su moneda oficial';
COMMENT ON TABLE CambioMoneda IS 'Histórico de cambios de moneda respecto al dólar';

COMMENT ON COLUMN CambioMoneda.Cambio IS 'Valor de cambio de la moneda respecto al dólar USD';
