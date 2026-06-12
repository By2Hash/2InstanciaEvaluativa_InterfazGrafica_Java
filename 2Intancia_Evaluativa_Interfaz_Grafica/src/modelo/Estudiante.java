package modelo;

import modelo.interfaces.Consultable;
import java.util.ArrayList;

public class Estudiante extends PersonaAcademica implements Consultable {

    private String carrera;
    private int anioIngreso;
    private ArrayList<InscripcionMateria> materias;

    public Estudiante(String nombre, String legajo, String carrera, int anioIngreso) {
        super(nombre, legajo);
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
        this.materias = new ArrayList<>();
    }

    public String getCarrera()    { return carrera; }
    public int getAnioIngreso()   { return anioIngreso; }

    public void inscribirse(Materia m, int totalClases) {
        if (getInscripcion(m.getCodigo()) != null)
            throw new IllegalArgumentException("Ya estas inscripto en esa materia.");
        materias.add(new InscripcionMateria(m, totalClases));
    }

    public void darDeBaja(String codigoMateria) {
        InscripcionMateria ins = getInscripcion(codigoMateria);
        if (ins == null)
            throw new IllegalArgumentException("No se encontro la materia con ese codigo.");
        materias.remove(ins);
    }

    public InscripcionMateria getInscripcion(String codigoMateria) {
        for (InscripcionMateria ins : materias)
            if (ins.getMateria().getCodigo().equalsIgnoreCase(codigoMateria))
                return ins;
        return null;
    }

    // Sobrecarga: buscar por cuatrimestre
    public ArrayList<InscripcionMateria> getInscripcion(int cuatrimestre) {
        ArrayList<InscripcionMateria> resultado = new ArrayList<>();
        for (InscripcionMateria ins : materias)
            if (ins.getMateria().getCuatrimestre() == cuatrimestre)
                resultado.add(ins);
        return resultado;
    }

    public double getPromedioGeneral() {
        if (materias.isEmpty()) return 0;
        double suma = 0;
        for (InscripcionMateria ins : materias) suma += ins.getPromedio();
        return suma / materias.size();
    }

    public ArrayList<InscripcionMateria> getMateriasCriticas() {
        ArrayList<InscripcionMateria> criticas = new ArrayList<>();
        for (InscripcionMateria ins : materias) {
            boolean tieneNotas = !ins.getNotas().isEmpty();
            boolean promedioEnRiesgo = ins.getPromedio() < 4;
            if (tieneNotas && promedioEnRiesgo) {
                criticas.add(ins);
            }
        }
        return criticas;
    }

    public ArrayList<InscripcionMateria> getMaterias() { return materias; }

    @Override
    public void mostrarResumen() {
        System.out.println("----------------------------------------");
        System.out.println("|         PERFIL DEL ESTUDIANTE        |");
        System.out.println("----------------------------------------");
        System.out.println("|  Nombre       : " + getNombre());
        System.out.println("|  Legajo       : " + getLegajo());
        System.out.println("|  Carrera      : " + carrera);
        System.out.println("|  Anio ingreso  : " + anioIngreso);
        System.out.printf( "|  Promedio gral: %.2f%n", getPromedioGeneral());
        System.out.println("|  Materias     : " + materias.size());
        System.out.println("----------------------------------------");
    }
}