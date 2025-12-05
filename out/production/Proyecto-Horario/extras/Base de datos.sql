DROP DATABASE IF EXISTS ProyectoHorarios;
CREATE DATABASE ProyectoHorarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ProyectoHorarios;
-- ============================================
-- 1. CARRERAS Y MAPA CURRICULAR (FIJO)
-- ============================================
CREATE TABLE Carreras (
                          idCarrera INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(120) NOT NULL,
                          clave VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE Semestres (
                           idSemestre INT AUTO_INCREMENT PRIMARY KEY,
                           numero INT NOT NULL CHECK (numero BETWEEN 1 AND 10)
);

CREATE TABLE Materias (
                          idMateria INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(120) NOT NULL,
                          idCarrera INT NOT NULL,
                          idSemestre INT NOT NULL,
                          horas_semana INT NOT NULL CHECK (horas_semana BETWEEN 1 AND 10),
                          UNIQUE(nombre, idCarrera, idSemestre),
                          FOREIGN KEY (idCarrera) REFERENCES Carreras(idCarrera) ON DELETE CASCADE,
                          FOREIGN KEY (idSemestre) REFERENCES Semestres(idSemestre) ON DELETE CASCADE
);

-- ============================================
-- 2. CICLOS Y GRUPOS (DINÁMICO)
-- ============================================

CREATE TABLE Ciclos (
                        idCiclo INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(20) NOT NULL UNIQUE, -- Ejemplo: 2025-2026
                        tipo ENUM('par','impar') NOT NULL,
                        fecha_inicio DATE,
                        fecha_fin DATE
);

CREATE TABLE Grupos (
                        idGrupo INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(20) NOT NULL,         -- Ej: "1-A", "3-A"
                        idCarrera INT NOT NULL,
                        idSemestre INT NOT NULL,
                        idCiclo INT NOT NULL,
                        UNIQUE(nombre, idCiclo),
                        FOREIGN KEY (idCarrera) REFERENCES Carreras(idCarrera),
                        FOREIGN KEY (idSemestre) REFERENCES Semestres(idSemestre),
                        FOREIGN KEY (idCiclo) REFERENCES Ciclos(idCiclo)
);

-- ============================================
-- 3. PROFESORES
-- ============================================

CREATE TABLE Profesores (
                            idProfesor INT AUTO_INCREMENT PRIMARY KEY,
                            nombre VARCHAR(120) NOT NULL,
                            apellidoP VARCHAR(60)NOT NULL,
                            apellidoM VARCHAR(60)NOT NULL,
                            identificador VARCHAR(50),
                            activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE Disponibilidad (
                                idDisponibilidad INT AUTO_INCREMENT PRIMARY KEY,
                                idProfesor INT NOT NULL,
                                dia ENUM('Lunes','Martes','Miercoles','Jueves','Viernes','Sabado') NOT NULL,
                                hora_inicio TIME NOT NULL,
                                hora_fin TIME NOT NULL,
                                FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor) ON DELETE CASCADE
);

CREATE TABLE MateriasProfesor (
                                  idMatProf INT AUTO_INCREMENT PRIMARY KEY,
                                  idProfesor INT NOT NULL,
                                  idMateria INT NOT NULL,
                                  preferencia INT DEFAULT 1, -- opcional para luego
                                  UNIQUE(idProfesor, idMateria),
                                  FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor) ON DELETE CASCADE,
                                  FOREIGN KEY (idMateria) REFERENCES Materias(idMateria) ON DELETE CASCADE
);

-- ============================================
-- 4. HORAS DE DESCARGA
-- ============================================

CREATE TABLE Descargas (
                           idDescarga INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(120) NOT NULL,
                           horas_semana INT NOT NULL CHECK(horas_semana BETWEEN 1 AND 10)
);

CREATE TABLE DescargaProfesor (
                                  idDescargaProfesor INT AUTO_INCREMENT PRIMARY KEY,
                                  idProfesor INT NOT NULL,
                                  idDescarga INT NOT NULL,
                                  horas_asignadas INT NOT NULL CHECK(horas_asignadas BETWEEN 1 AND 10),
                                  FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor),
                                  FOREIGN KEY (idDescarga) REFERENCES Descargas(idDescarga)
);

-- ============================================
-- 5. ASIGNACIONES Y HORARIOS GENERADOS
-- ============================================

CREATE TABLE Asignaciones (
                              idAsignacion INT AUTO_INCREMENT PRIMARY KEY,
                              idProfesor INT NOT NULL,
                              idMateria INT NOT NULL,
                              idGrupo INT NOT NULL,
                              horas_asignadas INT NOT NULL,
                              UNIQUE(idProfesor, idMateria, idGrupo),
                              FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor),
                              FOREIGN KEY (idMateria) REFERENCES Materias(idMateria),
                              FOREIGN KEY (idGrupo) REFERENCES Grupos(idGrupo)
);

CREATE TABLE Horarios (
                          idHorario INT AUTO_INCREMENT PRIMARY KEY,
                          idAsignacion INT,
                          idProfesor INT,
                          dia ENUM('Lunes','Martes','Miercoles','Jueves','Viernes') NOT NULL,
                          hora_inicio TIME NOT NULL,
                          hora_fin TIME NOT NULL,
                          tipo ENUM('CLASE','DESCARGA') NOT NULL,
                          FOREIGN KEY (idAsignacion) REFERENCES Asignaciones(idAsignacion),
                          FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor)
);
