CREATE TABLE IF NOT EXIST Responsables(
    "id" BIGSERIAL PRIMARY KEY,
    "email" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Procesos (
    "Proceso_Id" BIGSERIAL PRIMARY KEY,
    "Proceso_Padre_Id" INT,
    "Proceso_Nombre" VARCHAR NOT NULL,
    "Proceso_Codigo" VARCHAR NOT NULL,
    "Proceso_Tipo" VARCHAR NOT NULL,
    "Proceso_Responsable_Id" BIGSERIAL NOT NULL REFERENCES Responsables(id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "Proceso_Documento" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Productos_Servicios (
    "Producto_Servicio_Id" BIGSERIAL PRIMARY KEY,
    "Proceso_Id" BIGSERIAL NOT NULL REFERENCES Procesos(Proceso_Id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "Producto_Servicio_Nombre" VARCHAR NOT NULL,
    "Producto_Caracteristicas" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Proceso_Caracterizaciones(
    "Caraceterizacion_id" BIGSERIAL PRIMARY KEY,
    "Proceso_Id" BIGSERIAL NOT NULL REFERENCES Procesos(Proceso_Id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "Procedimiento_Nombre" VARCHAR NOT NULL,
    "Procedimiento_Codigo" VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS Proceso_Documentos(
    "Procedimiento_Documento_Id" BIGSERIAL PRIMARY KEY,
    "Caraceterizacion_id" BIGSERIAL NOT NULL REFERENCES Proceso_Caracterizaciones(Caraceterizacion_id) ON UPDATE RESTRICT ON DELETE CASCADE,
    "Procedimiento_Documento_Nombre" VARCHAR NOT NULL,
    "Procedimiento_Documento_Descripcion" VARCHAR NOT NULL,
    "Procedimiento_Documento_Codigo" VARCHAR NOT NULL,
    "Procedimiento_Documento_Arch" VARCHAR NOT NULL
);