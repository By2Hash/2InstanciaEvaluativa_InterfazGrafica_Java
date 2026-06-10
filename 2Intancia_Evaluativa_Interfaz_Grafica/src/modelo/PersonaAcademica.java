package modelo;

public abstract class PersonaAcademica {

    private String nombre;
    private String legajo;

    public PersonaAcademica(String nombre, String legajo) {
        setNombre(nombre);
        setLegajo(legajo);
    }

    public String getNombre() { return nombre; }
    public String getLegajo() { return legajo; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        this.nombre = nombre;
    }

    public void setLegajo(String legajo) {
        if (legajo == null)
            throw new IllegalArgumentException("El legajo no puede ser null.");
        this.legajo = legajo;
    }

    public abstract void mostrarResumen();
}