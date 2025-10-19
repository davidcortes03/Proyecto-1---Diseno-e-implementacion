package usuarios;

import persistencia.PersistenciaUsuarios;
import java.util.List;

public abstract class Cliente {
	
	protected String nombreUsuario;
	protected String usuarioEmail;
	protected String contraseña;

	public Cliente(String nombreUsuario, String usuarioEmail, String contraseña) {
		
		this.nombreUsuario = nombreUsuario;
		this.usuarioEmail = usuarioEmail;
		this.contraseña = contraseña;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	public String getUsuarioEmail() {
		return usuarioEmail;
	}
	
	public String getContraseña() {
		return contraseña;
	}
	
	public void setNombreUsuario(String nuevoNombre) {
		List<Cliente> lista = PersistenciaUsuarios.getClientes();

        for (Cliente c : lista) {
            if (c.getNombreUsuario().equalsIgnoreCase(nuevoNombre)) {
                System.out.println("⚠️ El nombre '" + nuevoNombre + "' ya existe.");
                
            }
        }
	}
}
