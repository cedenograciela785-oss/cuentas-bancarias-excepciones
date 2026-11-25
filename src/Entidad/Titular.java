package Entidad;

import Excepciones.DatoInvalidoException;

/** Dueño de la cuenta. Valida cédula (10 dígitos) y nombre desde el constructor. */
public class Titular {
    private String cedula;
    private String nombre;

    public Titular(String cedula, String nombre) {
        setCedula(cedula);
        setNombre(nombre);
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }

    public void setCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}"))
            throw new DatoInvalidoException("La cédula debe tener exactamente 10 dígitos numéricos.");
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().length() < 2)
            throw new DatoInvalidoException("El nombre debe tener al menos 2 caracteres.");
        this.nombre = nombre.trim();
    }

    @Override
    public String toString() {
        return nombre + " (C.I. " + cedula + ")";
    }
}
