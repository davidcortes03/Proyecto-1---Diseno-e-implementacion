package usuarios;

import persistencia.PersistenciaUsuarios;
import java.util.List;

/**
 * Clase abstracta que representa a un cliente dentro del sistema Boletamaster.
 * Contiene la información base de cualquier tipo de cliente (natural, corporativo, etc.)
 * y permite validar la unicidad de nombres de usuario durante actualizaciones.
 *
 * @version 1.0
 */
public abstract class Cliente {
	
	protected String nombreUsuario;
	protected String usuarioEmail;
	protected String contraseña;

	/**
	 * Crea un cliente con los datos básicos de registro en el sistema.
	 * 
	 * @param nombreUsuario Nombre único que identifica al cliente dentro de la plataforma.
	 * @param usuarioEmail Correo electrónico del cliente, usado para notificaciones o login.
	 * @param contraseña Contraseña de acceso asociada al usuario.
	 */
	public Cliente(String nombreUsuario, String usuarioEmail, String contraseña) {
		this.nombreUsuario = nombreUsuario;
		this.usuarioEmail = usuarioEmail;
		this.contraseña = contraseña;
	}
	
	/**
	 * Devuelve el nombre de usuario actual del cliente.
	 * 
	 * @return Nombre de usuario del cliente.
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	/**
	 * Devuelve el correo electrónico registrado del cliente.
	 * 
	 * @return Correo electrónico del cliente.
	 */
	public String getUsuarioEmail() {
		return usuarioEmail;
	}
	
	/**
	 * Devuelve la contraseña asociada a la cuenta del cliente.
	 * 
	 * @return Contraseña del cliente.
	 */
	public String getContraseña() {
		return contraseña;
	}
	
	/**
	 * Intenta cambiar el nombre de usuario por uno nuevo,
	 * verificando primero que no esté repetido en la lista
	 * de clientes persistidos en el sistema.
	 * 
	 * Si el nombre ya existe, muestra un mensaje por consola
	 * y no realiza el cambio.
	 * 
	 * @param nuevoNombre Nuevo nombre de usuario a asignar.
	 */
	public void setNombreUsuario(String nuevoNombre) {
		List<Cliente> lista = PersistenciaUsuarios.getClientes();

		for (Cliente c : lista) {
			if (c.getNombreUsuario().equalsIgnoreCase(nuevoNombre)) {
				System.out.println("El nombre '" + nuevoNombre + "' ya existe. Intenta con otro.");
				return;
			}
		}
		
		this.nombreUsuario = nuevoNombre;
		System.out.println("✅ Nombre de usuario actualizado correctamente a: " + nuevoNombre);
	}
}
