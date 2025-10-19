package persistencia;

import java.io.*;
import java.util.*;
import modelo.*;

/**
 * Clase encargada de manejar la carga y guardado de los eventos en el archivo plano.
 * Cada línea del archivo representa un evento con sus datos principales separados por ';'.
 */
public class PersistenciaEvento {

    private static final String RUTA_ARCHIVO = "data/eventos.txt";
    private static final List<Evento> eventos = new ArrayList<>();

    // Bloque que carga los eventos automáticamente al iniciar el programa
    static {
        cargarEventos();
    }

    public static void cargarEventos() {
        eventos.clear();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("No existe el archivo de eventos. Se creará al guardar.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea = lector.readLine();

            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(";");
                    if (partes.length >= 5) {
                        String nombre = partes[0];
                        String fecha = partes[1];
                        String tipo = partes[2];
                        String info = partes[3];
                        double ganancias = Double.parseDouble(partes[4]);

                        Evento e = new Evento(nombre, fecha, tipo, info, ganancias, null, null);
                        eventos.add(e);
                    }
                }
                linea = lector.readLine();
            }

            System.out.println("Eventos cargados: " + eventos.size());

        } catch (IOException e) {
            System.out.println("Error al leer eventos: " + e.getMessage());
        }
    }

    /**
     * Guarda todos los eventos en el archivo de texto.
     * Sobrescribe el archivo anterior con la lista actual.
     */
    public static void guardarEventos() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Evento e : eventos) {
                String linea = e.getNombreEvento() + ";" + e.getFechaHora() + ";" +
                        e.getTipoEvento() + ";" + e.getInfoEvento() + ";" +
                        e.getGananciasEvento();
                escritor.write(linea);
                escritor.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar eventos: " + e.getMessage());
        }
    }

    /**
     * Devuelve la lista completa de eventos en memoria.
     */
    public static List<Evento> getEventos() {
        return eventos;
    }

    /**
     * Agrega un nuevo evento a la lista (si no está repetido) y actualiza el archivo.
     */
    public static void agregarEvento(Evento nuevo) {
        for (Evento e : eventos) {
            if (e.getNombreEvento().equalsIgnoreCase(nuevo.getNombreEvento()) &&
                e.getFechaHora().equalsIgnoreCase(nuevo.getFechaHora())) {
                System.out.println("El evento ya existe, no se agregó.");
                return;
            }
        }

        eventos.add(nuevo);
        guardarEventos();
        System.out.println("Evento agregado: " + nuevo.getNombreEvento());
    }

    /**
     * Marca un evento como cancelado y actualiza el archivo.
     */
    public static void cancelarEvento(Evento e) {
        e.setEstado(false);
        guardarEventos();
    }
} 
