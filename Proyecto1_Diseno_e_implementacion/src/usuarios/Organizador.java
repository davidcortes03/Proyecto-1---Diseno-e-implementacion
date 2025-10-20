package usuarios;
import java.util.ArrayList;
import java.util.List;

import modelo.Evento;
import modelo.Venue;
import persistencia.PersistenciaEvento;
import persistencia.PersistenciaVenue;

/**
 * Representa un organizador de eventos en el sistema Boletamaster.
 * Hereda de UsuarioNatural y añade funcionalidades para crear y gestionar eventos,
 * proponer venues, y analizar estadísticas de ventas.
 * Los organizadores pueden crear eventos, gestionar localidades y aplicar descuentos.
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Organizador extends UsuarioNatural {

    private List<Evento> eventosCreados;

    /**
     * Crea un nuevo organizador con la información básica requerida.
     * 
     * @param nombreUsuario Nombre único de identificación del organizador
     * @param usuarioEmail Correo electrónico del organizador
     * @param contraseña Contraseña de acceso del organizador
     * @param saldoVirtual Saldo virtual inicial del organizador
     * @param totalTiquetes Total de tiquetes comprados históricamente
     * @param totalGastado Total gastado en compras de tiquetes
     * @throws IllegalArgumentException si nombreUsuario, usuarioEmail o contraseña son nulos o vacíos
     */
    public Organizador(String nombreUsuario, String usuarioEmail, String contraseña,
                       double saldoVirtual, String totalTiquetes, double totalGastado) {
        super(nombreUsuario, usuarioEmail, contraseña, saldoVirtual, totalTiquetes, totalGastado);
        this.eventosCreados = new ArrayList<>();
        this.tipo = "Organizador";
    }

    /**
     * Crea un nuevo evento asociado a este organizador y lo guarda en persistencia.
     * 
     * @param nombreEvento Nombre descriptivo del evento
     * @param fechaHora Fecha y hora en que se realizará el evento
     * @param tipoEvento Tipo de evento (Musical, Cultural, Deportivo, Religioso)
     * @param infoEvento Información adicional o descripción del evento
     * @param numeroLocalidadesDisponibles Número de localidades disponibles inicialmente
     * @param venue Venue donde se realizará el evento
     * @return El objeto Evento creado, o null si los parámetros son inválidos
     * @throws IllegalArgumentException si nombreEvento, fechaHora, tipoEvento, infoEvento o venue son nulos
     */
    public Evento crearEvento(String nombreEvento, String fechaHora, String tipoEvento,
                              String infoEvento, int numeroLocalidadesDisponibles, Venue venue) {
        if (nombreEvento == null || nombreEvento.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del evento no puede ser nulo o vacío");
        }
        if (fechaHora == null || fechaHora.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser nulas o vacías");
        }
        if (tipoEvento == null || tipoEvento.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de evento no puede ser nulo o vacío");
        }
        if (infoEvento == null || infoEvento.trim().isEmpty()) {
            throw new IllegalArgumentException("La información del evento no puede ser nula o vacía");
        }
        if (venue == null) {
            throw new IllegalArgumentException("El venue no puede ser nulo");
        }
        if (numeroLocalidadesDisponibles <= 0) {
            throw new IllegalArgumentException("El número de localidades debe ser positivo");
        }

        Evento nuevo = new Evento(nombreEvento, fechaHora, tipoEvento, infoEvento, 0, venue, this);
        eventosCreados.add(nuevo);
        PersistenciaEvento.agregarEvento(nuevo);
        return nuevo;
    }

    /**
     * Actualiza la información de un evento existente creado por este organizador.
     * 
     * @param evento Evento a actualizar
     * @param nuevaInfo Nueva información o descripción del evento
     * @param nuevoTipo Nuevo tipo de evento
     * @return true si la actualización fue exitosa, false si el evento es nulo o no pertenece al organizador
     * @throws IllegalArgumentException si nuevaInfo o nuevoTipo son nulos o vacíos
     */
    public boolean actualizarEvento(Evento evento, String nuevaInfo, String nuevoTipo) {
        if (evento == null || !eventosCreados.contains(evento)) {
            return false;
        }

        if (nuevaInfo != null && !nuevaInfo.isEmpty()) {
            evento.setInfoEvento(nuevaInfo);
        }
        if (nuevoTipo != null && !nuevoTipo.isEmpty()) {
            evento.setTipoEvento(nuevoTipo);
        }

        PersistenciaEvento.guardarEventos();
        return true;
    }

    /**
     * Cancela un evento activo creado por este organizador y actualiza la persistencia.
     * 
     * @param evento Evento a cancelar
     * @return true si la cancelación fue exitosa, false si el evento es nulo o no pertenece al organizador
     */
    public boolean cancelacionEvento(Evento evento) {
        if (evento == null || !eventosCreados.contains(evento)) return false;

        evento.cancelarEvento();
        eventosCreados.remove(evento);
        PersistenciaEvento.guardarEventos();
        return true;
    }

    /**
     * Propone un nuevo venue a la administración para su aprobación.
     * 
     * @param ubicacion Dirección física o descripción de la ubicación del venue
     * @param capacidadMaxima Número máximo de personas que puede albergar el venue
     * @param restriccionesDeUso Restricciones o condiciones especiales de uso
     * @param nombreVenue Nombre identificador del venue
     * @return El objeto Venue creado, o null si los datos son inválidos
     * @throws IllegalArgumentException si ubicacion o nombreVenue son nulos o vacíos, o si capacidadMaxima es negativa
     */
    public Venue proponerVenue(String ubicacion, int capacidadMaxima,
                               String restriccionesDeUso, String nombreVenue) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede ser nula o vacía");
        }
        if (nombreVenue == null || nombreVenue.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del venue no puede ser nulo o vacío");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser positiva");
        }

        Venue nuevo = new Venue(ubicacion, capacidadMaxima, restriccionesDeUso, nombreVenue);
        PersistenciaVenue.agregarVenue(nuevo);
        return nuevo;
    }

    // =========================================================
    // 🔹 ESTADÍSTICAS Y DESCUENTOS
    // =========================================================

    /**
     * Calcula el porcentaje de venta de un concierto basado en capacidad y tiquetes vendidos.
     * 
     * @param capacidadMax Capacidad máxima del evento o localidad
     * @param tiquetesVendidos Número de tiquetes vendidos
     * @return Porcentaje de venta (0-100), 0 si la capacidad es inválida
     * @throws IllegalArgumentException si capacidadMax es negativa o tiquetesVendidos es negativa
     */
    public double porcentajeVentaConcierto(int capacidadMax, int tiquetesVendidos) {
        if (capacidadMax <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser positiva");
        }
        if (tiquetesVendidos < 0) {
            throw new IllegalArgumentException("El número de tiquetes vendidos no puede ser negativo");
        }
        return ((double) tiquetesVendidos / capacidadMax) * 100;
    }

    /**
     * Crea un descuento temporal sobre los tiquetes del evento.
     * 
     * @param fechaFinOferta Fecha de finalización de la oferta en formato string
     * @param porcentajeDescuento Porcentaje de descuento a aplicar (0-100)
     * @return Porcentaje de descuento aplicado, 0 si el descuento es inválido
     * @throws IllegalArgumentException si porcentajeDescuento está fuera del rango válido
     */
    public double crearDescuento(String fechaFinOferta, double porcentajeDescuento) {
        if (porcentajeDescuento <= 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        if (fechaFinOferta == null || fechaFinOferta.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de fin de oferta no puede ser nula o vacía");
        }
        
        System.out.println("💸 Descuento del " + porcentajeDescuento + "% hasta " + fechaFinOferta);
        return porcentajeDescuento;
    }

    /**
     * Obtiene la lista de eventos creados por este organizador.
     * 
     * @return Lista de eventos creados por el organizador
     */
    public List<Evento> getEventosCreados() {
        return new ArrayList<>(eventosCreados); // Retorna copia para proteger la lista original
    }

    /**
     * Obtiene el tipo específico de cliente (organizador).
     * 
     * @return String "Organizador"
     */
    @Override
    public String getTipoCliente() {
        return "Organizador";
    }
    
    /**
     * Calcula las ganancias totales de todos los eventos creados por este organizador.
     * 
     * @return Total de ganancias generadas por los eventos del organizador
     */
    public double calcularGananciasTotales() {
        double total = 0;
        for (Evento evento : eventosCreados) {
            total += evento.getGananciasEvento();
        }
        return total;
    }
    
    /**
     * Obtiene el número total de eventos creados por este organizador.
     * 
     * @return Número de eventos creados
     */
    public int getTotalEventosCreados() {
        return eventosCreados.size();
    }
    
    /**
     * Obtiene el número total de eventos activos creados por este organizador.
     * 
     * @return Número de eventos activos
     */
    public int getTotalEventosActivos() {
        int count = 0;
        for (Evento evento : eventosCreados) {
            if (evento.getEstado()) {
                count++;
            }
        }
        return count;
    }
}