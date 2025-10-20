package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una localidad dentro de un evento o venue.
 * Puede tener asientos y está asociada a múltiples tiquetes.
 */
public class Localidad {

    private String nombreLocalidad;
    private int capacidad;
    private int precio;
    private boolean conAsientos;

    private Venues venue;
    private List<Asiento> asientos;
    private List<Tiquete> tiquetes;

    public Localidad(String nombreLocalidad, int capacidad, int precio, boolean conAsientos, Venues venue) {
        this.nombreLocalidad = nombreLocalidad;
        this.capacidad = capacidad;
        this.precio = precio;
        this.conAsientos = conAsientos;
        this.venue = venue;
        this.asientos = new ArrayList<>();
        this.tiquetes = new ArrayList<>();

        if (conAsientos) {
            for (int i = 1; i <= capacidad; i++) {
                asientos.add(new Asiento(i, false, this));
            }
        }
    }

    public int getBoletosDisponibles() {
        return capacidad - tiquetes.size();
    }

    public boolean agregarTiquete(Tiquete t) {
        if (t == null) return false;
        if (tiquetes.size() >= capacidad) return false;
        tiquetes.add(t);
        return true;
    }

    public boolean hayAsientosDisponibles() {
        if (!conAsientos) return false;
        for (Asiento a : asientos) {
            if (!a.isOcupado()) {
                return true;
            }
        }
        return false;
    }

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

    public String getNombreLocalidad() { 
    	return nombreLocalidad; 
    	}
    
    public void setNombreLocalidad(String nombreLocalidad) { 
    	this.nombreLocalidad = nombreLocalidad; 
    	}

    public int getCapacidad() { 
    	return capacidad; 
    	}
    
    public void setCapacidad(int capacidad) { 
    	this.capacidad = capacidad; 
    	}

    public int getPrecio() { 
    	return precio; 
    	}
    
    public void setPrecio(int precio) { 
    	this.precio = precio; 
    	}

    public boolean isConAsientos() { 
    	return conAsientos; 
    	}
    
    public void setConAsientos(boolean conAsientos) { 
    	this.conAsientos = conAsientos; 
    	}

    public Venues getVenue() { 
    	return venue; 
    	}
    public void setVenue(Venues venue) { 
    	this.venue = venue; 
    	}

    public List<Asiento> getAsientos() { 
    	return asientos; 
    	}
    public List<Tiquete> getTiquetes() { 
    	return tiquetes; 
    	}

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
