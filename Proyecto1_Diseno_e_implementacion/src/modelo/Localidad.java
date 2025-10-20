package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una localidad dentro de un evento o venue.
 * Cada localidad tiene un nombre, capacidad, precio y puede o no tener asientos numerados.
 * Además, mantiene la lista de tiquetes vendidos y los asientos disponibles cuando aplica.
 *
 * @version 1.0
 */
public class Localidad {

    private String nombreLocalidad;
    private int capacidad;
    private int precio;
    private boolean conAsientos;

    private Venues venue;
    private List<Asiento> asientos;
    private List<Tiquete> tiquetes;

    /**
     * Crea una nueva localidad asociada a un venue.
     * Si la localidad cuenta con asientos, se generan automáticamente según la capacidad indicada.
     *
     * @param nombreLocalidad Nombre que identifica la localidad (por ejemplo, “Platea” o “VIP”).
     * @param capacidad Cantidad máxima de personas o asientos disponibles.
     * @param precio Precio base del tiquete en esta localidad.
     * @param conAsientos Indica si la localidad tiene asientos numerados.
     * @param venue Venue (lugar) al que pertenece esta localidad.
     */
    public Localidad(String nombreLocalidad, int capacidad, int precio, boolean conAsientos, Venues venue) {
        this.nombreLocalidad = nombreLocalidad;
        this.capacidad = capacidad;
        this.precio = precio;
        this.conAsientos = conAsientos;
        this.venue = venue;
        this.asientos = new ArrayList<>();
        this.tiquetes = new ArrayList<>();

        // Si la localidad tiene asientos, se crean todos los asientos disponibles.
        if (conAsientos) {
            for (int i = 1; i <= capacidad; i++) {
                asientos.add(new Asiento(i, false, this));
            }
        }
    }

    /**
     * Retorna la cantidad de boletos disponibles para la venta,
     * restando los tiquetes ya vendidos a la capacidad total.
     *
     * @return Número de boletos aún disponibles.
     */
    public int getBoletosDisponibles() {
        return capacidad - tiquetes.size();
    }

    /**
     * Agrega un nuevo tiquete a la lista de tiquetes vendidos,
     * siempre y cuando la capacidad no haya sido superada.
     *
     * @param t Objeto Tiquete que se desea agregar a la localidad.
     * @return true si el tiquete fue agregado correctamente, false si ya no hay cupos o el tiquete es nulo.
     */
    public boolean agregarTiquete(Tiquete t) {
        if (t == null) return false;
        if (tiquetes.size() >= capacidad) return false;
        tiquetes.add(t);
        return true;
    }

    /**
     * Indica si la localidad aún cuenta con asientos disponibles (solo aplica si tiene asientos).
     *
     * @return true si hay al menos un asiento libre, false en caso contrario.
     */
    public boolean hayAsientosDisponibles() {
        if (!conAsientos) return false;
        for (Asiento a : asientos) {
            if (!a.isOcupado()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ocupa un asiento específico dentro de la localidad.
     * Busca el número indicado y lo marca como ocupado si estaba libre.
     *
     * @param numero Número del asiento a ocupar.
     * @return true si el asiento fue ocupado con éxito, false si no existe o ya estaba ocupado.
     */
    public boolean ocuparAsiento(int numero) {
        if (!conAsientos) return false;
        for (Asiento a : asientos) {
            if (a.getNumero() == numero && !a.isOcupado()) {
                a.setOcupado(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve el nombre de la localidad.
     *
     * @return Nombre de la localidad.
     */
    public String getNombreLocalidad() { 
        return nombreLocalidad; 
    }

    /**
     * Cambia el nombre de la localidad.
     *
     * @param nombreLocalidad Nuevo nombre que se asignará a la localidad.
     */
    public void setNombreLocalidad(String nombreLocalidad) { 
        this.nombreLocalidad = nombreLocalidad; 
    }

    /**
     * Devuelve la capacidad total de la localidad.
     *
     * @return Capacidad máxima (en número de personas o asientos).
     */
    public int getCapacidad() { 
        return capacidad; 
    }

    /**
     * Modifica la capacidad total de la localidad.
     *
     * @param capacidad Nueva capacidad total.
     */
    public void setCapacidad(int capacidad) { 
        this.capacidad = capacidad; 
    }

    /**
     * Devuelve el precio base de los tiquetes en esta localidad.
     *
     * @return Precio base del tiquete.
     */
    public int getPrecio() { 
        return precio; 
    }

    /**
     * Modifica el precio base de los tiquetes.
     *
     * @param precio Nuevo valor del precio base.
     */
    public void setPrecio(int precio) { 
        this.precio = precio; 
    }

    /**
     * Indica si la localidad cuenta con asientos numerados.
     *
     * @return true si tiene asientos, false en caso contrario.
     */
    public boolean isConAsientos() { 
        return conAsientos; 
    }

    /**
     * Cambia el valor que indica si la localidad tiene asientos numerados.
     *
     * @param conAsientos true si la localidad tendrá asientos, false si no.
     */
    public void setConAsientos(boolean conAsientos) { 
        this.conAsientos = conAsientos; 
    }

    /**
     * Devuelve el venue (lugar físico) al que pertenece la localidad.
     *
     * @return Objeto Venues asociado a esta localidad.
     */
    public Venues getVenue() { 
        return venue; 
    }

    /**
     * Asigna el venue (lugar) al que pertenece la localidad.
     *
     * @param venue Venue correspondiente a la localidad.
     */
    public void setVenue(Venues venue) { 
        this.venue = venue; 
    }

    /**
     * Retorna la lista completa de asientos de la localidad.
     *
     * @return Lista de objetos Asiento.
     */
    public List<Asiento> getAsientos() { 
        return asientos; 
    }

    /**
     * Retorna la lista de tiquetes vendidos o asociados a la localidad.
     *
     * @return Lista de objetos Tiquete.
     */
    public List<Tiquete> getTiquetes() { 
        return tiquetes; 
    }

    /**
     * Devuelve una representación en formato texto con la información principal de la localidad.
     *
     * @return Cadena con los datos más importantes de la localidad (nombre, precio, capacidad, etc.).
     */
    @Override
    public String toString() {
        String textoVenue;
        if (venue != null) {
            textoVenue = venue.getNombre();
        } else {
            textoVenue = "No asignado";
        }

        return "🎟 Localidad: " + nombreLocalidad +
               "\nPrecio: " + precio +
               "\nCapacidad: " + capacidad +
               "\nCon asientos: " + (conAsientos ? "Sí" : "No") +
               "\nBoletos vendidos: " + tiquetes.size() +
               "\nVenue: " + textoVenue;
    }
}
