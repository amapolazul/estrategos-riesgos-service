CREATE TABLE IF NOT EXISTS responsables(
    "id" BIGSERIAL PRIMARY KEY,
    "email" VARCHAR NOT NULL
);

INSERT INTO responsables (id, email) va lues (1, 'test@test.com.co');

CREATE TABLE IF NOT EXISTS procesos (
    "proceso_id" BIGSERIAL PRIMARY KEY,
    "proceso_padre_id" INT,
    "proceso_nombre" VARCHAR NOT NULL,
    "proceso_Descripcion" VARCHAR NOT NULL,
    "proceso_codigo" VARCHAR NOT NULL,
    "proceso_tipo" VARCHAR NOT NULL,
    "proceso_responsable_id" BIGSERIAL NOT NULL REFERENCES responsables(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "proceso_documento" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS productos_servicios (
    "producto_servicio_id" BIGSERIAL PRIMARY KEY,
    "proceso_id" BIGSERIAL NOT NULL REFERENCES procesos(proceso_id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "producto_servicio_nombre" VARCHAR NOT NULL,
    "producto_caracteristicas" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS proceso_caracterizaciones(
    "caraceterizacion_id" BIGSERIAL PRIMARY KEY,
    "proceso_id" BIGSERIAL NOT NULL REFERENCES procesos(proceso_id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "procedimiento_nombre" VARCHAR NOT NULL,
    "procedimiento_codigo" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS proceso_documentos(
    "procedimiento_documento_id" BIGSERIAL PRIMARY KEY,
    "caraceterizacion_id" BIGSERIAL NOT NULL REFERENCES proceso_caracterizaciones(caraceterizacion_id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "procedimiento_documento_nombre" VARCHAR NOT NULL,
    "procedimiento_documento_descripcion" VARCHAR NOT NULL,
    "procedimiento_documento_codigo" VARCHAR NOT NULL,
    "procedimiento_documento_arch" VARCHAR NOT NULL
);