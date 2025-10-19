package persistencia;
import java.io.*;
import java.util.*;
import modelo.*;

public class PersistenciaUsuarios {

    private static final String RUTA_ARCHIVO = "data/usuarios.txt";

    // 🧠 Lista única y viva de clientes cargados
    private static final List<Cliente> clientes = new ArrayList<>();

    // =========================================================
    // 1️⃣ Inicialización automática (bloque estático)
    // =========================================================
    static {
        cargarUsuarios(); // se ejecuta una sola vez al cargar la clase
    }

    // =========================================================
    // 2️⃣ CARGAR USUARIOS DESDE EL ARCHIVO
    // =========================================================
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

            System.out.println("✅ Usuarios cargados: " + clientes.size());

        } catch (IOException e) {
            System.out.println("⚠️ Error al leer el archivo: " + e.getMessage());
        }
    }

    // =========================================================
    // 3️⃣ GUARDAR TODOS LOS USUARIOS EN EL ARCHIVO
    // =========================================================
    public static void guardarUsuarios() {

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {

            for (Cliente c : clientes) {
                String linea = c.getTipoCliente() + ";" +
                        c.getNombreUsuario() + ";" +
                        c.getUsuarioEmail() + ";" +
                        c.getContraseña();

                if (c instanceof ClienteNatural) {
                    ClienteNatural cn = (ClienteNatural) c;
                    linea = linea + ";" + cn.getSaldoVirtual() + ";" + cn.getTotalGastado();
                } else {
                    linea = linea + ";0;0";
                }

                escritor.write(linea);
                escritor.newLine();
            }

        } catch (IOException e) {
            System.out.println("⚠️ Error al guardar usuarios: " + e.getMessage());
        }
    }

    // =========================================================
    // 4️⃣ AGREGAR CLIENTE NUEVO (PERSISTENTE)
    // =========================================================
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
            System.out.println("🟢 Cliente agregado: " + nuevo.getNombreUsuario());
        } else {
            System.out.println("⚠️ Cliente duplicado, no se agregó.");
        }
    }

    // =========================================================
    // 5️⃣ MÉTODOS DE ACCESO A LA LISTA EN MEMORIA
    // =========================================================
    public static List<Cliente> getClientes() {
        return clientes; // devuelve la lista viva
    }

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
