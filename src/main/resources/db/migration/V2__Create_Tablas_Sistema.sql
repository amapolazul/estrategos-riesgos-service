CREATE TABLE IF NOT EXISTS causas_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "causa_riesgo" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS impacto_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "impacto" VARCHAR NOT NULL,
    "puntaje" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS probabilidad_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "probabilidad" VARCHAR NOT NULL,
    "puntaje" VARCHAR NOT NULL,
    "descripcion" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS tipo_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "tipo_riesgo" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS calificacion_riesgos (
    "id" BIGSERIAL PRIMARY KEY,
    "rango_minimo" VARCHAR NOT NULL,
    "rango_maximo" VARCHAR NOT NULL,
    "color_alerta" VARCHAR NOT NULL,
    "accion" VARCHAR NOT NULL
)


