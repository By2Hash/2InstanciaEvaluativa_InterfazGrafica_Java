package modelo;

import modelo.interfaces.Evaluable;
import java.util.ArrayList;

public class InscripcionMateria implements Evaluable {

    private Materia materia;
    private int totalClases;
    private int clasesAsistidas;
    private ArrayList<Double> notas;

    // -------------------------------------------------------
    //  Constructor
    // -------------------------------------------------------
    public InscripcionMateria(Materia materia, int totalClases) {
        this.materia = materia;
        this.totalClases = totalClases;
        this.clasesAsistidas = totalClases; // asistencia perfecta por defecto
        this.notas = new ArrayList<>();
    }

    // -------------------------------------------------------
    //  Getters
    // -------------------------------------------------------
    public Materia getMateria() {
        return materia;
    }

    public int getTotalClases() {
        return totalClases;
    }

    public int getClasesAsistidas() {
        return clasesAsistidas;
    }

    public ArrayList<Double> getNotas() {
        return notas;
    }

    public void setClasesAsistidas(int clasesAsistidas) {
        this.clasesAsistidas = clasesAsistidas;
    }

    // -------------------------------------------------------
    //  Registrar asistencia
    // -------------------------------------------------------
    public void registrarAsistencia(boolean presente) {
        if (!presente) {
            if (clasesAsistidas > 0) clasesAsistidas--;
        }
        // si presente == true, no hace nada (ya tiene asistencia perfecta)
    }

    // -------------------------------------------------------
    //  Agregar nota con validacion 0-10
    // -------------------------------------------------------
    public void agregarNota(double nota) {
        if (nota < 0 || nota > 10)
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10.");
        if (notas.size() >= 5)
            throw new IllegalStateException("No se pueden agregar mas de 5 notas.");
        notas.add(nota);
    }

    // -------------------------------------------------------
    //  Porcentaje de asistencia
    // -------------------------------------------------------
    public double getPorcentajeAsistencia() {
        if (totalClases == 0) return 100;
        return (clasesAsistidas * 100.0) / totalClases;
    }

    // -------------------------------------------------------
    //  Implementacion de Evaluable
    // -------------------------------------------------------

    /**
     * FIX: la condicion ahora tiene en cuenta TANTO la asistencia
     * como el promedio de notas.
     *
     * Reglas:
     *   - Si asistencia < 75%  → Libre (por inasistencias)
     *   - Si tiene las 3 notas cargadas y el promedio < 4 → Libre (por promedio)
     *   - En cualquier otro caso → Regular
     */
    @Override
    public String getCondicion() {
        if (getPorcentajeAsistencia() < 75) {
            return "Libre";
        }
        if (notas.size() == 3 && getPromedio() < 4) {
            return "Libre";
        }
        return "Regular";
    }

    @Override
    public double getPromedio() {
        if (notas.isEmpty()) return 0;
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        return suma / notas.size();
    }

    @Override
    public boolean estaAprobada() {
        // Exige promedio >= 6 Y condicion Regular (asistencia >= 75% y promedio >= 4)
        return getPromedio() >= 6 && getCondicion().equals("Regular");
    }

    // -------------------------------------------------------
    //  Serializacion
    // -------------------------------------------------------
    public String toTexto() {
        String notasStr = notas.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
        return materia.toTexto() + ";" + totalClases + ";" + clasesAsistidas + ";" + notasStr;
    }

    // -------------------------------------------------------
    //  toString para el listado de materias
    // -------------------------------------------------------
    @Override
    public String toString() {
        return String.format("%s | Condicion: %-8s | Promedio: %.2f | Asistencia: %.1f%%",
                materia.toString(),
                getCondicion(),
                getPromedio(),
                getPorcentajeAsistencia());
    }
}