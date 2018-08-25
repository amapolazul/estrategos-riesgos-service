CREATE TABLE procesos (
    proceso_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_padre_id BIGINT,
    proceso_nombre VARCHAR(255) NOT NULL,
    proceso_descripcion VARCHAR(255),
    proceso_codigo VARCHAR(255),
    proceso_tipo VARCHAR(255),
    proceso_responsable_id VARCHAR (255),
    proceso_documento VARCHAR(255)
);


CREATE TABLE productos_servicios (
    producto_servicio_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_id BIGINT FOREIGN KEY REFERENCES procesos(proceso_id) ON UPDATE CASCADE ON DELETE CASCADE,
    producto_servicio_codigo VARCHAR(255) NOT NULL,
    producto_servicio_nombre VARCHAR(255) NOT NULL,
    producto_caracteristicas VARCHAR(255)
);

CREATE TABLE proceso_caracterizaciones(
    caraceterizacion_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    proceso_id BIGINT FOREIGN KEY REFERENCES procesos(proceso_id) ON UPDATE CASCADE ON DELETE CASCADE,
    procedimiento_nombre VARCHAR(255) NOT NULL,
    procedimiento_objetivo VARCHAR(255) NOT NULL,
    procedimiento_codigo VARCHAR(255)
);

CREATE TABLE proceso_documentos(
    procedimiento_documento_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    caraceterizacion_id BIGINT FOREIGN KEY REFERENCES proceso_caracterizaciones(caraceterizacion_id) ON UPDATE CASCADE ON DELETE CASCADE,
    procedimiento_documento_nombre VARCHAR(255) NOT NULL,
    procedimiento_documento_descripcion VARCHAR(255) NOT NULL,
    procedimiento_documento_codigo VARCHAR(255) NOT NULL,
    procedimiento_documento_arch VARCHAR(255) NOT NULL
);