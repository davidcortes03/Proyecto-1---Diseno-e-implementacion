package modelo;

import java.util.*;
import persistencia.PersistenciaEvento;

/**
 * Representa un evento dentro de la plataforma Boletamaster.
 * Contiene la información general del evento y sus localidades.
 */
public class Evento {

    private String nombreEvento;
    private String fechaHora;
    private boolean estado;
    private String tipoEvento;
    private String infoEvento;
    private double gananciasEvento;
    private Venues venue;
    private Organizador organizador;
    private List<Localidad> localidades;

    public Evento(String nombreEvento, String fechaHora, String tipoEvento,
                  String infoEvento, double gananciasEvento, Venues venue, Organizador organizador) {
        this.nombreEvento = nombreEvento;
        this.fechaHora = fechaHora;
        this.tipoEvento = tipoEvento;
        this.infoEvento = infoEvento;
        this.gananciasEvento = gananciasEvento;
        this.venue = venue;
        this.organizador = organizador;
        this.localidades = new ArrayList<>();
        this.estado = true;
    }

    /**
     * Agrega una nueva localidad al evento si no existe ya.
     */
    public boolean agregarLocalidad(Localidad localidad) {
        if (localidad == null) {
        	return false;
        }
        

        for (Localidad l : localidades) {
            if (l.getNombreLocalidad().equalsIgnoreCase(localidad.getNombreLocalidad())) {
                return false;
            }
        }

        localidades.add(localidad);
        return true;
    }

    /**
     * Cancela el evento y actualiza el archivo de persistencia.
     */
    public boolean cancelarEvento() {
        if (!estado) {
        	return false;
        }
        estado = false;
        PersistenciaEvento.cancelarEvento(this);
        System.out.println("⚠ Evento cancelado: " + nombreEvento);
        return true;
    }

    /**
     * Calcula las ganancias con base en las localidades vendidas.
     */
    public double calcularGanacias() {
        double total = 0;
        for (Localidad l : localidades) {
            total += (l.getCapacidad() - l.getBoletosDisponibles()) * l.getPrecio();
        }
        this.gananciasEvento = total;
        return total;
    }

    /**
     * Actualiza el valor de las ganancias del evento y guarda cambios.
     */
    public void actualizarGanancias(double total) {
        this.gananciasEvento = total;
        PersistenciaEvento.guardarEventos();
    }

    // ======= Getters y Setters =======

    public String getNombreEvento() { 
    	return nombreEvento; 
    }
    
    public String getFechaHora() { 
    	return fechaHora; 
    }
    
    public String getTipoEvento() { 
    	return tipoEvento; 
    }
    
    public String getInfoEvento() { 
    	return infoEvento; 
    }
    
    public double getGananciasEvento() { 
    	return gananciasEvento; 
    }
    
    public Venues getVenue() {
    	return venue; 
    }
    
    public Organizador getOrganizador() { 
    	return organizador; 
    }
    
    public boolean getEstado() { 
    	return estado; 
    }

    public void setEstado(boolean estado) { 
    	this.estado = estado;
    }

    @Override
    public String toString() {
        return "🎫 Evento: " + nombreEvento +
                "\nFecha: " + fechaHora +
                "\nTipo: " + tipoEvento +
                "\nVenue: " + (venue != null ? venue.getUbicacion() : "Sin asignar");
    }
}
