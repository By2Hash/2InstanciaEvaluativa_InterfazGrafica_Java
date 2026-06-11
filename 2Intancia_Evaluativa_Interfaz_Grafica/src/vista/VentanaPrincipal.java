
package vista;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class VentanaPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }

    public VentanaPrincipal() {
        initComponents();
        configurarVentanaEstandar();
    }

    private void configurarVentanaEstandar() {
        setExtendedState(javax.swing.JFrame.NORMAL);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // ================================================================
        // COLOR PALETTE
        // ================================================================
        java.awt.Color bgColor        = new java.awt.Color(0xF5, 0xF6, 0xF8);
        java.awt.Color textColor      = new java.awt.Color(0x2C, 0x3E, 0x50);
        java.awt.Color accentColor    = new java.awt.Color(0x34, 0x98, 0xDB);
        java.awt.Color accentHover    = new java.awt.Color(0x29, 0x80, 0xB9);
        java.awt.Color cardBg         = java.awt.Color.WHITE;
        java.awt.Color borderColor    = new java.awt.Color(0xDD, 0xDF, 0xE2);
        java.awt.Color tableAltColor  = new java.awt.Color(0xF0, 0xF2, 0xF5);

        java.awt.Font titleFont      = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        java.awt.Font labelFont      = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12);
        java.awt.Font fieldFont      = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12);
        java.awt.Font buttonFont     = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12);
        java.awt.Font tableHeaderFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12);

        // ================================================================
        // MENU BAR
        // ================================================================
        jMenuBar1 = new javax.swing.JMenuBar();
        MenuArchivo = new javax.swing.JMenu("Archivo");
        MenuReportes = new javax.swing.JMenu("Reportes");
        MenuAyuda = new javax.swing.JMenu("Ayuda");
        jMenuBar1.add(MenuArchivo);
        jMenuBar1.add(MenuReportes);
        jMenuBar1.add(MenuAyuda);
        setJMenuBar(jMenuBar1);

        // ================================================================
        // CREATE ALL LABELS
        // ================================================================
        jLabel1  = new javax.swing.JLabel("");
        jLabel2  = new javax.swing.JLabel("Nombre");
        jLabel3  = new javax.swing.JLabel("Carrera");
        jLabel4  = new javax.swing.JLabel("A\u00f1o de ingreso");
        jLabel5  = new javax.swing.JLabel("");
        jLabel6  = new javax.swing.JLabel("Nombre");
        jLabel7  = new javax.swing.JLabel("C\u00f3digo");
        jLabel8  = new javax.swing.JLabel("Cuatrimestre");
        jLabel9  = new javax.swing.JLabel("A\u00f1o");
        jLabel10 = new javax.swing.JLabel("(75% - 85%)");
        jLabel11 = new javax.swing.JLabel("");
        jLabel12 = new javax.swing.JLabel("DAR DE BAJA");
        jLabel13 = new javax.swing.JLabel("Clases Totales: ");
        jLabel14 = new javax.swing.JLabel("Presente:");
        jLabel15 = new javax.swing.JLabel("Asistencia: ");
        jLabel16 = new javax.swing.JLabel("| Ausente: ");
        jLabel17 = new javax.swing.JLabel("REGISTRAR ASISTENCIA");
        jLabel18 = new javax.swing.JLabel("Nota (0 - 10, m\u00e1x. 5)");
        jLabel19 = new javax.swing.JLabel("Promedio: ");
        jLabel20 = new javax.swing.JLabel("Notas: ");
        jLabel21 = new javax.swing.JLabel("REGISTRAR NOTA");
        jLabel22 = new javax.swing.JLabel("Elimina la materia seleccionada de la tabla.");

        javax.swing.JLabel[] allLabels = {
            jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8,
            jLabel9, jLabel10, jLabel11, jLabel12, jLabel13, jLabel14, jLabel15, jLabel16,
            jLabel17, jLabel18, jLabel19, jLabel20, jLabel21, jLabel22
        };
        for (javax.swing.JLabel lbl : allLabels) {
            lbl.setFont(lbl == jLabel1 || lbl == jLabel5 || lbl == jLabel10 || lbl == jLabel11
                        ? labelFont : labelFont);
            lbl.setForeground(textColor);
        }

        // Title labels inside sub-cards get title font
        jLabel12.setFont(titleFont);
        jLabel17.setFont(titleFont);
        jLabel21.setFont(titleFont);

        // ================================================================
        // CREATE TEXT FIELDS
        // ================================================================
        txtNombreEstudiante  = new javax.swing.JTextField(20);
        txtCarrera           = new javax.swing.JTextField(20);
        txtAño               = new javax.swing.JTextField(10);
        txtNombreMateria     = new javax.swing.JTextField(20);
        txtCodigo            = new javax.swing.JTextField(8);
        txtNota              = new javax.swing.JTextField(5);

        javax.swing.JTextField[] allFields = {
            txtNombreEstudiante, txtCarrera, txtAño,
            txtNombreMateria, txtCodigo, txtNota
        };
        for (javax.swing.JTextField f : allFields) {
            f.setFont(fieldFont);
            f.setPreferredSize(new java.awt.Dimension(f.getPreferredSize().width, 28));
        }

        txtNombreEstudiante.addActionListener(this::txtNombreEstudianteActionPerformed);
        txtCarrera.addActionListener(this::txtCarreraActionPerformed);
        txtNombreMateria.addActionListener(this::txtNombreMateriaActionPerformed);
        txtCodigo.addActionListener(this::txtCodigoActionPerformed);

        // ================================================================
        // CREATE SPINNERS
        // ================================================================
        NupCuatrimestre = new javax.swing.JSpinner();
        NupCuatrimestre.setPreferredSize(new java.awt.Dimension(60, 28));
        NupAño = new javax.swing.JSpinner();
        NupAño.setPreferredSize(new java.awt.Dimension(70, 28));

        // ================================================================
        // CREATE BUTTONS (Swing)
        // ================================================================
        btnInscribir = new javax.swing.JButton("Inscribir");
        btnAgregarNota = new javax.swing.JButton("Agregar");
        btnBajaMateria = new javax.swing.JButton("Dar de Baja");

        javax.swing.JButton[] allButtons = { btnInscribir, btnAgregarNota, btnBajaMateria };
        for (javax.swing.JButton b : allButtons) {
            b.setFont(buttonFont);
            b.setBackground(accentColor);
            b.setForeground(java.awt.Color.WHITE);
            b.setFocusPainted(false);
            b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            b.setPreferredSize(new java.awt.Dimension(140, 30));
        }

        btnAgregarNota.addActionListener(this::btnAgregarNotaActionPerformed);
        btnBajaMateria.addActionListener(this::btnBajaMateriaActionPerformed);

        // ================================================================
        // CREATE RADIO BUTTONS
        // ================================================================
        checkbtnPresente = new javax.swing.JRadioButton("Presente");
        checkbtnAusente = new javax.swing.JRadioButton("Ausente");
        checkbtnPresente.setFont(labelFont);
        checkbtnAusente.setFont(labelFont);
        checkbtnPresente.setForeground(textColor);
        checkbtnAusente.setForeground(textColor);
        checkbtnPresente.addActionListener(this::checkbtnPresenteActionPerformed);

        // ================================================================
        // CREATE RESULT LABELS
        // ================================================================
        lblTotalClases = new javax.swing.JLabel("0");
        lblPresente    = new javax.swing.JLabel("0");
        lblAusente     = new javax.swing.JLabel("0");
        lblAsistencia  = new javax.swing.JLabel("0%");
        lblNotas       = new javax.swing.JLabel("-");
        lblPromedio    = new javax.swing.JLabel("-");

        javax.swing.JLabel[] resultLabels = {
            lblTotalClases, lblPresente, lblAusente, lblAsistencia, lblNotas, lblPromedio
        };
        for (javax.swing.JLabel l : resultLabels) {
            l.setFont(fieldFont);
            l.setForeground(textColor);
        }

        // ================================================================
        // CREATE TABLE
        // ================================================================
        TablaMaterias = new javax.swing.JTable();
        TablaMaterias.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String[] {
                "Codigo", "Nombre", "Cuatrimestre", "A\u00f1o", "Asistencia %", "Promedio", "Condicion"
            }
        ));
        TablaMaterias.setRowHeight(25);
        TablaMaterias.setShowVerticalLines(false);
        TablaMaterias.setShowHorizontalLines(true);
        TablaMaterias.setGridColor(new java.awt.Color(0xE8, 0xE8, 0xE8));
        TablaMaterias.setSelectionBackground(accentColor);
        TablaMaterias.setSelectionForeground(java.awt.Color.WHITE);
        TablaMaterias.setIntercellSpacing(new java.awt.Dimension(8, 4));
        TablaMaterias.setFillsViewportHeight(true);
        TablaMaterias.setFont(fieldFont);
        TablaMaterias.getTableHeader().setFont(tableHeaderFont);
        TablaMaterias.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(0xF0, 0xF2, 0xF5));
                }
                return c;
            }
        });

        jScrollPane2 = new javax.swing.JScrollPane(TablaMaterias);
        jScrollPane2.setBorder(null);
        jScrollPane2.getViewport().setBackground(java.awt.Color.WHITE);

        // ================================================================
        // CREATE ALERT LIST (Swing JList inside JScrollPane)
        // ================================================================
        list1 = new javax.swing.JList<String>();
        list1.setFont(fieldFont);
        list1.setVisibleRowCount(5);
        javax.swing.JScrollPane listScroll = new javax.swing.JScrollPane(list1);
        listScroll.setBorder(null);

        // ================================================================
        // BUILD CARD: PERFIL DEL ESTUDIANTE (jPanel1)
        // ================================================================
        jPanel1 = new javax.swing.JPanel(new GridBagLayout());
        jPanel1.setBackground(cardBg);
        jPanel1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Perfil del Estudiante",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                titleFont,
                textColor
            ),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        jPanel1.add(jLabel2, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel1.add(txtNombreEstudiante, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        jPanel1.add(jLabel3, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel1.add(txtCarrera, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        jPanel1.add(jLabel4, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel1.add(txtAño, gbc);

        // ================================================================
        // BUILD CARD: INSCRIPCION A MATERIA (jPanel2)
        // ================================================================
        jPanel2 = new javax.swing.JPanel(new GridBagLayout());
        jPanel2.setBackground(cardBg);
        jPanel2.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Inscripcion a Materia",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                titleFont,
                textColor
            ),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        ));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        jPanel2.add(jLabel6, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 2;
        jPanel2.add(txtNombreMateria, gbc);

        // Row 1: Codigo
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        jPanel2.add(jLabel7, gbc);
        gbc.gridx = 1; gbc.weightx = 0; gbc.gridwidth = 2;
        jPanel2.add(txtCodigo, gbc);

        // Row 2: Cuatrimestre | Año labels
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        jPanel2.add(jLabel8, gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        jPanel2.add(new javax.swing.JLabel(" "), gbc);
        gbc.gridx = 2;
        jPanel2.add(jLabel9, gbc);

        // Row 3: Cuatrimestre | Año spinners
        gbc.gridy = 3; gbc.gridx = 0; gbc.fill = GridBagConstraints.NONE;
        jPanel2.add(NupCuatrimestre, gbc);
        gbc.gridx = 1;
        jPanel2.add(new javax.swing.JLabel(" "), gbc);
        gbc.gridx = 2;
        jPanel2.add(NupAño, gbc);

        // Row 4: Inscribir button (centered, full width)
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        javax.swing.JPanel btnWrap = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        btnWrap.setOpaque(false);
        btnWrap.add(btnInscribir);
        jPanel2.add(btnWrap, gbc);

        // ================================================================
        // BUILD CARD: ALERTAS DE INASISTENCIAS (jPanel3)
        // ================================================================
        jPanel3 = new javax.swing.JPanel(new BorderLayout());
        jPanel3.setBackground(cardBg);
        jPanel3.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Alertas de Inasistencias (75% - 85%)",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                titleFont,
                textColor
            ),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        ));
        jPanel3.add(listScroll, BorderLayout.CENTER);

        // ================================================================
        // BUILD CARD: MATERIAS INSCRIPTAS (jPanel4)
        // ================================================================
        jPanel4 = new javax.swing.JPanel(new BorderLayout());
        jPanel4.setBackground(cardBg);
        jPanel4.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Materias Inscriptas",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                titleFont,
                textColor
            ),
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        ));
        jPanel4.add(jScrollPane2, BorderLayout.CENTER);

        // ================================================================
        // BUILD SUB-CARD: REGISTRAR ASISTENCIA (jPanel6)
        // ================================================================
        jPanel6 = new javax.swing.JPanel(new GridBagLayout());
        jPanel6.setBackground(cardBg);
        jPanel6.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new java.awt.Color(0xE8, 0xEB, 0xEE)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.weightx = 1;
        jPanel6.add(jLabel17, gbc);

        gbc.gridy = 1; gbc.gridwidth = 4;
        jPanel6.add(checkbtnPresente, gbc);

        gbc.gridy = 2;
        jPanel6.add(checkbtnAusente, gbc);

        gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0;
        jPanel6.add(jLabel13, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel6.add(lblTotalClases, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        jPanel6.add(jLabel14, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel6.add(lblPresente, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        jPanel6.add(jLabel16, gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        jPanel6.add(lblAusente, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0; gbc.gridwidth = 1;
        jPanel6.add(jLabel15, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 3;
        jPanel6.add(lblAsistencia, gbc);

        // ================================================================
        // BUILD SUB-CARD: REGISTRAR NOTA (jPanel7)
        // ================================================================
        jPanel7 = new javax.swing.JPanel(new GridBagLayout());
        jPanel7.setBackground(cardBg);
        jPanel7.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new java.awt.Color(0xE8, 0xEB, 0xEE)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1;
        jPanel7.add(jLabel21, gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        jPanel7.add(jLabel18, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        jPanel7.add(txtNota, gbc);

        gbc.gridy = 3; gbc.gridwidth = 2;
        javax.swing.JPanel btnWrapN = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        btnWrapN.setOpaque(false);
        btnWrapN.add(btnAgregarNota);
        jPanel7.add(btnWrapN, gbc);

        gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        jPanel7.add(jLabel20, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel7.add(lblNotas, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        jPanel7.add(jLabel19, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        jPanel7.add(lblPromedio, gbc);

        // ================================================================
        // BUILD SUB-CARD: DAR DE BAJA (jPanel8)
        // ================================================================
        jPanel8 = new javax.swing.JPanel(new GridBagLayout());
        jPanel8.setBackground(cardBg);
        jPanel8.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new java.awt.Color(0xE8, 0xEB, 0xEE)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel8.add(jLabel12, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel8.add(jLabel22, gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        javax.swing.JPanel bajaBtnWrap = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        bajaBtnWrap.setOpaque(false);
        bajaBtnWrap.add(btnBajaMateria);
        jPanel8.add(bajaBtnWrap, gbc);

        // ================================================================
        // LEGACY: jPanel5 (unused, kept for field declaration compatibility)
        // ================================================================
        jPanel5 = new javax.swing.JPanel();

        // ================================================================
        // THREE-COLUMN LAYOUT (BorderLayout)
        // ================================================================
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        // --- LEFT COLUMN (WEST, ~300px): Perfil + Inscripcion ---
        javax.swing.JPanel leftCol = new javax.swing.JPanel();
        leftCol.setLayout(new javax.swing.BoxLayout(leftCol, javax.swing.BoxLayout.Y_AXIS));
        leftCol.setBackground(bgColor);
        leftCol.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 8));
        leftCol.add(jPanel1);
        leftCol.add(javax.swing.Box.createVerticalStrut(12));
        leftCol.add(jPanel2);
        leftCol.add(javax.swing.Box.createVerticalGlue());
        java.awt.Dimension leftPref = leftCol.getPreferredSize();
        leftCol.setPreferredSize(new java.awt.Dimension(300, leftPref.height));
        getContentPane().add(leftCol, BorderLayout.WEST);

        // --- CENTER: Tabla de Materias ---
        javax.swing.JPanel centerWrap = new javax.swing.JPanel(new BorderLayout());
        centerWrap.setBackground(bgColor);
        centerWrap.setBorder(BorderFactory.createEmptyBorder(15, 8, 15, 8));
        centerWrap.add(jPanel4, BorderLayout.CENTER);
        getContentPane().add(centerWrap, BorderLayout.CENTER);

        // --- RIGHT COLUMN (EAST, ~320px): Asistencia + Nota + Alertas + Baja ---
        javax.swing.JPanel rightCol = new javax.swing.JPanel();
        rightCol.setLayout(new javax.swing.BoxLayout(rightCol, javax.swing.BoxLayout.Y_AXIS));
        rightCol.setBackground(bgColor);
        rightCol.setBorder(BorderFactory.createEmptyBorder(15, 8, 15, 15));
        rightCol.add(jPanel6);
        rightCol.add(javax.swing.Box.createVerticalStrut(10));
        rightCol.add(jPanel7);
        rightCol.add(javax.swing.Box.createVerticalStrut(10));
        rightCol.add(jPanel3);
        rightCol.add(javax.swing.Box.createVerticalStrut(10));
        rightCol.add(jPanel8);
        rightCol.add(javax.swing.Box.createVerticalGlue());
        java.awt.Dimension rightPref = rightCol.getPreferredSize();
        rightCol.setPreferredSize(new java.awt.Dimension(320, rightPref.height));
        getContentPane().add(rightCol, BorderLayout.EAST);
    }

    private void txtCarreraActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtNombreEstudianteActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtNombreMateriaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void checkbtnPresenteActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnAgregarNotaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnBajaMateriaActionPerformed(java.awt.event.ActionEvent evt) {
    }

    // Variables declaration
    private javax.swing.JMenu MenuArchivo;
    private javax.swing.JMenu MenuAyuda;
    private javax.swing.JMenu MenuReportes;
    private javax.swing.JSpinner NupAño;
    private javax.swing.JSpinner NupCuatrimestre;
    private javax.swing.JTable TablaMaterias;
    private javax.swing.JButton btnAgregarNota;
    private javax.swing.JButton btnBajaMateria;
    private javax.swing.JButton btnInscribir;
    private javax.swing.JRadioButton checkbtnAusente;
    private javax.swing.JRadioButton checkbtnPresente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAsistencia;
    private javax.swing.JLabel lblAusente;
    private javax.swing.JLabel lblNotas;
    private javax.swing.JLabel lblPresente;
    private javax.swing.JLabel lblPromedio;
    private javax.swing.JLabel lblTotalClases;
    private javax.swing.JList<String> list1;
    private javax.swing.JTextField txtAño;
    private javax.swing.JTextField txtCarrera;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombreEstudiante;
    private javax.swing.JTextField txtNombreMateria;
    private javax.swing.JTextField txtNota;
}
