/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  chemo
 * Created: 3 nov 2025
 */

DROP DATABASE IF EXISTS proyecto_horarios; 
CREATE DATABASE proyecto_horarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE proyecto_horarios;

-- ===========================
-- Tabla: carreras
-- ===========================
CREATE TABLE carreras (
    idCarrera INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    clave VARCHAR(20) UNIQUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- ===========================
-- Tabla: ciclos
-- ===========================
CREATE TABLE ciclos (
    idCiclo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo ENUM('par','impar') NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    UNIQUE KEY uq_nombre (nombre)
) ENGINE=InnoDB;

-- ===========================
-- Tabla: semestres
-- ===========================
CREATE TABLE semestres (
    idSemestre INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    idCiclo INT,
    idCarrera INT,
    INDEX ix_ciclo (idCiclo),
    INDEX ix_carrera (idCarrera),
    CONSTRAINT fk_semestres_ciclos FOREIGN KEY (idCiclo) REFERENCES ciclos(idCiclo)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_semestres_carreras FOREIGN KEY (idCarrera) REFERENCES carreras(idCarrera)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

-- ===========================
-- Tabla: materias
-- ===========================
CREATE TABLE materias (
    idMateria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    idSemestre INT NOT NULL,
    horas_semana INT NOT NULL,
    UNIQUE KEY uq_nombre_semestre (nombre, idSemestre),
    INDEX ix_semestre (idSemestre),
    CONSTRAINT fk_materias_semestres FOREIGN KEY (idSemestre) REFERENCES semestres(idSemestre)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===========================
-- Tabla: profesores
-- ===========================
CREATE TABLE profesores (
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
-- Tabla: disponibilidades
-- ===========================
CREATE TABLE disponibilidades (
    idDisponibilidad INT AUTO_INCREMENT PRIMARY KEY,
    idProfesor INT NOT NULL,
    dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    INDEX ix_profesor (idProfesor),
    INDEX ix_dia (dia),
    CONSTRAINT fk_disponibilidad_profesor FOREIGN KEY (idProfesor) REFERENCES profesores(idProfesor)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===========================
-- Tabla: grupos
-- ===========================
CREATE TABLE grupos (
    idGrupo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    idSemestre INT NOT NULL,
    idCiclo INT NOT NULL,
    idCarrera INT NOT NULL, 
    UNIQUE KEY uq_grupo_ciclo (nombre, idCiclo),
    INDEX ix_semestre (idSemestre),
    INDEX ix_ciclo (idCiclo),
    INDEX ix_carrera (idCarrera),
    CONSTRAINT fk_grupos_semestres FOREIGN KEY (idSemestre) REFERENCES semestres(idSemestre)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_grupos_ciclos FOREIGN KEY (idCiclo) REFERENCES ciclos(idCiclo)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_grupos_carreras FOREIGN KEY (idCarrera) REFERENCES carreras(idCarrera)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===========================
-- Tabla: asignaciones
-- ===========================
CREATE TABLE asignaciones (
    idAsignacion INT AUTO_INCREMENT PRIMARY KEY,
    idProfesor INT NOT NULL,
    idMateria INT NOT NULL,
    idGrupo INT NOT NULL,
    horas_asignadas INT NOT NULL,
    UNIQUE KEY uq_asignacion (idProfesor, idMateria, idGrupo),
    INDEX ix_profesor (idProfesor),
    INDEX ix_materia (idMateria),
    INDEX ix_grupo (idGrupo),
    CONSTRAINT fk_asig_prof FOREIGN KEY (idProfesor) REFERENCES profesores(idProfesor)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asig_mat FOREIGN KEY (idMateria) REFERENCES materias(idMateria)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_asig_gru FOREIGN KEY (idGrupo) REFERENCES grupos(idGrupo)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===========================
-- Tabla: horarios_generados
-- ===========================
CREATE TABLE horarios_generados (
    idHorario INT AUTO_INCREMENT PRIMARY KEY,
    idAsignacion INT NOT NULL,
    dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes', 'Sabado') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    aula VARCHAR(50),
    tipo_bloque ENUM('CLASE','DESCARGA') DEFAULT 'CLASE',
    estado ENUM('PENDIENTE','VALIDADO','MODIFICADO') DEFAULT 'PENDIENTE',
    INDEX ix_asignacion (idAsignacion),
    INDEX ix_dia (dia),
    INDEX ix_aula (aula),
    CONSTRAINT fk_horarios_asig FOREIGN KEY (idAsignacion) REFERENCES asignaciones(idAsignacion)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===========================
-- Tabla: reglas_horarias
-- ===========================
CREATE TABLE reglas_horarias (
    idRegla INT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    valor VARCHAR(100) NOT NULL,
    UNIQUE KEY uq_clave (clave)
) ENGINE=InnoDB;

