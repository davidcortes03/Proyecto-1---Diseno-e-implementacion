package usuarios;

import persistencia.PersistenciaUsuarios;
import java.util.List;

/**
 * Clase abstracta que representa un cliente en el sistema de venta de tiquetes.
 * Define la estructura base y comportamiento común para todos los tipos de clientes.
 * Las clases concretas deben extender esta clase para implementar funcionalidades específicas.
 * 
 * @version 1.0
 */
public abstract class Cliente {
	
	protected String nombreUsuario;
	protected String usuarioEmail;
	protected String contraseña;

	/**
	 * Constructor base para la creación de clientes en el sistema.
	 * 
	 * @param nombreUsuario Nombre único de identificación del cliente en el sistema
	 * @param usuarioEmail Dirección de correo electrónico del cliente
	 * @param contraseña Contraseña de acceso del cliente al sistema
	 * @throws IllegalArgumentException si nombreUsuario, usuarioEmail o contraseña son nulos o vacíos
	 */
	public Cliente(String nombreUsuario, String usuarioEmail, String contraseña) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
		}
		if (usuarioEmail == null || usuarioEmail.trim().isEmpty()) {
			throw new IllegalArgumentException("El email del usuario no puede ser nulo o vacío");
		}
		if (contraseña == null || contraseña.trim().isEmpty()) {
			throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
		}
		
		this.nombreUsuario = nombreUsuario;
		this.usuarioEmail = usuarioEmail;
		this.contraseña = contraseña;
	}
	
	/**
	 * Obtiene el nombre de usuario único del cliente.
	 * 
	 * @return Nombre de usuario del cliente
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	/**
	 * Obtiene la dirección de correo electrónico del cliente.
	 * 
	 * @return Email del cliente
	 */
	public String getUsuarioEmail() {
		return usuarioEmail;
	}
	
	/**
	 * Obtiene la contraseña de acceso del cliente.
	 * 
	 * @return Contraseña del cliente
	 */
	public String getContraseña() {
		return contraseña;
	}
	
	/**
	 * Establece un nuevo nombre de usuario para el cliente, validando que no exista otro cliente con el mismo nombre.
	 * 
	 * @param nuevoNombre Nuevo nombre de usuario a asignar
	 * @throws IllegalArgumentException si el nuevo nombre es nulo, vacío o ya existe en el sistema
	 */
	public void setNombreUsuario(String nuevoNombre) {
		if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nuevo nombre no puede ser nulo o vacío");
		}
		
		List<Cliente> lista = PersistenciaUsuarios.getClientes();

        for (Cliente c : lista) {
            if (c.getNombreUsuario().equalsIgnoreCase(nuevoNombre)) {
                throw new IllegalArgumentException("El nombre '" + nuevoNombre + "' ya existe en el sistema");
            }
        }
        
        this.nombreUsuario = nuevoNombre;
	}
	
	/**
	 * Valida las credenciales de acceso del cliente.
	 * 
	 * @param usuario Nombre de usuario a validar
	 * @param contraseña Contraseña a validar
	 * @return true si las credenciales son correctas, false en caso contrario
	 */
	public boolean validarCredenciales(String usuario, String contraseña) {
		return this.nombreUsuario.equals(usuario) && this.contraseña.equals(contraseña);
	}
	
	/**
	 * Método abstracto para obtener el tipo específico de cliente.
	 * Debe ser implementado por las clases hijas.
	 * 
	 * @return String que representa el tipo de cliente
	 */
	public abstract String getTipoCliente();
	
	/**
	 * Método abstracto para realizar la compra de tiquetes.
	 * Debe ser implementado por las clases hijas según las reglas específicas de cada tipo de cliente.
	 * 
	 * @param tiquetes Lista de tiquetes a comprar
	 * @return true si la compra se realiza exitosamente, false en caso contrario
	 */
	public abstract boolean comprarTiquetes(List<modelo.Tiquete> tiquetes);
}