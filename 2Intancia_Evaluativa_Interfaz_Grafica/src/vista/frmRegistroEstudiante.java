package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class frmRegistroEstudiante extends JDialog {

    private JTextField txtNombre;
    private JTextField txtLegajo;
    private JTextField txtCarrera;
    private JTextField txtAnio;
    private JButton btnConfirmar;

    public frmRegistroEstudiante() {
        super((Frame) null, "Registro de Estudiante", true); // modal
        setSize(300, 220);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // no se puede cerrar sin completar

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        Font f = new Font("SansSerif", Font.PLAIN, 11);

        txtNombre  = new JTextField(15); txtNombre.setFont(f);
        txtLegajo  = new JTextField(15); txtLegajo.setFont(f);
        txtCarrera = new JTextField(15); txtCarrera.setFont(f);
        txtAnio    = new JTextField(15); txtAnio.setFont(f);
        btnConfirmar = new JButton("Confirmar");

        String[] labels = {"Nombre:", "Legajo:", "Carrera:", "Anio ingreso:"};
        JTextField[] fields = {txtNombre, txtLegajo, txtCarrera, txtAnio};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]); lbl.setFont(f);
            panel.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(fields[i], gbc);
        }

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 4, 4, 4);
        panel.add(btnConfirmar, gbc);

        add(panel);

        // La validacion basica de campos vacios queda aqui porque es
        // responsabilidad de la vista impedir el cierre del dialogo.
        // La logica de negocio (que hacer con los datos) sigue en el Controlador.
        btnConfirmar.setActionCommand("CONFIRMAR_REGISTRO");
        btnConfirmar.addActionListener(e -> {
            if (camposVacios()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            setVisible(false); // cierra el dialogo y devuelve control al Controlador
        });
    }

    // -------------------------------------------------------
    //  Validacion interna de la vista
    // -------------------------------------------------------
    private boolean camposVacios() {
        return txtNombre.getText().trim().isEmpty()
                || txtLegajo.getText().trim().isEmpty()
                || txtCarrera.getText().trim().isEmpty()
                || txtAnio.getText().trim().isEmpty();
    }

    // -------------------------------------------------------
    //  Getters para que el Controlador lea los datos
    // -------------------------------------------------------
    public String getNombre()  { return txtNombre.getText().trim(); }
    public String getLegajo()  { return txtLegajo.getText().trim(); }
    public String getCarrera() { return txtCarrera.getText().trim(); }
    public String getTxtAnio() { return txtAnio.getText().trim(); }

    // -------------------------------------------------------
    //  Permite al Controlador agregar listeners adicionales
    //  (por ejemplo, para validaciones de negocio mas complejas)
    // -------------------------------------------------------
    public void registrarListener(ActionListener al) {
        btnConfirmar.addActionListener(al);
    }
}