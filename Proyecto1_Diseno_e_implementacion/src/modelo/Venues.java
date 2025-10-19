package modelo;

/**
 * Representa un lugar físico donde se pueden realizar eventos.
 * Clase nombrada "Venues" para mantener compatibilidad con el resto del proyecto.
 */
public class Venues {

    private String nombre;         
    private String ubicacion;          
    private int capacidadMaxima;       
    private String restriccionesUso;    
    private boolean aprobado;           

    // Constructor completo
    public Venues(String nombre, String ubicacion, int capacidadMaxima, String restriccionesUso, boolean aprobado) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.restriccionesUso = restriccionesUso;
        this.aprobado = aprobado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getRestriccionesUso() {
        return restriccionesUso;
    }

    /**
     * Devuelve si el venue está aprobado por el administrador.
     * Método usado por la persistencia (isAprobado -> boolean).
     */
    public boolean isAprobado() {
        return aprobado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public void setRestriccionesUso(String restriccionesUso) {
        this.restriccionesUso = restriccionesUso;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Venues)) return false;
        Venues v = (Venues) obj;
        return this.nombre != null && this.nombre.equalsIgnoreCase(v.getNombre());
    }
       
        @Override
        public int hashCode() {
            return (nombre == null) ? 0 : nombre.toLowerCase().hashCode();
        }

        @Override
        public String toString() {
            return "Venue: " + nombre +
                   "\nUbicación: " + ubicacion +
                   "\nCapacidad: " + capacidadMaxima +
                   "\nRestricciones: " + (restriccionesUso == null ? "-" : restriccionesUso) +
                   "\nAprobado: " + (aprobado ? "Sí" : "No");
        }
    }
