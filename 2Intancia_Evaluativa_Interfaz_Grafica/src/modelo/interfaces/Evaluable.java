package modelo.interfaces;

public interface Evaluable {

    // -------------------------------------------------------
    //  Métodos a implementar obligatoriamente
    // -------------------------------------------------------
    String getCondicion();

    double getPromedio();

    boolean estaAprobada();

    // -------------------------------------------------------
    //  Método default (ya implementado, no requiere override)
    // -------------------------------------------------------
    default void mostrarEstadoAcademico() {
        System.out.println("  Condición : " + getCondicion());
        System.out.printf ("  Promedio  : %.2f%n", getPromedio());
        System.out.println("  Aprobada  : " + (estaAprobada() ? "Sí" : "No"));
    }
}