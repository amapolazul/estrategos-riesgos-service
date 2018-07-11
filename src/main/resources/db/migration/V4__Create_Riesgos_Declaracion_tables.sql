CREATE TABLE IF NOT EXISTS respuestas_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "respuesta_riesgo_nombre" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS estuatus_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "estatus_riesgo_nombre" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS efectividad_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "efectividad_nombre" VARCHAR NOT NULL,
    "puntaje" INTEGER NOT NULL,
    "descripcion" INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS declaracion_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "proceso_id" BIGSERIAL NOT NULL REFERENCES procesos(proceso_id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "ejercicio_riesgo_id" BIGSERIAL NOT NULL REFERENCES ejercicios_evaluacion_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "tipo_riesgo_id" BIGSERIAL NOT NULL REFERENCES tipo_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "respuesta_riesgo_id" BIGSERIAL NOT NULL REFERENCES respuestas_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "estatus_riesgo_id" BIGSERIAL NOT NULL REFERENCES estuatus_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "factor_riesgo" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL,
    "probabilidad" VARCHAR NOT NULL,
    "historico" VARCHAR NOT NULL,
    "impacto" VARCHAR NOT NULL,
    "severidad" VARCHAR NOT NULL,
    "riesgo_residual" VARCHAR NOT NULL,
    "fecha_creacion" BIGINT NOT NULL,
    "fecha_actualizacion" BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS causas_declaracion_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "probabilidad_riesgo_id" BIGSERIAL NOT NULL REFERENCES probabilidad_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "declaracion_riesgo_id" BIGSERIAL NOT NULL REFERENCES declaracion_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "causa" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS efectos_declaracion_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "impacto_riesgos_id" BIGSERIAL NOT NULL REFERENCES impacto_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "declaracion_riesgo_id" BIGSERIAL NOT NULL REFERENCES declaracion_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "impacto" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS controles_declaracion_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "efectividad_riesgos_id" BIGSERIAL NOT NULL REFERENCES efectividad_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "declaracion_riesgo_id" BIGSERIAL NOT NULL REFERENCES declaracion_riesgos(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "control" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);