package transacciones;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

import usuarios.Cliente;
import modelo.Tiquete;
import modelo.Evento;
import modelo.Localidad;
import usuarios.ClienteNatural;

/**
 * Representa una transacción de compra de tiquetes en el sistema.
 * Gestiona el procesamiento de pagos, generación de comprobantes y reembolsos.
 * 
 * @version 1.0
 */
public class Transacciones {
	
	private double precio;
	private String fechaHora;
	private String idTransaccion;
	private String metodoPago;
	private String tipo;
	private String estado;
	private Cliente cliente;
	private List<Tiquete> tiquetes;
	

	/**
	 * Crea una nueva transacción con los datos básicos requeridos.
	 * 
	 * @param precio Precio total de la transacción
	 * @param fechaHora Fecha y hora en que se realiza la transacción
	 * @param idTransaccion Identificador único de la transacción
	 * @param metodoPago Método de pago utilizado (Efectivo, Tarjeta, Saldo, etc.)
	 * @param tipo Tipo de transacción (Normal, Cortesía, etc.)
	 * @param cliente Cliente que realiza la transacción
	 * @throws IllegalArgumentException si cliente, metodoPago o idTransaccion son nulos o vacíos
	 */
	public Transacciones(double precio, String fechaHora, String idTransaccion, String metodoPago, String tipo, Cliente cliente) {
		
		if (cliente == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
		if (metodoPago == null || metodoPago.isEmpty()) throw new IllegalArgumentException("Debe especificar un método de pago");
		if (idTransaccion == null || idTransaccion.isEmpty()) throw new IllegalArgumentException("El ID de transaccion no puede ser nulo");
	
		this.precio = precio;
		this.fechaHora = fechaHora;
		this.idTransaccion = idTransaccion;
		this.metodoPago = metodoPago;
		this.cliente = cliente;
		this.tipo = tipo;
		this.estado = "Completada";
		this.tiquetes = new ArrayList<>();
		
	}
	

	/**
	 * Procesa el pago de la transacción validando los datos requeridos.
	 * 
	 * @return true si el pago se procesa exitosamente
	 * @throws IllegalArgumentException si el precio es menor o igual a cero
	 * @throws NullPointerException si el cliente no está definido
	 * @throws Exception si el pago no se puede procesar por razones externas
	 */
	public boolean procesarPago() throws Exception {
		if (this.precio <= 0) {
			throw new IllegalArgumentException("El precio de la transaccion debe ser mayor que cero");
		}
		if (this.cliente == null) {
			throw new NullPointerException("El cliente no definido para procesar pago");
		}
		
		boolean pagoExitoso = true;
		
		if (!pagoExitoso) {
			this.estado = "Fallida";
			throw new Exception("El pago no se pudo procesar correctamente");
		}
		
		this.estado = "Completada";
		return true;
	}

	
	/**
	 * Genera un comprobante de compra con formato legible que incluye todos los detalles de la transacción.
	 * 
	 * @return String con el comprobante formateado que incluye ID, cliente, fecha, total, método de pago y detalles de tiquetes
	 */
	public String generarComprobante() {
		
		StringBuilder comprobante = new StringBuilder();
		comprobante.append("=== COMPROBANTE DE COMPRA ===\n");
		comprobante.append("ID Transacción: ").append(this.idTransaccion).append("\n");
		comprobante.append("Cliente: ").append(this.cliente.getNombreUsuario()).append("\n");
		comprobante.append("Fecha: ").append(this.fechaHora).append("\n");
		comprobante.append("Total: $").append(this.precio).append("\n");
		comprobante.append("Método de Pago: ").append(this.metodoPago).append("\n");
		comprobante.append("Tipo: ").append(this.tipo).append("\n");
		comprobante.append("Estado: ").append(this.estado).append("\n");
		comprobante.append("Tiquetes: ").append(this.tiquetes.size()).append("\n");
	
		if (!this.tiquetes.isEmpty()) {
			comprobante.append("--- Detalle Tiquetes ---\n");
			for (Tiquete tiquete : this.tiquetes) {
				comprobante.append("- ").append(tiquete.getInformacion()).append("\n");
			}
		}
		
		comprobante.append("========================");
		return comprobante.toString();
		
	}
	
	/**
	 * Genera y agrega tiquetes a la transacción para un evento y localidad específicos.
	 * 
	 * @param evento Evento para el cual se generan los tiquetes
	 * @param localidad Localidad dentro del evento
	 * @param cantidad Número de tiquetes a generar
	 * @throws IllegalArgumentException si la cantidad es menor o igual a cero
	 */
	public void generarTiquete(Evento evento, Localidad localidad, int cantidad) {
		
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad de tiquetes debe ser mayor a 0");
		}
		
		for (int i = 0; i < cantidad; i++) {
			Tiquete nuevoTiquete = new Tiquete(
					evento,
					localidad,
					this.cliente,
					localidad.getPrecio(),
					this.tipo.equals("Cortesía")
					);
			
			this.tiquetes.add(nuevoTiquete);
		}
	}
	
	/**
	 * Procesa el reembolso de la transacción devolviendo el saldo al cliente.
	 * Solo aplicable para transacciones completadas que no sean cortesías.
	 * 
	 * @return true si el reembolso se procesa exitosamente, false en caso contrario
	 */
	public boolean procesarReembolso() {
		if (!"Completada".equals(this.estado)) {
			System.out.println("No se puede reembolsar - Transacción no completada");
			return false;
		}
		
		if (esCortesia()) {
			System.out.println("No se puede reembolsar - Transacción no completada");
			return false;
		}
		
		ClienteNatural clienteNatural = (ClienteNatural) this.cliente;
		boolean saldoAnadido = clienteNatural.añadirSaldo(this.precio);
		
		if (saldoAnadido) {
			this.estado = "Reembolsada";
			System.out.println("✅ Reembolso procesado - Saldo devuelto: $" + this.precio);
			return true;
		} else {
			System.out.println("❌ Error al añadir saldo - Reembolso fallido");
			return false;
		}
	}
	
	/**
	 * Agrega un tiquete existente a la transacción.
	 * 
	 * @param tiquete Tiquete a agregar a la transacción
	 */
	public void agregarTiquete(Tiquete tiquete) {
		this.tiquetes.add(tiquete);
	}
	
	/**
	 * Verifica si la transacción es de tipo cortesía.
	 * 
	 * @return true si la transacción es cortesía, false en caso contrario
	 */
	public boolean esCortesia() {
		return "Cortesia".equalsIgnoreCase(this.tipo);
	}
	
	/**
	 * Determina si la transacción afecta los ingresos del sistema.
	 * Las transacciones deben estar completadas y no ser cortesías para afectar ingresos.
	 * 
	 * @return true si la transacción afecta los ingresos, false en caso contrario
	 */
	public boolean afectaIngresos() {
		return !esCortesia() && "Completada".equals(this.estado);
	}
	
	/**
	 * Genera un resumen breve de la transacción para visualización rápida.
	 * 
	 * @return String con el resumen de la transacción
	 */
	public String getResumenTransaccion() {
		return "Transacción " + this.idTransaccion + " | " + this.tipo + " | $" + this.precio + " | " + this.estado
				+ " | " + this.tiquetes.size() + " tiquetes";
	}
	
	
	public double getPrecioTotal() { return this.precio; }
	public String getFechaHora() { return this.fechaHora; }
	public String getIdTransaccion() { return this.idTransaccion; }
	public String getMetodoPago() { return this.metodoPago; }
	public Cliente getCliente() { return this.cliente; }
	public List<Tiquete> getTiquetes() { return this.tiquetes; }
	public String getTipo() { return this.tipo; }
	
	public void setPrecioTotal(double precioTotal) { this.precio = precioTotal; }
	public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
	public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
	public void setTipo(String tipo) { this.tipo = tipo; }
}