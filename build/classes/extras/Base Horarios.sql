DROP DATABASE IF EXISTS Proyecto_Horarios; 
CREATE DATABASE Proyecto_Horarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Proyecto_Horarios;

-- ===========================
-- Tabla: Carreras
-- ===========================
CREATE TABLE Carreras (
    idCarrera INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    clave VARCHAR(20) UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Ciclos
-- ===========================
CREATE TABLE Ciclos (
    idCiclo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo ENUM('par','impar') NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    UNIQUE KEY uq_nombre (nombre)
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Semestres
-- ===========================
CREATE TABLE Semestres (
    idSemestre INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    idCiclo INT,
    idCarrera INT,
    INDEX ix_ciclo (idCiclo),
    INDEX ix_carrera (idCarrera),
    CONSTRAINT fk_semestres_ciclos FOREIGN KEY (idCiclo) REFERENCES Ciclos(idCiclo)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_semestres_carreras FOREIGN KEY (idCarrera) REFERENCES Carreras(idCarrera)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Materias
-- ===========================
CREATE TABLE Materias (
    idMateria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    idSemestre INT NOT NULL,
    horas_semana INT NOT NULL,
    UNIQUE KEY uq_nombre_semestre (nombre, idSemestre),
    INDEX ix_semestre (idSemestre),
    CONSTRAINT fk_materias_semestres FOREIGN KEY (idSemestre) REFERENCES Semestres(idSemestre)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Profesores
-- ===========================
CREATE TABLE Profesores (
    idProfesor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidoP VARCHAR(50),
    apellidoM VARCHAR(50),
    correo VARCHAR(100),
    telefono VARCHAR(20),
    horas_descarga INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    UNIQUE KEY uq_correo (correo)
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Disponibilidades
-- ===========================
CREATE TABLE Disponibilidades (
    idDisponibilidad INT AUTO_INCREMENT PRIMARY KEY,
    idProfesor INT NOT NULL,
    dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    INDEX ix_profesor (idProfesor),
    INDEX ix_dia (dia),
    CONSTRAINT fk_disponibilidad_profesor FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Grupos
-- ===========================
CREATE TABLE Grupos (
    idGrupo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    idSemestre INT NOT NULL,
    idCiclo INT NOT NULL,
    idCarrera INT NOT NULL, 
    UNIQUE KEY uq_grupo_ciclo (nombre, idCiclo),
    INDEX ix_semestre (idSemestre),
    INDEX ix_ciclo (idCiclo),
    INDEX ix_carrera (idCarrera),
    CONSTRAINT fk_grupos_semestres FOREIGN KEY (idSemestre) REFERENCES Semestres(idSemestre)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_grupos_ciclos FOREIGN KEY (idCiclo) REFERENCES Ciclos(idCiclo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_grupos_carreras FOREIGN KEY (idCarrera) REFERENCES Carreras(idCarrera)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Asignaciones
-- ===========================
CREATE TABLE Asignaciones (
    idAsignacion INT AUTO_INCREMENT PRIMARY KEY,
    idProfesor INT NOT NULL,
    idMateria INT NOT NULL,
    idGrupo INT NOT NULL,
    horas_asignadas INT NOT NULL,
    UNIQUE KEY uq_asignacion (idProfesor, idMateria, idGrupo),
    INDEX ix_profesor (idProfesor),
    INDEX ix_materia (idMateria),
    INDEX ix_grupo (idGrupo),
    CONSTRAINT fk_asig_prof FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asig_mat FOREIGN KEY (idMateria) REFERENCES Materias(idMateria)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asig_gru FOREIGN KEY (idGrupo) REFERENCES Grupos(idGrupo)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Horarios_Generados
-- ===========================
CREATE TABLE Horarios_Generados (
    idHorario INT AUTO_INCREMENT PRIMARY KEY,
    idAsignacion INT NOT NULL,
    dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    aula VARCHAR(50),
    tipo_bloque ENUM('CLASE','DESCARGA') DEFAULT 'CLASE',
    estado ENUM('PENDIENTE','VALIDADO','MODIFICADO') DEFAULT 'PENDIENTE', -- ✅ Nuevo campo para versiones
    INDEX ix_asignacion (idAsignacion),
    INDEX ix_dia (dia),
    INDEX ix_aula (aula),
    CONSTRAINT fk_horarios_asig FOREIGN KEY (idAsignacion) REFERENCES Asignaciones(idAsignacion)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===========================
-- Tabla: Reglas_Horarias
-- ===========================
CREATE TABLE Reglas_Horarias (
    idRegla INT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    valor VARCHAR(100) NOT NULL,
    UNIQUE KEY uq_clave (clave)
) ENGINE=InnoDB;