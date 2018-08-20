CREATE TABLE riesgos_logs_auditoria (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    tipo_log VARCHAR(255) NOT NULL,
    clase_origen VARCHAR(255) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha_creacion BIGINT NOT NULL
);