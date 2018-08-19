CREATE TABLE ejercicios_evaluacion_estatus (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    estatus VARCHAR(255) NOT NULL
);

INSERT INTO ejercicios_evaluacion_estatus(estatus) values ('En Proceso');
INSERT INTO ejercicios_evaluacion_estatus(estatus) values ('Culminado');

CREATE TABLE ejercicios_evaluacion_riesgos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_id BIGINT NOT NULL FOREIGN KEY REFERENCES procesos(proceso_id) ON UPDATE CASCADE ON DELETE CASCADE,
    estatus_id BIGINT NOT NULL FOREIGN KEY REFERENCES ejercicios_evaluacion_estatus(id) ON UPDATE CASCADE ON DELETE CASCADE,
    descripcion VARCHAR(255),
    fecha_creacion_ejercicio BIGINT NOT NULL
);