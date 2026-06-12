package controlador;

import dao.EstudianteDAO;
import dao.InscripcionDAO;
import modelo.Estudiante;
import modelo.InscripcionMateria;
import modelo.Materia;
import vista.VentanaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;

public class Controlador implements ActionListener {

    private VentanaPrincipal vista;
    private Estudiante estudiante;
    private EstudianteDAO estudianteDAO;
    private InscripcionDAO inscripcionDAO;

    // ────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ────────────────────────────────────────────────────────────

    public Controlador(VentanaPrincipal vista) {
        this.vista = vista;
        this.estudianteDAO  = new EstudianteDAO();
        this.inscripcionDAO = new InscripcionDAO();

        // Cargar o registrar estudiante
        this.estudiante = estudianteDAO.cargar();
        if (this.estudiante == null) {
            String[] datos = vista.mostrarRegistroEstudiante();
            try {
                if (datos[0].isEmpty() || datos[1].isEmpty() || datos[2].isEmpty() || datos[3].isEmpty()) {
                    this.estudiante = new Estudiante("Sin nombre", "00000", "Sin carrera", 2024);
                } else {
                    this.estudiante = new Estudiante(
                            datos[0], datos[1], datos[2], Integer.parseInt(datos[3])
                    );
                }
                estudianteDAO.guardar(this.estudiante);
            } catch (NumberFormatException e) {
                this.estudiante = new Estudiante("Sin nombre", "00000", "Sin carrera", 2024);
                estudianteDAO.guardar(this.estudiante);
            }
        }

        // Restaurar inscripciones guardadas
        ArrayList<InscripcionMateria> inscripciones = inscripcionDAO.cargar();
        for (InscripcionMateria ins : inscripciones) {
            estudiante.restaurarInscripcion(ins);
        }

        // Conectar listeners y mostrar estado inicial
        vista.registrarListeners(this);

        // Listener de seleccion de tabla: actualiza metricas al hacer clic en una fila
        vista.registrarSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                actualizarMetricas();
            }
        });

        actualizarVista();
    }

    // ────────────────────────────────────────────────────────────
    //  DISPATCHER
    // ────────────────────────────────────────────────────────────

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "INSCRIBIR"            -> inscribir();
            case "REGISTRAR_ASISTENCIA" -> registrarAsistencia();
            case "NOTA_AGREGADA"        -> agregarNota();
            case "BAJA"                 -> { if (vista.mostrarConfirmacionBaja()) confirmarBaja(); }
            case "SITUACION_GENERAL"    -> mostrarSituacionGeneral();
            case "MATERIAS_EN_RIESGO"   -> mostrarMateriasEnRiesgo();
            case "APROBADAS"            -> mostrarAprobadas();
            case "VOLVER_PRINCIPAL"     -> vista.mostrarPanelPrincipal();
            case "ACERCA_DE"            -> vista.mostrarInfo("Acerca de",
                    "Sistema de Autogestion Estudiantil\nVersion 1.0");
            case "CERRAR"               -> System.exit(0);
        }
    }

    // ────────────────────────────────────────────────────────────
    //  INSCRIBIR MATERIA
    // ────────────────────────────────────────────────────────────

    private void inscribir() {
        String nombre    = vista.getTxtInscNombre();
        String codigo    = vista.getTxtInscCodigo();
        String anioStr   = vista.getTxtInscAnio();
        int cuatrimestre = vista.getComboCuatrimestre();

        if (nombre.isEmpty() || codigo.isEmpty() || anioStr.isEmpty()) {
            vista.mostrarError("Complete todos los campos.");
            return;
        }

        if (codigo.length() < 3 || codigo.length() > 10) {
            vista.mostrarError("El codigo debe tener entre 3 y 10 caracteres.");
            return;
        }

        try {
            int anio    = Integer.parseInt(anioStr);
            Materia mat = new Materia(nombre, codigo, cuatrimestre, anio);
            estudiante.inscribirse(mat, 20);
            inscripcionDAO.guardar(estudiante.getMaterias());
            actualizarVista();
            vista.limpiarFormulario();
        } catch (NumberFormatException ex) {
            vista.mostrarError("El anio debe ser un numero.");
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────────
    //  REGISTRAR ASISTENCIA
    // ────────────────────────────────────────────────────────────

    private void registrarAsistencia() {
        String codigo = vista.getSelectedCodigo();
        if (codigo == null) {
            vista.mostrarError("Seleccione una materia de la tabla.");
            return;
        }

        InscripcionMateria ins = estudiante.getInscripcion(codigo);
        if (ins == null) {
            vista.mostrarError("No se encontro la inscripcion.");
            return;
        }

        ins.registrarAsistencia(vista.isPresente());
        inscripcionDAO.guardar(estudiante.getMaterias());

        double porcentaje = ins.getPorcentajeAsistencia();
        if (porcentaje < 75) {
            vista.mostrarError("Asistencia critica en " + ins.getMateria().getNombre()
                    + ": " + String.format("%.1f%%", porcentaje)
                    + " — Perdiste la regularidad por inasistencias.");
        } else if (porcentaje >= 75 && porcentaje <= 85) {
            vista.mostrarAlertaAsistencia(ins.getMateria().getNombre(), porcentaje);
        }

        actualizarVista();
    }

    // ────────────────────────────────────────────────────────────
    //  AGREGAR NOTA
    // ────────────────────────────────────────────────────────────

    private void agregarNota() {
        String codigo = vista.getSelectedCodigo();
        if (codigo == null) {
            vista.mostrarError("Seleccione una materia de la tabla.");
            return;
        }

        InscripcionMateria ins = estudiante.getInscripcion(codigo);
        if (ins == null) {
            vista.mostrarError("No se encontro la inscripcion.");
            return;
        }

        try {
            double nota = Double.parseDouble(vista.getTxtNota());
            ins.agregarNota(nota);
            inscripcionDAO.guardar(estudiante.getMaterias());
            actualizarVista();
        } catch (NumberFormatException ex) {
            vista.mostrarError("Ingrese un numero valido para la nota.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────────
    //  DAR DE BAJA
    //
    //  FIX 2: usamos getCodigoPendienteDeBaja() en lugar de
    //  getSelectedCodigo(), porque al momento de presionar "Aceptar"
    //  la tabla ya puede haber perdido la seleccion.
    // ────────────────────────────────────────────────────────────

    private void confirmarBaja() {
        String codigo = vista.getCodigoPendienteDeBaja();
        if (codigo == null) {
            vista.mostrarError("Seleccione una materia de la tabla.");
            return;
        }

        try {
            estudiante.darDeBaja(codigo);
            inscripcionDAO.guardar(estudiante.getMaterias());
            actualizarVista();
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────────
    //  REPORTES
    // ────────────────────────────────────────────────────────────

    private void mostrarSituacionGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estudiante  : ").append(estudiante.getNombre()).append("\n");
        sb.append("Legajo      : ").append(estudiante.getLegajo()).append("\n");
        sb.append("Carrera     : ").append(estudiante.getCarrera()).append("\n");
        sb.append("Anio ingreso: ").append(estudiante.getAnioIngreso()).append("\n");
        sb.append(String.format("Promedio general: %.2f\n", estudiante.getPromedioGeneral()));
        sb.append("Materias inscriptas: ").append(estudiante.getMaterias().size()).append("\n");
        sb.append("─────────────────────────────────────\n");

        if (estudiante.getMaterias().isEmpty()) {
            sb.append("Sin materias inscriptas.");
        } else {
            for (InscripcionMateria ins : estudiante.getMaterias())
                sb.append(ins.toString()).append("\n");
        }

        vista.mostrarInfo("Situacion General", sb.toString());
        vista.mostrarPanelReportes();
    }

    private void mostrarMateriasEnRiesgo() {
        ArrayList<InscripcionMateria> criticas = estudiante.getMateriasCriticas();
        StringBuilder sb = new StringBuilder();

        if (criticas.isEmpty()) {
            sb.append("No hay materias en riesgo academico.");
        } else {
            sb.append("Materias con promedio menor a 4:\n");
            sb.append("─────────────────────────────────────\n");
            for (InscripcionMateria ins : criticas)
                sb.append(ins.toString()).append("\n");
        }

        vista.mostrarInfo("Materias en Riesgo", sb.toString());
        vista.mostrarPanelReportes();
    }

    private void mostrarAprobadas() {
        List<InscripcionMateria> aprobadas = new ArrayList<>();
        for (InscripcionMateria ins : estudiante.getMaterias())
            if (ins.estaAprobada()) aprobadas.add(ins);

        StringBuilder sb = new StringBuilder();
        if (aprobadas.isEmpty()) {
            sb.append("No hay materias aprobadas aun.");
        } else {
            sb.append("Materias aprobadas:\n");
            sb.append("─────────────────────────────────────\n");
            for (InscripcionMateria ins : aprobadas)
                sb.append(ins.toString()).append("\n");
        }

        vista.mostrarInfo("Materias Aprobadas", sb.toString());
        vista.mostrarPanelReportes();
    }

    // ────────────────────────────────────────────────────────────
    //  ACTUALIZAR VISTA
    // ────────────────────────────────────────────────────────────

    private void actualizarVista() {
        vista.setPerfil(
                estudiante.getNombre(),
                estudiante.getCarrera(),
                String.valueOf(estudiante.getAnioIngreso())
        );

        List<String[]> filas   = new ArrayList<>();
        List<String>   alertas = new ArrayList<>();

        for (InscripcionMateria ins : estudiante.getMaterias()) {
            Materia m     = ins.getMateria();
            double  asist = ins.getPorcentajeAsistencia();

            filas.add(new String[]{
                    m.getCodigo(),
                    m.getNombre(),
                    String.valueOf(m.getCuatrimestre()),
                    String.valueOf(m.getAnio()),
                    String.format("%.1f", asist),
                    String.format("%.2f", ins.getPromedio()),
                    ins.getCondicion()
            });

            if (asist >= 75 && asist < 85)
                alertas.add(m.getNombre() + " - " + String.format("%.1f%%", asist));
        }

        vista.actualizarTabla(filas);
        vista.actualizarAlertas(alertas);

        actualizarMetricas();
    }

    // ────────────────────────────────────────────────────────────
    //  ACTUALIZAR METRICAS (fila seleccionada en la tabla)
    // ────────────────────────────────────────────────────────────

    private void actualizarMetricas() {
        String codigoSel = vista.getSelectedCodigo();
        if (codigoSel == null) return;
        InscripcionMateria ins = estudiante.getInscripcion(codigoSel);
        if (ins == null) return;
        int presentes = ins.getClasesAsistidas();
        int ausentes  = ins.getTotalClases() - ins.getClasesAsistidas();
        vista.setClasesTotales(ins.getTotalClases());
        vista.setAsistenciaStats(presentes, ausentes, ins.getPorcentajeAsistencia());
        String historial = ins.getNotas().isEmpty() ? "—" : ins.getNotas().toString();
        vista.setNotasHistorial(historial);
        vista.setPromedio(ins.getPromedio());
    }
}