package modelo;

public class Asiento {

	private int numero;
	private boolean estaOcupado;
	private Localidad localidad;
	
	public Asiento (int numero, boolean estaOcupado) {
		this.numero = numero;
		this.estaOcupado = false;
	}
	
    public Asiento(int numero, boolean ocupado, Localidad localidad) {
        this.numero = numero;
        this.estaOcupado = ocupado;
        this.localidad = localidad;
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
	
    public boolean isOcupado() { 
    	return estaOcupado; 
    	}
    
    public void setOcupado(boolean ocupado) {
    	this.estaOcupado = ocupado; 
    	}
	
	public void liberarAsiento() {
		
		if (!this.estaOcupado) {
			throw new IllegalStateException("El asiento" + this.numero + "está libre"); 
		}
		
		this.estaOcupado = false;
	}
		
		
		
	}
	
	
