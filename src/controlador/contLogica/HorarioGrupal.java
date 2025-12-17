package controlador.contLogica;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.utils.IOUtils;

import modelo.secundarias.ReporteGrupoCabecera;

public class HorarioGrupal
{

    // Constantes de Diseño y Posición
    private static final String TITULO_ARCHIVO = "Horarios Grupales Finales";//Set para cambiar el titulo
    private static final int FILA_INICIO_TABLA_ASIGNATURAS = 11;
    private int FILA_INICIO_TABLA_HORARIO; //Dinamico para modificarse dentro del metodo de la tabla asignaturas
    private int FILA_INICIO_PIEDEPAGINA; //Dinamico para modificarse dentro del metodo de la tabla horario
    private static final int COLUMNA_INICIO = 0;

    private static final List<String> DIAS_SEMANA = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");
    private static final List<LocalTime> HORAS_INICIO = List.of(
            LocalTime.of(7, 0), LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
            LocalTime.of(11, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0),
            LocalTime.of(15, 0), LocalTime.of(16, 0), LocalTime.of(17, 0), LocalTime.of(18, 0),
            LocalTime.of(19, 0), LocalTime.of(20, 0)
    );

    public static void generarHorarioCompleto() {
        System.out.println("--- Iniciando creación de archivos Excel ---");
        HorarioGrupal generador = new HorarioGrupal();

        HorarioGrupalService service = new HorarioGrupalService();
        Map<Integer, List<ReporteGrupoCabecera>> gruposConAsignaciones
                = service.asignacionesDetallePorCiclo();

        if (gruposConAsignaciones.isEmpty()) {
            System.out.println("No se encontraron grupos para generar el reporte.");
            return;
        }

        try {
            generador.createExcelFile(TITULO_ARCHIVO + ".xlsx", gruposConAsignaciones, service);
            System.out.println("--- Proceso finalizado: Archivo generado exitosamente ---");

        } catch (IOException e) {
            System.err.println("ERROR al generar el archivo Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //Main para pruebas de escritorio------
    /*
    public static void main(String[] args) throws IOException {
        System.out.println("--- Iniciando creación de archivos Excel ---");

        HorarioGrupal generador = new HorarioGrupal();

        HorarioGrupalService service = new HorarioGrupalService();
        Map<Integer, List<ReporteGrupoCabecera>> gruposConAsignaciones
                = service.asignacionesDetallePorCiclo();

        if (gruposConAsignaciones.isEmpty()) {
            System.out.println("No se encontraron grupos para generar el reporte.");
            return;
        }

        generador.createExcelFile(TITULO_ARCHIVO + ".xlsx", gruposConAsignaciones, service);
        System.out.println("--- Proceso finalizado ---");
    }
    */
    
    public void createExcelFile(String fileName, Map<Integer, List<ReporteGrupoCabecera>> gruposConAsignaciones,
            HorarioGrupalService service) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        for (Map.Entry<Integer, List<ReporteGrupoCabecera>> entry : gruposConAsignaciones.entrySet()) {

            List<ReporteGrupoCabecera> asignaciones = entry.getValue();
            Map<String, Map<LocalTime, String>> matrizHorario = construirMatrizContenido(asignaciones);

            ReporteGrupoCabecera cabecera = asignaciones.get(0);
            int idGrupo = entry.getKey();

            XSSFSheet sheet = workbook.createSheet(cabecera.getNombreGrupo());

            // Desactiva vista de los bordes de los sheets
            sheet.setDisplayGridlines(false);

            // 1. CREACIÓN DE ESTILOS
            Map<String, CellStyle> styles = crearEstilos(workbook);

            // 2. INSERCIÓN DEL LOGO (Mockeado)
            insertarLogo(workbook, sheet);

            // 3. ENCABEZADO SUPERIOR
            crearEncabezadoSuperior(sheet, styles, cabecera);

            // 4. TABLA DE ASIGNATURAS
            crearTablaAsignaturas(sheet, styles, asignaciones);

            // 5. ENCABEZADO DE HORARIOS
            crearEncabezadoTabla2(sheet, styles);

            // 6. CUERPO DE HORARIOS
            crearTablaHorarios(sheet, styles, asignaciones, matrizHorario);

            // 6. PieDelFormato
            creaPiedePagina(sheet, styles);

        }
        // 9. ESCRITURA Y CIERRE
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            System.out.println("Archivo creado exitosamente: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Map<LocalTime, String>> construirMatrizContenido(List<ReporteGrupoCabecera> asignaciones) {

        // Matriz: Map<Día, Map<HoraInicio, Contenido>>
        Map<String, Map<LocalTime, String>> matriz = new HashMap<>();

        // Inicializar la matriz con los días definidos en las constantes
        DIAS_SEMANA.forEach(dia -> matriz.put(dia, new HashMap<>()));

        for (ReporteGrupoCabecera asignacion : asignaciones) {
            String nombreMateria = asignacion.getNombreMateria();
            // Si se debe incluir el profesor:
            // String nombreProfesor = asignacion.getNombreProfesor(); 
            String contenido = nombreMateria; // + "\n" + nombreProfesor;

            for (modelo.secundarias.ReporteGrupoHorario bloque : asignacion.getBloquesHorario()) {
                String dia = bloque.getDia();
                LocalTime horaInicio = bloque.getHoraInicio();

                if (dia != null && horaInicio != null && matriz.containsKey(dia)) {
                    // Mapear la materia en el día y hora correctos
                    matriz.get(dia).put(horaInicio, contenido);
                }
            }
        }
        return matriz;
    }

    private Map<String, Double> calcularHorasTotales(List<ReporteGrupoCabecera> asignacionesDelGrupo) {

        Map<String, Double> horasPorDia = new HashMap<>();

        DIAS_SEMANA.forEach(dia -> horasPorDia.put(dia, 0.0));

        for (ReporteGrupoCabecera asignacion : asignacionesDelGrupo) {
            for (modelo.secundarias.ReporteGrupoHorario bloque : asignacion.getBloquesHorario()) {

                String dia = bloque.getDia();
                LocalTime inicio = bloque.getHoraInicio();
                LocalTime fin = bloque.getHoraFin();

                if (dia != null && inicio != null && fin != null) {

                    long duracionMinutos = java.time.Duration.between(inicio, fin).toMinutes();

                    double duracionHoras = duracionMinutos / 60.0;

                    horasPorDia.compute(dia, (k, v) -> (v == null ? 0.0 : v) + duracionHoras);
                }
            }
        }

        double totalSemanal = horasPorDia.values().stream().mapToDouble(Double::doubleValue).sum();
        horasPorDia.put("SEMANAL", totalSemanal);

        return horasPorDia;
    }

    private static void insertarLogo(XSSFWorkbook workbook, XSSFSheet sheet) throws IOException {
        byte[] bytesDeImagen;

        // --- Imagen 1: GobEdomex ---
        try (InputStream imagen1 = HorarioGrupal.class.getResourceAsStream("/Images/GobEdomex.png")) {
            System.out.println("Ruta imagen1: " + HorarioGrupal.class.getResource("/Images/GobEdomex.png"));
            if (imagen1 != null) {
                bytesDeImagen = IOUtils.toByteArray(imagen1);
                int pictureIdx = workbook.addPicture(bytesDeImagen, Workbook.PICTURE_TYPE_PNG);
                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 0, 0, 5, 3);
                drawing.createPicture(anchor, pictureIdx);
            } else {
                System.err.println("No se encontró /Images/GobEdomex.png");
            }
        }

        // --- Imagen 2: UMB ---
        try (InputStream imagen2 = HorarioGrupal.class.getResourceAsStream("/Images/UMB.jpg")) {
            System.out.println("Ruta imagen2: " + HorarioGrupal.class.getResource("/Images/UMB.jpg"));
            if (imagen2 != null) {
                bytesDeImagen = IOUtils.toByteArray(imagen2);
                int pictureIdx = workbook.addPicture(bytesDeImagen, Workbook.PICTURE_TYPE_JPEG);
                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 22, 0, 24, 3);
                drawing.createPicture(anchor, pictureIdx);
            } else {
                System.err.println("No se encontró /Images/UMB.jpg");
            }
        }
    }

    private static Map<String, CellStyle> crearEstilos(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();

        // Título Principal
        CellStyle styleTitulo = workbook.createCellStyle();
        Font fontTitulo = workbook.createFont();
        fontTitulo.setBold(true);
        fontTitulo.setFontHeightInPoints((short) 11);
        styleTitulo.setFont(fontTitulo);
        styleTitulo.setAlignment(HorizontalAlignment.CENTER);
        styles.put("TituloPrincipal", styleTitulo);

        // Subtítulo
        CellStyle styleSubtitulo = workbook.createCellStyle();
        Font fontSubtitulo = workbook.createFont();
        fontSubtitulo.setBold(true);
        fontSubtitulo.setFontHeightInPoints((short) 10);
        styleSubtitulo.setFont(fontSubtitulo);
        styleSubtitulo.setAlignment(HorizontalAlignment.CENTER);
        styles.put("Subtitulo", styleSubtitulo);

        // Subtítulo sin negrita al 10
        CellStyle styleSubtitulo1 = workbook.createCellStyle();
        Font fontSubtitulo1 = workbook.createFont();
        fontSubtitulo1.setFontHeightInPoints((short) 10);
        styleSubtitulo1.setFont(fontSubtitulo1);
        styleSubtitulo1.setAlignment(HorizontalAlignment.CENTER);
        styles.put("Subtitulo1", styleSubtitulo1);

        // Subtítulo sin negrita al 8
        CellStyle styleSubtitulo2 = workbook.createCellStyle();
        Font fontSubtitulo2 = workbook.createFont();
        fontSubtitulo2.setFontHeightInPoints((short) 8);
        styleSubtitulo2.setFont(fontSubtitulo2);
        styleSubtitulo2.setAlignment(HorizontalAlignment.CENTER);
        styles.put("Subtitulo2", styleSubtitulo2);

        // Firma
        CellStyle styleFirma = workbook.createCellStyle();
        styleFirma.setFont(fontSubtitulo1);
        styleFirma.setAlignment(HorizontalAlignment.CENTER);
        styleFirma.setBorderTop(BorderStyle.MEDIUM);
        styles.put("Firma", styleFirma);

        // Encabezados de tabla
        byte[] rgb = new byte[]{
            (byte) 150, // Red
            (byte) 190, // Green
            (byte) 90 // Blue
        };

        byte[] rgbGris = new byte[]{
            (byte) 192, // Red
            (byte) 192, // Green
            (byte) 192 // Blue
        };

        //Asignacíon de negritas al 8
        Font negritas = workbook.createFont();
        negritas.setBold(true);
        negritas.setFontHeightInPoints((short) 8);

        //No negritas
        Font nonegritas = workbook.createFont();
        nonegritas.setFontHeightInPoints((short) 8);

        //No negritas al 7
        Font nonegritas2 = workbook.createFont();
        nonegritas2.setFontHeightInPoints((short) 7);

        //Encabezado para las 2 tablas
        XSSFColor xssfColor = new XSSFColor(rgb, null);
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setFont(negritas);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setFillForegroundColor(xssfColor);
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setBorderBottom(BorderStyle.NONE);
        styleHeader.setBorderTop(BorderStyle.NONE);
        styleHeader.setBorderLeft(BorderStyle.NONE);
        styleHeader.setBorderRight(BorderStyle.NONE);
        styleHeader.setWrapText(true);
        styles.put("HeaderTabla", styleHeader);

        //Encabezado sin fondo (subencabezados)
        CellStyle styleHeader1 = workbook.createCellStyle();
        styleHeader1.setFont(nonegritas);
        styleHeader1.setAlignment(HorizontalAlignment.CENTER);
        styleHeader1.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader1.setFillPattern(FillPatternType.NO_FILL);
        styleHeader1.setBorderBottom(BorderStyle.DOTTED);
        styleHeader1.setBorderTop(BorderStyle.DOTTED);
        styleHeader1.setBorderLeft(BorderStyle.DOTTED);
        styleHeader1.setBorderRight(BorderStyle.DOTTED);
        styleHeader1.setWrapText(true);
        styles.put("HeaderTabla1", styleHeader1);

        //Encabezado fondo gris (subencabezados)
        XSSFColor xssfColorGris = new XSSFColor(rgbGris, null);
        CellStyle styleHeader2 = workbook.createCellStyle();
        styleHeader2.setFont(nonegritas);
        styleHeader2.setAlignment(HorizontalAlignment.CENTER);
        styleHeader2.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader2.setFillForegroundColor(xssfColorGris);
        styleHeader2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader2.setBorderBottom(BorderStyle.DOTTED);
        styleHeader2.setBorderTop(BorderStyle.DOTTED);
        styleHeader2.setBorderLeft(BorderStyle.DOTTED);
        styleHeader2.setBorderRight(BorderStyle.DOTTED);
        styleHeader2.setWrapText(true);
        styles.put("HeaderTabla2", styleHeader2);

        //Datos sin negritas
        // Datos con bordes punteados a la izquierda
        CellStyle styleData = workbook.createCellStyle();
        styleData.setFont(nonegritas);
        styleData.setBorderBottom(BorderStyle.DOTTED);
        styleData.setBorderTop(BorderStyle.DOTTED);
        styleData.setBorderLeft(BorderStyle.DOTTED);
        styleData.setBorderRight(BorderStyle.DOTTED);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);
        styleData.setAlignment(HorizontalAlignment.LEFT);
        styleData.setWrapText(true);
        styles.put("DataBordersI", styleData);

        //Datos con negritas
        //Datos con bordes punteados a la derecha
        CellStyle styleDataC = workbook.createCellStyle();
        styleDataC.setFont(negritas);
        styleDataC.setBorderBottom(BorderStyle.DOTTED);
        styleDataC.setBorderTop(BorderStyle.DOTTED);
        styleDataC.setBorderLeft(BorderStyle.DOTTED);
        styleDataC.setBorderRight(BorderStyle.DOTTED);
        styleDataC.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDataC.setAlignment(HorizontalAlignment.RIGHT);
        styleDataC.setWrapText(true);
        styles.put("DataBordersD", styleDataC);

        // Datos con bordes punteados al centro
        CellStyle styleDataG = workbook.createCellStyle();
        styleDataG.setFont(negritas);
        styleDataG.setBorderBottom(BorderStyle.DOTTED);
        styleDataG.setBorderTop(BorderStyle.DOTTED);
        styleDataG.setBorderLeft(BorderStyle.DOTTED);
        styleDataG.setBorderRight(BorderStyle.DOTTED);
        styleDataG.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDataG.setAlignment(HorizontalAlignment.CENTER);
        styleDataG.setWrapText(true);
        styles.put("DataBordersC", styleDataG);

        //Datos sin negritas al centro
        CellStyle styleDataS = workbook.createCellStyle();
        styleDataS.setFont(nonegritas2);
        styleDataS.setBorderBottom(BorderStyle.DOTTED);
        styleDataS.setBorderTop(BorderStyle.DOTTED);
        styleDataS.setBorderLeft(BorderStyle.DOTTED);
        styleDataS.setBorderRight(BorderStyle.DOTTED);
        styleDataS.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDataS.setAlignment(HorizontalAlignment.CENTER);
        styleDataS.setWrapText(true);
        styles.put("DataBordersCSN", styleDataS);

        // Horario
        CellStyle styleHorario = workbook.createCellStyle();
        styleHorario.cloneStyleFrom(styleData);
        styleHorario.setAlignment(HorizontalAlignment.CENTER);
        styles.put("HorarioCell", styleHorario);

        // Materia en horario
        CellStyle styleMateria = workbook.createCellStyle();
        styleMateria.cloneStyleFrom(styleData);
        styleMateria.setAlignment(HorizontalAlignment.CENTER);
        styleMateria.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put("HorarioMateria", styleMateria);

        //Pie de tabla alineado a la derecha
        CellStyle styleFooter = workbook.createCellStyle();
        styleFooter.setFont(negritas);
        styleFooter.setAlignment(HorizontalAlignment.RIGHT);
        styleFooter.setVerticalAlignment(VerticalAlignment.CENTER);
        styleFooter.setFillForegroundColor(xssfColor);
        styleFooter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleFooter.setBorderBottom(BorderStyle.NONE);
        styleFooter.setBorderTop(BorderStyle.NONE);
        styleFooter.setBorderLeft(BorderStyle.NONE);
        styleFooter.setBorderRight(BorderStyle.NONE);
        styleFooter.setWrapText(true);
        styles.put("FooterDer", styleFooter);

        return styles;
    }

    private void crearEncabezadoSuperior(Sheet sheet, Map<String, CellStyle> styles, ReporteGrupoCabecera cabecera) {
        int anchoFusion = 4 * 256;//Columnas cortas
        int anchoIndividual = 16 * 256;//Columnas grandes
        //Anchos de columnas predefinidos

        for (int i = 1; i < 25; i++) {
            if (i % 4 == 0) {
                sheet.setColumnWidth(i - 1, anchoIndividual);
            } else {
                sheet.setColumnWidth(i - 1, anchoFusion);
            }
        }
        //Semestre par o impar
        int n;
        if (cabecera.getNumeroSemestre() % 2 == 0) {
            n = 2;
        } else {
            n = 1;
        }

        String[] Semestre = {"PRIMERO", "SEGUNDO", "TERCERO", "CUART0", "QUINTO", "SEXTO", "SEPTIMO", "OCTAVO", "NOVENO", "DECIMO"};

        Row row1 = sheet.createRow(3);
        Cell cellA1 = row1.createCell(COLUMNA_INICIO);
        cellA1.setCellValue("UNIVERSIDAD MEXIQUENSE DEL BICENTENARIO");
        cellA1.setCellStyle(styles.get("TituloPrincipal"));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 23));

        Row row2 = sheet.createRow(4);
        Cell cellA2 = row2.createCell(COLUMNA_INICIO);
        cellA2.setCellValue("DIRECCIÓN ACADÉMICA");
        cellA2.setCellStyle(styles.get("Subtitulo1"));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 23));

        Row row4 = sheet.createRow(6);
        Cell cellA5 = row4.createCell(COLUMNA_INICIO);
        cellA5.setCellValue("HORARIO GRUPAL PARA EL PERIODO ( " + cabecera.getAnioCicloConcatenado() + "/" + n + " )");//CONSTRUCTOR
        cellA5.setCellStyle(styles.get("Subtitulo"));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 23));

        Row row5 = sheet.createRow(7);
        Cell cellA6 = row5.createCell(COLUMNA_INICIO);
        cellA6.setCellValue("UNIDAD DE ESTUDIOS SUPERIORES XALATLACO");
        cellA6.setCellStyle(styles.get("Subtitulo"));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 23));

        Row row6 = sheet.createRow(9);
        Cell cellA7 = row6.createCell(1);
        cellA7.setCellValue("PROGRAMA EDUCATIVO: ");
        cellA7.setCellStyle(styles.get("Subtitulo2"));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 3));

        Cell cellA8 = row6.createCell(4);
        cellA8.setCellValue(cabecera.getNombreCarrera().toUpperCase());//CONSTRUCTOR
        cellA8.setCellStyle(styles.get("Subtitulo2"));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 4, 11));

        Cell cellA9 = row6.createCell(15);
        cellA9.setCellValue("SEMESTRE: " + Semestre[cabecera.getNumeroSemestre() - 1]);//CONSTRUCTOR PARA INTEGRAR SEMESTRE CON CONCATENACION
        cellA9.setCellStyle(styles.get("Subtitulo2"));

        Cell cellA10 = row6.createCell(20);
        cellA10.setCellValue("GRUPO:");
        cellA10.setCellStyle(styles.get("Subtitulo2"));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 20, 22));

        Cell cellA11 = row6.createCell(23);
        cellA11.setCellValue(cabecera.getNombreGrupo().toUpperCase());//CONSTRUCTOR PARA LLAMAR EL VALOR DEL GRUPO
        cellA11.setCellStyle(styles.get("Subtitulo2"));
    }

    private void crearTablaAsignaturas(Sheet sheet, Map<String, CellStyle> styles, List<ReporteGrupoCabecera> asignaciones) {

        int rowNum = FILA_INICIO_TABLA_ASIGNATURAS;
        Row headerPrincipalRow = sheet.createRow(rowNum++);
        Cell cellHeaderP = headerPrincipalRow.createCell(COLUMNA_INICIO);
        cellHeaderP.setCellValue("ASIGNATURAS Y HORARIO AUTORIZADO");
        cellHeaderP.setCellStyle(styles.get("HeaderTabla"));
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 23));

        Row headerRow1 = sheet.createRow(rowNum++);

        // Definiciones de la estructura de la 1er tabla
        int[] posicionesIniciales = {COLUMNA_INICIO, 3, 11, 12, 15, 23};
        String[] nombresEncabezado = {"No", "Asignatura", "Horas", "Grupo", "DOCENTES", "Horas"};
        int[] longitudesFusion = {3, 8, 1, 3, 8, 1};

        // Bucle para crear las Celdas del ENCABEZADO y asegurar los bordes
        for (int j = 0; j < posicionesIniciales.length; j++) {
            int columnaInicio = posicionesIniciales[j];
            int longitud = longitudesFusion[j];
            int columnaFin = columnaInicio + longitud - 1;

            for (int col = columnaInicio; col <= columnaFin; col++) {
                Cell cell = headerRow1.createCell(col);

                cell.setCellStyle(styles.get("HeaderTabla1"));

                if (col == columnaInicio) {
                    cell.setCellValue(nombresEncabezado[j]);
                }
            }

            // Fusión de la región completa
            if (longitud > 1) {
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, columnaInicio, columnaFin));
            }
        }

        int contadorNo = 1;

        for (ReporteGrupoCabecera asignacion : asignaciones) {

            Row dataRow = sheet.createRow(rowNum++);

            for (int j = 0; j < posicionesIniciales.length; j++) {
                int columnaInicio = posicionesIniciales[j];
                int longitud = longitudesFusion[j];
                int columnaFin = columnaInicio + longitud - 1;

                // Aplicacion individual por columna
                for (int col = columnaInicio; col <= columnaFin; col++) {
                    Cell cell = dataRow.createCell(col);
                    cell.setCellStyle(styles.get("HeaderTabla1"));

                    if (col == columnaInicio) {

                        // Llenado de datos
                        if (j == 0) { // Columna "No"
                            cell.setCellStyle(styles.get("DataBordersCSN"));
                            cell.setCellValue(contadorNo);
                        } else if (j == 1) { // Columna "Asignatura" (Nombre de la materia)
                            cell.setCellStyle(styles.get("DataBordersI"));
                            cell.setCellValue(asignacion.getNombreMateria().toUpperCase());
                        } else if (j == 2 || j == 5) {
                            cell.setCellStyle(styles.get("DataBordersCSN"));
                            cell.setCellValue(asignacion.getHorasSemana());
                        } else if (j == 3) { // Columna "Grupo"
                            cell.setCellStyle(styles.get("DataBordersI"));
                            cell.setCellValue(asignacion.getNombreGrupo().toUpperCase());
                        } else if (j == 4) { // Columna "Docente"
                            cell.setCellStyle(styles.get("DataBordersI"));
                            cell.setCellValue(asignacion.getNombreProfesor().toUpperCase());
                        }
                    }
                }

                // FUSIÓN DE LA REGIÓN
                if (longitud > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, columnaInicio, columnaFin));
                }
            }
            contadorNo++;
        }

        int totHoras = asignaciones.stream()
                .mapToInt(ReporteGrupoCabecera::getHorasSemana)
                .sum();

        // Subtotales
        int longitud1 = 11;
        int longitud2 = 23;

        Row subtotalRow = sheet.createRow(rowNum++);

        for (int col = COLUMNA_INICIO; col < longitud1; col++) {
            Cell cell = subtotalRow.createCell(col);
            cell.setCellStyle(styles.get("DataBordersD"));

            if (col == COLUMNA_INICIO) {
                cell.setCellValue("Subtotal");
            }

        }
        sheet.addMergedRegion(new CellRangeAddress(subtotalRow.getRowNum(), subtotalRow.getRowNum(), 0, 10));

        Cell totalHorasIzq = subtotalRow.createCell(COLUMNA_INICIO + 11);
        totalHorasIzq.setCellValue(totHoras);
        totalHorasIzq.setCellStyle(styles.get("DataBordersC"));

        for (int col = COLUMNA_INICIO + 12; col < longitud2; col++) {
            Cell cell = subtotalRow.createCell(col);
            cell.setCellStyle(styles.get("DataBordersD"));

            if (col == COLUMNA_INICIO + 12) {
                cell.setCellValue("Subtotal");
            }

        }
        sheet.addMergedRegion(new CellRangeAddress(subtotalRow.getRowNum(), subtotalRow.getRowNum(), 12, 22));

        Cell totalHorasDer = subtotalRow.createCell(COLUMNA_INICIO + 23);
        totalHorasDer.setCellValue(" ");
        totalHorasDer.setCellStyle(styles.get("HeaderTabla1"));

        Row Tabla1Fin = sheet.createRow(rowNum++);
        for (int col = COLUMNA_INICIO + 18; col < longitud2; col++) {
            Cell cell = Tabla1Fin.createCell(col);
            cell.setCellStyle(styles.get("FooterDer"));

            if (col == COLUMNA_INICIO + 18) {
                cell.setCellValue("Total x Materia/actividad");
            }

        }
        sheet.addMergedRegion(new CellRangeAddress(Tabla1Fin.getRowNum(), Tabla1Fin.getRowNum(), 18, 22));

        Cell totalHoras = Tabla1Fin.createCell(COLUMNA_INICIO + 23);
        totalHoras.setCellValue(totHoras);
        totalHoras.setCellStyle(styles.get("HeaderTabla"));

        FILA_INICIO_TABLA_HORARIO = rowNum + 1;
    }

    private void crearEncabezadoTabla2(Sheet sheet, Map<String, CellStyle> styles) {
        // Definimos los días a incluir en el encabezado
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        String[] color = {"HeaderTabla2", "HeaderTabla1", "HeaderTabla2", "HeaderTabla1", "HeaderTabla2", "HeaderTabla1"};

        int rowNum = FILA_INICIO_TABLA_HORARIO;
        Row headerRow = sheet.createRow(rowNum);
        int longHorario = 3;
        int colNum = COLUMNA_INICIO;

        for (int i = 0; i < dias.length; i++) {

            String dia = dias[i];

            int columnaInicioHora = colNum;
            int columnaFinHora = colNum + longHorario - 1;

            for (int j = 0; j < longHorario; j++) {
                Cell cell = headerRow.createCell(colNum++); // ColNum avanza 3 veces
                cell.setCellStyle(styles.get(color[i]));

                if (j == 0) {
                    cell.setCellValue("Horario");
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, columnaInicioHora, columnaFinHora));

            Cell cellDia = headerRow.createCell(colNum++); // ColNum avanza 1 vez
            cellDia.setCellValue(dia);
            cellDia.setCellStyle(styles.get(color[i]));
        }
    }

    //Valores nulos
    private String toUpperCaseSafe(String str) {
        return (str == null || str.isEmpty()) ? "" : str.toUpperCase();
    }

    private void crearTablaHorarios(Sheet sheet, Map<String, CellStyle> styles, List<ReporteGrupoCabecera> asignacionesDelGrupo, Map<String, Map<LocalTime, String>> matrizHorario) {

        List<LocalTime> horasInicio = HORAS_INICIO;

        String[] horas = {"7 - 8", "8 - 9", "9 - 10", "10 - 11", "11 - 12", "12 - 13", "13 - 14", "14 - 15", "15 - 16", "16 - 17", "17 - 18", "18 - 19", "19 - 20", "20 - 21"};
        int rowNum = FILA_INICIO_TABLA_HORARIO + 1;
        int columnasfusion = 3;
        for (int q = 0; q < horasInicio.size(); q++) {

            LocalTime horaActual = horasInicio.get(q);
            int colNum = COLUMNA_INICIO;
            Row DataRow = sheet.createRow(rowNum++);

            // Iteración por DÍA (n)
            for (int n = 0; n < DIAS_SEMANA.size(); n++) {

                String diaActual = DIAS_SEMANA.get(n);

                String contenidoCelda = matrizHorario
                        .getOrDefault(diaActual, new HashMap<>())
                        .get(horaActual);

                String contenidoFinal = toUpperCaseSafe(contenidoCelda);

                int columnaInicioHora = colNum;
                int columnaFinHora = colNum + columnasfusion - 1;

                // Iteración para crear las 3 celdas fusionadas
                for (int i = 0; i < columnasfusion; i++) {
                    Cell cell = DataRow.createCell(colNum);
                    cell.setCellStyle(styles.get("DataBordersCSN"));

                    if (i == 0) {
                        cell.setCellValue(horas[q]);
                    }
                    colNum++;
                }

                sheet.addMergedRegion(new CellRangeAddress(DataRow.getRowNum(), DataRow.getRowNum(), columnaInicioHora, columnaFinHora));

                // Celda de Margen (separador)
                Cell cellDia = DataRow.createCell(colNum++);
                cellDia.setCellValue(contenidoFinal);
                cellDia.setCellStyle(styles.get("DataBordersCSN"));
            }
        }

        Map<String, Double> horasTotales = calcularHorasTotales(asignacionesDelGrupo);

        int colNum = COLUMNA_INICIO;
        Row DataRow = sheet.createRow(rowNum++);
        for (int n = 0; n < DIAS_SEMANA.size(); n++) { // 6 días (Lunes a Sábado)

            String diaActual = DIAS_SEMANA.get(n);

            double horasDelDia = horasTotales.getOrDefault(diaActual, 0.0);

            int columnaInicioHora = colNum;
            int columnaFinHora = colNum + columnasfusion - 1;

            for (int i = 0; i < columnasfusion; i++) {
                Cell cell = DataRow.createCell(colNum); // ColNum avanza 3 veces
                cell.setCellStyle(styles.get("HeaderTabla1"));

                if (n == 0) {
                    cell.setCellValue("Total x dia");
                }

                colNum++;
            }

            sheet.addMergedRegion(new CellRangeAddress(DataRow.getRowNum(), DataRow.getRowNum(), columnaInicioHora, columnaFinHora));

            Cell cellDia = DataRow.createCell(colNum++);

            String valorHorasDia = (horasDelDia == (long) horasDelDia) ? String.valueOf((long) horasDelDia) : String.format("%.2f", horasDelDia);

            cellDia.setCellValue(valorHorasDia);//Agregar cantidad de horas por dia
            cellDia.setCellStyle(styles.get("HeaderTabla1"));
        }

        Row DataRow1 = sheet.createRow(rowNum++);//Fila 39 contando desde 0

        double totalSemanal = horasTotales.getOrDefault("SEMANAL", 0.0);

        int columnasfusion1 = 4;
        int filainicio = 19;

        int columnaIniciototal = filainicio;
        int columnaFintotal = filainicio + columnasfusion1 - 1;

        for (int i = 0; i < columnasfusion1; i++) {
            Cell cell = DataRow1.createCell(filainicio); // ColNum avanza 3 veces
            cell.setCellStyle(styles.get("FooterDer"));

            if (columnaIniciototal == 19) {
                cell.setCellValue("Total");
            }

            filainicio++;
        }

        sheet.addMergedRegion(new CellRangeAddress(DataRow1.getRowNum(), DataRow1.getRowNum(), columnaIniciototal, columnaFintotal));

        Cell cellDia = DataRow1.createCell(filainicio++);

        String valorHorasSemana = (totalSemanal == (long) totalSemanal) ? String.valueOf((long) totalSemanal) : String.format("%.2f", totalSemanal);

        cellDia.setCellValue(valorHorasSemana);//Agregar cantidad de horas por semana
        cellDia.setCellStyle(styles.get("HeaderTabla"));

        FILA_INICIO_PIEDEPAGINA = rowNum + 3;

    }

    private void creaPiedePagina(XSSFSheet sheet, Map<String, CellStyle> styles) {

        int rowIndex = FILA_INICIO_PIEDEPAGINA;
        final int COL_NOTA_INICIO = 2;
        final int COL_COORD_INICIO = 14;
        final int ANCHO_FUSION = 10;

        Row rowNota = sheet.createRow(rowIndex++);

        for (int i = 0; i < ANCHO_FUSION; i++) {
            Cell cell = rowNota.createCell(COL_NOTA_INICIO + i);
            cell.setCellStyle(styles.get("Subtitulo"));
            if (i == 0) {
                cell.setCellValue("Nota: Si tenemos más de 3 hrs. seguidas, tenemos 5 minutos para despejarnos.");
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(rowNota.getRowNum(), rowNota.getRowNum(), COL_NOTA_INICIO, COL_NOTA_INICIO + ANCHO_FUSION - 1));

        for (int i = 0; i < ANCHO_FUSION; i++) {
            Cell cell = rowNota.createCell(COL_COORD_INICIO + i);
            cell.setCellStyle(styles.get("Subtitulo"));
            if (i == 0) {
                cell.setCellValue("COORDINADOR DE LA UNIDAD DE");
            }
        }
        // Fusión de la región del coordinador (Línea 1)
        sheet.addMergedRegion(new CellRangeAddress(rowNota.getRowNum(), rowNota.getRowNum(), COL_COORD_INICIO, COL_COORD_INICIO + ANCHO_FUSION - 1));

        Row rowEstudios = sheet.createRow(rowIndex++);
        for (int i = 0; i < ANCHO_FUSION; i++) {
            Cell cell = rowEstudios.createCell(COL_COORD_INICIO + i);
            cell.setCellStyle(styles.get("Subtitulo"));
            if (i == 0) {
                cell.setCellValue("ESTUDIOS SUPERIORES");
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(rowEstudios.getRowNum(), rowEstudios.getRowNum(), COL_COORD_INICIO, COL_COORD_INICIO + ANCHO_FUSION - 1));

        sheet.createRow(rowIndex++);

        Row rowFirma = sheet.createRow(rowIndex++);

        for (int i = 0; i < ANCHO_FUSION; i++) {
            Cell cell = rowFirma.createCell(COL_COORD_INICIO + i);

            cell.setCellStyle(styles.get("Firma"));

            if (i == 0) {
                cell.setCellValue("M.A.D.N JUAN JOSÉ OLÍN FABELA"); //Se puede cambiar por si llega a cambiar el coordinador
            }
        }
        // Fusión de la región del nombre
        sheet.addMergedRegion(new CellRangeAddress(rowFirma.getRowNum(), rowFirma.getRowNum(), COL_COORD_INICIO, COL_COORD_INICIO + ANCHO_FUSION - 1));
    }
}
