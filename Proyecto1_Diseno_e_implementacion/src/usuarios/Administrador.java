package usuarios;

import modelo.Venues;
import persistencia.PersistenciaEvento;
import persistencia.PersistenciaUsuarios;
import persistencia.PersistenciaVenue;

import java.util.List;

import modelo.Evento;
import modelo.Tiquete;

/**
 * Representa un administrador en el sistema Boletamaster con permisos especiales
 * para gestionar venues, eventos, precios, reembolsos y ganancias del sistema.
 * Hereda de la clase Cliente y añade funcionalidades administrativas.
 * 
 * @version 1.0
 */
public class Administrador extends Cliente {
	
	private int gananciasTiqueteria;
	
	/**
	 * Crea un nuevo administrador con credenciales de acceso y ganancias iniciales.
	 * 
	 * @param nombreUsuario Nombre único de identificación del administrador
	 * @param usuarioEmail Correo electrónico del administrador
	 * @param contraseña Contraseña de acceso del administrador
	 * @param gananciasTiqueteria Ganancias iniciales de la tiquetería
	 * @throws IllegalArgumentException si nombreUsuario, usuarioEmail o contraseña son nulos o vacíos
	 */
	public Administrador(String nombreUsuario, String usuarioEmail, String contraseña, int gananciasTiqueteria) {
        super(nombreUsuario, usuarioEmail, contraseña);
        if (gananciasTiqueteria < 0) {
            throw new IllegalArgumentException("Las ganancias no pueden ser negativas");
        }
        this.gananciasTiqueteria = gananciasTiqueteria;
        this.tipo = "Administrador";
    }

    /**
     * Fija un recargo porcentual sobre todos los tiquetes asociados a un evento.
     * Este método recalcula los precios de los tiquetes, actualiza el evento
     * y guarda los cambios en el archivo de persistencia.
     *
     * @param porcentaje Porcentaje adicional a aplicar (por ejemplo, 10 = +10%)
     * @param e Evento sobre el cual se aplicará el recargo
     * @throws IllegalArgumentException si el evento es nulo o el porcentaje es negativo
     */
    public void fijarPrecioAdicional(double porcentaje, Evento e) {
        if (e == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }
        if (porcentaje < 0) {
            throw new IllegalArgumentException("El porcentaje no puede ser negativo.");
        }

        List<Tiquete> tiquetes = e.getTiquetes();
        if (tiquetes != null) {
            for (Tiquete t : tiquetes) {
                double precioBase = t.getPrecio();
                double nuevoPrecio = precioBase + (precioBase * porcentaje / 100);
                t.setPrecio(nuevoPrecio);
            }
        }

        e.setPrecioAdicional(porcentaje);
        PersistenciaEvento.guardarEventos();
    }

    /**
     * Establece un costo fijo adicional por la impresión o emisión de los tiquetes de un evento.
     * Este valor se suma al precio actual de cada tiquete y se actualiza en la persistencia.
     *
     * @param precioImpresion Valor fijo que se agregará al precio de cada tiquete
     * @param e Evento sobre el cual se aplicará el cambio
     * @throws IllegalArgumentException si el evento es nulo o el valor es negativo
     */
    public void precioFijoImpresion(double precioImpresion, Evento e) {
        if (e == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }
        if (precioImpresion < 0) {
            throw new IllegalArgumentException("El precio fijo no puede ser negativo.");
        }

        List<Tiquete> tiquetes = e.getTiquetes();
        if (tiquetes != null) {
            for (Tiquete t : tiquetes) {
                double nuevoPrecio = t.getPrecio() + precioImpresion;
                t.setPrecio(nuevoPrecio);
            }
        }

        e.setCostoImpresion(precioImpresion);
        PersistenciaEvento.guardarEventos();
    }

    /**
     * Permite que el administrador proponga un nuevo venue para eventos futuros.
     * El venue se almacena en el archivo de persistencia como "PROPUESTO".
     *
     * @param ubicacion Dirección o descripción del lugar
     * @param capacidadMaxima Número máximo de asistentes que puede albergar
     * @param restriccionesDeUso Reglas o limitaciones de uso del lugar
     * @return El objeto Venue creado, o null si los datos son inválidos
     * @throws IllegalArgumentException si la ubicación es nula o vacía, o si la capacidad es negativa
     */
    public Venue proponerVenue(String ubicacion, int capacidadMaxima, String restriccionesDeUso) {
        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede ser nula o vacía");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser positiva");
        }

        Venue nuevo = new Venue(ubicacion, capacidadMaxima, restriccionesDeUso, "PROPUESTO");
        PersistenciaVenue.agregarVenue(nuevo);
        return nuevo;
    }

    /**
     * Acepta una propuesta de venue previamente registrada.
     * Cambia su estado a "ACEPTADO" y guarda los cambios.
     *
     * @param v Venue propuesto
     * @return true si el proceso fue exitoso, false si el venue es nulo
     */
    public boolean aceptarPropuestaVenue(Venue v) {
        if (v == null) {
            System.out.println("⚠ No se puede aceptar un venue nulo.");
            return false;
        }

        v.setEstado("ACEPTADO");
        PersistenciaVenue.guardarVenues();
        return true;
    }

    /**
     * Cancela un evento existente, cambia su estado a inactivo
     * y reembolsa el dinero base a los compradores de tiquetes.
     * 
     * @param e Evento a cancelar
     * @return true si la cancelación fue exitosa, false si el evento ya estaba cancelado o es nulo
     */
    public boolean cancelarEvento(Evento e) {
        if (e == null || !e.getEstado()) {
            return false;
        }

        e.setEstado(false);
        PersistenciaEvento.guardarEventos();

        if (e.getTiquetes() != null) {
            for (Tiquete t : e.getTiquetes()) {
                if (t.isVendido()) {
                    Cliente comprador = PersistenciaUsuarios.buscarPorNombre(t.getComprador());
                    if (comprador instanceof UsuarioNatural) {
                        UsuarioNatural un = (UsuarioNatural) comprador;
                        un.añadirSaldo(t.getPrecio());
                        PersistenciaUsuarios.guardarUsuarios();
                    }
                }
            }
        }

        return true;
    }

    /**
     * Aprueba la cancelación de un evento solicitado por un organizador.
     * Marca el evento como inactivo y actualiza su estado en el archivo de persistencia.
     *
     * @param e Evento a cancelar
     * @return true si la cancelación se realizó correctamente, false si el evento es nulo
     */
    public boolean aprobarCancelacionEvento(Evento e) {
        if (e == null) return false;

        e.setEstado(false);
        PersistenciaEvento.guardarEventos();
        return true;
    }

    /**
     * Calcula las ganancias totales de la tiquetería en una fecha específica.
     * Suma las ganancias de todos los eventos con esa fecha.
     *
     * @param fecha Fecha del evento (formato "dd/MM/yyyy")
     * @return Total de ganancias de esa fecha, 0 si la fecha es nula o vacía
     */
    public double calcularGananciasFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return 0;
        }
        
        double total = 0;
        for (Evento e : PersistenciaEvento.getEventos()) {
            if (e.getFechaHora().equalsIgnoreCase(fecha)) {
                total += e.getGananciasEvento();
            }
        }
        return total;
    }

    /**
     * Calcula las ganancias generadas por un evento específico.
     *
     * @param evento Evento objetivo
     * @return Ganancia total del evento, 0 si el evento es nulo
     */
    public double calcularGananciasEvento(Evento evento) {
        if (evento == null) return 0;
        return evento.getGananciasEvento();
    }

    /**
     * Calcula las ganancias totales de todos los eventos de un organizador específico.
     *
     * @param o Organizador cuyo historial se analizará
     * @return Total de ganancias generadas por sus eventos, 0 si el organizador es nulo
     */
    public double calcularGananciasOrganizador(Organizador o) {
        if (o == null) return 0;

        double total = 0;
        for (Evento e : PersistenciaEvento.getEventos()) {
            if (e.getOrganizador() != null && e.getOrganizador().equalsIgnoreCase(o.getNombreUsuario())) {
                total += e.getGananciasEvento();
            }
        }
        return total;
    }
    
    /**
     * Autoriza un reembolso a un cliente natural, devolviendo el dinero base
     * del tiquete y actualizando los archivos de persistencia correspondientes.
     *
     * @param t Tiquete a reembolsar
     * @param c Cliente al que se le devolverá el dinero
     * @return true si el reembolso fue exitoso, false si el tiquete o cliente son nulos
     */
    public boolean autorizarReembolso(Tiquete t, Cliente c) {
        if (t == null || c == null) return false;

        if (c instanceof UsuarioNatural) {
            UsuarioNatural un = (UsuarioNatural) c;
            un.añadirSaldo(t.getPrecio());
            t.setReembolsado(true);
            PersistenciaUsuarios.guardarUsuarios();
            PersistenciaEvento.guardarEventos();
            return true;
        }

        return false;
    }

    /**
     * Devuelve las ganancias totales de la tiquetería.
     *
     * @return ganancias actuales del sistema
     */
    public int getGananciasTiqueteria() {
        return gananciasTiqueteria;
    }

    /**
     * Establece las nuevas ganancias de la tiquetería y actualiza el archivo de persistencia.
     *
     * @param gananciasTiqueteria nuevas ganancias
     * @throws IllegalArgumentException si las ganancias son negativas
     */
    public void setGananciasTiqueteria(int gananciasTiqueteria) {
        if (gananciasTiqueteria < 0) {
            throw new IllegalArgumentException("Las ganancias no pueden ser negativas");
        }
        this.gananciasTiqueteria = gananciasTiqueteria;
        PersistenciaUsuarios.guardarUsuarios();
    }
    
    /**
     * Obtiene el tipo de cliente (administrador).
     * 
     * @return String "Administrador"
     */
    @Override
    public String getTipoCliente() {
        return "Administrador";
    }
    
    /**
     * Realiza la compra de tiquetes como administrador (no permitido).
     * Los administradores no pueden comprar tiquetes según las reglas del negocio.
     * 
     * @param tiquetes Lista de tiquetes a comprar
     * @return false siempre, ya que los administradores no pueden comprar tiquetes
     */
    @Override
    public boolean comprarTiquetes(List<modelo.Tiquete> tiquetes) {
        System.out.println("⚠ Los administradores no pueden comprar tiquetes.");
        return false;
    }
}