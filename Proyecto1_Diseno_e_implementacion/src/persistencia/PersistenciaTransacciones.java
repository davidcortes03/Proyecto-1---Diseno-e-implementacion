package persistencia;
import java.io.*;
import java.util.*;
import transacciones.Transacciones;
import usuarios.Cliente;


public class PersistenciaTransacciones {
	
	private static final String ARCHIVO = "data/transacciones.txt";
	
	public void guardarTransacciones(List<Transacciones> transacciones) {
	// Se crea el directorio si no existe
		
	File directorio = new File("data");
	if (!directorio.exists()) {
		directorio.mkdirs();
	}
	
	// Se abre el archivo para escritura
	try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO))){
		
		
		// Escribir cada transacción en el archivo
		for (Transacciones transaccion :  transacciones) {
			String linea = transaccion.getIdTransaccion() + "," +
							transaccion.getFechaHora() + "," +
							transaccion.getCliente().getNombreUsuario() + "," +
							transaccion.getPrecioTotal();
			writer.println(linea);
			
			}
		
	} catch (IOException e) {
		System.err.println("Error guardando: " + e.getMessage());
	}
		
	}
	
	public List<Transacciones> cargarTransacciones() {
		List<Transacciones> transacciones = new ArrayList<>();
		File archivo = new File(ARCHIVO);
		
		// Verificar si archivo existe
		if (!archivo.exists() ) {
			return transacciones;
		}
		
		// Leer archivo linea por linea
		try (BufferedReader reader = new BufferedReader(new FileReader(archivo))){
			String linea;
			while ((linea = reader.readLine()) !=  null) {
				Transacciones transaccion = parsearLinea(linea, null);
				if (transaccion != null) {
					transacciones.add(transaccion);
				}
			}
		} catch (IOException e) {
			System.err.println("Error cargando: " + e.getMessage());
		}
		
		return transacciones;
	}
	
	private Transacciones parsearLinea(String linea, List<Cliente> usuariosExistentes) {
		try {
			//Dividir linea por comas
			String[] partes = linea.split(",");
			
			// Se crea objeto
			Transacciones transaccion = new Transacciones(
					Double.parseDouble(partes[3]), // precioTotal
					partes[1], //fechaHora
					partes[0], //idTransaccion
					partes[4], //metodoPago
					partes[5], // tipo
					buscarCliente(partes[2], usuariosExistentes) // cliente
			);
			return transaccion;
			
		} catch (Exception e) {
			System.err.println("Error parseando línea: " + linea);
            return null;
		}
	}
	
	
	
	//Buscar usuario por nombre en la lista existente
	
	private Cliente buscarCliente (String nombreUsuario, List<Cliente> clientes) {
		
		for (Cliente cliente : clientes) {
			if (cliente.getNombreUsuario().equals(nombreUsuario)) {
				return cliente;
			}
		} 
		
		System.err.println("Usuario no encontrado: " + nombreUsuario);
        return null;
	}

}
