package persistencia;

import java.io.*;
import java.util.*;
import modelo.*;
import usuarios.Cliente;

/**
 * Clase encargada de manejar la persistencia de los usuarios en el sistema.
 * Administra la carga, almacenamiento y actualización de objetos que extienden la clase {@link Cliente}.
 *
 * Toda la información se guarda en el archivo <b>usuarios.txt</b> dentro de la carpeta <i>data/</i>,
 * donde cada línea representa un usuario con sus atributos separados por punto y coma (;).
 *
 * Esta clase garantiza que los datos de los usuarios se mantengan sincronizados
 * entre la memoria del programa y el archivo de texto.
 *
 * @version 1.0
 */
public class PersistenciaUsuarios {

    private static final String RUTA_ARCHIVO = "data/usuarios.txt";

    /** Lista principal de clientes cargados desde el archivo */
    private static final List<Cliente> clientes = new ArrayList<>();

    // =========================================================
    // 1️⃣ Inicialización automática
    // =========================================================
    static {
        // Este bloque se ejecuta una sola vez cuando la clase es cargada
        cargarUsuarios();
    }

    // =========================================================
    // 2️⃣ CARGA DE USUARIOS DESDE ARCHIVO
    // =========================================================

    /**
     * Carga todos los usuarios almacenados en el archivo <b>usuarios.txt</b>.
     * Si el archivo no existe, se muestra un mensaje y el archivo será creado
     * automáticamente la próxima vez que se guarden usuarios.
     *
     * Cada línea del archivo debe seguir el formato:
     * <pre>
     * tipo;nombre;email;contraseña;saldo;gastado
     * </pre>
     *
     * Donde:
     * - tipo puede ser "ClienteNatural", "Organizador" o "Administrador".
     * - Los últimos dos campos solo aplican para los clientes naturales.
     */
    public static void cargarUsuarios() {

        clientes.clear();
        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("⚠️ No existe el archivo de usuarios. Se creará automáticamente al guardar.");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {

            String linea = lector.readLine();
            while (linea != null) {

                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(";");

                    if (partes.length >= 4) {
                        String tipo = partes[0];
                        String nombre = partes[1];
                        String email = partes[2];
                        String contrasena = partes[3];

                        // Dependiendo del tipo, se crea el objeto correspondiente
                        if (tipo.equals("ClienteNatural") && partes.length >= 6) {
                            double saldo = Double.parseDouble(partes[4]);
                            double gastado = Double.parseDouble(partes[5]);
                            clientes.add(new ClienteNatural(nombre, email, contrasena, saldo, gastado));

                        } else if (tipo.equals("Organizador")) {
                            clientes.add(new Organizador(nombre, email, contrasena));

                        } else if (tipo.equals("Administrador")) {
                            clientes.add(new Administrador(nombre, email, contrasena, 0));
                        }
                    }
                }

                linea = lector.readLine();
            }

            System.out.println("✅ Usuarios cargados correctamente: " + clientes.size());

        } catch (IOException e) {
            System.out.println("⚠️ Error al leer el archivo de usuarios: " + e.getMessage());
        }
    }

    // =========================================================
    // 3️⃣ GUARDADO DE USUARIOS EN ARCHIVO
    // =========================================================

    /**
     * Guarda todos los usuarios actualmente almacenados en memoria dentro del
     * archivo <b>usuarios.txt</b>. Si el archivo no existe, se crea automáticamente.
     *
     * Cada usuario se escribe en una línea de texto según su tipo:
     * <ul>
     *   <li><b>ClienteNatural</b>: tipo;nombre;email;contraseña;saldo;gastado</li>
     *   <li><b>Organizador</b>: tipo;nombre;email;contraseña;0;0</li>
     *   <li><b>Administrador</b>: tipo;nombre;email;contraseña;0;0</li>
     * </ul>
     */
    public static void guardarUsuarios() {

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {

            for (Cliente c : clientes) {
                String linea = c.getTipoCliente() + ";" +
                        c.getNombreUsuario() + ";" +
                        c.getUsuarioEmail() + ";" +
                        c.getContraseña();

                // Se añaden campos específicos dependiendo del tipo
                if (c instanceof ClienteNatural) {
                    ClienteNatural cn = (ClienteNatural) c;
                    linea = linea + ";" + cn.getSaldoVirtual() + ";" + cn.getTotalGastado();
                } else {
                    linea = linea + ";0;0";
                }

                escritor.write(linea);
                escritor.newLine();
            }

            System.out.println("💾 Usuarios guardados correctamente.");

        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar usuarios: " + e.getMessage());
        }
    }

    // =========================================================
    // 4️⃣ AGREGAR NUEVO CLIENTE
    // =========================================================

    /**
     * Agrega un nuevo cliente a la lista de usuarios en memoria, siempre y cuando
     * no exista otro con el mismo nombre o correo electrónico.
     *
     * Si el usuario es agregado con éxito, se actualiza el archivo de persistencia.
     *
     * @param nuevo Nuevo cliente a registrar
     */
    public static void agregarCliente(Cliente nuevo) {

        boolean existe = false;

        for (Cliente c : clientes) {
            if (c.getNombreUsuario().equalsIgnoreCase(nuevo.getNombreUsuario())
                    || c.getUsuarioEmail().equalsIgnoreCase(nuevo.getUsuarioEmail())) {
                existe = true;
            }
        }

        if (!existe) {
            clientes.add(nuevo);
            guardarUsuarios();
            System.out.println("🟢 Cliente agregado correctamente: " + nuevo.getNombreUsuario());
        } else {
            System.out.println("⚠️ Cliente duplicado, no se agregó.");
        }
    }

    // =========================================================
    // 5️⃣ MÉTODOS DE ACCESO
    // =========================================================

    /**
     * Devuelve la lista de usuarios cargados en memoria.
     * Es una referencia viva, por lo que cualquier modificación
     * afecta directamente a la lista interna.
     *
     * @return Lista de clientes cargados
     */
    public static List<Cliente> getClientes() {
        return clientes;
    }

    /**
     * Busca un cliente dentro de la lista en memoria según su nombre de usuario.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param nombre Nombre de usuario a buscar
     * @return El cliente encontrado o null si no existe
     */
    public static Cliente buscarPorNombre(String nombre) {
        Cliente encontrado = null;
        for (Cliente c : clientes) {
            if (c.getNombreUsuario().equalsIgnoreCase(nombre)) {
                encontrado = c;
            }
        }
        return encontrado;
    }
}
