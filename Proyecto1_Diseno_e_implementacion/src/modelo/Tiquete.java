package modelo;

import modelo.Evento;
import usuarios.Cliente;
import modelo.Localidad;

/**
 * Representa un tiquete para eventos con precio, cargos y funcionalidades de transferencia.
 * Gestiona el cálculo de precios finales y determina la transferibilidad según reglas de negocio.
 * 

 * @version 1.0
 */
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
    
    /**
     * Crea un nuevo tiquete con todos los atributos requeridos.
     * 
     * @param precio Precio base del tiquete sin cargos adicionales
     * @param cargoPorcentual Porcentaje de cargo por servicio aplicado al precio base
     * @param cargoEmision Cargo fijo por emisión/impresión del tiquete
     * @param fechaHora Fecha y hora del evento en formato string
     * @param id Identificador único del tiquete
     * @param esCortesia Indica si el tiquete es de cortesía (gratuito)
     * @param evento Evento al que pertenece el tiquete
     * @param localidad Localidad específica del evento
     * @param cliente Cliente dueño del tiquete
     * @param tipoTiquete Tipo de tiquete (Normal, Cortesia, Deluxe, Multiple)
     * @throws IllegalArgumentException si precio, cargoPorcentual o cargoEmision son negativos
     */
    public Tiquete(double precio, double cargoPorcentual, double cargoEmision, String fechaHora, 
                   String id, boolean esCortesia, Evento evento, Localidad localidad, 
                   Cliente cliente, String tipoTiquete) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (cargoPorcentual < 0) {
            throw new IllegalArgumentException("El cargo porcentual no puede ser negativo");
        }
        if (cargoEmision < 0) {
            throw new IllegalArgumentException("El cargo de emisión no puede ser negativo");
        }
        
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
    
    /**
     * Calcula el precio final del tiquete incluyendo cargos porcentuales y fijos.
     * Para tiquetes de cortesía retorna 0.0 independientemente de los cargos.
     * 
     * @return Precio final calculado como: precio + (precio * cargoPorcentual/100) + cargoEmision
     */
    public double calcularPrecioFinal() {
        if (esCortesia) {
            return 0.0;
        }
        
        double cargo = precio * (cargoPorcentual/100);
        return precio + cargo + cargoEmision;
    }
    
    /**
     * Determina si el tiquete puede ser transferido a otro usuario según reglas de negocio.
     * Los tiquetes de cortesía y tipo Deluxe no son transferibles.
     * Los tiquetes múltiples y normales son transferibles.
     * 
     * @return true si el tiquete es transferible, false en caso contrario
     */
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
    
    /**
     * Genera una representación en formato String con la información completa del tiquete.
     * Incluye ID, evento, localidad, cliente, fecha, precio final y estado de cortesía.
     * 
     * @return String formateado con la información completa del tiquete
     */
    public String getInformación() {
        StringBuilder info = new StringBuilder();
        info.append("Id: ").append(id);
        info.append(" | Evento: ").append(evento.getNombreEvento());
        info.append(" | Localidad: ").append(localidad.getNombre());
        info.append(" | Cliente: ").append(cliente);
        info.append(" | Fecha: ").append(fechaHora);
        info.append(" | Precio Final: ").append(calcularPrecioFinal());
        if (esCortesia) {
            info.append(" | Cortesía");
        }
        
        return info.toString();
    }
    
   
    public double getPrecio() { return this.precio; }
    public double getCargoPorcentual() { return this.cargoPorcentual; }
    public String getFechaHora() { return this.fechaHora; }
    public String getId() { return this.id; }
    public boolean getEsCortesia() { return this.esCortesia; }
    
    public void setPrecio(double precio) {
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        this.precio = precio;
    }
    
    public void setCargoPorcentual(double cargoPorcentual) {
        if (cargoPorcentual < 0) throw new IllegalArgumentException("El cargo no puede ser negativo");
        this.cargoPorcentual = cargoPorcentual;
    }
    
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public void setEsCortesia(boolean esCortesia) { this.esCortesia = esCortesia; }
}