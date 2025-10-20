-package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.Evento;
import usuarios.Cliente;
import modelo.Localidad;

/**
 * Representa un tiquete de entrada múltiple que agrupa varias entradas individuales.
 * Hereda de la clase Tiquete y añade funcionalidades para manejar múltiples entradas.
 * 
 * @version 1.0
 * @see Tiquete
 */
public class TiquetesEntradaMultiple extends Tiquete {
	
	private int numeroEntradas;
	private double precioTotal;
	private List<Tiquete> tiquetes;
	
	/**
	 * Crea un nuevo tiquete de entrada múltiple con los atributos especificados.
	 * 
	 * @param precio Precio base del tiquete múltiple
	 * @param cargoPorcentual Porcentaje de cargo por servicio aplicado al precio base
	 * @param fechaHora Fecha y hora del evento en formato string
	 * @param id Identificador único del tiquete múltiple
	 * @param esCortesia Indica si el tiquete múltiple es de cortesía
	 * @param evento Evento al que pertenece el tiquete múltiple
	 * @param localidad Localidad específica del evento
	 * @param cliente Cliente dueño del tiquete múltiple
	 * @param numeroEntradas Número de entradas individuales incluidas en el paquete múltiple
	 * @throws IllegalArgumentException si numeroEntradas es menor o igual a cero
	 */
	public TiquetesEntradaMultiple(double precio, double cargoPorcentual, String fechaHora, String id, 
	                               boolean esCortesia, Evento evento, Localidad localidad, 
	                               Cliente cliente, int numeroEntradas) {
		super(precio, cargoPorcentual, fechaHora, id, esCortesia, evento, localidad, cliente);
		if (numeroEntradas <= 0) {
			throw new IllegalArgumentException("El número de entradas debe ser mayor a cero");
		}
		this.numeroEntradas = numeroEntradas;
		this.tiquetes = new ArrayList<>();
		this.precioTotal = precioTotal();
	}
	
	/**
	 * Obtiene el número de entradas individuales incluidas en el paquete múltiple.
	 * 
	 * @return Número de entradas individuales en el paquete
	 */
	public int getNumeroEntradas() {
		return numeroEntradas;
	}
	
	/**
	 * Obtiene la lista de tiquetes individuales que componen el paquete múltiple.
	 * 
	 * @return Lista de tiquetes individuales incluidos en el paquete
	 */
	public List<Tiquete> getEntradas() {
		return tiquetes;
	}
	
	/**
	 * Calcula el precio total del paquete de entradas múltiples.
	 * Utiliza el precio base heredado de la clase Tiquete.
	 * 
	 * @return Precio total del paquete de entradas múltiples
	 */
	public double precioTotal() {
		return super.getPrecio();
	}
	
	/**
	 * Valida si el número de entradas en el paquete no excede el límite permitido por transacción.
	 * 
	 * @param limiteCompraBoletas Límite máximo de boletas permitidas por transacción
	 * @return true si el número de entradas es menor o igual al límite, false en caso contrario
	 */
	public boolean validarCantidadMaximaEntradas(int limiteCompraBoletas) {
		return numeroEntradas <= limiteCompraBoletas;
	}
	
	/**
	 * Establece el número de entradas individuales en el paquete múltiple.
	 * 
	 * @param cantidad Nuevo número de entradas para el paquete
	 * @throws IllegalArgumentException si la cantidad es menor o igual a cero
	 */
	public void setNumeroEntradas(int cantidad) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad de entradas debe ser mayor a 0");
		}
		this.numeroEntradas = cantidad;
	}
	
	/**
	 * Establece el precio total del paquete de entradas múltiples.
	 * 
	 * @param nuevoTotal Nuevo precio total para el paquete
	 * @throws IllegalArgumentException si el nuevo total es negativo
	 */
	public void setPrecioTotal(double nuevoTotal) {
		if (nuevoTotal < 0) {
			throw new IllegalArgumentException("El precio total no puede ser negativo");
		}
		this.precioTotal = nuevoTotal;
	}
}