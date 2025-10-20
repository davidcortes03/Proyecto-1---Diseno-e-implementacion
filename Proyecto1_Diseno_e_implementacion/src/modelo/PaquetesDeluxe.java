package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un paquete Deluxe que incluye tiquetes para eventos junto con mercancía y beneficios adicionales.
 * Los paquetes Deluxe ofrecen una experiencia premium que combina acceso a eventos con productos exclusivos.
 * 
 * @version 1.0
 */
public class PaquetesDeluxe {

	private String idPaquete;
	private String nombre;
	private String mercancia;
	private String beneficiosAdicionales;
	private double precioPaquete;
	private Evento evento;
	private List<Tiquete> tiquetes;
	
	/**
	 * Crea un nuevo paquete Deluxe con los atributos especificados.
	 * 
	 * @param idPaquete Identificador único del paquete Deluxe
	 * @param nombre Nombre descriptivo del paquete
	 * @param mercancia Descripción de la mercancía incluida en el paquete
	 * @param beneficiosAdicionales Descripción de los beneficios adicionales del paquete
	 * @param precioPaquete Precio base del paquete sin incluir los tiquetes
	 * @throws IllegalArgumentException si idPaquete, nombre, mercancia o beneficiosAdicionales son nulos o vacíos
	 */
	public PaquetesDeluxe(String idPaquete, String nombre, String mercancia, String beneficiosAdicionales, double precioPaquete) {
		if (idPaquete == null || idPaquete.trim().isEmpty()) {
			throw new IllegalArgumentException("El ID del paquete no puede ser nulo o vacío");
		}
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del paquete no puede ser nulo o vacío");
		}
		if (precioPaquete < 0) {
			throw new IllegalArgumentException("El precio del paquete no puede ser negativo");
		}
		
		this.idPaquete = idPaquete;
		this.nombre = nombre;
		this.mercancia = mercancia;
		this.beneficiosAdicionales = beneficiosAdicionales;
		this.precioPaquete = precioPaquete;
		this.tiquetes = new ArrayList<>();
	}
	
	/**
	 * Agrega un tiquete al paquete Deluxe.
	 * 
	 * @param tiquete Tiquete a agregar al paquete
	 * @throws IllegalArgumentException si el tiquete es nulo
	 */
	public void agregartiqeute(Tiquete tiquete) {
		if (tiquete == null) {
			throw new IllegalArgumentException("El tiquete no puede ser nulo");
		}
		
		tiquetes.add(tiquete);
	}
	
	/**
	 * Verifica si el paquete Deluxe está disponible para la venta.
	 * Un paquete se considera disponible si contiene al menos un tiquete.
	 * 
	 * @return true si el paquete contiene tiquetes, false en caso contrario
	 */
	public boolean estaDisponible() {
		return !tiquetes.isEmpty();
	}
	
	/**
	 * Genera una representación detallada en formato String con toda la información del paquete Deluxe.
	 * Incluye información del paquete, mercancía, beneficios, tiquetes incluidos y cálculos de precio.
	 * 
	 * @return String formateado con la información completa del paquete Deluxe
	 */
	public String getInformacion() {
		StringBuilder info = new StringBuilder();
        info.append("=== Paquete Deluxe ===\n");
        info.append("ID Paquete: ").append(idPaquete).append("\n");
        info.append("Nombre: ").append(nombre).append("\n");
        info.append("Mercancía incluida: ").append(mercancia).append("\n");
        info.append("Beneficios adicionales: ").append(beneficiosAdicionales).append("\n");
        info.append("Número de tiquetes: ").append(getNumeroTiquetes()).append("\n");
	
        double totalTiquetes = 0;
        for (Tiquete t : tiquetes) {
            totalTiquetes += t.getPrecio(); 
        }

        double total = totalTiquetes + precioPaquete;

        info.append("Precio base del paquete: $").append(precioPaquete).append("\n");
        info.append("Precio total (con tiquetes): $").append(total).append("\n");

        if (!tiquetes.isEmpty()) {
            info.append("--- Tiquetes incluidos ---\n");
            for (Tiquete t : tiquetes) {
                info.append(t.getInformación()).append("\n");
            }
        }

        info.append("=========================\n");
        return info.toString();
	}
	
	/**
	 * Obtiene el número de tiquetes incluidos en el paquete Deluxe.
	 * 
	 * @return Número de tiquetes en el paquete
	 */
	public int getNumeroTiquetes() {
		return tiquetes.size();
	}
	
	/**
	 * Obtiene el identificador único del paquete Deluxe.
	 * 
	 * @return ID del paquete
	 */
	public String getIdpaquete() {
		return idPaquete;
	}
	
	/**
	 * Obtiene el nombre descriptivo del paquete Deluxe.
	 * 
	 * @return Nombre del paquete
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Obtiene la descripción de la mercancía incluida en el paquete.
	 * 
	 * @return Descripción de la mercancía
	 */
	public String getMercancia() {
		return mercancia;
	}
	
	/**
	 * Obtiene la descripción de los beneficios adicionales del paquete.
	 * 
	 * @return Descripción de los beneficios adicionales
	 */
	public String getBeneficiosAdicionales() {
		return beneficiosAdicionales;
	}
	
	/**
	 * Obtiene el precio base del paquete Deluxe sin incluir los tiquetes.
	 * 
	 * @return Precio base del paquete
	 */
	public double getPrecio() {
		return precioPaquete;
	}
	
	/**
	 * Obtiene la lista de tiquetes incluidos en el paquete Deluxe.
	 * 
	 * @return Lista de tiquetes del paquete
	 */
	public List<Tiquete> getTiqeutesIncluidos() {
		return tiquetes;
	}
	
	/**
	 * Obtiene el evento principal asociado al paquete Deluxe.
	 * 
	 * @return Evento asociado al paquete
	 */
	public Evento getEvento() {
		return evento;
	}
	
	/**
	 * Establece el nombre descriptivo del paquete Deluxe.
	 * 
	 * @param nombre Nuevo nombre del paquete
	 * @throws IllegalArgumentException si el nombre es nulo o vacío
	 */
	public void setNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		this.nombre = nombre;
	}
	
	/**
	 * Establece la descripción de la mercancía incluida en el paquete.
	 * 
	 * @param mercancia Nueva descripción de la mercancía
	 */
	public void setMercancia(String mercancia) {
		this.mercancia = mercancia;
	}
	
	/**
	 * Establece la descripción de los beneficios adicionales del paquete.
	 * 
	 * @param beneficiosAdicionales Nueva descripción de los beneficios
	 */
	public void setBeneficiosAdicionales(String beneficiosAdicionales) {
		this.beneficiosAdicionales = beneficiosAdicionales;
	}
	
	/**
	 * Establece el precio base del paquete Deluxe.
	 * 
	 * @param precio Nuevo precio del paquete
	 * @throws IllegalArgumentException si el precio es negativo
	 */
	public void setPrecio(double precio) {
		if (precio < 0) {
			throw new IllegalArgumentException("El precio no puede ser negativo");
		}
		this.precioPaquete = precio;
	}
}