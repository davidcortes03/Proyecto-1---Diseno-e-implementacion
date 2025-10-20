package usuarios;

import modelo.Tiquete;
import persistencia.PersistenciaUsuarios;
import transacciones.Transaccion;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un usuario natural dentro del sistema BoletaMaster.
 * 
 * Administra el saldo virtual, historial de tiquetes y transacciones realizadas,
 * manteniendo actualizada la información en persistencia.
 * 
 * @version 1.0
 */
public class UsuarioNatural extends Cliente {

    protected double saldoVirtual;
    protected List<Tiquete> tiquetes;
    protected double totalGastado;
    protected List<Transaccion> transacciones;
    private String totalTiquetes;

    /**
     * Crea un nuevo usuario natural con los datos básicos.
     * 
     * @param nombreUsuario Nombre de usuario
     * @param usuarioEmail Correo electrónico del usuario
     * @param contraseña Contraseña de acceso
     * @param saldoVirtual Saldo inicial del usuario
     * @param totalTiquetes Cantidad total de tiquetes adquiridos
     * @param totalGastado Total gastado inicialmente
     */
    public UsuarioNatural(String nombreUsuario, String usuarioEmail, String contraseña,
            double saldoVirtual, String totalTiquetes, double totalGastado) {
        super(nombreUsuario, usuarioEmail, contraseña);
        this.saldoVirtual = saldoVirtual;
        this.totalTiquetes = totalTiquetes;
        this.totalGastado = totalGastado;
        this.tiquetes = new ArrayList<>();
        this.transacciones = new ArrayList<>();
        this.tipo = "UsuarioNatural";
    }

    public double getSaldoVirtual() {
        return saldoVirtual;
    }

    public String getTotalTiquetes() {
        return totalTiquetes;
    }

    public double getTotalGastado() {
        return totalGastado;
    }

    public List<Tiquete> getTiquetes() {
        return tiquetes;
    }

    /**
     * Añade saldo al usuario y actualiza la persistencia.
     * 
     * @param monto Monto a añadir (debe ser positivo)
     * @return {@code true} si la operación fue exitosa, {@code false} si el monto es inválido
     */
    public boolean añadirSaldo(double monto) {
        if (monto <= 0) {
            return false;
        }
        saldoVirtual += monto;
        PersistenciaUsuarios.guardarUsuarios();
        return true;
    }

    /**
     * Descuenta saldo del usuario, si tiene suficiente disponible.
     * 
     * @param monto Monto a descontar
     * @return {@code true} si el descuento fue exitoso, {@code false} si no tiene saldo suficiente o el monto es inválido
     */
    public boolean descontarSaldo(double monto) {
        if (monto <= 0 || monto > saldoVirtual) {
            return false;
        }
        saldoVirtual -= monto;
        totalGastado += monto;
        PersistenciaUsuarios.guardarUsuarios();
        return true;
    }

    /**
     * Agrega un tiquete al historial del usuario.
     * 
     * @param t Objeto {@link Tiquete} a agregar
     * @throws IllegalArgumentException si el tiquete es {@code null}
     */
    public void agregarTiquete(Tiquete t) {
        if (t == null) {
            throw new IllegalArgumentException("El tiquete no puede ser nulo");
        }
        tiquetes.add(t);
        PersistenciaUsuarios.guardarUsuarios();
    }

    /**
     * Registra una transacción asociada al usuario.
     * 
     * @param t Objeto {@link Transaccion} a agregar
     * @throws IllegalArgumentException si la transacción es {@code null}
     */
    public void agregarTransaccion(Transaccion t) {
        if (t == null) {
            throw new IllegalArgumentException("La transacción no puede ser nula");
        }
        transacciones.add(t);
        PersistenciaUsuarios.guardarUsuarios();
    }

    /**
     * Calcula el total gastado en tiquetes por el usuario.
     * 
     * @return Total acumulado de dinero gastado
     */
    public double calcularTotalGastado() {
        double total = 0;
        for (Tiquete t : tiquetes) {
            total += t.getPrecio();
        }
        totalGastado = total;
        PersistenciaUsuarios.guardarUsuarios();
        return totalGastado;
    }

    @Override
    public String getTipoCliente() {
        return "UsuarioNatural";
    }
}