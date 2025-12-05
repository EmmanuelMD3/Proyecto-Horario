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

CREATE TABLE Disponibilidades (
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

-- =========================================
-- INSERTA CARRERAS
-- =========================================
INSERT INTO Carreras (nombre, clave) VALUES
                                         ('Licenciatura en Psicología Industrial', 'LPI'),
                                         ('Ingeniería en Gestión Empresarial', 'IGE'),
                                         ('Ingeniería en Mecánica Automotriz', 'IMA');

-- =========================================
-- INSERTA SEMESTRES
-- =========================================
INSERT INTO Semestres (numero) VALUES
                                   (1),(2),(3),(4),(5),(6),(7),(8),(9),(10);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 1
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Estrategia de Organizacion Institucional',1,1,5),
                                                                       ('Fundamentos de Psicologia Industrial',1,1,4),
                                                                       ('Historial del Sindicalismo en México',1,1,2),
                                                                       ('Historia de la Psicologia',1,1,3),
                                                                       ('Destrezas y Competencias Basicas',1,1,5),
                                                                       ('Economia Empresarial PS',1,1,5),
                                                                       ('Teoria y Modelos de Organizacion Instiucional',1,1,5),
                                                                       ('Ingles I',1,1,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 2
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Planeacion Estrategica y Operatividad Institucional',1,2,5),
                                                                       ('Taller de Valores Eticos Profesionales',1,2,5),
                                                                       ('Taller de Expresion Oral y Escrita',1,2,4),
                                                                       ('Presentaciones Electronicas',1,2,5),
                                                                       ('Bases de Datos Electronicas',1,2,5),
                                                                       ('Tecnicas de Entrevistas',1,2,5),
                                                                       ('Fundamentos de Investigacion',1,2,3),
                                                                       ('Ingles II',1,2,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 3
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Fundamentos de Medicion en Psicologia',1,3,5),
                                                                       ('Fundamentos de Planeacion Financiera',1,3,6),
                                                                       ('Procesos Psicologicos',1,3,5),
                                                                       ('Psicologia Social',1,3,4),
                                                                       ('Reclutamiento y Seleccion de Personal',1,3,5),
                                                                       ('Capacitacion de Recursos Humanos',1,3,5),
                                                                       ('Estadistica I',1,3,5),
                                                                       ('Ingles III',1,3,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 4
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Introduccion al Estudio del Derecho',1,4,4),
                                                                       ('Estadistica II',1,4,5),
                                                                       ('Desarrollo Sustentable',1,4,5),
                                                                       ('Administracion de Sueldos y Salarios',1,4,5),
                                                                       ('Administracion Estrategica',1,4,5),
                                                                       ('Investigacion Cualitativa',1,4,5),
                                                                       ('Evaluacion Psicometrica',1,4,5),
                                                                       ('Ingles IV',1,4,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 5
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Inteligencia Emocional y Relaciones Humanas',1,5,6),
                                                                       ('Dinamicas de Grupos',1,5,5),
                                                                       ('Habilidades de Supervision',1,5,5),
                                                                       ('Manuales de Estructuras de Organizacion',1,5,5),
                                                                       ('Derecho Laboral',1,5,5),
                                                                       ('Negociacion Colectiva',1,5,5),
                                                                       ('Investigacion Cuantitativa',1,5,5),
                                                                       ('Ingles V',1,5,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 6
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Problemas Psicosociales en la Industria',1,6,4),
                                                                       ('Derecho de la Seguridad Social',1,6,5),
                                                                       ('Seguridad e Higiene Industrial',1,6,5),
                                                                       ('Entrevista por Competencias',1,6,5),
                                                                       ('Formacion y Desarrollo de Directivos',1,6,5),
                                                                       ('Capacitacion Atravez de Coaching',1,6,5),
                                                                       ('Control de Calidad',1,6,5),
                                                                       ('Ingles VI',1,6,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 7
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Evaluacion del Desempeño',1,7,5),
                                                                       ('Taller de Redaccion de Contratos Laborales',1,7,4),
                                                                       ('Reingenieria y Cambio Organizacional',1,7,5),
                                                                       ('Estrategias de Negociacion Organizacional',1,7,5),
                                                                       ('Gestion de Talento Humano por Competencia',1,7,5),
                                                                       ('Optativa I',1,7,5),
                                                                       ('Optativa II',1,7,5),
                                                                       ('Ingles VII',1,7,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 8
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Seminario de Relaciones Industriales',1,8,5),
                                                                       ('Optativa III',1,8,4),
                                                                       ('Mercado de Talento Humano',1,8,5),
                                                                       ('Estres y Salud en las Organizaciones',1,8,5),
                                                                       ('Optativa IV',1,8,5),
                                                                       ('Seminario de Tesis I',1,8,6),
                                                                       ('Desarrollo Organizacional',1,8,5),
                                                                       ('Ingles Tecnico I',1,8,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN PSICOLOGIA INDUSTRIAL
-- SEMESTRE: 9
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Residencia Profesional',1,9,5),
                                                                       ('Seminario de Tesis II',1,9,6),
                                                                       ('Ingles Tecnico II',1,9,5);

-- ================================================
-- ================================================
-- ================================================

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 1
-- =========================================
