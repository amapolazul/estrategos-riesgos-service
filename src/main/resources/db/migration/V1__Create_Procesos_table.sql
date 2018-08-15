--CREATE TABLE responsable (
--    responsable_id BIGINT IDENTITY(1,1) PRIMARY KEY,
--    usuario_id BIGINT,
--    nombre VARCHAR(255) NOT NULL,
--    cargo VARCHAR(255) NOT NULL,
--    ubicacion VARCHAR(255),
--    email VARCHAR(255),
--    notas VARCHAR(255),
--    children_count BIGINT,
--    tipo BIGINT NOT NULL,
--    grupo BIGINT NOT NULL,
--    organizacion_id BIGINT
--);

INSERT INTO responsable (nombre,cargo,email,tipo,grupo) values ('Sebastian','c','test@test.com.co',1,1);

CREATE TABLE procesos (
    proceso_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_padre_id BIGINT,
    proceso_nombre VARCHAR(255) NOT NULL,
    proceso_descripcion VARCHAR(255) NOT NULL,
    proceso_codigo VARCHAR(255) NOT NULL,
    proceso_tipo VARCHAR(255) NOT NULL,
    proceso_responsable_id BIGINT FOREIGN KEY REFERENCES responsable(responsable_id) ON UPDATE CASCADE ON DELETE CASCADE,
    proceso_documento VARCHAR(255) NOT NULL
);


CREATE TABLE productos_servicios (
    producto_servicio_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_id BIGINT FOREIGN KEY REFERENCES procesos(proceso_id) ON UPDATE CASCADE ON DELETE CASCADE,
    producto_servicio_codigo VARCHAR(255) NOT NULL,
    producto_servicio_nombre VARCHAR(255) NOT NULL,
    producto_caracteristicas VARCHAR(255) NOT NULL
);

CREATE TABLE proceso_caracterizaciones(
    caraceterizacion_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_id BIGINT FOREIGN KEY REFERENCES procesos(proceso_id) ON UPDATE CASCADE ON DELETE CASCADE,
    procedimiento_nombre VARCHAR(255) NOT NULL,
    procedimiento_objetivo VARCHAR(255) NOT NULL,
    procedimiento_codigo VARCHAR(255) NOT NULL
);

CREATE TABLE proceso_documentos(
    procedimiento_documento_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    caraceterizacion_id BIGINT FOREIGN KEY REFERENCES proceso_caracterizaciones(caraceterizacion_id) ON UPDATE CASCADE ON DELETE CASCADE,
    procedimiento_documento_nombre VARCHAR(255) NOT NULL,
    procedimiento_documento_descripcion VARCHAR(255) NOT NULL,
    procedimiento_documento_codigo VARCHAR(255) NOT NULL,
    procedimiento_documento_arch VARCHAR(255) NOT NULL
);