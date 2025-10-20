package persistencia;

import modelo.Tiquete;
import modelo.Evento;
import modelo.Localidad;
import usuarios.Cliente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Maneja la persistencia de tiquetes en archivos de texto plano.
 * Proporciona métodos para guardar y cargar tiquetes desde/hacia archivos de texto.
 * 
 * @version 1.0
 */
public class PersistenciaTiquete {
    private static final String ARCHIVO_TIQUETES = "data/tiquetes.txt";
    private static final String SEPARADOR = "|";
    
    /**
     * Guarda una lista de tiquetes en el archivo de persistencia.
     * Crea el directorio si no existe y escribe los tiquetes en formato texto.
     * 
     * @param tiquetes Lista de tiquetes a guardar en el archivo
     * @throws IOException si ocurre un error durante la escritura del archivo
     */
    public static void guardarTiquetes(List<Tiquete> tiquetes) {
        try {
            new File("data").mkdirs();
            
            PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_TIQUETES));
            writer.println("# Formato: ID|Precio|CargoPorcentual|FechaHora|EsCortesia|TipoTiquete|EventoId|LocalidadId|ClienteId");
            
            for (Tiquete tiquete : tiquetes) {
                String linea = construirLineaTiquete(tiquete);
                writer.println(linea);
            }
            
            writer.close();
            System.out.println("Tiquetes guardados: " + tiquetes.size());
            
        } catch (IOException e) {
            System.err.println("Error guardando tiquetes: " + e.getMessage());
        }
    }
    
    /**
     * Construye una línea de texto con los datos de un tiquete para almacenamiento.
     * 
     * @param tiquete Tiquete a serializar en formato texto
     * @return String con los datos del tiquete separados por el separador definido
     */
    private static String construirLineaTiquete(Tiquete tiquete) {
        return String.join(SEPARADOR,
            tiquete.getId(),
            String.valueOf(tiquete.getPrecio()),
            String.valueOf(tiquete.getCargoPorcentual()),
            tiquete.getFechaHora(),
            String.valueOf(tiquete.getEsCortesia()),
            getTipoTiquete(tiquete),
            getEventoId(tiquete),
            getLocalidadId(tiquete),
            getClienteId(tiquete)
        );
    }
    
    /**
     * Obtiene el tipo de tiquete usando reflexión para acceder al campo protegido.
     * 
     * @param t Tiquete del cual obtener el tipo
     * @return Tipo de tiquete como String, o "Normal" si no se puede acceder al campo
     */
    private static String getTipoTiquete(Tiquete t) {
        try {
            java.lang.reflect.Field field = Tiquete.class.getDeclaredField("tipoTiquete");
            field.setAccessible(true);
            return (String) field.get(t);
        } catch (Exception e) {
            return "Normal";
        }
    }
    
    /**
     * Obtiene un identificador único para el evento asociado al tiquete.
     * 
     * @param t Tiquete del cual obtener el ID del evento
     * @return String con el ID del evento o "NULL" si no existe
     */
    private static String getEventoId(Tiquete t) {
        try {
            java.lang.reflect.Field field = Tiquete.class.getDeclaredField("evento");
            field.setAccessible(true);
            Evento evento = (Evento) field.get(t);
            return evento != null ? "EVENTO_" + evento.hashCode() : "NULL";
        } catch (Exception e) {
            return "NULL";
        }
    }
    
    /**
     * Obtiene un identificador único para la localidad asociada al tiquete.
     * 
     * @param t Tiquete del cual obtener el ID de la localidad
     * @return String con el ID de la localidad o "NULL" si no existe
     */
    private static String getLocalidadId(Tiquete t) {
        try {
            java.lang.reflect.Field field = Tiquete.class.getDeclaredField("localidad");
            field.setAccessible(true);
            Localidad localidad = (Localidad) field.get(t);
            return localidad != null ? "LOCALIDAD_" + localidad.hashCode() : "NULL";
        } catch (Exception e) {
            return "NULL";
        }
    }
    
    /**
     * Obtiene un identificador único para el cliente asociado al tiquete.
     * 
     * @param t Tiquete del cual obtener el ID del cliente
     * @return String con el ID del cliente o "NULL" si no existe
     */
    private static String getClienteId(Tiquete t) {
        try {
            java.lang.reflect.Field field = Tiquete.class.getDeclaredField("cliente");
            field.setAccessible(true);
            Cliente cliente = (Cliente) field.get(t);
            return cliente != null ? "CLIENTE_" + cliente.hashCode() : "NULL";
        } catch (Exception e) {
            return "NULL";
        }
    }
    
    /**
     * Carga todos los tiquetes almacenados en el archivo de persistencia.
     * Si el archivo no existe, retorna una lista vacía.
     * 
     * @return Lista de tiquetes cargados desde el archivo
     * @throws IOException si ocurre un error durante la lectura del archivo
     */
    public static List<Tiquete> cargarTiquetes() {
        List<Tiquete> tiquetes = new ArrayList<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TIQUETES));
            String linea;
            
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) {
                    continue;
                }
                
                Tiquete tiquete = parsearLineaTiquete(linea);
                if (tiquete != null) {
                    tiquetes.add(tiquete);
                }
            }
            
            reader.close();
            System.out.println("Tiquetes cargados: " + tiquetes.size());
            
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró archivo de tiquetes. Se creará uno nuevo.");
        } catch (IOException e) {
            System.err.println("Error cargando tiquetes: " + e.getMessage());
        }
        
        return tiquetes;
    }
    
    /**
     * Convierte una línea de texto en un objeto Tiquete.
     * 
     * @param linea Línea de texto a parsear
     * @return Objeto Tiquete creado a partir de la línea, o null si la línea está mal formateada
     * @throws NumberFormatException si los campos numéricos no tienen formato válido
     */
    private static Tiquete parsearLineaTiquete(String linea) {
        try {
            String[] partes = linea.split("\\" + SEPARADOR);
            
            if (partes.length < 9) {
                System.err.println("Línea inválida: " + linea);
                return null;
            }
            
            String id = partes[0];
            double precio = Double.parseDouble(partes[1]);
            double cargoPorcentual = Double.parseDouble(partes[2]);
            String fechaHora = partes[3];
            boolean esCortesia = Boolean.parseBoolean(partes[4]);
            String tipoTiquete = partes[5];
            
            Evento eventoTemp = new Evento();
            Localidad localidadTemp = new Localidad();
            Cliente clienteTemp = new Cliente();
            
            Tiquete tiquete = new Tiquete(
                precio, cargoPorcentual, 0, fechaHora, id, esCortesia,
                eventoTemp, localidadTemp, clienteTemp, tipoTiquete
            );
            
            return tiquete;
            
        } catch (Exception e) {
            System.err.println("Error parseando tiquete: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Muestra el contenido completo del archivo de tiquetes en la consola.
     */
    public static void mostrarArchivoTiquetes() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_TIQUETES));
            String linea;
            System.out.println("\n=== CONTENIDO DEL ARCHIVO TIQUETES ===");
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No hay archivo de tiquetes para mostrar");
        }
    }
}