CREATE TABLE causas_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    causa_riesgo VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE impacto_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    impacto VARCHAR(255) NOT NULL,
    puntaje INTEGER NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE probabilidad_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    probabilidad VARCHAR(255) NOT NULL,
    puntaje INTEGER NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE tipo_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    tipo_riesgo VARCHAR(255) NOT NULL
);

CREATE TABLE calificacion_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre_calificacion_riesgo VARCHAR(255) NOT NULL,
    rango_minimo INTEGER NOT NULL,
    rango_maximo INTEGER NOT NULL ,
    color VARCHAR(255) NOT NULL,
    accion_tomar VARCHAR(255) NOT NULL
);