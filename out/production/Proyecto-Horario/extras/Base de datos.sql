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
                                  UNIQUE(idProfesor, idMateria),
                                  FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor) ON DELETE CASCADE,
                                  FOREIGN KEY (idMateria) REFERENCES Materias(idMateria) ON DELETE CASCADE
);

-- ============================================
-- 4. HORAS DE DESCARGA
-- ============================================
CREATE TABLE Descargas (
                           idDescarga INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(120) NOT NULL
);


CREATE TABLE DescargaProfesor (
                                  idDescargaProfesor INT AUTO_INCREMENT PRIMARY KEY,
                                  idProfesor INT NOT NULL,
                                  idDescarga INT NOT NULL,
                                  horas_asignadas INT NOT NULL CHECK(horas_asignadas BETWEEN 1 AND 10),
                                  UNIQUE(idProfesor, idDescarga),
                                  FOREIGN KEY (idProfesor) REFERENCES Profesores(idProfesor) ON DELETE CASCADE,
                                  FOREIGN KEY (idDescarga) REFERENCES Descargas(idDescarga) ON DELETE CASCADE
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
-- INSERTA HORAS DE DESCARGA
-- =========================================
INSERT INTO Descargas (nombre) VALUES
                                   ('Coordinacion de Carrera'),
                                   ('Apoyo Academico'),
                                   ('Titulacion');

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
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Fundamentos de Física',2,1,4),
                                                                       ('Cálculo Diferencial e Integral',2,1,5),
                                                                       ('Taller de Ética',2,1,4),
                                                                       ('Fudamentos de Gestión Empresarial',2,1,5),
                                                                       ('Fundamentos de Investigacion',2,1,3),
                                                                       ('Probabilidad y Estadistica',2,1,5),
                                                                       ('Ingles I',2,1,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 2
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Software de Aplicacion Ejecutivo',2,2,5),
                                                                       ('Desarrollo Humano',2,2,4),
                                                                       ('Contabilidad Orientada a los Negocios',2,2,5),
                                                                       ('Dinamica Social',2,2,4),
                                                                       ('Fundamentos de Quimica',2,2,5),
                                                                       ('Legislacion Laboral',2,2,4),
                                                                       ('Ingles II',2,2,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 3
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Marco Legal de las Organizacíones',2,3,4),
                                                                       ('Ecónomia Empresarial',2,3,5),
                                                                       ('Costos Empresariales',2,3,5),
                                                                       ('Habílidades Directivas I',2,3,4),
                                                                       ('Entorno Macroeconomico',2,3,5),
                                                                       ('Álgebra Lineal',2,3,5),
                                                                       ('Ingles III',2,3,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 4
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Ingeniería Economica',2,4,5),
                                                                       ('Estadística Inferencial I',2,4,6),
                                                                       ('Instrumentos de Presupuestación Empresarial',2,4,5),
                                                                       ('Habílidades Directivas II',2,4,4),
                                                                       ('Desarrollo Sustentable',2,4,5),
                                                                       ('Investigación de Operaciones',2,4,5),
                                                                       ('Ingles IV',2,4,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 5
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Finanzas en las Organizaciones',2,5,4),
                                                                       ('Estadística Inferencial II',2,5,6),
                                                                       ('Ingeniería de Procesos',2,5,5),
                                                                       ('Gestion del Capital Humano',2,5,4),
                                                                       ('Taller de Investigación I',2,5,4),
                                                                       ('Mercadotecnia',2,5,5),
                                                                       ('Ingles V',2,5,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 6
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Administracion de la Salud y Seguridad Ocupacional',2,6,5),
                                                                       ('El Emprendedor y la Innovacion',2,6,4),
                                                                       ('Gestion de la Producción I',2,6,4),
                                                                       ('Diseño Organizacional',2,6,5),
                                                                       ('Taller de Investigación II',2,6,4),
                                                                       ('Sistema de Informacion de la Mercadotecnia',2,6,5),
                                                                       ('Evaluacion del Desempeño Empresarial (Optativa I)',2,6,5),
                                                                       ('Ingles VI',2,6,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 7
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Calidad Aplicada en la Gestión Empresarial',2,7,5),
                                                                       ('Formulación y Evaluación de Proyectos',2,7,5),
                                                                       ('Gestion de la Producción II',2,7,4),
                                                                       ('Gestion Estrategica',2,7,5),
                                                                       ('Comercio Electronico',2,7,4),
                                                                       ('Estrategias para el Desarrollo de Empresas (Optativa II)',2,7,5),
                                                                       ('Simulacion de Negocios (Optativa III)',2,7,5),
                                                                       ('Ingles VII',2,7,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 8
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Cadenas de Suministro',2,8,5),
                                                                       ('Plan de Negocios Internacionales (Optativa IV)',2,8,5),
                                                                       ('Tablero de Control (Optativa V)',2,8,5),
                                                                       ('Análisis Financiero Gerencial (Optativa VI)',2,8,5),
                                                                       ('Control de la Gestion Empresarial (Optativa VII)',2,8,5),
                                                                       ('Decisiones Financieras (Optativa VIII)',2,8,5),
                                                                       ('Ingles Tecnico I',2,8,5);

-- =========================================
-- INSERTA MATERIAS
-- LICENCIATURA EN GESTION EMPRESARIAL
-- SEMESTRE: 9
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Residencia Profesional',2,9,4),
                                                                       ('Ingles Tecnico II',2,9,5);

-- ================================================
-- ================================================
-- ================================================

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 1
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Dibujo Asistido por Computadora',3,1,4),
                                                                       ('Álgebra',3,1,5),
                                                                       ('Probabilidad y Estadística para Ingeniería',3,1,6),
                                                                       ('Fundamentos de Contabilidad',3,1,3),
                                                                       ('Geometría Analitica y Trigonomtría',3,1,4),
                                                                       ('Expresión Lectora y Escritura',3,1,3),
                                                                       ('Indentidad y Cultural',3,1,3),
                                                                       ('Actividades Deportivas, Sociales y Culturales',3,1,3);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 2
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Electricidad y Magnetismo',3,2,4),
                                                                       ('Álgebra Lineal',3,2,4),
                                                                       ('Mecánica Estática',3,2,4),
                                                                       ('Calculo Diferencial',3,2,5),
                                                                       ('Fundamentos de Administracion para Ingenieros',3,2,4),
                                                                       ('Metodologia de Investigación Cuantitativa',3,2,3),
                                                                       ('Axiologia y Deontologia',3,2,3),
                                                                       ('Ingles Remedial',3,2,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 3
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Circuitos Eléctricos',3,3,4),
                                                                       ('Cálculo Integral',3,3,4),
                                                                       ('Mecánica Dinámica',3,3,5),
                                                                       ('Cálculo Vectorial',3,3,4),
                                                                       ('Química',3,3,4),
                                                                       ('Metodología de Investigación Cualitativa',3,3,4),
                                                                       ('Introduccion a la Ingeniería Atomotriz',3,3,3),
                                                                       ('Inglés Elemental',3,3,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 4
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Dibujo de Elementos Asistidos por Software',3,4,5),
                                                                       ('Ecuaciones Diferenciales',3,4,4),
                                                                       ('Metrologia y Normalizacion',3,4,5),
                                                                       ('Métodos Numéricos',3,4,4),
                                                                       ('Resistencia de Materiales',3,4,4),
                                                                       ('Taller de Investigacion',3,4,4),
                                                                       ('Derechos Humanos para la Convivencia Pacífica',3,4,3),
                                                                       ('Inglés Elemental Superior',3,4,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 5
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Fundamentos de Mecatrónica',3,5,4),
                                                                       ('Termodinámica',3,5,4),
                                                                       ('Análisis y Síntesis de Mecánismos',3,5,4),
                                                                       ('Electrónica Automotriz',3,5,4),
                                                                       ('Tecnología y Comportamiento de Materiales',3,5,4),
                                                                       ('Tópicos de Tribología para Sistemas Automotrices',3,5,4),
                                                                       ('Conservación Ambiental y Desarrollo Sustentable',3,5,3),
                                                                       ('Inglés Preintermedio',3,5,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 6
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Circuitos Neumáticos e Hidráulicos',3,6,4),
                                                                       ('Transferencia de Calor',3,6,4),
                                                                       ('Sistemas de Dirección, Suspensión y Frenos',3,6,4),
                                                                       ('Electrónica Automotriz de Potencia',3,6,4),
                                                                       ('Sistema de Encendido, Inyección y Auxiliares',3,6,4),
                                                                       ('Motores de Explosión y Combustión Interna',3,6,4),
                                                                       ('Inglés Intermedio',3,6,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 7
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Vehículos Híbridos y Eléctricos',3,7,4),
                                                                       ('Vibraciones Mecánicas',3,7,4),
                                                                       ('Mecánica de Fluidos',3,7,4),
                                                                       ('Sistema de Transmisión de Potencia',3,7,4),
                                                                       ('Diseño y Selección de Elementos de Máquinas',3,7,4),
                                                                       ('Semiario de Investigacion',3,7,4),
                                                                       ('Visión Empresarial',3,7,3),
                                                                       ('Inglés Intermedio Superior',3,7,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 8
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
                                                                       ('Taller de Sistemas Inteligentes',3,8,4),
                                                                       ('Planeación y Control de Proyectos',3,8,4),
                                                                       ('Gestión y Calidad Automotriz',3,8,4),
                                                                       ('Procesos de Ensamble Automotriz',3,8,4),
                                                                       ('Procesos de Manufactura de Elementos Automotrices',3,8,4),
                                                                       ('Laboratorio de Mantenimiento Automotriz',3,8,4),
                                                                       ('Diseño de Producto',3,8,5),
                                                                       ('Inglés Tecnico',3,8,6);

-- =========================================
-- INSERTA MATERIAS
-- INGENIERÍA EN MECÁNICA AUTOMOTRIZ
-- SEMESTRE: 9
-- =========================================
INSERT INTO Materias (nombre, idCarrera, idSemestre, horas_semana) VALUES
    ('Residencia Profesional',3,9,4);

