package transacciones;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

import usuarios.Cliente;
import modelo.Tiquete;
import modelo.Evento;
import modelo.Localidad;
import usuarios.ClienteNatural;

public class Transacciones {
	
	private double precio;
	private String fechaHora;
	private String idTransaccion;
	private String metodoPago;
	private String tipo;
	private String estado;
	private Cliente cliente;
	private List<Tiquete> tiquetes;
	

	public Transacciones (double precio, String fechaHora, String idTransaccion, String metodoPago, String tipo, Cliente cliente) {
		
		if (cliente == null) throw new IllegalArgumentException ("El cliente no puede ser nulo");
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
			throw new Exception ("El pago no se pudo procesar correctamente");
		}
		
		this.estado = "Completada";
		return true;
	}

	
	
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
	
	public void generarTiquete(Evento evento, Localidad localidad, int cantidad) {
		
		if (cantidad <= 0) {
			throw new IllegalArgumentException ("la cantidad de tiquetes debe ser mayor a 0");
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
	
	public void agregarTiquete(Tiquete tiquete) {
		this.tiquetes.add(tiquete);
	}
	
	public boolean esCortesia() {
		return "Cortesia".equalsIgnoreCase(this.tipo);
	}
	
	public boolean afectaIngresos() {
		return !esCortesia() && "Completada".equals(this.estado);
	}
	
	public String getResumenTransaccion() {
		return "Transacción" + this.idTransaccion + " | " + this.tipo + "| $" + this.precio + " | " + this.estado
				+ " | " + this.tiquetes.size() + "tiquetes";
		
	}
	
	public double getPrecioTotal() {
		return this.precio;
	}
	
	public String getFechaHora() {
		return this.fechaHora;
	}
	
	public String getIdTransaccion() {
		return this.idTransaccion;
	}
	
	public String getMetodoPago() {
		return this.metodoPago;
	}
	
	public Cliente getCliente() {
		return this.cliente;
	}
	
	public List<Tiquete> getTiquetes(){
		return this.tiquetes;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public void setPrecioTotal(double precioTotal) {
		this.precio = precioTotal;
	}
	
	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
