package dao;

import modelo.Estudiante;
import java.io.*;

public class EstudianteDAO {

    private static final String ARCHIVO = "estudiante.txt";

    public void guardar(Estudiante estudiante) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            bw.write(estudiante.getNombre() + ";"
                    + estudiante.getLegajo() + ";"
                    + estudiante.getCarrera() + ";"
                    + estudiante.getAnioIngreso());
        } catch (IOException e) {
            System.err.println("Error al guardar estudiante: " + e.getMessage());
        }
    }

    public Estudiante cargar() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine();
            if (linea == null || linea.isBlank()) return null;

            String[] partes = linea.split(";");
            return new Estudiante(partes[0], partes[1], partes[2], Integer.parseInt(partes[3]));

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar estudiante: " + e.getMessage());
            return null;
        }
    }
}