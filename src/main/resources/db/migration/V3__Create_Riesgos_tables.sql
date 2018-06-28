CREATE TABLE IF NOT EXISTS ejercicios_evaluacion_estatus (
    "id" BIGSERIAL PRIMARY KEY,
    "estatus" VARCHAR NOT NULL
);

INSERT INTO ejercicios_evaluacion_estatus(id, estatus) values (1, 'En Proceso');
INSERT INTO ejercicios_evaluacion_estatus(id, estatus) values (2, 'Culminado');

CREATE TABLE IF NOT EXISTS ejercicios_evaluacion_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "proceso_id" BIGSERIAL NOT NULL REFERENCES procesos(proceso_id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "estatus_id" BIGSERIAL NOT NULL REFERENCES ejercicios_evaluacion_estatus(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "descripcion" VARCHAR NOT NULL,
    "fecha_creacion_ejercicio" BIGINT NOT NULL
);