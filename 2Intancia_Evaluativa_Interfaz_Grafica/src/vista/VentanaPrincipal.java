package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    // ────────────────────────────────────────────────────────────
    //  PALETA DE COLORES
    // ────────────────────────────────────────────────────────────

    private static final Color AZUL_PRIMARIO   = new Color(0, 0, 200);
    private static final Color FONDO_APP       = new Color(210, 210, 210);
    private static final Color FONDO_PANEL     = new Color(235, 235, 235);
    private static final Color AMARILLO_ALERTA = new Color(255, 255, 190);
    private static final Color ROJO_BAJA       = new Color(180, 0, 0);
    private static final Color TEXTO_INVERSO   = Color.WHITE;

    // ────────────────────────────────────────────────────────────
    //  MENU BAR
    // ────────────────────────────────────────────────────────────

    private JMenuItem itemCerrar;
    private JMenuItem itemSituacion;
    private JMenuItem itemRiesgo;
    private JMenuItem itemAprobadas;
    private JMenuItem itemAcercaDe;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA IZQUIERDA — PERFIL
    // ────────────────────────────────────────────────────────────

    private JTextField txtPerfilNombre;
    private JTextField txtPerfilCarrera;
    private JTextField txtPerfilAnioIngreso;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA IZQUIERDA — INSCRIPCION
    // ────────────────────────────────────────────────────────────

    private JTextField txtInscNombre;
    private JTextField txtInscCodigo;
    private JComboBox<String> comboCuatrimestre;
    private JTextField txtInscAnio;
    private JButton btnInscribir;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA IZQUIERDA — ALERTAS
    // ────────────────────────────────────────────────────────────

    private DefaultListModel<String> modeloAlertas;
    private JList<String> listaAlertas;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA DERECHA — TABLA
    // ────────────────────────────────────────────────────────────

    private static final String[] COLUMNAS = {
            "Codigo", "Nombre", "Cuatrimestre", "Anio",
            "Asistencia %", "Promedio", "Condicion"
    };
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA DERECHA — ACCIONES / ASISTENCIA
    // ────────────────────────────────────────────────────────────

    private JRadioButton rbPresente;
    private JRadioButton rbAusente;
    private JButton btnRegistrarAsistencia;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA DERECHA — ACCIONES / NOTA
    // ────────────────────────────────────────────────────────────

    private JTextField txtNota;
    private JButton btnNotaAgregada;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA DERECHA — ACCIONES / DATOS Y METRICAS
    // ────────────────────────────────────────────────────────────

    private JLabel lblClasesTotales;
    private JLabel lblPresentes;
    private JLabel lblAusentes;
    private JLabel lblPorcentajeAsistencia;
    private JLabel lblHistorialNotas;
    private JLabel lblPromedio;

    // ────────────────────────────────────────────────────────────
    //  COLUMNA DERECHA — ACCIONES / BAJA
    // ────────────────────────────────────────────────────────────

    private JButton btnDarDeBaja;
    private JPanel confirmacionBajaPanel;
    private JButton btnAceptarBaja;
    private JButton btnCancelarBaja;

    // ────────────────────────────────────────────────────────────
    //  CARDS
    // ────────────────────────────────────────────────────────────

    private CardLayout cardLayout;
    private JPanel panelCards;
    private JPanel panelPrincipal;
    private JPanel panelReportes;

    // ────────────────────────────────────────────────────────────
    //  BOTON VOLVER (campo de instancia para poder registrar listener)
    // ────────────────────────────────────────────────────────────

    private JButton btnVolver;

    // ────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ────────────────────────────────────────────────────────────

    public VentanaPrincipal() {
        super("Sistema de Autogestion Estudiantil");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024, 640);
        this.setMinimumSize(new Dimension(1024, 640));
        this.setMaximumSize(new Dimension(1024, 640));
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        initComponentes();
        initLayouts();
    }

    // ────────────────────────────────────────────────────────────
    //  CREACION DE COMPONENTES
    // ────────────────────────────────────────────────────────────

    private void initComponentes() {

        // ── BARRA DE MENU ──────────────────────────────────

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        itemCerrar = new JMenuItem("Cerrar");
        menuArchivo.add(itemCerrar);

        JMenu menuReportes = new JMenu("Reportes");
        itemSituacion   = new JMenuItem("Situacion General");
        itemRiesgo      = new JMenuItem("Materias en Riesgo");
        itemAprobadas   = new JMenuItem("Aprobadas");
        menuReportes.add(itemSituacion);
        menuReportes.add(itemRiesgo);
        menuReportes.add(itemAprobadas);

        JMenu menuAyuda = new JMenu("Ayuda");
        itemAcercaDe = new JMenuItem("Acerca de");
        menuAyuda.add(itemAcercaDe);

        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        menuBar.add(menuAyuda);
        this.setJMenuBar(menuBar);

        // ── PERFIL DEL ESTUDIANTE ─────────────────────────

        txtPerfilNombre      = new JTextField(15);
        txtPerfilCarrera     = new JTextField(15);
        txtPerfilAnioIngreso = new JTextField(15);
        Font compactFont = new Font("SansSerif", Font.PLAIN, 11);
        txtPerfilNombre.setFont(compactFont);
        txtPerfilCarrera.setFont(compactFont);
        txtPerfilAnioIngreso.setFont(compactFont);

        // Solo lectura — no se pueden editar desde la UI
        txtPerfilNombre.setEditable(false);
        txtPerfilCarrera.setEditable(false);
        txtPerfilAnioIngreso.setEditable(false);

        // ── INSCRIPCION A MATERIAS ────────────────────────

        txtInscNombre = new JTextField(15);
        txtInscCodigo = new JTextField(15);
        txtInscCodigo.setToolTipText("Codigo unico de 3 a 10 caracteres");
        txtInscNombre.setFont(compactFont);
        txtInscCodigo.setFont(compactFont);

        comboCuatrimestre = new JComboBox<>(new String[]{"1", "2"});
        txtInscAnio = new JTextField(15);
        txtInscAnio.setFont(compactFont);

        btnInscribir = new JButton("Inscribir");
        btnInscribir.setBackground(AZUL_PRIMARIO);
        btnInscribir.setForeground(TEXTO_INVERSO);
        btnInscribir.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnInscribir.setFocusPainted(false);

        // ── ALERTAS DE INASISTENCIAS ──────────────────────

        modeloAlertas = new DefaultListModel<>();
        listaAlertas = new JList<>(modeloAlertas);
        listaAlertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAlertas.setCellRenderer(new AlertaListCellRenderer());
        listaAlertas.setFixedCellHeight(18);

        // ── TABLA CON LAS MATERIAS ────────────────────────

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int col) { return String.class; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(20);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 11));
        tabla.setRowMargin(2);
        tabla.setIntercellSpacing(new Dimension(5, 2));

        JTableHeader header = tabla.getTableHeader();
        header.setDefaultRenderer(new HeaderCustomRenderer());
        tabla.setDefaultRenderer(Object.class, new CellCustomRenderer());

        // ── REGISTRAR ASISTENCIAS ─────────────────────────

        rbPresente = new JRadioButton("Presente", true);
        rbAusente  = new JRadioButton("Ausente");
        rbPresente.setFont(compactFont);
        rbAusente.setFont(compactFont);
        ButtonGroup grupoAsistencia = new ButtonGroup();
        grupoAsistencia.add(rbPresente);
        grupoAsistencia.add(rbAusente);
        btnRegistrarAsistencia = new JButton("Registrar");

        // ── REGISTRAR NOTA ────────────────────────────────

        txtNota = new JTextField(5);
        txtNota.setFont(compactFont);
        btnNotaAgregada = new JButton("Nota Agregada");

        // ── DATOS Y METRICAS ──────────────────────────────

        lblClasesTotales        = new JLabel("Clases Totales: 0");
        lblPresentes            = new JLabel("Presentes: 0");
        lblAusentes             = new JLabel("Ausentes: 0");
        lblPorcentajeAsistencia = new JLabel("Asistencia: 0 %");
        lblHistorialNotas       = new JLabel("Historial Notas: \u2014");
        lblPromedio             = new JLabel("Promedio: \u2014");

        Font labelFont = new Font("SansSerif", Font.PLAIN, 11);
        lblClasesTotales.setFont(labelFont);
        lblPresentes.setFont(labelFont);
        lblAusentes.setFont(labelFont);
        lblPorcentajeAsistencia.setFont(labelFont);
        lblHistorialNotas.setFont(labelFont);
        lblPromedio.setFont(labelFont);

        // ── DAR DE BAJA ───────────────────────────────────

        btnDarDeBaja = new JButton("Dar de Baja");
        btnDarDeBaja.setBackground(ROJO_BAJA);
        btnDarDeBaja.setForeground(TEXTO_INVERSO);
        btnDarDeBaja.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnDarDeBaja.setFocusPainted(false);

        btnAceptarBaja  = new JButton("Aceptar");
        btnCancelarBaja = new JButton("Cancelar");

        confirmacionBajaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        confirmacionBajaPanel.setBackground(FONDO_PANEL);
        confirmacionBajaPanel.add(new JLabel("Confirma la baja?"));
        confirmacionBajaPanel.add(btnAceptarBaja);
        confirmacionBajaPanel.add(btnCancelarBaja);
        confirmacionBajaPanel.setVisible(false);

        // ── BOTON VOLVER ──────────────────────────────────

        btnVolver = new JButton("Volver al panel principal");

        // ── SISTEMA DE CARDS ──────────────────────────────

        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
    }

    // ────────────────────────────────────────────────────────────
    //  LAYOUT
    // ────────────────────────────────────────────────────────────

    private void initLayouts() {

        this.getContentPane().setBackground(FONDO_APP);

        // ── COLUMNA IZQUIERDA (320px) ─────────────────────

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(FONDO_PANEL);
        panelIzquierdo.setPreferredSize(new Dimension(320, 640));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // -- PERFIL DEL ESTUDIANTE --
        JPanel perfilPanel = new JPanel(new GridBagLayout());
        perfilPanel.setBackground(FONDO_PANEL);
        perfilPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Perfil del Estudiante",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 10)
        ));

        GridBagConstraints gbcP = new GridBagConstraints();
        gbcP.fill = GridBagConstraints.HORIZONTAL;
        gbcP.insets = new Insets(3, 4, 3, 4);
        Font lblFont = new Font("SansSerif", Font.PLAIN, 11);

        gbcP.gridx = 0; gbcP.gridy = 0; gbcP.weightx = 0;
        JLabel l1 = new JLabel("Nombre:"); l1.setFont(lblFont);
        perfilPanel.add(l1, gbcP);
        gbcP.gridx = 1; gbcP.weightx = 1;
        perfilPanel.add(txtPerfilNombre, gbcP);

        gbcP.gridx = 0; gbcP.gridy = 1; gbcP.weightx = 0;
        JLabel l2 = new JLabel("Carrera:"); l2.setFont(lblFont);
        perfilPanel.add(l2, gbcP);
        gbcP.gridx = 1; gbcP.weightx = 1;
        perfilPanel.add(txtPerfilCarrera, gbcP);

        gbcP.gridx = 0; gbcP.gridy = 2; gbcP.weightx = 0;
        JLabel l3 = new JLabel("Anio Ingreso:"); l3.setFont(lblFont);
        perfilPanel.add(l3, gbcP);
        gbcP.gridx = 1; gbcP.weightx = 1;
        perfilPanel.add(txtPerfilAnioIngreso, gbcP);

        // -- INSCRIPCION A MATERIAS --
        JPanel inscripcionPanel = new JPanel(new GridBagLayout());
        inscripcionPanel.setBackground(FONDO_PANEL);
        inscripcionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Inscripcion a materias",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 10)
        ));

        GridBagConstraints gbcI = new GridBagConstraints();
        gbcI.fill = GridBagConstraints.HORIZONTAL;
        gbcI.insets = new Insets(3, 4, 3, 4);

        gbcI.gridx = 0; gbcI.gridy = 0; gbcI.weightx = 0;
        JLabel i1 = new JLabel("Nombre:"); i1.setFont(lblFont);
        inscripcionPanel.add(i1, gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(txtInscNombre, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 1; gbcI.weightx = 0;
        JLabel i2 = new JLabel("Codigo (unico):"); i2.setFont(lblFont);
        inscripcionPanel.add(i2, gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(txtInscCodigo, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 2; gbcI.weightx = 0;
        JLabel i3 = new JLabel("Cuatrimestre:"); i3.setFont(lblFont);
        inscripcionPanel.add(i3, gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(comboCuatrimestre, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 3; gbcI.weightx = 0;
        JLabel i4 = new JLabel("Anio:"); i4.setFont(lblFont);
        inscripcionPanel.add(i4, gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(txtInscAnio, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 4; gbcI.gridwidth = 2;
        gbcI.weightx = 1; gbcI.insets = new Insets(5, 4, 2, 4);
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnWrap.setBackground(FONDO_PANEL);
        btnWrap.add(btnInscribir);
        inscripcionPanel.add(btnWrap, gbcI);

        // -- ALERTAS DE INASISTENCIAS --
        JScrollPane scrollAlertas = new JScrollPane(listaAlertas);
        scrollAlertas.setPreferredSize(new Dimension(0, 110));
        scrollAlertas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Alertas de Inasistencias (75% - 85%)",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 10)
        ));

        panelIzquierdo.add(perfilPanel);
        panelIzquierdo.add(Box.createVerticalStrut(8));
        panelIzquierdo.add(inscripcionPanel);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(scrollAlertas);
        panelIzquierdo.add(Box.createVerticalGlue());

        // ── COLUMNA DERECHA (704px) ───────────────────────

        panelPrincipal = new JPanel(new BorderLayout(6, 6));
        panelPrincipal.setBackground(FONDO_APP);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // -- TABLA CON LAS MATERIAS --
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Tabla con las materias",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 10)
        ));

        // -- ACCIONES (CONTENEDOR INFERIOR) --
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        panelAcciones.setBackground(FONDO_PANEL);
        panelAcciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Acciones",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11)
        ));

        // Subpanel Registrar Asistencias
        JPanel subAsistencia = new JPanel();
        subAsistencia.setLayout(new BoxLayout(subAsistencia, BoxLayout.Y_AXIS));
        subAsistencia.setBackground(FONDO_PANEL);
        subAsistencia.setBorder(BorderFactory.createTitledBorder("Asistencias"));
        subAsistencia.add(rbPresente);
        subAsistencia.add(rbAusente);
        subAsistencia.add(Box.createVerticalStrut(4));
        subAsistencia.add(btnRegistrarAsistencia);

        // Subpanel Registrar Nota
        JPanel subNota = new JPanel();
        subNota.setLayout(new BoxLayout(subNota, BoxLayout.Y_AXIS));
        subNota.setBackground(FONDO_PANEL);
        subNota.setBorder(BorderFactory.createTitledBorder("Nota"));
        JPanel notaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        notaRow.setBackground(FONDO_PANEL);
        notaRow.add(new JLabel("Nota:"));
        notaRow.add(txtNota);
        subNota.add(notaRow);
        subNota.add(Box.createVerticalStrut(4));
        subNota.add(btnNotaAgregada);

        // Subpanel Datos y Metricas
        JPanel subDatos = new JPanel();
        subDatos.setLayout(new BoxLayout(subDatos, BoxLayout.Y_AXIS));
        subDatos.setBackground(FONDO_PANEL);
        subDatos.setBorder(BorderFactory.createTitledBorder("Metricas"));
        subDatos.add(lblClasesTotales);
        subDatos.add(lblPresentes);
        subDatos.add(lblAusentes);
        subDatos.add(lblPorcentajeAsistencia);
        subDatos.add(lblHistorialNotas);
        subDatos.add(lblPromedio);

        // Subpanel Dar de Baja
        JPanel subBaja = new JPanel();
        subBaja.setLayout(new BoxLayout(subBaja, BoxLayout.Y_AXIS));
        subBaja.setBackground(FONDO_PANEL);
        subBaja.setBorder(BorderFactory.createTitledBorder("Baja"));
        subBaja.add(btnDarDeBaja);
        subBaja.add(Box.createVerticalStrut(6));
        subBaja.add(confirmacionBajaPanel);

        panelAcciones.add(subAsistencia);
        panelAcciones.add(subNota);
        panelAcciones.add(subDatos);
        panelAcciones.add(subBaja);

        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);

        // ── PANEL DE REPORTES ──────────────────────────────

        panelReportes = new JPanel(new BorderLayout());
        panelReportes.setBackground(FONDO_APP);
        panelReportes.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblReporte = new JLabel(
                "Seleccione un reporte del menu Reportes",
                SwingConstants.CENTER);
        lblReporte.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelReportes.add(lblReporte, BorderLayout.CENTER);

        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVolver.setBackground(FONDO_APP);
        panelVolver.add(btnVolver);
        panelReportes.add(panelVolver, BorderLayout.NORTH);

        // ── ENSAMBLAR CARDS ───────────────────────────────

        panelCards.add(panelPrincipal, "PRINCIPAL");
        panelCards.add(panelReportes, "REPORTES");

        this.add(panelIzquierdo, BorderLayout.WEST);
        this.add(panelCards, BorderLayout.CENTER);
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — REGISTRO DE LISTENERS
    // ────────────────────────────────────────────────────────────

    public void registrarListeners(ActionListener al) {
        itemCerrar.setActionCommand("CERRAR");
        itemCerrar.addActionListener(al);

        itemSituacion.setActionCommand("SITUACION_GENERAL");
        itemSituacion.addActionListener(al);

        itemRiesgo.setActionCommand("MATERIAS_EN_RIESGO");
        itemRiesgo.addActionListener(al);

        itemAprobadas.setActionCommand("APROBADAS");
        itemAprobadas.addActionListener(al);

        itemAcercaDe.setActionCommand("ACERCA_DE");
        itemAcercaDe.addActionListener(al);

        btnInscribir.setActionCommand("INSCRIBIR");
        btnInscribir.addActionListener(al);

        btnRegistrarAsistencia.setActionCommand("REGISTRAR_ASISTENCIA");
        btnRegistrarAsistencia.addActionListener(al);

        rbPresente.setActionCommand("PRESENTE");
        rbPresente.addActionListener(al);

        rbAusente.setActionCommand("AUSENTE");
        rbAusente.addActionListener(al);

        btnNotaAgregada.setActionCommand("NOTA_AGREGADA");
        btnNotaAgregada.addActionListener(al);

        btnDarDeBaja.setActionCommand("BAJA");
        btnDarDeBaja.addActionListener(al);

        btnAceptarBaja.setActionCommand("CONFIRMAR_BAJA");
        btnAceptarBaja.addActionListener(al);

        btnCancelarBaja.setActionCommand("CANCELAR_BAJA");
        btnCancelarBaja.addActionListener(al);

        btnVolver.setActionCommand("VOLVER_PRINCIPAL");
        btnVolver.addActionListener(al);
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — GETTERS
    // ────────────────────────────────────────────────────────────

    public String getTxtInscNombre()  { return txtInscNombre.getText().trim(); }
    public String getTxtInscCodigo()  { return txtInscCodigo.getText().trim(); }
    public String getTxtInscAnio()    { return txtInscAnio.getText().trim(); }
    public String getTxtNota()        { return txtNota.getText().trim(); }
    public boolean isPresente()       { return rbPresente.isSelected(); }

    public int getComboCuatrimestre() {
        return Integer.parseInt((String) comboCuatrimestre.getSelectedItem());
    }

    public int getSelectedRow() { return tabla.getSelectedRow(); }

    public String getSelectedCodigo() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return null;
        Object val = modeloTabla.getValueAt(fila, 0);
        if (val == null || val.toString().equals("\u2014")) return null;
        return val.toString();
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — SETTERS
    // ────────────────────────────────────────────────────────────

    public void setPerfil(String nombre, String carrera, String anio) {
        txtPerfilNombre.setText(nombre);
        txtPerfilCarrera.setText(carrera);
        txtPerfilAnioIngreso.setText(anio);
    }

    public void actualizarTabla(List<String[]> filas) {
        modeloTabla.setRowCount(0);
        if (filas == null || filas.isEmpty()) {
            modeloTabla.addRow(new Object[]{
                    "\u2014", "Sin materias inscriptas", "\u2014", "\u2014", "\u2014", "\u2014", "\u2014"
            });
            return;
        }
        for (String[] fila : filas) modeloTabla.addRow(fila);
    }

    public void actualizarAlertas(List<String> items) {
        modeloAlertas.clear();
        if (items == null) return;
        for (String item : items) modeloAlertas.addElement(item);
    }

    public void setClasesTotales(int total) {
        lblClasesTotales.setText("Clases Totales: " + total);
    }

    public void setAsistenciaStats(int presentes, int ausentes, double porcentaje) {
        lblPresentes.setText("Presentes: " + presentes);
        lblAusentes.setText("Ausentes: " + ausentes);
        lblPorcentajeAsistencia.setText(String.format("Asistencia: %.1f %%", porcentaje));
    }

    public void setNotasHistorial(String historial) {
        lblHistorialNotas.setText("Historial Notas: " + historial);
    }

    public void setPromedio(double promedio) {
        lblPromedio.setText(String.format("Promedio: %.2f", promedio));
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — NAVEGACION CARDS
    // ────────────────────────────────────────────────────────────

    public void mostrarPanelPrincipal() { cardLayout.show(panelCards, "PRINCIPAL"); }
    public void mostrarPanelReportes()  { cardLayout.show(panelCards, "REPORTES"); }

    public void mostrarConfirmacionBaja(boolean mostrar) {
        btnDarDeBaja.setVisible(!mostrar);
        confirmacionBajaPanel.setVisible(mostrar);
        confirmacionBajaPanel.getParent().revalidate();
        confirmacionBajaPanel.getParent().repaint();
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — DIALOGOS
    // ────────────────────────────────────────────────────────────

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAlertaAsistencia(String materia, double porcentaje) {
        JOptionPane.showMessageDialog(
                this,
                "Asistencia critica en " + materia + ": " +
                        String.format("%.1f%%", porcentaje) +
                        "\nRiesgo de perder la regularidad.",
                "Alerta de Asistencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public void mostrarInfo(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public int mostrarConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(
                this, mensaje, "Confirmacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ────────────────────────────────────────────────────────────
    //  RENDERERS ESTATICOS INTERNOS
    // ────────────────────────────────────────────────────────────

    private static class HeaderCustomRenderer extends DefaultTableCellRenderer {
        public HeaderCustomRenderer() {
            setBackground(new Color(0, 0, 200));
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.BOLD, 11));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 150), 1),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
            ));
        }
    }

    private static class CellCustomRenderer extends DefaultTableCellRenderer {
        public CellCustomRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.PLAIN, 11));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                setBackground(new Color(100, 100, 255));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    private static class AlertaListCellRenderer extends DefaultListCellRenderer {
        public AlertaListCellRenderer() {
            setOpaque(true);
            setBackground(new Color(255, 255, 190));
            setFont(new Font("SansSerif", Font.BOLD, 11));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            label.setBackground(new Color(255, 255, 190));
            label.setForeground(Color.BLACK);
            label.setFont(new Font("SansSerif", Font.BOLD, 11));
            return label;
        }
    }
}