package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    // ────────────────────────────────────────────────────────────
    //  PALETA DE COLORES
    // ────────────────────────────────────────────────────────────

    private static final Color AZUL_PRIMARIO   = new Color(0, 0, 200);
    private static final Color FONDO_APP       = new Color(210, 210, 210);
    private static final Color FONDO_PANEL     = new Color(235, 235, 235);
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
    //
    //  FIX 1: usamos CardLayout interno en panelCardBaja para que
    //  el area de "Baja" nunca colapse cuando alternamos vistas.
    //  setVisible() sobre BoxLayout hijos colapsa el espacio;
    //  CardLayout mantiene el tamaño maximo de ambas cartas.
    //
    //  FIX 2: guardamos codigoPendienteDeBaja en el momento en que
    //  el usuario presiona "Dar de Baja", antes de que la tabla
    //  pueda perder su seleccion al redibujar el panel.
    // ────────────────────────────────────────────────────────────

    private JButton btnDarDeBaja;

    // Codigo guardado al presionar "Dar de Baja"
    private String codigoPendienteDeBaja = null;

    // ────────────────────────────────────────────────────────────
    //  CARDS PRINCIPALES
    // ────────────────────────────────────────────────────────────

    private CardLayout cardLayout;
    private JPanel panelCards;
    private JPanel panelPrincipal;
    private JPanel panelReportes;

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
        itemSituacion = new JMenuItem("Situacion General");
        itemRiesgo    = new JMenuItem("Materias en Riesgo");
        itemAprobadas = new JMenuItem("Aprobadas");
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

        Font compactFont = new Font("SansSerif", Font.PLAIN, 11);
        txtPerfilNombre      = new JTextField(15); txtPerfilNombre.setFont(compactFont);
        txtPerfilCarrera     = new JTextField(15); txtPerfilCarrera.setFont(compactFont);
        txtPerfilAnioIngreso = new JTextField(15); txtPerfilAnioIngreso.setFont(compactFont);
        txtPerfilNombre.setEditable(false);
        txtPerfilCarrera.setEditable(false);
        txtPerfilAnioIngreso.setEditable(false);

        // ── INSCRIPCION A MATERIAS ────────────────────────

        txtInscNombre = new JTextField(15); txtInscNombre.setFont(compactFont);
        txtInscCodigo = new JTextField(15); txtInscCodigo.setFont(compactFont);
        txtInscCodigo.setToolTipText("Codigo unico de 3 a 10 caracteres");
        comboCuatrimestre = new JComboBox<>(new String[]{"1", "2"});
        txtInscAnio = new JTextField(15); txtInscAnio.setFont(compactFont);

        btnInscribir = new JButton("Inscribir");
        btnInscribir.setBackground(AZUL_PRIMARIO);
        btnInscribir.setForeground(TEXTO_INVERSO);
        btnInscribir.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnInscribir.setFocusPainted(false);

        // ── ALERTAS DE INASISTENCIAS ──────────────────────

        modeloAlertas = new DefaultListModel<>();
        listaAlertas  = new JList<>(modeloAlertas);
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
        tabla.getTableHeader().setDefaultRenderer(new HeaderCustomRenderer());
        tabla.setDefaultRenderer(Object.class, new CellCustomRenderer());

        // ── REGISTRAR ASISTENCIAS ─────────────────────────

        rbPresente = new JRadioButton("Presente", true); rbPresente.setFont(compactFont);
        rbAusente  = new JRadioButton("Ausente");        rbAusente.setFont(compactFont);
        ButtonGroup grupoAsistencia = new ButtonGroup();
        grupoAsistencia.add(rbPresente);
        grupoAsistencia.add(rbAusente);
        btnRegistrarAsistencia = new JButton("Registrar");

        // ── REGISTRAR NOTA ────────────────────────────────

        txtNota = new JTextField(5); txtNota.setFont(compactFont);
        btnNotaAgregada = new JButton("Nota Agregada");

        // ── DATOS Y METRICAS ──────────────────────────────

        Font labelFont = new Font("SansSerif", Font.PLAIN, 11);
        lblClasesTotales        = new JLabel("Clases Totales: 0");      lblClasesTotales.setFont(labelFont);
        lblPresentes            = new JLabel("Presentes: 0");            lblPresentes.setFont(labelFont);
        lblAusentes             = new JLabel("Ausentes: 0");             lblAusentes.setFont(labelFont);
        lblPorcentajeAsistencia = new JLabel("Asistencia: 0 %");        lblPorcentajeAsistencia.setFont(labelFont);
        lblHistorialNotas       = new JLabel("Historial Notas: \u2014"); lblHistorialNotas.setFont(labelFont);
        lblPromedio             = new JLabel("Promedio: \u2014");        lblPromedio.setFont(labelFont);

        // ── DAR DE BAJA ─────────────────

        btnDarDeBaja = new JButton("Dar de Baja");
        btnDarDeBaja.setBackground(ROJO_BAJA);
        btnDarDeBaja.setForeground(TEXTO_INVERSO);
        btnDarDeBaja.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnDarDeBaja.setFocusPainted(false);

        // ── BOTON VOLVER ──────────────────────────────────

        btnVolver = new JButton("Volver al panel principal");

        // ── SISTEMA DE CARDS PRINCIPAL ────────────────────

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

        Font lblFont = new Font("SansSerif", Font.PLAIN, 11);

        // -- PERFIL DEL ESTUDIANTE --
        JPanel perfilPanel = new JPanel(new GridBagLayout());
        perfilPanel.setBackground(FONDO_PANEL);
        perfilPanel.setBorder(titledBorder("Perfil del Estudiante"));
        GridBagConstraints gbcP = hgbc();

        gbcP.gridx = 0; gbcP.gridy = 0; gbcP.weightx = 0;
        perfilPanel.add(label("Nombre:", lblFont), gbcP);
        gbcP.gridx = 1; gbcP.weightx = 1;
        perfilPanel.add(txtPerfilNombre, gbcP);

        gbcP.gridx = 0; gbcP.gridy = 1; gbcP.weightx = 0;
        perfilPanel.add(label("Carrera:", lblFont), gbcP);
        gbcP.gridx = 1; gbcP.weightx = 1;
        perfilPanel.add(txtPerfilCarrera, gbcP);

        gbcP.gridx = 0; gbcP.gridy = 2; gbcP.weightx = 0;
        perfilPanel.add(label("Anio Ingreso:", lblFont), gbcP);
        gbcP.gridx = 1; gbcP.weightx = 1;
        perfilPanel.add(txtPerfilAnioIngreso, gbcP);

        // -- INSCRIPCION A MATERIAS --
        JPanel inscripcionPanel = new JPanel(new GridBagLayout());
        inscripcionPanel.setBackground(FONDO_PANEL);
        inscripcionPanel.setBorder(titledBorder("Inscripcion a materias"));
        GridBagConstraints gbcI = hgbc();

        gbcI.gridx = 0; gbcI.gridy = 0; gbcI.weightx = 0;
        inscripcionPanel.add(label("Nombre:", lblFont), gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(txtInscNombre, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 1; gbcI.weightx = 0;
        inscripcionPanel.add(label("Codigo (unico):", lblFont), gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(txtInscCodigo, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 2; gbcI.weightx = 0;
        inscripcionPanel.add(label("Cuatrimestre:", lblFont), gbcI);
        gbcI.gridx = 1; gbcI.weightx = 1;
        inscripcionPanel.add(comboCuatrimestre, gbcI);

        gbcI.gridx = 0; gbcI.gridy = 3; gbcI.weightx = 0;
        inscripcionPanel.add(label("Anio:", lblFont), gbcI);
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
        scrollAlertas.setBorder(titledBorder("Alertas de Inasistencias (75% - 85%)"));

        panelIzquierdo.add(perfilPanel);
        panelIzquierdo.add(Box.createVerticalStrut(8));
        panelIzquierdo.add(inscripcionPanel);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(scrollAlertas);
        panelIzquierdo.add(Box.createVerticalGlue());

        // ── COLUMNA DERECHA ───────────────────────────────

        panelPrincipal = new JPanel(new BorderLayout(6, 6));
        panelPrincipal.setBackground(FONDO_APP);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(titledBorder("Tabla con las materias"));

        // -- ACCIONES --
        JPanel panelAcciones = new JPanel(new GridLayout(1, 4, 8, 0));
        panelAcciones.setBackground(FONDO_PANEL);
        panelAcciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY), "Acciones",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 11)));

        // Subpanel Asistencias
        JPanel subAsistencia = new JPanel();
        subAsistencia.setLayout(new BoxLayout(subAsistencia, BoxLayout.Y_AXIS));
        subAsistencia.setBackground(FONDO_PANEL);
        subAsistencia.setBorder(BorderFactory.createTitledBorder("Asistencias"));
        subAsistencia.add(rbPresente);
        subAsistencia.add(rbAusente);
        subAsistencia.add(Box.createVerticalStrut(4));
        subAsistencia.add(btnRegistrarAsistencia);

        // Subpanel Nota
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

        // Subpanel Metricas
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

        // Subpanel Baja
        JPanel subBaja = new JPanel();
        subBaja.setLayout(new BoxLayout(subBaja, BoxLayout.Y_AXIS));
        subBaja.setBackground(FONDO_PANEL);
        subBaja.setBorder(BorderFactory.createTitledBorder("Baja"));
        subBaja.add(btnDarDeBaja);

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
        JLabel lblReporte = new JLabel("Seleccione un reporte del menu Reportes", SwingConstants.CENTER);
        lblReporte.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelReportes.add(lblReporte, BorderLayout.CENTER);
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVolver.setBackground(FONDO_APP);
        panelVolver.add(btnVolver);
        panelReportes.add(panelVolver, BorderLayout.NORTH);

        // ── ENSAMBLAR CARDS ───────────────────────────────

        panelCards.add(panelPrincipal, "PRINCIPAL");
        panelCards.add(panelReportes,  "REPORTES");

        this.add(panelIzquierdo, BorderLayout.WEST);
        this.add(panelCards,     BorderLayout.CENTER);
    }

    // ────────────────────────────────────────────────────────────
    //  HELPERS PRIVADOS DE LAYOUT
    // ────────────────────────────────────────────────────────────

    private static javax.swing.border.Border titledBorder(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY), titulo,
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 10));
    }

    private static GridBagConstraints hgbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 4, 3, 4);
        return g;
    }

    private static JLabel label(String text, Font f) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        return l;
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — REGISTRO DE LISTENERS
    // ────────────────────────────────────────────────────────────

    public void registrarSelectionListener(ListSelectionListener sl) {
        tabla.getSelectionModel().addListSelectionListener(sl);
    }

    public void registrarListeners(ActionListener al) {
        itemCerrar.setActionCommand("CERRAR");             itemCerrar.addActionListener(al);
        itemSituacion.setActionCommand("SITUACION_GENERAL"); itemSituacion.addActionListener(al);
        itemRiesgo.setActionCommand("MATERIAS_EN_RIESGO"); itemRiesgo.addActionListener(al);
        itemAprobadas.setActionCommand("APROBADAS");       itemAprobadas.addActionListener(al);
        itemAcercaDe.setActionCommand("ACERCA_DE");        itemAcercaDe.addActionListener(al);

        btnInscribir.setActionCommand("INSCRIBIR");        btnInscribir.addActionListener(al);

        btnRegistrarAsistencia.setActionCommand("REGISTRAR_ASISTENCIA");
        btnRegistrarAsistencia.addActionListener(al);

        btnNotaAgregada.setActionCommand("NOTA_AGREGADA"); btnNotaAgregada.addActionListener(al);

        btnDarDeBaja.addActionListener(e -> {
            codigoPendienteDeBaja = getSelectedCodigo();
            al.actionPerformed(new java.awt.event.ActionEvent(
                    btnDarDeBaja, java.awt.event.ActionEvent.ACTION_PERFORMED, "BAJA"));
        });

        btnVolver.setActionCommand("VOLVER_PRINCIPAL");     btnVolver.addActionListener(al);
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

    public String[] mostrarRegistroEstudiante() {
        frmRegistroEstudiante dlg = new frmRegistroEstudiante();
        dlg.setVisible(true);
        return new String[]{dlg.getNombre(), dlg.getLegajo(), dlg.getCarrera(), dlg.getTxtAnio()};
    }

    public void limpiarFormulario() {
        txtInscNombre.setText("");
        txtInscCodigo.setText("");
        txtInscAnio.setText("");
        comboCuatrimestre.setSelectedIndex(0);
    }

    public int getSelectedRow() { return tabla.getSelectedRow(); }

    public String getSelectedCodigo() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return null;
        Object val = modeloTabla.getValueAt(fila, 0);
        if (val == null || val.toString().equals("\u2014")) return null;
        return val.toString();
    }

    /**
     * FIX 2: el controlador llama este metodo en confirmarBaja()
     * en lugar de getSelectedCodigo(), porque al momento de confirmar
     * la tabla ya puede no tener fila seleccionada.
     */
    public String getCodigoPendienteDeBaja() {
        return codigoPendienteDeBaja;
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

    public void setClasesTotales(int total) { lblClasesTotales.setText("Clases Totales: " + total); }

    public void setAsistenciaStats(int presentes, int ausentes, double porcentaje) {
        lblPresentes.setText("Presentes: " + presentes);
        lblAusentes.setText("Ausentes: " + ausentes);
        lblPorcentajeAsistencia.setText(String.format("Asistencia: %.1f %%", porcentaje));
    }

    public void setNotasHistorial(String historial) { lblHistorialNotas.setText("Historial Notas: " + historial); }
    public void setPromedio(double promedio)         { lblPromedio.setText(String.format("Promedio: %.2f", promedio)); }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — NAVEGACION CARDS
    // ────────────────────────────────────────────────────────────

    public void mostrarPanelPrincipal() { cardLayout.show(panelCards, "PRINCIPAL"); }
    public void mostrarPanelReportes()  { cardLayout.show(panelCards, "REPORTES"); }

    public boolean mostrarConfirmacionBaja() {
        if (codigoPendienteDeBaja == null) {
            mostrarError("Seleccione una materia de la tabla.");
            return false;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "Confirma la baja de la materia " + codigoPendienteDeBaja + "?",
                "Confirmar Baja",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) {
            codigoPendienteDeBaja = null;
            return false;
        }
        return true;
    }

    // ────────────────────────────────────────────────────────────
    //  API PUBLICA — DIALOGOS
    // ────────────────────────────────────────────────────────────

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAlertaAsistencia(String materia, double porcentaje) {
        JOptionPane.showMessageDialog(this,
                "Asistencia critica en " + materia + ": " +
                        String.format("%.1f%%", porcentaje) + "\nRiesgo de perder la regularidad.",
                "Alerta de Asistencia", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarInfo(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public int mostrarConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmacion",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    // ────────────────────────────────────────────────────────────
    //  RENDERERS INTERNOS
    // ────────────────────────────────────────────────────────────

    private static class HeaderCustomRenderer extends DefaultTableCellRenderer {
        public HeaderCustomRenderer() {
            setBackground(new Color(0, 0, 200));
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("SansSerif", Font.BOLD, 11));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 150), 1),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)));
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