package persistencia;

import java.io.*;
import java.util.*;
import modelo.*;

/**
 * Clase encargada de la persistencia de los objetos {@link Evento}.
 * Administra la carga, almacenamiento y actualización de los eventos registrados
 * en el sistema, manteniendo la sincronización entre el archivo plano y la memoria.
 * 
 * Cada evento se representa como una línea dentro del archivo <b>eventos.txt</b>,
 * con los atributos principales separados por punto y coma (;).
 * 
 * Estructura del archivo:
 * <pre>
 * nombre;fecha;tipo;info;ganancias
 * </pre>
 * 
 * @version 1.0
 */
public class PersistenciaEvento {

    /** Ruta del archivo donde se guardan los eventos */
    private static final String RUTA_ARCHIVO = "data/eventos.txt";

    /** Lista principal de eventos cargados en memoria */
    private static final List<Evento> eventos = new ArrayList<>();

    // =========================================================
    // 1️⃣ INICIALIZACIÓN AUTOMÁTICA
    // =========================================================

    /**
     * Bloque estático que se ejecuta una única vez al cargar la clase.
     * Carga automáticamente los eventos almacenados previamente.
     */
    static {
        cargarEventos();
    }

    // =========================================================
    // 2️⃣ CARGA DE EVENTOS DESDE ARCHIVO
    // =========================================================

    /**
     * Carga todos los eventos desde el archivo <b>eventos.txt</b>.
     * Si el archivo no existe, se creará automáticamente al guardar nuevos eventos.
     *
     * Cada línea debe tener el formato:
     * <pre>
     * nombre;fecha;tipo;información;ganancias
     * </pre>
     *
     * Ejemplo:
     * <pre>
     * Concierto Coldplay;2025-08-14 20:00;Música;Gira mundial;5000000.0
     * </pre>
     */
    public static void cargarEventos() {
        eventos.clear();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("⚠️ No existe el archivo de eventos. Se creará automáticamente al guardar.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea = lector.readLine();

            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(";");

                    // Validación de formato
                    if (partes.length >= 5) {
                        String nombre = partes[0];
                        String fecha = partes[1];
                        String tipo = partes[2];
                        String info = partes[3];
                        double ganancias = Double.parseDouble(partes[4]);

                        // Se crea un objeto Evento básico sin relaciones (venue, organizador)
                        Evento e = new Evento(nombre, fecha, tipo, info, ganancias, null, null);
                        eventos.add(e);
                    }
                }
                linea = lector.readLine();
            }

            System.out.println("✅ Eventos cargados correctamente: " + eventos.size());

        } catch (IOException e) {
            System.out.println("⚠️ Error al leer eventos: " + e.getMessage());
        }
    }

    // =========================================================
    // 3️⃣ GUARDADO DE EVENTOS EN ARCHIVO
    // =========================================================

    /**
     * Guarda todos los eventos actuales en el archivo <b>eventos.txt</b>.
     * 
     * El archivo se sobrescribe completamente para reflejar la lista actual
     * en memoria. Cada evento se guarda en una línea con el formato:
     * <pre>
     * nombre;fecha;tipo;información;ganancias
     * </pre>
     */
    public static void guardarEventos() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {

            for (Evento e : eventos) {
                String linea = e.getNombreEvento() + ";" +
                               e.getFechaHora() + ";" +
                               e.getTipoEvento() + ";" +
                               e.getInfoEvento() + ";" +
                               e.getGananciasEvento();

                escritor.write(linea);
                escritor.newLine();
            }

            System.out.println("💾 Eventos guardados correctamente.");

        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar eventos: " + e.getMessage());
        }
    }

    // =========================================================
    // 4️⃣ ACCESO A LOS EVENTOS EN MEMORIA
    // =========================================================

    /**
     * Devuelve la lista de todos los eventos cargados en memoria.
     * 
     * @return Lista viva de eventos
     */
    public static List<Evento> getEventos() {
        return eventos;
    }

    // =========================================================
    // 5️⃣ AGREGAR NUEVO EVENTO
    // =========================================================

    /**
     * Agrega un nuevo evento al sistema si no existe otro con el mismo nombre y fecha.
     * En caso de éxito, el archivo se actualiza automáticamente.
     *
     * @param nuevo Evento a registrar
     */
    public static void agregarEvento(Evento nuevo) {
        for (Evento e : eventos) {
            if (e.getNombreEvento().equalsIgnoreCase(nuevo.getNombreEvento()) &&
                e.getFechaHora().equalsIgnoreCase(nuevo.getFechaHora())) {
                System.out.println("⚠️ El evento ya existe, no se agregó.");
                return;
            }
        }

        eventos.add(nuevo);
        guardarEventos();
        System.out.println("🟢 Evento agregado: " + nuevo.getNombreEvento());
    }

    // =========================================================
    // 6️⃣ CANCELAR EVENTO
    // =========================================================

    /**
     * Marca un evento como cancelado, actualizando su estado interno y guardando los cambios.
     * 
     * @param e Evento a cancelar
     */
    public static void cancelarEvento(Evento e) {
        e.setEstado(false);
        guardarEventos();
        System.out.println("🚫 Evento cancelado: " + e.getNombreEvento());
    }
}
