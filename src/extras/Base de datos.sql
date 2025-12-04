DROP DATABASE IF EXISTS Proyecto_Horarios;
CREATE DATABASE Proyecto_Horarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Proyecto_Horarios;

CREATE TABLE Carreras (
                          idCarrera INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          clave VARCHAR(20) UNIQUE,
                          descripcion VARCHAR(255)
) ENGINE=InnoDB;

INSERT INTO Carreras VALUES(1,'Licenciatura en Psicologia Industrial','LPI','Licenciatura en Psicologia Industrial');
INSERT INTO Carreras VALUES(2,'Ingenieria en Gestion Empresarial','IGE','Ingenieria en Gestion Empresarial');
INSERT INTO Carreras VALUES(3,'Ingenieria en Mecanica Automotriz','IMA','Ingenieria en Mecanica Automotriz');
SELECT nombre FROM Carreras;

CREATE TABLE Ciclos (
                        idCiclo INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(50) NOT NULL,
                        tipo ENUM('par','impar') NOT NULL,
                        fecha_inicio DATE,
                        fecha_fin DATE,
                        UNIQUE KEY uq_nombre (nombre)
) ENGINE=InnoDB;

INSERT INTO Ciclos (idCiclo, nombre, tipo, fecha_inicio, fecha_fin) VALUES (1, '2025/2026', 'par', '2026-01-01', '2026-07-01');
SELECT * FROM Ciclos;

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

CREATE TABLE Profesores (
                            idProfesor INT AUTO_INCREMENT PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL,
                            apellidoP VARCHAR(50),
                            apellidoM VARCHAR(50),
                            identificador VARCHAR(50) NOT NULL,
                            activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

SELECT * FROM Profesores;

CREATE TABLE Materias_Profesor (
                                   idMateriaProfesor INT AUTO_INCREMENT PRIMARY KEY,
                                   idProfesor INT NOT NULL,
                                   idMateria INT NOT NULL,
                                   preferencia INT DEFAULT 1,
                                   UNIQUE KEY uq_prof_materia (idProfesor, idMateria),
                                   CONSTRAINT fk_matprof_prof FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor)
                                       ON UPDATE CASCADE ON DELETE CASCADE,
                                   CONSTRAINT fk_matprof_mat FOREIGN KEY (idMateria) REFERENCES Materias(idMateria)
                                       ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Disponibilidades (
                                  idDisponibilidad INT AUTO_INCREMENT PRIMARY KEY,
                                  idProfesor INT NOT NULL,
                                  dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes','Sabado') NOT NULL,
                                  hora_inicio TIME NOT NULL,
                                  hora_fin TIME NOT NULL,
                                  CONSTRAINT fk_dispon_prof FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor)
                                      ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Grupos (
                        idGrupo INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(50) NOT NULL,
                        idSemestre INT NOT NULL,
                        idCiclo INT NOT NULL,
                        idCarrera INT NOT NULL,
                        UNIQUE KEY uq_grupo_ciclo (nombre, idCiclo),
                        CONSTRAINT fk_grupos_sem FOREIGN KEY (idSemestre) REFERENCES Semestres(idSemestre),
                        CONSTRAINT fk_grupos_cic FOREIGN KEY (idCiclo) REFERENCES Ciclos(idCiclo),
                        CONSTRAINT fk_grupos_car FOREIGN KEY (idCarrera) REFERENCES Carreras(idCarrera)
) ENGINE=InnoDB;

CREATE TABLE Asignaciones (
                              idAsignacion INT AUTO_INCREMENT PRIMARY KEY,
                              idProfesor INT NOT NULL,
                              idMateria INT NOT NULL,
                              idGrupo INT NOT NULL,
                              horas_asignadas INT NOT NULL,
                              UNIQUE KEY uq_asig (idProfesor, idMateria, idGrupo),
                              CONSTRAINT fk_asig_prof FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor),
                              CONSTRAINT fk_asig_mat FOREIGN KEY (idMateria) REFERENCES Materias(idMateria),
                              CONSTRAINT fk_asig_gru FOREIGN KEY (idGrupo) REFERENCES Grupos(idGrupo)
) ENGINE=InnoDB;

CREATE TABLE Descargas (
                           idDescarga INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion VARCHAR(255),
                           horas_semana INT NOT NULL,
                           idCarrera INT,
                           CONSTRAINT fk_descarga_carrera FOREIGN KEY (idCarrera) REFERENCES Carreras(idCarrera)
                               ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE Asignacion_Descarga (
                                     idAsigDescarga INT AUTO_INCREMENT PRIMARY KEY,
                                     idProfesor INT NOT NULL,
                                     idDescarga INT NOT NULL,
                                     idGrupo INT,
                                     horas_asignadas INT NOT NULL,
                                     CONSTRAINT fk_asigd_prof FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor),
                                     CONSTRAINT fk_asigd_desc FOREIGN KEY (idDescarga) REFERENCES Descargas(idDescarga),
                                     CONSTRAINT fk_asigd_gru FOREIGN KEY (idGrupo) REFERENCES Grupos(idGrupo)
) ENGINE=InnoDB;

CREATE TABLE Horarios_Generados (
                                    idHorario INT AUTO_INCREMENT PRIMARY KEY,
                                    idAsignacion INT,
                                    idAsigDescarga INT,
                                    dia ENUM('Lunes','Martes','Miércoles','Jueves','Viernes') NOT NULL,
                                    hora_inicio TIME NOT NULL,
                                    hora_fin TIME NOT NULL,
                                    aula VARCHAR(50),
                                    tipo_bloque ENUM('CLASE','DESCARGA') DEFAULT 'CLASE',
                                    estado ENUM('PENDIENTE','VALIDADO','MODIFICADO') DEFAULT 'PENDIENTE',
                                    CONSTRAINT fk_horario_asig FOREIGN KEY (idAsignacion) REFERENCES Asignaciones(idAsignacion)
                                        ON UPDATE CASCADE ON DELETE SET NULL,
                                    CONSTRAINT fk_horario_desc FOREIGN KEY (idAsigDescarga) REFERENCES Asignacion_Descarga(idAsigDescarga)
                                        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE Reglas_Horarias (
                                 idRegla INT AUTO_INCREMENT PRIMARY KEY,
                                 clave VARCHAR(100) NOT NULL,
                                 descripcion VARCHAR(255),
                                 valor VARCHAR(100) NOT NULL,
                                 UNIQUE KEY uq_clave (clave)
) ENGINE=InnoDB;

CREATE TABLE Profesor_Descarga (
                                   idProfesor INT NOT NULL,
                                   idDescarga INT NOT NULL,
                                   horas_asignadas INT NOT NULL,
                                   PRIMARY KEY (idProfesor, idDescarga),
                                   FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor)
                                       ON UPDATE CASCADE ON DELETE CASCADE,
                                   FOREIGN KEY (idDescarga) REFERENCES Descargas(idDescarga)
                                       ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

