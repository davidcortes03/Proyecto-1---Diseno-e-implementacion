package persistencia;

import java.io.*;
import java.util.*;
import transacciones.Transacciones;
import usuarios.Cliente;

/**
 * Maneja la persistencia de transacciones en archivos de texto plano.
 * Proporciona métodos para guardar y cargar transacciones desde/hacia archivos.
 * 
 * @version 1.0
 */
public class PersistenciaTransacciones {
	
	private static final String ARCHIVO = "data/transacciones.txt";
	
	/**
	 * Guarda una lista de transacciones en el archivo de persistencia.
	 * Crea el directorio si no existe y escribe cada transacción en formato CSV.
	 * 
	 * @param transacciones Lista de transacciones a guardar en el archivo
	 * @throws IOException si ocurre un error durante la escritura del archivo
	 */
	public void guardarTransacciones(List<Transacciones> transacciones) {
		// Se crea el directorio si no existe
		File directorio = new File("data");
		if (!directorio.exists()) {
			directorio.mkdirs();
		}
		
		// Se abre el archivo para escritura
		try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO))){
			
			// Escribir cada transacción en el archivo
			for (Transacciones transaccion : transacciones) {
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
	
	/**
	 * Carga todas las transacciones almacenadas en el archivo de persistencia.
	 * Si el archivo no existe, retorna una lista vacía.
	 * 
	 * @return Lista de transacciones cargadas desde el archivo
	 * @throws IOException si ocurre un error durante la lectura del archivo
	 */
	public List<Transacciones> cargarTransacciones() {
		List<Transacciones> transacciones = new ArrayList<>();
		File archivo = new File(ARCHIVO);
		
		// Verificar si archivo existe
		if (!archivo.exists()) {
			return transacciones;
		}
		
		// Leer archivo linea por linea
		try (BufferedReader reader = new BufferedReader(new FileReader(archivo))){
			String linea;
			while ((linea = reader.readLine()) != null) {
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
	
	/**
	 * Convierte una línea de texto en un objeto Transacciones.
	 * 
	 * @param linea Línea de texto a parsear en formato CSV
	 * @param usuariosExistentes Lista de clientes existentes para buscar referencias
	 * @return Objeto Transacciones creado a partir de la línea, o null si la línea está mal formateada
	 * @throws NumberFormatException si los campos numéricos no tienen formato válido
	 * @throws ArrayIndexOutOfBoundsException si la línea no tiene suficientes campos
	 */
	private Transacciones parsearLinea(String linea, List<Cliente> usuariosExistentes) {
		try {
			// Dividir linea por comas
			String[] partes = linea.split(",");
			
			// Se crea objeto
			Transacciones transaccion = new Transacciones(
					Double.parseDouble(partes[3]), // precioTotal
					partes[1], // fechaHora
					partes[0], // idTransaccion
					partes[4], // metodoPago
					partes[5], // tipo
					buscarCliente(partes[2], usuariosExistentes) // cliente
			);
			return transaccion;
			
		} catch (Exception e) {
			System.err.println("Error parseando línea: " + linea);
			return null;
		}
	}
	
	/**
	 * Busca un cliente por nombre de usuario en la lista de clientes existentes.
	 * 
	 * @param nombreUsuario Nombre de usuario a buscar
	 * @param clientes Lista de clientes donde buscar
	 * @return Cliente encontrado, o null si no se encuentra
	 */
	private Cliente buscarCliente(String nombreUsuario, List<Cliente> clientes) {
		if (clientes == null) {
			System.err.println("Lista de clientes no proporcionada para: " + nombreUsuario);
			return null;
		}
		
		for (Cliente cliente : clientes) {
			if (cliente.getNombreUsuario().equals(nombreUsuario)) {
				return cliente;
			}
		} 
		
		System.err.println("Usuario no encontrado: " + nombreUsuario);
		return null;
	}
}