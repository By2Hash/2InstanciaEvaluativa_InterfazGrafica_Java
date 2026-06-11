package main;

import controlador.Controlador;
import vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            new Controlador(vista);
            vista.setVisible(true);
        });
    }
}