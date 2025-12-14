-- =========================================
-- I. INSERTA CICLO (ID = 1)
-- =========================================
INSERT INTO Ciclos (nombre, tipo, fecha_inicio, fecha_fin) VALUES
('2025-2026_I', 'impar', '2025-08-01', '2026-01-31');

-- =========================================
-- II. INSERTA GRUPOS (Todos en idCiclo = 1)
-- =========================================
-- LPI (Licenciatura en Psicología Industrial - idCarrera=1)
INSERT INTO Grupos (nombre, idCarrera, idSemestre, idCiclo) VALUES
('1-A', 1, 1, 1), -- LPI, 1er Semestre
('5-A', 1, 5, 1); -- LPI, 5to Semestre

-- IGE (Ingeniería en Gestión Empresarial - idCarrera=2)
INSERT INTO Grupos (nombre, idCarrera, idSemestre, idCiclo) VALUES
('1-B', 2, 1, 1), -- IGE, 1er Semestre
('5-B', 2, 5, 1); -- IGE, 5to Semestre

-- IMA (Ingeniería en Mecánica Automotriz - idCarrera=3)
INSERT INTO Grupos (nombre, idCarrera, idSemestre, idCiclo) VALUES
('1-C', 3, 1, 1); -- IMA, 1er Semestre


-- =========================================
-- III. INSERTA PROFESORES (ID 1 - 5)
-- =========================================
INSERT INTO Profesores (nombre, apellidoP, apellidoM, identificador) VALUES
('Dr. Arturo', 'Reyes', 'Gómez', 'AR100'), -- ID 1 (Especialista en Psicología y Lógica)
('Mtra. Laura', 'Sánchez', 'Pérez', 'LS200'), -- ID 2 (Especialista en Gestión y Económicas)
('Ing. Marco', 'Juárez', 'Díaz', 'MJ300'), -- ID 3 (Especialista en Matemáticas e Ingeniería)
('Mtro. Luis', 'Hernández', 'Cruz', 'LH400'), -- ID 4 (Especialista en Lenguas)
('Dra. Cecilia', 'Vargas', 'Ruiz', 'CV500'); -- ID 5 (Especialista en RRHH)

-- =========================================
-- IV. INSERTA DESCARGAS
-- =========================================
INSERT INTO Descargas (nombre, horas_semana) VALUES
('Coordinación LPI', 5), -- ID 1
('Investigación', 3);    -- ID 2

-- Asignar 5 horas de descarga (Coordinación) al Dr. Arturo Reyes (ID 1)
INSERT INTO DescargaProfesor (idProfesor, idDescarga, horas_asignadas) VALUES
(1, 1, 5);


-- =========================================
-- V. ASIGNACIÓN MATERIA -> PROFESOR
-- =========================================
-- Profesor 1 (Arturo Reyes) dará Fundamentos de Psicología Industrial (LPI, Sem 1)
INSERT INTO MateriasProfesor (idProfesor, idMateria) VALUES (1, 2); 
-- Profesor 5 (Cecilia Vargas) dará Habilidades de Supervisión (LPI, Sem 5)
INSERT INTO MateriasProfesor (idProfesor, idMateria) VALUES (5, 36); 
-- Profesor 3 (Marco Juárez) dará Álgebra (IMA, Sem 1)
INSERT INTO MateriasProfesor (idProfesor, idMateria) VALUES (3, 76); 
-- Profesor 4 (Luis Hernández) dará Inglés I (LPI, Sem 1)
INSERT INTO MateriasProfesor (idProfesor, idMateria) VALUES (4, 8); 

-- =========================================
-- VI. ASIGNACIONES (PROFESOR -> MATERIA -> GRUPO)
-- =========================================
-- 1. LPI 1-A (idGrupo=1): Fundamentos de Psicología Industrial (idMateria=2) - Profesor 1 (4 hrs)
INSERT INTO Asignaciones (idProfesor, idMateria, idGrupo, horas_asignadas) VALUES
(1, 2, 1, 4); 
-- 2. LPI 5-A (idGrupo=2): Habilidades de Supervisión (idMateria=36) - Profesor 5 (5 hrs)
INSERT INTO Asignaciones (idProfesor, idMateria, idGrupo, horas_asignadas) VALUES
(5, 36, 2, 5); 
-- 3. IMA 1-C (idGrupo=5): Álgebra (idMateria=76) - Profesor 3 (5 hrs)
INSERT INTO Asignaciones (idProfesor, idMateria, idGrupo, horas_asignadas) VALUES
(3, 76, 5, 5); 
-- 4. LPI 1-A (idGrupo=1): Inglés I (idMateria=8) - Profesor 4 (5 hrs)
INSERT INTO Asignaciones (idProfesor, idMateria, idGrupo, horas_asignadas) VALUES
(4, 8, 1, 5);


-- =========================================
-- VII. INSERTA HORARIOS (CLASES y DESCARGA)
-- =========================================

-- BLOQUES DE CLASE:

-- LPI 1-A: Fundamentos de Psicología Industrial (idAsignacion=1, Prof 1)
INSERT INTO Horarios (idAsignacion, idProfesor, dia, hora_inicio, hora_fin, tipo) VALUES
(1, 1, 'Lunes', '07:00:00', '08:00:00', 'CLASE'),
(1, 1, 'Lunes', '08:00:00', '09:00:00', 'CLASE'),
(1, 1, 'Miercoles', '07:00:00', '08:00:00', 'CLASE'),
(1, 1, 'Miercoles', '08:00:00', '09:00:00', 'CLASE'); -- Total 4 hrs

-- LPI 5-A: Habilidades de Supervisión (idAsignacion=2, Prof 5)
INSERT INTO Horarios (idAsignacion, idProfesor, dia, hora_inicio, hora_fin, tipo) VALUES
(2, 5, 'Martes', '10:00:00', '11:00:00', 'CLASE'),
(2, 5, 'Martes', '11:00:00', '12:00:00', 'CLASE'),
(2, 5, 'Jueves', '10:00:00', '11:00:00', 'CLASE'),
(2, 5, 'Jueves', '11:00:00', '12:00:00', 'CLASE'),
(2, 5, 'Viernes', '10:00:00', '11:00:00', 'CLASE'); -- Total 5 hrs

-- LPI 1-A: Inglés I (idAsignacion=4, Prof 4)
INSERT INTO Horarios (idAsignacion, idProfesor, dia, hora_inicio, hora_fin, tipo) VALUES
(4, 4, 'Lunes', '10:00:00', '11:00:00', 'CLASE'),
(4, 4, 'Martes', '10:00:00', '11:00:00', 'CLASE'),
(4, 4, 'Miercoles', '10:00:00', '11:00:00', 'CLASE'),
(4, 4, 'Jueves', '10:00:00', '11:00:00', 'CLASE'),
(4, 4, 'Viernes', '10:00:00', '11:00:00', 'CLASE'); -- Total 5 hrs

-- BLOQUES DE DESCARGA:

-- Profesor 1 (Arturo Reyes): 5 horas de Descarga (Coordinación LPI)
INSERT INTO Horarios (idProfesor, dia, hora_inicio, hora_fin, tipo) VALUES
(1, 'Martes', '07:00:00', '08:00:00', 'DESCARGA'),
(1, 'Martes', '08:00:00', '09:00:00', 'DESCARGA'),
(1, 'Jueves', '07:00:00', '08:00:00', 'DESCARGA'),
(1, 'Jueves', '08:00:00', '09:00:00', 'DESCARGA'),
(1, 'Viernes', '07:00:00', '08:00:00', 'DESCARGA');