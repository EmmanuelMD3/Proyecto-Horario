package controlador.contLogica;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.secundarias.ReporteDescargas;
import org.apache.commons.compress.utils.IOUtils;

import modelo.secundarias.ReporteGrupoCabecera;
import modelo.secundarias.ReporteGrupoHorario;

public class HorarioDocente {

    // Constantes de Diseño y Posición
    private static final String TITULO_ARCHIVO = "HORARIOS POR DOCENTES FINALES";//Set para cambiar el titulo
    private static final int FILA_INICIO_TABLA_ASIGNATURAS = 11;
    private int FILA_INICIO_TABLA_HORARIO;
    private int FILA_INICIO_PIEDEPAGINA;
    private static final int COLUMNA_INICIO = 0;

    private static final List<String> DIAS_SEMANA = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado");
    private static final List<LocalTime> HORAS_INICIO = List.of(
            LocalTime.of(7, 0), LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
            LocalTime.of(11, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0),
            LocalTime.of(15, 0), LocalTime.of(16, 0), LocalTime.of(17, 0), LocalTime.of(18, 0),
            LocalTime.of(19, 0), LocalTime.of(20, 0)
    );
    
    public static void generarHorarioGrupal() throws IOException {
        System.out.println("--- Iniciando creación de archivos Excel ---");

        HorarioDocente generador = new HorarioDocente();
        HorarioGrupalService service = new HorarioGrupalService();

        Map<Integer, List<ReporteGrupoCabecera>> horariosPorProfesor
                = service.asignacionesDetallePorProfesor();

        if (horariosPorProfesor.isEmpty()) {
            System.out.println("No se encontraron asignaciones para generar el reporte de docentes.");
            return;
        }
        
        try{
            generador.createExcelFile(TITULO_ARCHIVO + ".xlsx", horariosPorProfesor);
            System.out.println("--- Proceso finalizado ---");
        } catch(IOException e) {
            System.err.println("ERROR al generar el archivo Excel: " + e.getMessage());
        }
        
    }
    
    /*
    public static void main(String[] args) throws IOException {
        System.out.println("--- Iniciando creación de archivos Excel ---");

        HorarioDocente generador = new HorarioDocente();
        HorarioGrupalService service = new HorarioGrupalService();

        Map<Integer, List<ReporteGrupoCabecera>> horariosPorProfesor
                = service.asignacionesDetallePorProfesor();

        if (horariosPorProfesor.isEmpty()) {
            System.out.println("No se encontraron asignaciones para generar el reporte de docentes.");
            return;
        }

        generador.createExcelFile(TITULO_ARCHIVO + ".xlsx", horariosPorProfesor);

        System.out.println("--- Proceso finalizado ---");
    }
    
    */
    
    public void createExcelFile(String fileName, Map<Integer, List<ReporteGrupoCabecera>> horariosPorProfesor) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        // 1. CREACIÓN DE ESTILOS
        Map<String, CellStyle> styles = crearEstilos(workbook);

        for (Map.Entry<Integer, List<ReporteGrupoCabecera>> entry : horariosPorProfesor.entrySet()) {

            List<ReporteGrupoCabecera> asignacionesDelProfesor = entry.getValue();
            Map<String, Map<LocalTime, String>> matrizHorario = construirMatrizContenido(asignacionesDelProfesor);

            ReporteGrupoCabecera cabecera = asignacionesDelProfesor.get(0);

            XSSFSheet sheet = workbook.createSheet(cabecera.getNombreProfesor().toUpperCase());

            // Desactiva vista de los bordes de los sheets
            sheet.setDisplayGridlines(false);

            // 2. INSERCIÓN DEL LOGO (Mockeado)
            insertarLogo(workbook, sheet);

            // 3. ENCABEZADO SUPERIOR
            crearEncabezadoSuperior(sheet, styles, cabecera);

            // 4. TABLA DE ASIGNATURAS
            crearTablaAsignaturas(sheet, styles, asignacionesDelProfesor);

            // 5. ENCABEZADO DE HORARIOS
            crearEncabezadoTabla2(sheet, styles);

            // 6. CUERPO DE HORARIOS
            crearTablaHorarios(sheet, styles, asignacionesDelProfesor, matrizHorario);

            // 7. PieDelFormato
            creaPiedePagina(sheet, styles, cabecera);
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
        Map<String, Map<LocalTime, String>> matriz = new HashMap<>();

        // 1. Agregar las materias (Asignaturas)
        for (ReporteGrupoCabecera asig : asignaciones) {
            for (ReporteGrupoHorario bloque : asig.getBloquesHorario()) {
                matriz.computeIfAbsent(bloque.getDia(), k -> new HashMap<>())
                        .put(bloque.getHoraInicio(), asig.getNombreMateria().toUpperCase());
            }
        }

        // 2. AGREGAR LAS COMPLEMENTARIAS (Descargas)
        // Obtenemos la lista de descargas del primer objeto (ya que todos la tienen igual)
        if (!asignaciones.isEmpty()) {
            List<ReporteDescargas> listaDescargas = asignaciones.get(0).getDescargas();
            for (ReporteDescargas desc : listaDescargas) {
                // Aquí es vital que tus descargas también tengan una lista de bloques de horario
                for (ReporteGrupoHorario bloqueDesc : desc.getBloquesHorario()) {
                    matriz.computeIfAbsent(bloqueDesc.getDia(), k -> new HashMap<>())
                            .put(bloqueDesc.getHoraInicio(), desc.getNombre().toUpperCase());
                }
            }
        }
        return matriz;
    }

    private Map<String, Double> calcularHorasTotalesDocente(List<ReporteGrupoCabecera> asignaciones) {
        Map<String, Double> totales = new HashMap<>();
        double semanal = 0;

        for (ReporteGrupoCabecera asig : asignaciones) {
            for (ReporteGrupoHorario bloque : asig.getBloquesHorario()) {
                double duracion = java.time.Duration.between(bloque.getHoraInicio(), bloque.getHoraFin()).toMinutes() / 60.0;

                totales.put(bloque.getDia(), totales.getOrDefault(bloque.getDia(), 0.0) + duracion);
                semanal += duracion;
            }
        }
        totales.put("SEMANAL", semanal);
        return totales;
    }

    private static void insertarLogo(XSSFWorkbook workbook, XSSFSheet sheet) throws IOException {
        byte[] bytesDeImagen;

        // --- Imagen 1: GobEdomex ---
        try (InputStream imagen1 = HorarioDocente.class.getResourceAsStream("/Images/GobEdomex.png")) {
            System.out.println("Ruta imagen1: " + HorarioDocente.class.getResource("/Images/GobEdomex.png"));
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
        try (InputStream imagen2 = HorarioDocente.class.getResourceAsStream("/Images/UMB.jpg")) {
            System.out.println("Ruta imagen2: " + HorarioDocente.class.getResource("/Images/UMB.jpg"));
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
        fontTitulo.setFontHeightInPoints((short) 12);
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
        
        // Subtítulo sin negrita al 8 a la izquierda
        CellStyle styleSubtitulo3 = workbook.createCellStyle();
        styleSubtitulo3.setFont(fontSubtitulo2);
        styleSubtitulo3.setAlignment(HorizontalAlignment.LEFT);
        styles.put("Subtitulo3", styleSubtitulo3);
        
        // Firma
        CellStyle styleFirma = workbook.createCellStyle();
        styleFirma.setFont(fontSubtitulo1);
        styleFirma.setAlignment(HorizontalAlignment.CENTER);
        styleFirma.setBorderTop(BorderStyle.MEDIUM);
        styles.put("Firma", styleFirma);

        // Encabezado de tabla verde
        byte[] rgb = new byte[]{
            (byte) 150, // Red
            (byte) 190, // Green
            (byte) 90 // Blue
        };
        
        // Encabezados de tabla gris gris
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
        
        //Encabezado para la tabla 1
        XSSFColor xssfColor = new XSSFColor(rgb, null);
        CellStyle styleHeader = workbook.createCellStyle();
        Font fontHeader = workbook.createFont();
        fontHeader.setBold(true);
        styleHeader.setFont(fontHeader);
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
        cellA5.setCellValue("HORARIO INDIVIDUAL PARA EL PERIODO ( " + cabecera.getAnioCicloConcatenado() + "/" + n + " )");
        cellA5.setCellStyle(styles.get("Subtitulo"));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 23));

        Row row5 = sheet.createRow(7);
        Cell cellA6 = row5.createCell(COLUMNA_INICIO);
        cellA6.setCellValue("UNIDAD DE ESTUDIOS SUPERIORES XALATLACO");
        cellA6.setCellStyle(styles.get("Subtitulo"));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 23));

        Row row6 = sheet.createRow(9);
        Cell cellA7 = row6.createCell(1);
        cellA7.setCellValue("NOMBRE DEL DOCENTE: ");
        cellA7.setCellStyle(styles.get("Subtitulo2"));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 3));

        Cell cellA8 = row6.createCell(4);
        cellA8.setCellValue(cabecera.getNombreProfesor().toUpperCase());//CONSTRUCTOR
        cellA8.setCellStyle(styles.get("Subtitulo3"));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 4, 11));
    }

    private void crearTablaAsignaturas(Sheet sheet, Map<String, CellStyle> styles, List<ReporteGrupoCabecera> asignacionesDelProfesor) {

        int rowNum = FILA_INICIO_TABLA_ASIGNATURAS;
        Row headerPrincipalRow = sheet.createRow(rowNum++);
        Cell cellHeaderP = headerPrincipalRow.createCell(COLUMNA_INICIO);
        cellHeaderP.setCellValue("ASIGNATURAS Y HORARIO AUTORIZADO");
        cellHeaderP.setCellStyle(styles.get("HeaderTabla"));
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 23));

        Row headerRow1 = sheet.createRow(rowNum++);

        // Definiciones de la estructura de la 1er tabla
        int[] posicionesIniciales = {COLUMNA_INICIO, 3, 11, 12, 15, 23};
        String[] nombresEncabezado = {"No", "Asignatura", "Horas", "Grupo", "Complementarias", "Horas"};
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

        List<ReporteDescargas> listaDescargas = asignacionesDelProfesor.get(0).getDescargas();

        int maxFilas = Math.max(asignacionesDelProfesor.size(), listaDescargas.size());

        int contadorNo = 1;
        int sumaHorasAsig = 0;
        int sumaHorasComp = 0;

        for (int i = 0; i < maxFilas; i++) {

            Row dataRow = sheet.createRow(rowNum++);

            for (int j = 0; j < posicionesIniciales.length; j++) {
                int columnaInicio = posicionesIniciales[j];
                int longitud = longitudesFusion[j];
                int columnaFin = columnaInicio + longitud - 1;

                for (int col = columnaInicio; col <= columnaFin; col++) {
                    Cell cell = dataRow.createCell(col);
                    cell.setCellStyle(styles.get("HeaderTabla1"));

                    if (col == columnaInicio) {

                        // --- COLUMNAS DE LA IZQUIERDA (Materias)
                        if (i < asignacionesDelProfesor.size()) {
                            ReporteGrupoCabecera asig = asignacionesDelProfesor.get(i);
                            if (j == 0) {
                                cell.setCellStyle(styles.get("DataBordersCSN"));
                                cell.setCellValue(contadorNo);
                            } else if (j == 1) {
                                cell.setCellStyle(styles.get("DataBordersI"));
                                cell.setCellValue(asig.getNombreMateria().toUpperCase());
                            } else if (j == 2) {
                                cell.setCellStyle(styles.get("DataBordersCSN"));
                                cell.setCellValue(+asig.getHorasSemana());
                                sumaHorasAsig += (col == columnaInicio) ? asig.getHorasSemana() : 0;
                            } else if (j == 3) {
                                cell.setCellStyle(styles.get("DataBordersI"));
                                cell.setCellValue(asig.getNombreGrupo().toUpperCase());
                            }
                        }

                        // --- COLUMNAS DE LA DERECHA (Descargas)
                        if (i < listaDescargas.size()) {
                            ReporteDescargas desc = listaDescargas.get(i);
                            if (j == 4) {
                                cell.setCellStyle(styles.get("DataBordersI"));
                                cell.setCellValue(desc.getNombre().toUpperCase());
                            } else if (j == 5) {
                                cell.setCellStyle(styles.get("DataBordersCSN"));
                                cell.setCellValue(desc.getHoras());
                                sumaHorasComp += (col == columnaInicio) ? desc.getHoras() : 0;
                            }
                        }
                    }
                }

                if (longitud > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, columnaInicio, columnaFin));
                }
            }
            contadorNo++;
        }

        sumaHorasAsig = asignacionesDelProfesor.stream().mapToInt(ReporteGrupoCabecera::getHorasSemana).sum();
        sumaHorasComp = listaDescargas.stream().mapToInt(ReporteDescargas::getHoras).sum();

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
        totalHorasIzq.setCellValue(sumaHorasAsig);
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
        totalHorasDer.setCellValue(sumaHorasComp);
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
        totalHoras.setCellValue(sumaHorasAsig + sumaHorasComp);
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
            String dia = dias [i];
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

    private void crearTablaHorarios(Sheet sheet, Map<String, CellStyle> styles, List<ReporteGrupoCabecera> asignacionesDelProfesor, Map<String, Map<LocalTime, String>> matrizHorario) {

        String[] horas = {"7 - 8", "8 - 9", "9 - 10", "10 - 11", "11 - 12", "12 - 13", "13 - 14", "14 - 15", "15 - 16", "16 - 17", "17 - 18", "18 - 19", "19 - 20", "20 - 21"};
        List<LocalTime> horasInicio = HORAS_INICIO;

        int rowNum = FILA_INICIO_TABLA_HORARIO + 1;
        int columnasfusion = 3;

        for (int q = 0; q < horasInicio.size(); q++) {
            LocalTime horaActual = horasInicio.get(q);
            int colNum = COLUMNA_INICIO;
            Row DataRow = sheet.createRow(rowNum++);

            for (int n = 0; n < DIAS_SEMANA.size(); n++) {
                String diaActual = DIAS_SEMANA.get(n);
                String contenidoCelda = matrizHorario
                        .getOrDefault(diaActual, new HashMap<>())
                        .get(horaActual);

                int columnaInicioHora = colNum;
                int columnaFinHora = colNum + columnasfusion - 1;

                for (int i = 0; i < columnasfusion; i++) {
                    Cell cell = DataRow.createCell(colNum); // ColNum avanza 3 veces
                    cell.setCellStyle(styles.get("DataBordersCSN"));

                    if (i == 0) {
                        cell.setCellValue(horas[q]);
                    }
                    colNum++;
                }

                sheet.addMergedRegion(new CellRangeAddress(DataRow.getRowNum(), DataRow.getRowNum(), columnaInicioHora, columnaFinHora));

                Cell cellDia = DataRow.createCell(colNum++);
                cellDia.setCellValue(contenidoCelda);
                cellDia.setCellStyle(styles.get("DataBordersCSN"));
            }
        }

        //Horas por dia
        Map<String, Double> horasTotales = calcularHorasTotalesDocente(asignacionesDelProfesor);

        int colNum = COLUMNA_INICIO;
        Row DataRow = sheet.createRow(rowNum++);
        for (int n = 0; n < DIAS_SEMANA.size(); n++) {

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

        //Horas por semana
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

        FILA_INICIO_PIEDEPAGINA = rowNum + 1;
    }

    private void creaPiedePagina(XSSFSheet sheet, Map<String, CellStyle> styles, ReporteGrupoCabecera cabecera) {

        int rowIndex = FILA_INICIO_PIEDEPAGINA;
        final int COL_NOTA_INICIO = 2;
        final int COL_COORD_INICIO = 14;
        final int ANCHO_FUSION = 10;

        Row rowNota = sheet.createRow(rowIndex++);

        for (int i = 0; i < ANCHO_FUSION; i++) {
            Cell cell = rowNota.createCell(COL_NOTA_INICIO + i);
            cell.setCellStyle(styles.get("Subtitulo"));
            if (i == 0) {
                cell.setCellValue("Firma del docente.");
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
            Cell cell = rowFirma.createCell(COL_NOTA_INICIO + i);

            cell.setCellStyle(styles.get("Firma"));

            if (i == 0) {
                cell.setCellValue(cabecera.getNombreProfesor().toUpperCase());
            }
        }

        sheet.addMergedRegion(new CellRangeAddress(rowFirma.getRowNum(), rowFirma.getRowNum(), COL_NOTA_INICIO, COL_NOTA_INICIO + ANCHO_FUSION - 1));

        for (int i = 0; i < ANCHO_FUSION; i++) {
            Cell cell = rowFirma.createCell(COL_COORD_INICIO + i);

            cell.setCellStyle(styles.get("Firma"));

            if (i == 0) {
                cell.setCellValue("M.A.D.N JUAN JOSÉ OLÍN FABELA");
            }
        }
        // Fusión de la región del nombre
        sheet.addMergedRegion(new CellRangeAddress(rowFirma.getRowNum(), rowFirma.getRowNum(), COL_COORD_INICIO, COL_COORD_INICIO + ANCHO_FUSION - 1));
    }
}
