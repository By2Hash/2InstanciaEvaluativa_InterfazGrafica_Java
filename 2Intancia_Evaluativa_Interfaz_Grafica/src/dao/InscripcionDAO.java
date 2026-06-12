package dao;

import modelo.InscripcionMateria;
import modelo.Materia;
import java.io.*;
import java.util.ArrayList;

public class InscripcionDAO {

    private static final String ARCHIVO = "inscripciones.txt";

    public void guardar(ArrayList<InscripcionMateria> inscripciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (InscripcionMateria ins : inscripciones) {
                bw.write(toTexto(ins));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar inscripciones: " + e.getMessage());
        }
    }

    public ArrayList<InscripcionMateria> cargar() {
        ArrayList<InscripcionMateria> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isBlank()) {
                    InscripcionMateria ins = fromTexto(linea);
                    if (ins != null) lista.add(ins);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar inscripciones: " + e.getMessage());
        }

        return lista;
    }

    // ── Serialización ────────────────────────────────────────────

    private String toTexto(InscripcionMateria ins) {
        Materia m = ins.getMateria();

        StringBuilder sb = new StringBuilder();
        sb.append(m.getNombre()).append(";")
          .append(m.getCodigo()).append(";")
          .append(m.getCuatrimestre()).append(";")
          .append(m.getAnio()).append(";")
          .append(ins.getTotalClases()).append(";")
          .append(ins.getClasesAsistidas()).append(";");

        ArrayList<Double> notas = ins.getNotas();
        if (notas.isEmpty()) {
            sb.append("");
        } else {
            for (int i = 0; i < notas.size(); i++) {
                sb.append(notas.get(i));
                if (i < notas.size() - 1) sb.append(",");
            }
        }

        return sb.toString();
    }

    private InscripcionMateria fromTexto(String linea) {
        try {
            String[] partes = linea.split(";", -1);
            // partes[0] nombre, [1] codigo, [2] cuatrimestre, [3] anio,
            // [4] totalClases, [5] clasesAsistidas, [6] notas (puede estar vacío)

            Materia materia = new Materia(
                partes[0],
                partes[1],
                Integer.parseInt(partes[2]),
                Integer.parseInt(partes[3])
            );

            int totalClases     = Integer.parseInt(partes[4]);
            int clasesAsistidas = Integer.parseInt(partes[5]);

            InscripcionMateria ins = new InscripcionMateria(materia, totalClases);
            ins.setClasesAsistidas(clasesAsistidas);

            // Restaurar notas
            if (partes.length > 6 && !partes[6].isBlank()) {
                String[] notasStr = partes[6].split(",");
                for (String n : notasStr) {
                    ins.agregarNota(Double.parseDouble(n));
                }
            }

            return ins;

        } catch (Exception e) {
            System.err.println("Línea corrupta ignorada: " + linea);
            return null;
        }
    }
}