package persistencia;

import java.io.*;
import java.util.*;
import modelo.*;

/**
 * Clase que gestiona la persistencia de los objetos de tipo {@link Venues}.
 * Su función es cargar y guardar la información de los venues desde y hacia
 * el archivo de texto plano <b>venues.txt</b>, manteniendo los datos sincronizados
 * con la lista en memoria.
 *
 * Cada línea del archivo representa un venue con sus datos separados por punto y coma (;).
 *
 * @version 1.0
 */
public class PersistenciaVenue {

    private static final String RUTA_ARCHIVO = "data/venues.txt";
    private static final List<Venues> venues = new ArrayList<>();

    // Bloque estático que carga los venues al iniciar el programa
    static {
        cargarVenues();
    }

    /**
     * Carga los venues almacenados en el archivo <b>venues.txt</b>.
     * Si el archivo no existe, simplemente muestra un mensaje y lo creará
     * automáticamente la próxima vez que se guarde un venue.
     *
     * Cada línea del archivo debe tener el siguiente formato:
     * <pre>
     * nombre;ubicacion;capacidadMaxima;restriccionesUso;aprobado
     * </pre>
     */
    public static void cargarVenues() {
        venues.clear();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("⚠ No existe el archivo de venues. Se creará automáticamente al guardar.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea = lector.readLine();

            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(";");

                    // Se valida que la línea tenga todos los campos esperados
                    if (partes.length >= 5) {
                        String nombre = partes[0];
                        String ubicacion = partes[1];
                        int capacidad = Integer.parseInt(partes[2]);
                        String restricciones = partes[3];
                        boolean aprobado = Boolean.parseBoolean(partes[4]);

                        Venues v = new Venues(nombre, ubicacion, capacidad, restricciones, aprobado);
                        venues.add(v);
                    }
                }
                linea = lector.readLine();
            }

            System.out.println("✅ Venues cargados correctamente: " + venues.size());

        } catch (IOException e) {
            System.out.println("⚠ Error al leer venues: " + e.getMessage());
        }
    }

    /**
     * Guarda todos los venues que están en memoria dentro del archivo de texto.
     * Si el archivo no existe, se crea automáticamente.
     *
     * Cada venue se guarda en una línea con sus datos separados por punto y coma (;).
     */
    public static void guardarVenues() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Venues v : venues) {
                String linea = v.getNombre() + ";" +
                               v.getUbicacion() + ";" +
                               v.getCapacidadMaxima() + ";" +
                               v.getRestriccionesUso() + ";" +
                               v.isAprobado();
                escritor.write(linea);
                escritor.newLine();
            }
            System.out.println("💾 Datos de venues guardados correctamente.");
        } catch (IOException e) {
            System.out.println("⚠ Error al guardar venues: " + e.getMessage());
        }
    }

    /**
     * Devuelve la lista completa de venues cargados en memoria.
     * Esta lista se mantiene actualizada con la información del archivo.
     *
     * @return Lista de objetos {@link Venues} actualmente en memoria.
     */
    public static List<Venues> getVenues() {
        return venues;
    }

    /**
     * Agrega un nuevo venue a la lista si no está repetido.
     * Antes de agregarlo, se verifica que no exista otro venue
     * con el mismo nombre (ignorando mayúsculas o minúsculas).
     * 
     * Si el venue se agrega correctamente, el archivo de persistencia se actualiza.
     *
     * @param nuevo Objeto {@link Venues} que se desea registrar.
     */
    public static void agregarVenue(Venues nuevo) {
        for (Venues v : venues) {
            if (v.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                System.out.println("⚠ Venue duplicado, no se agregó: " + nuevo.getNombre());
                return;
            }
        }

        venues.add(nuevo);
        guardarVenues();
        System.out.println("🟢 Venue agregado correctamente: " + nuevo.getNombre());
    }

    /**
     * Marca un venue como aprobado y guarda los cambios en el archivo.
     * Esto permite identificar qué venues ya fueron validados por la administración.
     *
     * @param v Objeto {@link Venues} que será marcado como aprobado.
     */
    public static void aprobarVenue(Venues v) {
        v.setAprobado(true);
        guardarVenues();
        System.out.println("✅ Venue aprobado: " + v.getNombre());
    }
}
