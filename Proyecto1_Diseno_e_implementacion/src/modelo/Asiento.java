package modelo;

public class Asiento {

	private int numero;
	private boolean estaOcupado;
	
	public Asiento (int numero) {
		this.numero = numero;
		this.estaOcupado = false;
	}
	
	public void setNumero (int nuevoNumero) {
		if (nuevoNumero <= 0) {
			throw new IllegalArgumentException("El número de asiento debe ser positivo");
			
		}
		
		
	}
	
	public int getNumero() {
		return this.numero;
	}
	
	public void ocuparAsiento() {
		if (this.estaOcupado) {
			throw new IllegalStateException("El asiento" + this.numero + "está libre"); 
		}
		
		this.estaOcupado = true;
	}
	
	
	
	public void liberarAsiento() {
		
		if (!this.estaOcupado) {
			throw new IllegalStateException("El asiento" + this.numero + "está libre"); 
		}
		
		this.estaOcupado = false;
	}
		
		
		
	}
	
	
