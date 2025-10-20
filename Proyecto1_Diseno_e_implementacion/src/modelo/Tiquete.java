package modelo;

import modelo.Evento;
import usuarios.Cliente;
import modelo.Localidad;

public class Tiquete {

	protected double precio;
	protected double cargoPorcentual;
	protected String fechaHora;
	protected String id;
	protected boolean esCortesia;
	protected String tipoTiquete;
	protected double cargoEmision;
	private Evento evento;
	private Cliente cliente;
	private Localidad localidad;
	
	public Tiquete (double precio, double cargoPorcentual, double cargoEmision, String fechaHora, String id, boolean esCortesia, Evento evento, Localidad localidad, Cliente cliente, String tipoTiquete) {
		this.precio = precio;
		this.cargoPorcentual = cargoPorcentual;
		this.fechaHora = fechaHora;
		this.id = id;
		this.esCortesia = esCortesia;
		this.tipoTiquete = tipoTiquete;
		this.cargoEmision = cargoEmision;
		this.evento = evento;
		this.localidad = localidad;
		this.cliente = cliente;
		
		
	}
	
	public double calcularPrecioFinal() {
		if (esCortesia) {
			return 0.0;
		}
		
		double cargo = precio* (cargoPorcentual/100);
		return precio + cargo + cargoEmision;
	}
	
	public boolean esTransferible() {
		
		if (esCortesia || "Cortesia".equalsIgnoreCase(tipoTiquete)) {
			return false;
		}
		
		if ("Deluxe".equalsIgnoreCase(tipoTiquete)) {
			return false;
		}
		
		if ("Multiple".equalsIgnoreCase(tipoTiquete)) {
			
			return true;
	
		}
		
		return false;
	}
	
	public String getInformación() {
		
		StringBuilder info = new StringBuilder();
		info.append("Id: ").append(id);
		info.append("Evento: ").append(evento.getNombreEvento());
		info.append("Localidad: ").append(localidad.getNombre());
		info.append("Cliente: ").append(cliente);
		info.append("Fecha: ").append(fechaHora);
		info.append("Precio Final: ").append(calcularPrecioFinal());
		if (esCortesia) {
			info.append("Cortesia");
		}
		
		return info.toString();
	}
	
	public double getPrecio() {
		return this.precio;
	}
	
	public double getCargoPorcentual() {
		return this.cargoPorcentual;
	}
	
	public String getFechaHora() {
		return this.fechaHora;
	}
	
	public String getId() {
		return this.id;
		
	}
	
	public boolean getEsCortesia() {
		return this.esCortesia;
		
	}
	
	public void setPrecio (double precio ) {
		if  (precio < 0) {
			throw new IllegalArgumentException("El precio no puede ser negativo");
			
		}
		this.precio = precio;
	}
	
	public void setCargoPorcentual( double cargoPorcentual) {
		if (cargoPorcentual < 0) {
			throw new IllegalArgumentException("El csrgo no puede ser negativo");
		}
		this.cargoPorcentual = cargoPorcentual;
	}
	
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	public void setEsCortesia(boolean esCortesia) {
		this.esCortesia = esCortesia;
	}
	
	
	
	
	
	

}
