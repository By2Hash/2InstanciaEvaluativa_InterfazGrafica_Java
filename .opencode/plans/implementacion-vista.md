# Plan de Implementacion — VentanaPrincipal.java

## Archivo a reemplazar
`2Intancia_Evaluativa_InterfazGrafica_Java\src\vista\VentanaPrincipal.java`

## Codigo completo

```java
package vista;

import controlador.Controlador;
import modelo.InscripcionMateria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private Controlador controlador;

    private static final Color FONDO_APP        = new Color(192, 192, 192);
    private static final Color FONDO_PANEL      = new Color(211, 211, 211);
    private static final Color ACENTO_PRIMARIO  = new Color(0, 0, 200);
    private static final Color ALERTA_AMARILLO  = new Color(255, 255, 100);
    private static final Color FILA_SELECCION   = new Color(100, 100, 255);
    private static final Color ROJO_PELIGRO     = new Color(180, 0, 0);

    private JMenuItem itemCerrar;
    private JMenuItem itemSituacion;
    private JMenuItem itemRiesgo;
    private JMenuItem itemAprobadas;

    private JLabel lblNombre;
    private JLabel lblCarrera;
    private JLabel lblAnioIngreso;

    private JTextField txtNombre;
    private JTextField txtCodigo;
    private JSpinner spnCuatrimestre;
    private JSpinner spnAnio;
    private JButton btnInscribir;

    private String[] columnas = {"Codigo", "Nombre", "Cuatrimestre", "Anio", "Asistencia %", "Promedio", "Condicion"};
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JRadioButton rbPresente;
    private JRadioButton rbAusente;
    private JButton btnRegistrarAsistencia;

    private JTextField txtNota;
    private JButton btnAgregarNota;

    private JButton btnDarDeBaja;

    private DefaultListModel<String> modeloAlertas;
    private JList<String> listaAlertas;

    private CardLayout cardLayout;
    private JPanel panelCards;
    private JPanel panelPrincipal;
    private JPanel panelReportes;

    public VentanaPrincipal(Controlador controlador) {
        super("Sistema de Autogestion Estudiantil");
        this.controlador = controlador;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);

        initComponentes();
        initLayouts();
        initListeners();
    }

    private void initComponentes() {
        // Menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        itemCerrar = new JMenuItem("Cerrar");
        menuArchivo.add(itemCerrar);

        JMenu menuReportes = new JMenu("Reportes");
        itemSituacion = new JMenuItem("Situacion general");
        itemRiesgo = new JMenuItem("Materias en riesgo");
        itemAprobadas = new JMenuItem("Materias aprobadas");
        menuReportes.add(itemSituacion);
        menuReportes.add(itemRiesgo);
        menuReportes.add(itemAprobadas);

        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        setJMenuBar(menuBar);

        // Perfil
        lblNombre = new JLabel("Nombre: —");
        lblCarrera = new JLabel("Carrera: —");
        lblAnioIngreso = new JLabel("Anio ingreso: —");
        Font fuentePerfil = new Font("SansSerif", Font.PLAIN, 12);
        lblNombre.setFont(fuentePerfil);
        lblCarrera.setFont(fuentePerfil);
        lblAnioIngreso.setFont(fuentePerfil);

        // Inscripcion
        txtNombre = new JTextField(15);
        txtCodigo = new JTextField(15);
        txtCodigo.setToolTipText("3 a 10 caracteres, unico");

        SpinnerNumberModel modeloCuatri = new SpinnerNumberModel(1, 1, 2, 1);
        spnCuatrimestre = new JSpinner(modeloCuatri);

        SpinnerNumberModel modeloAnio = new SpinnerNumberModel(2024, 2000, 2030, 1);
        spnAnio = new JSpinner(modeloAnio);
        JSpinner.NumberEditor editorAnio = new JSpinner.NumberEditor(spnAnio, "#");
        spnAnio.setEditor(editorAnio);

        btnInscribir = new JButton("Inscribir");
        btnInscribir.setBackground(ACENTO_PRIMARIO);
        btnInscribir.setForeground(Color.WHITE);
        btnInscribir.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnInscribir.setFocusPainted(false);

        // Tabla
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (sel) {
                    setBackground(FILA_SELECCION);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        // Asistencia
        rbPresente = new JRadioButton("Presente");
        rbAusente = new JRadioButton("Ausente");
        rbPresente.setSelected(true);
        ButtonGroup grupoAsistencia = new ButtonGroup();
        grupoAsistencia.add(rbPresente);
        grupoAsistencia.add(rbAusente);
        btnRegistrarAsistencia = new JButton("Registrar");

        // Notas
        txtNota = new JTextField(5);
        btnAgregarNota = new JButton("Agregar");

        // Baja
        btnDarDeBaja = new JButton("Dar de Baja");
        btnDarDeBaja.setBackground(ROJO_PELIGRO);
        btnDarDeBaja.setForeground(Color.WHITE);
        btnDarDeBaja.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnDarDeBaja.setFocusPainted(false);

        // Alertas
        modeloAlertas = new DefaultListModel<>();
        listaAlertas = new JList<>(modeloAlertas);
        listaAlertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAlertas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean sel, boolean foc) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, sel, foc);
                label.setBackground(ALERTA_AMARILLO);
                label.setOpaque(true);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                return label;
            }
        });

        // CardLayout
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
    }

    private void initLayouts() {
        getContentPane().setBackground(FONDO_APP);
        setLayout(new BorderLayout());

        // Panel izquierdo
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(FONDO_PANEL);
        panelIzquierdo.setPreferredSize(new Dimension(220, 0));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Perfil
        JPanel perfilPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        perfilPanel.setBackground(FONDO_PANEL);
        perfilPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Perfil del Estudiante",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11)
        ));
        perfilPanel.add(lblNombre);
        perfilPanel.add(lblCarrera);
        perfilPanel.add(lblAnioIngreso);

        // Inscripcion
        JPanel inscripcionPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inscripcionPanel.setBackground(FONDO_PANEL);
        inscripcionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Inscribir Materia",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11)
        ));
        inscripcionPanel.add(new JLabel("Nombre:"));
        inscripcionPanel.add(txtNombre);
        inscripcionPanel.add(new JLabel("Codigo:"));
        inscripcionPanel.add(txtCodigo);
        inscripcionPanel.add(new JLabel("Cuatrimestre:"));
        inscripcionPanel.add(spnCuatrimestre);
        inscripcionPanel.add(new JLabel("Anio:"));
        inscripcionPanel.add(spnAnio);

        JPanel btnInscribirPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnInscribirPanel.setBackground(FONDO_PANEL);
        btnInscribirPanel.add(btnInscribir);

        panelIzquierdo.add(perfilPanel);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(inscripcionPanel);
        panelIzquierdo.add(btnInscribirPanel);
        panelIzquierdo.add(Box.createVerticalGlue());

        // Panel central (CardLayout)
        panelPrincipal = new JPanel(new BorderLayout(5, 5));
        panelPrincipal.setBackground(FONDO_APP);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(0, 250));

        // Acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelAcciones.setBackground(FONDO_PANEL);
        panelAcciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Acciones",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11)
        ));

        JPanel subAsistencia = new JPanel(new GridLayout(3, 1, 3, 3));
        subAsistencia.setBackground(FONDO_PANEL);
        subAsistencia.setBorder(BorderFactory.createTitledBorder("Asistencia"));
        subAsistencia.add(rbPresente);
        subAsistencia.add(rbAusente);
        subAsistencia.add(btnRegistrarAsistencia);

        JPanel subNotas = new JPanel(new GridLayout(3, 1, 3, 3));
        subNotas.setBackground(FONDO_PANEL);
        subNotas.setBorder(BorderFactory.createTitledBorder("Notas"));
        subNotas.add(new JLabel("Nota (0-10):"));
        subNotas.add(txtNota);
        subNotas.add(btnAgregarNota);

        JPanel subBaja = new JPanel(new GridLayout(1, 1, 3, 3));
        subBaja.setBackground(FONDO_PANEL);
        subBaja.setBorder(BorderFactory.createTitledBorder("Baja"));
        subBaja.add(btnDarDeBaja);

        panelAcciones.add(subAsistencia);
        panelAcciones.add(subNotas);
        panelAcciones.add(subBaja);

        // Alertas
        JScrollPane scrollAlertas = new JScrollPane(listaAlertas);
        scrollAlertas.setPreferredSize(new Dimension(0, 100));
        scrollAlertas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Alertas de Asistencia (75% - 85%)",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11)
        ));

        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(FONDO_APP);
        panelSur.add(scrollAlertas, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.PAGE_END);

        // Panel reportes (placeholder)
        panelReportes = new JPanel(new BorderLayout());
        panelReportes.setBackground(FONDO_APP);
        JLabel lblReportes = new JLabel("Seleccione un reporte del menu Reportes", SwingConstants.CENTER);
        lblReportes.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelReportes.add(lblReportes, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> mostrarPanel("PRINCIPAL"));
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVolver.setBackground(FONDO_APP);
        panelVolver.add(btnVolver);
        panelReportes.add(panelVolver, BorderLayout.NORTH);

        panelCards.add(panelPrincipal, "PRINCIPAL");
        panelCards.add(panelReportes, "REPORTES");

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelCards, BorderLayout.CENTER);
    }

    private void initListeners() {
        itemCerrar.addActionListener(e -> System.exit(0));

        itemSituacion.addActionListener(e -> {
            controlador.mostrarReporteSituacion();
            mostrarPanel("REPORTES");
        });
        itemRiesgo.addActionListener(e -> {
            controlador.mostrarReporteRiesgo();
            mostrarPanel("REPORTES");
        });
        itemAprobadas.addActionListener(e -> {
            controlador.mostrarReporteAprobadas();
            mostrarPanel("REPORTES");
        });

        btnInscribir.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String codigo = txtCodigo.getText().trim();
            int cuatri = (Integer) spnCuatrimestre.getValue();
            int anio = (Integer) spnAnio.getValue();
            controlador.inscribirMateria(nombre, codigo, cuatri, anio);
        });

        btnDarDeBaja.addActionListener(e -> {
            String codigo = obtenerCodigoSeleccionado();
            if (codigo == null) {
                mostrarError("Seleccione una materia de la tabla primero.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Desea eliminar la materia " + codigo + "?\nEsta accion no se puede deshacer.",
                    "Confirmar Baja",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controlador.confirmarBaja(codigo);
            }
        });

        btnRegistrarAsistencia.addActionListener(e -> {
            String codigo = obtenerCodigoSeleccionado();
            if (codigo == null) {
                mostrarError("Seleccione una materia de la tabla primero.");
                return;
            }
            controlador.registrarAsistencia(codigo, rbPresente.isSelected());
        });

        btnAgregarNota.addActionListener(e -> {
            String codigo = obtenerCodigoSeleccionado();
            if (codigo == null) {
                mostrarError("Seleccione una materia de la tabla primero.");
                return;
            }
            double nota;
            try {
                nota = Double.parseDouble(txtNota.getText().trim());
            } catch (NumberFormatException ex) {
                mostrarError("Ingrese un numero valido para la nota.");
                return;
            }
            controlador.registrarNota(codigo, nota);
        });
    }

    // ─── Metodos publicos (llamados por el Controlador) ───

    public void actualizarTabla(List<InscripcionMateria> inscripciones) {
        modeloTabla.setRowCount(0);
        if (inscripciones == null || inscripciones.isEmpty()) {
            modeloTabla.addRow(new Object[]{"—", "Sin materias inscriptas", "—", "—", "—", "—", "—"});
            return;
        }
        for (InscripcionMateria im : inscripciones) {
            modelo.Materia m = im.getMateria();
            modeloTabla.addRow(new Object[]{
                m.getCodigo(), m.getNombre(), m.getCuatrimestre(), m.getAnio(),
                String.format("%.0f%%", im.getPorcentajeAsistencia()),
                String.format("%.1f", im.getPromedio()),
                im.getCondicion()
            });
        }
    }

    public void actualizarAlertas(List<InscripcionMateria> criticas) {
        modeloAlertas.clear();
        if (criticas == null) return;
        for (InscripcionMateria im : criticas) {
            modeloAlertas.addElement(
                im.getMateria().getNombre() + " - " +
                String.format("%.0f%%", im.getPorcentajeAsistencia())
            );
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAlertaAsistencia(String nombreMateria, double porcentaje) {
        JOptionPane.showMessageDialog(
                this,
                "Asistencia critica en " + nombreMateria + ": " +
                String.format("%.0f%%", porcentaje) +
                "\nRiesgo de perder la regularidad.",
                "Alerta de Asistencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public void mostrarReporte(String titulo, String contenido) {
        JOptionPane.showMessageDialog(this, contenido, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarPanel(String nombre) {
        cardLayout.show(panelCards, nombre);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtCodigo.setText("");
        spnCuatrimestre.setValue(1);
        spnAnio.setValue(2024);
    }

    public String obtenerCodigoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return null;
        Object val = modeloTabla.getValueAt(fila, 0);
        if (val == null || val.toString().equals("—")) return null;
        return val.toString();
    }

    public void setPerfil(String nombre, String carrera, int anioIngreso) {
        lblNombre.setText("Nombre: " + nombre);
        lblCarrera.setText("Carrera: " + carrera);
        lblAnioIngreso.setText("Anio ingreso: " + anioIngreso);
    }
}
```

## Resumen de cambios

| Aspecto | Antes | Despues |
|---------|-------|---------|
| Constructor | `VentanaPrincipal()` sin args | `VentanaPrincipal(Controlador)` |
| Main method | Tenia `main()` | Eliminado (sin main) |
| Layout | GroupLayout (NetBeans) | BorderLayout + BoxLayout + GridLayout + FlowLayout + CardLayout |
| Componentes | Ninguno | JMenuBar, JTable, JList, JSpinner, JRadioButton, JButton, JLabel |
| Tabla | No existia | DefaultTableModel no editable, 7 columnas, renderer personalizado |
| Alertas | No existia | DefaultListModel<String> con renderer amarillo |
| Perfil | No existia | 3 JLabels en TitledBorder |
| Inscripcion | No existia | 2 JTextField + 2 JSpinner + btn Inscribir (azul) |
| CardLayout | No existia | panelPrincipal ↔ panelReportes |
| Metodos publicos | Ninguno | 8 metodos para que el Controlador actualice la Vista |
| Dependencia a Controlador | Ninguna | Los ActionListeners delegan en controlador.metodo() |

## Metodos que debe tener el Controlador

Para que la vista funcione, el Controlador necesita estos metodos:
- `inscribirMateria(String, String, int, int)`
- `confirmarBaja(String)`
- `registrarAsistencia(String, boolean)`
- `registrarNota(String, double)`
- `mostrarReporteSituacion()`
- `mostrarReporteRiesgo()`
- `mostrarReporteAprobadas()`
