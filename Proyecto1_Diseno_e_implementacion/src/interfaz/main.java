public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=====  SISTEMA DE EVENTOS - INICIO =====");

        // Cargar persistencias
        PersistenciaUsuarios.cargarUsuarios();
        PersistenciaEvento.cargarEventos();
        PersistenciaVenue.cargarVenues();

        List<Tiquete> tiquetes = PersistenciaTiquete.cargarTiquetes();
        List<Transacciones> transacciones = new PersistenciaTransacciones().cargarTransacciones();

        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Crear usuario");
            System.out.println("2. Crear evento (Organizador)");
            System.out.println("3. Comprar tiquete (Cliente)");
            System.out.println("4. Cancelar evento (Administrador)");
            System.out.println("5. Mostrar reportes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> crearEvento();
                case 3 -> comprarTiquete(tiquetes, transacciones);
                case 4 -> cancelarEvento();
                case 5 -> mostrarReportes(tiquetes, transacciones);
                case 0 -> {
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                }
                default -> System.out.println(" Opción inválida");
            }
        }

        // Guardar los cambios en persistencia
        PersistenciaUsuarios.guardarUsuarios();
        PersistenciaEvento.guardarEventos();
        PersistenciaTiquete.guardarTiquetes(tiquetes);
        new PersistenciaTransacciones().guardarTransacciones(transacciones);

        System.out.println("=====  SISTEMA FINALIZADO =====");
    }

    private static void crearUsuario() {
        System.out.print("Ingrese nombre de usuario: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese email: ");
        String email = sc.nextLine();
        System.out.print("Ingrese contraseña: ");
        String pass = sc.nextLine();
        System.out.print("Tipo de usuario (1=ClienteNatural, 2=Organizador, 3=Administrador): ");
        int tipo = sc.nextInt();
        sc.nextLine();

        Cliente nuevo;
        switch (tipo) {
            case 1 -> nuevo = new UsuarioNatural(nombre, email, pass, 1000.0, "UsuarioNatural", 0.0);
            case 2 -> nuevo = new Organizador(nombre, email, pass, 0.0, "Organizador", 0.0);
            case 3 -> nuevo = new Administrador(nombre, email, pass, 0);
            default -> {
                System.out.println(" Tipo no válido.");
                return;
            }
        }

        PersistenciaUsuarios.agregarCliente(nuevo);
        System.out.println(" Usuario creado correctamente: " + nombre);
    }

    private static void crearEvento() {
        System.out.print("Ingrese nombre del organizador: ");
        String nombreOrg = sc.nextLine();
        Organizador org = (Organizador) PersistenciaUsuarios.buscarPorNombre(nombreOrg);

        if (org == null) {
            System.out.println(" Organizador no encontrado.");
            return;
        }

        System.out.print("Nombre del evento: ");
        String nombreEvento = sc.nextLine();
        System.out.print("Fecha y hora (YYYY-MM-DD HH:mm): ");
        String fecha = sc.nextLine();
        System.out.print("Tipo de evento: ");
        String tipo = sc.nextLine();
        System.out.print("Descripción: ");
        String info = sc.nextLine();

        // Buscar o crear venue
        System.out.print("Nombre del venue: ");
        String nombreVenue = sc.nextLine();
        Venue venue = new Venue(nombreVenue, 5000, "Sin restricciones", "Ubicación Centro");
        PersistenciaVenue.agregarVenue(venue);

        Evento evento = new Evento(nombreEvento, fecha, tipo, info, 0.0, venue, org);
        PersistenciaEvento.agregarEvento(evento);

        System.out.println(" Evento creado correctamente.");
    }

    private static void comprarTiquete(List<Tiquete> tiquetes, List<Transacciones> transacciones) {
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = sc.nextLine();
        UsuarioNatural cliente = (UsuarioNatural) PersistenciaUsuarios.buscarPorNombre(nombre);

        if (cliente == null) {
            System.out.println("⚠ Cliente no encontrado.");
            return;
        }

        System.out.print("Ingrese nombre del evento: ");
        String eventoNombre = sc.nextLine();
        Evento evento = null;
        for (Evento e : PersistenciaEvento.getEventos()) {
            if (e.getNombreEvento().equalsIgnoreCase(eventoNombre)) {
                evento = e;
                break;
            }
        }

        if (evento == null) {
            System.out.println(" Evento no encontrado.");
            return;
        }

        // Crear tiquete
        Tiquete t = new Tiquete("TQ" + (tiquetes.size() + 1), 200.0, 10.0, evento.getFechaHora(), false,
                "Normal", evento, null, cliente);

        tiquetes.add(t);
        cliente.agregarTiquete(t);
        PersistenciaTiquete.guardarTiquetes(tiquetes);

        // Registrar transacción
        Transacciones tx = new Transacciones("TX" + (transacciones.size() + 1),
                new Date().toString(), cliente, t.getPrecio());
        transacciones.add(tx);
        new PersistenciaTransacciones().guardarTransacciones(transacciones);

        System.out.println("Tiquete comprado exitosamente para el evento " + evento.getNombreEvento());
    }

    private static void cancelarEvento() {
        System.out.print("Ingrese nombre del evento a cancelar: ");
        String nombre = sc.nextLine();

        Evento evento = null;
        for (Evento e : PersistenciaEvento.getEventos()) {
            if (e.getNombreEvento().equalsIgnoreCase(nombre)) {
                evento = e;
                break;
            }
        }

        if (evento == null) {
            System.out.println("Evento no encontrado.");
            return;
        }

        Administrador admin = new Administrador("admin", "admin@mail.com", "123", 0);
        admin.aprobarCancelacionEvento(evento);

        System.out.println(" Evento cancelado correctamente.");
    }