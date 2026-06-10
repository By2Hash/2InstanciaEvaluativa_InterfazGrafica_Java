package modelo;

import modelo.interfaces.Consultable;

public class Materia implements Consultable {

    private String nombre;
    private String codigo;
    private int cuatrimestre;
    private int anio;

    // -------------------------------------------------------
    //  Constructor
    // -------------------------------------------------------
    public Materia(String nombre, String codigo, int cuatrimestre, int anio) {
        this.nombre = nombre;
        setCodigo(codigo);
        setCuatrimestre(cuatrimestre);
        this.anio = anio;
    }

    // -------------------------------------------------------
    //  Getters
    // -------------------------------------------------------
    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public int getAnio() {
        return anio;
    }

    // -------------------------------------------------------
    //  Setters con validación
    // -------------------------------------------------------
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.length() < 3 || codigo.length() > 10) {
            throw new IllegalArgumentException("El código debe tener entre 3 y 10 caracteres.");
        }
        this.codigo = codigo;
    }

    public void setCuatrimestre(int cuatrimestre) {
        if (cuatrimestre != 1 && cuatrimestre != 2) {
            throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2.");
        }
        this.cuatrimestre = cuatrimestre;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
    
    public String toTexto() {
        return nombre + ";" + codigo + ";" + cuatrimestre + ";" + anio;
    }

     public static Materia fromTexto(String linea) {
        String[] p = linea.split(";");
        return new Materia(p[0], p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3]));
    }

    // -------------------------------------------------------
    //  toString para mostrar info básica
    // -------------------------------------------------------
    @Override
    public void mostrarResumen() {
        System.out.printf("  [%s] %s - %dC Año %d%n", codigo, nombre, cuatrimestre, anio);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %dC Anio %d", codigo, nombre, cuatrimestre, anio);
    }

}