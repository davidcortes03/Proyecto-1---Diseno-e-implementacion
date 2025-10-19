package persistencia;

import java.io.*;
import java.util.*;
import modelo.*;

/**
 * Clase encargada de manejar la carga y guardado de los venues en el archivo plano.
 * Cada línea del archivo representa un venue separado por ';'.
 */
public class PersistenciaVenue {

    private static final String RUTA_ARCHIVO = "data/venues.txt";
    private static final List<Venues> venues = new ArrayList<>();

    static {
        cargarVenues();
    }

    /**
     * Carga los venues almacenados en el archivo "venues.txt".
     * Si el archivo no existe, se creará al guardar.
     */
    public static void cargarVenues() {
        venues.clear();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("No existe el archivo de venues. Se creará al guardar.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea = lector.readLine();

            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(";");
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

            System.out.println("Venues cargados: " + venues.size());

        } catch (IOException e) {
            System.out.println("Error al leer venues: " + e.getMessage());
        }
    }

    /**
     * Guarda todos los venues en el archivo de texto.
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
        } catch (IOException e) {
            System.out.println("Error al guardar venues: " + e.getMessage());
        }
    }

    /**
     * Devuelve la lista de venues cargados en memoria.
     */
    public static List<Venues> getVenues() {
        return venues;
    }

    /**
     * Agrega un nuevo venue a la lista (si no está repetido) y actualiza el archivo.
     */
    public static void agregarVenue(Venues nuevo) {
        for (Venues v : venues) {
            if (v.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                System.out.println("Venue duplicado, no se agregó.");
                return;
            }
        }

        venues.add(nuevo);
        guardarVenues();
        System.out.println("Venue agregado: " + nuevo.getNombre());
    }

    /**
     * Marca un venue como aprobado y guarda los cambios.
     */
    public static void aprobarVenue(Venues v) {
        v.setAprobado(true);
        guardarVenues();
        System.out.println("Venue aprobado: " + v.getNombre());
    }
}
