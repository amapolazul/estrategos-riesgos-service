CREATE TABLE respuestas_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    respuesta_riesgo_nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE estatus_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    estatus_riesgo_nombre VARCHAR(255) NOT NULL
);

CREATE TABLE efectividad_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    efectividad_nombre VARCHAR(255) NOT NULL,
    puntaje INTEGER NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE declaracion_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_id BIGINT NOT NULL FOREIGN KEY REFERENCES procesos(proceso_id) ON UPDATE CASCADE ON DELETE NO ACTION,
    ejercicio_riesgo_id BIGINT NOT NULL FOREIGN KEY REFERENCES ejercicios_evaluacion_riesgos(id),
    tipo_riesgo_id BIGINT NOT NULL FOREIGN KEY REFERENCES tipo_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    respuesta_riesgo_id BIGINT NOT NULL FOREIGN KEY REFERENCES respuestas_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    estatus_riesgo_id BIGINT NOT NULL FOREIGN KEY REFERENCES estatus_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    factor_riesgo VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    efectividad_controles  VARCHAR(255) NOT NULL,
    probabilidad VARCHAR(255) NOT NULL,
    historico BIT NOT NULL,
    impacto VARCHAR(255) NOT NULL,
    severidad VARCHAR(255) NOT NULL,
    riesgo_residual VARCHAR(255) NOT NULL,
    fecha_creacion BIGINT NOT NULL,
    fecha_actualizacion BIGINT NOT NULL
);

CREATE TABLE causas_declaracion_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    probabilidad_riesgo_id BIGINT FOREIGN KEY REFERENCES probabilidad_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    declaracion_riesgo_id BIGINT FOREIGN KEY REFERENCES declaracion_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    causa VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE efectos_declaracion_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    impacto_riesgos_id BIGINT FOREIGN KEY REFERENCES impacto_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    declaracion_riesgo_id BIGINT FOREIGN KEY REFERENCES declaracion_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    impacto VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE controles_declaracion_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    efectividad_riesgos_id BIGINT FOREIGN KEY REFERENCES efectividad_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    declaracion_riesgo_id BIGINT FOREIGN KEY REFERENCES declaracion_riesgos(id) ON UPDATE CASCADE ON DELETE NO ACTION,
    control VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);

INSERT INTO estatus_riesgos(estatus_riesgo_nombre) VALUES ('Pendiente');
INSERT INTO estatus_riesgos(estatus_riesgo_nombre) VALUES ('Mitigado');