package TPF;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Menu {
    Scanner scan;
    Aerotaxi aerotaxi;
    Usuario usuario;

    public Menu(Aerotaxi aerotaxi) {
        this.scan = new Scanner(System.in);
        this.aerotaxi = aerotaxi;
        this.usuario = null;
    }

    public void menuPrincipal() {
        int op = 0;

        do {
            clearScreen();
            ingresoRegistro();
            while (true) {
                clearScreen();
                imprimirTitulo();
                System.out.println("\n**************** Bienvenido a AEROTAXI " + usuario.getNombre() + " " + usuario.getApellido() + " ****************");
                imprimirOpcMenuPrincipal();
                try {
                    op = scan.nextInt();
                    switch (op) {
                        case 1:
                            clearScreen();
                            imprimirOpcMenuContratar();
                            int opcion = scan.nextInt();
                            if (opcion != 0)
                                ingresoDatosVuelo(opcion);
                            break;
                        case 2:
                            clearScreen();
                            imprimirTitulo();
                            System.out.println("\n");
                            cancelarVuelo(usuario.getDni());
                            break;
                        case 3:
                            clearScreen();
                            imprimirTitulo();
                            aerotaxi.listarVuelosUser(usuario);
                            System.out.println("Presione 'c' para continuar..");
                            String c;
                            do {
                                c = scan.nextLine();
                            } while (!c.equals("c"));
                            clearScreen();
                            break;
                        case 4:
                            clearScreen();
                            imprimirTitulo();
                            System.out.println("\n");
                            aerotaxi.listarVuelosDisponibles(scan);
                            break;
                        case 5:
                            clearScreen();
                            imprimirTitulo();
                            System.out.println("\n");
                            aerotaxi.listarFlota(scan);
                            break;
                        case 0:
                            clearScreen();
                            System.out.println("Gracias por elegir AEROTAXI");
                            break;
                        default:
                            /*System.out.println("Opcion incorrecta. Intente nuevamente");
                            System.out.println("Presione 'c' para continuar..");
                            do {
                                c = scan.nextLine();
                            } while (!c.equals("c"));*/
                            break;
                    }
                } catch (InputMismatchException e) {
                    clearScreen();
                    System.out.println("Ingrese un numero valido\n");
                    scan = new Scanner(System.in);  //limpiar buffer
                }
            }
        } while (op != 0);
    }


    public void ingresoRegistro() {      // ingresar o registrarse para acceder al menu
        int dni = -1;
        imprimirTitulo();
        while (usuario == null) {
            System.out.println("\nBIENVENIDO A AEROTAXI");
            System.out.println("\nPor favor, ingrese su DNI:");
            try {
                dni = scan.nextInt();
                usuario = this.aerotaxi.buscarUsuario(dni);
                if (usuario == null) {
                    clearScreen();
                    imprimirTitulo();
                    System.out.println("\nUsuario no registrado. Registrese:");
                    usuario = registrarUsuario();
                }
            } catch (InputMismatchException e) {
                clearScreen();
                imprimirTitulo();
                System.out.println("\nIngrese un numero valido");
                scan.nextLine(); //limpiar buffer
            }
        }
    }


    public int ingresoDatosVuelo(int opcion) {
        LocalDate fechaBuscada;
        TipoVuelo tipoElegido;
        int numVuelo = 0;
        int cantAcompañantes = 0;
        ArrayList<Usuario> acompañantesDelUsuario = new ArrayList<Usuario>();

        clearScreen();
        switch (opcion) {
            case 1: //filtro x origen y destino
                tipoElegido = seleccionarTipoVuelo();  //Elige origen y destino
                if (tipoElegido != null) {
                    clearScreen();
                    imprimirTitulo();
                    System.out.println("\nVuelos " + tipoElegido.getOrigen() + " - " + tipoElegido.getDestino());
                    System.out.println("\nIngrese la cantidad de acompañantes:");
                    cantAcompañantes = scan.nextInt();
                    if (aerotaxi.existenVuelos(tipoElegido, cantAcompañantes + 1)) { //validar q existan vuelos con esas caract
                        if (cantAcompañantes != 0) {
                            clearScreen();
                            imprimirTitulo();
                            System.out.println("\nIngrese los datos de sus acompañantes: ");
                            acompañantesDelUsuario = registrarAcompañantes(cantAcompañantes);
                        }
                        clearScreen();
                        imprimirTitulo();
                        System.out.println("\n");
                        aerotaxi.listarAvionesPorRecorrido(tipoElegido, cantAcompañantes + 1);
                        System.out.println("\nSeleccione el numero de vuelo:");
                        numVuelo = scan.nextInt();
                    } else {
                        System.out.println("No hay vuelos para esa ruta");
                        System.out.println("Presione 'c' para continuar..");
                        String c;
                        do {
                            c = scan.nextLine();
                        } while (!c.equals("c"));
                    }
                }
                break;
            case 2: //filtro x fecha
                fechaBuscada = datosFechaDelVuelo();
                if (fechaBuscada != null) {
                    System.out.println("Fecha: " + fechaBuscada);
                    System.out.println("Cantidad de acompañantes:");
                    cantAcompañantes = scan.nextInt();
                    if (aerotaxi.existenVuelos(fechaBuscada, cantAcompañantes + 1)) { //validar q existan vuelos
                        if (cantAcompañantes != 0) {
                            System.out.println("Ingrese los datos de sus acompañantes: ");
                            acompañantesDelUsuario = registrarAcompañantes(cantAcompañantes);
                        }
                        clearScreen();
                        imprimirTitulo();
                        System.out.println("\n");
                        aerotaxi.listarAvionesPorFecha(fechaBuscada, cantAcompañantes + 1);
                        System.out.println("Seleccione el numero de vuelo:");
                        numVuelo = scan.nextInt();
                    } else {
                        System.out.println("No hay vuelos para esa ruta");
                        System.out.println("Presione 'c' para continuar..");
                        String c;
                        do {
                            c = scan.nextLine();
                        } while (!c.equals("c"));
                    }
                }
                break;
            default:
                System.out.println("Opcion incorrecta. Intente nuevamente");
                break;
        }

        //HABRÍA QUE MODULARIZAR ESTO EN METODOS
        //chequear asientos disponibles
        if (numVuelo != 0) {
            Vuelo aux = aerotaxi.getVuelos().get(aerotaxi.getIndexVuelo(numVuelo)); //Busco el vuelo elegido
            if ((aux.getCantPasajeros() + cantAcompañantes + 1) <= aux.getAvion().getCapacidadMaxPasajeros()) { //Si hay lugar para el usuario y sus acompañantes en el vuelo
                for (Usuario aAgregar : acompañantesDelUsuario) {
                    if (!aerotaxi.isPasajero(aux.getPasajeros(), aAgregar.getDni())) {    //valido que el acompañante ya no exista como pasajero del vuelo
                        aerotaxi.getVuelos().get(aerotaxi.getIndexVuelo(numVuelo)).agregarPasajero(aAgregar); //agrega el acompañante al vuelo
                        aAgregar.agregarVuelo(aux);       //Agrega el vuelo a la lista de vuelos del acompañante
                    } else
                        System.out.println("Su acompañante ya está registrado en ese vuelo");
                }
                if (!aerotaxi.isPasajero(aux.getPasajeros(), usuario.getDni())) {   //Valido que el usuario ya no exista como pasajeros del vuelo
                    aerotaxi.getVuelos().get(aerotaxi.getIndexVuelo(numVuelo)).agregarPasajero(usuario); //agrega el usuario al vuelo
                    usuario.agregarVuelo(aux);       //Agrega el vuelo a la lista de vuelos del usuario
                } else
                    System.out.println("Ud ya está registrado en ese vuelo");
            } else
                System.out.println("No hay esos asientos disponibles");

            System.out.println("Presione 'c' para continuar..");
            String c;
            do {
                c = scan.nextLine();
            } while (!c.equals("c"));
        }
        return numVuelo;
    }

    public LocalDate datosFechaDelVuelo() {
        LocalDate elegida = null;
        LocalDate fechaActual = LocalDate.now();
        imprimirTitulo();
        System.out.println("\nFECHA ACTUAL: " + fechaActual);
        System.out.println("\nIngrese los datos de la fecha en la cual desea realizar el vuelo: ");
        System.out.println("Año: ");
        int año = scan.nextInt();
        if (año > 2019 && año < 2100) {
            System.out.println("Mes: ");
            int mes = scan.nextInt();
            if (mes > 0 && mes < 13) {
                System.out.println("Día: ");
                int dia = scan.nextInt();
                if ((dia > 0) && (dia < 32)) {
                    LocalDate aux = LocalDate.of(año, mes, dia);
                    if (aux.isAfter(fechaActual))
                        elegida = aux;
                    else
                        System.out.println("Eligió una fecha vieja");
                } else
                    System.out.println("Día inválido");
            } else
                System.out.println("Mes inválido");
        } else
            System.out.println("Año inválido");

        if (elegida == null) {
            System.out.println("Presione una 'c' para continuar..");
            String c;
            do {
                c = scan.nextLine();
            } while (!c.equals("c"));
        }
        return elegida;  //Retorna la fecha elegida o null si se ingresó una fecha inválida
    }

    public TipoVuelo seleccionarTipoVuelo() {  //Metodo para elegir origen y destino
        TipoVuelo rta = null;
        int origenElegido = 0;
        int destinoElegido = 0;

        do {
            imprimirTitulo();
            System.out.println("\n************* ORIGEN *************\n");
            imprimirCiudades();
            origenElegido = scan.nextInt();
            if (origenElegido < 1 || origenElegido > 4) {
                clearScreen();
                System.out.println("Opcion incorrecta. Elija un origen de las opciones");
            }
        } while (origenElegido < 1 || origenElegido > 4);
        do {
            clearScreen();
            imprimirTitulo();
            System.out.println("\n************* DESTINO *************\n");
            imprimirCiudades();
            destinoElegido = scan.nextInt();
            if (destinoElegido < 1 || destinoElegido > 4) {
                clearScreen();
                System.out.println("Opcion incorrecta. Elija un destino de las opciones");
            }
        } while (destinoElegido < 1 || destinoElegido > 4);

        if (origenElegido != destinoElegido) {    //valido que no se haya elegido el mismo origen y destino
            String origen = elegirCiudad(origenElegido);
            String destino = elegirCiudad(destinoElegido);
            for (TipoVuelo tipo : TipoVuelo.values()) {
                if (origen.equals(tipo.getOrigen()) && (destino.equals(tipo.getDestino())))
                    rta = tipo;           //Convierto los datos en un TipoVuelo (enum)
            }
        } else
            rta = null; //anular vuelo si coincide origen y destino
        return rta; //Retorna el tipo de vuelo elegido(enum) o null si no existe un vuelo con ese origen y destino
    }

    public void cancelarVuelo(int dni) {
        aerotaxi.listarVuelosUser(usuario);     //Muestro los vuelos
        if (usuario.getVuelos().size() > 0) {     //Valido que haya vuelos para cancelar
            System.out.println("\n-- No se cancelaran los pasajes de sus acompañantes --");
            System.out.println("Ingrese el numero de vuelo a cancelar: ");
            int cancelar = scan.nextInt();
            if ((usuario.getIndexVuelo(cancelar) <= usuario.getVuelos().size()) && (aerotaxi.getIndexVuelo(cancelar) >= 0)) {  //Valido que el numero elegido coincida con un vuelo
                usuario.darDeBajaVuelo(cancelar);          //Se elimina el vuelo de la lista de vuelos del pasajero
                aerotaxi.getVuelos().get(aerotaxi.getIndexVuelo(cancelar)).quitarPasajero(dni); //Se quita el pasajero de la lista de pasajeros del vuelo
            }
        } else {
            String c;
            System.out.println("Presione 'c' para continuar");
            do {
                c = scan.nextLine();
            } while (!c.equals("c"));
        }

    }

    public Usuario registrarUsuario() {
        scan.nextLine(); //limpiar buffer
        System.out.println("Nombre: ");
        String nombre = scan.nextLine();
        System.out.println("Apellido: ");
        String apellido = scan.nextLine();
        System.out.println("DNI: ");
        int dni = scan.nextInt();
        System.out.println("Edad: ");
        int edad = scan.nextInt();

        Usuario nuevo = new Usuario(nombre, apellido, dni, edad);
        aerotaxi.addUsuario(nuevo);

        return nuevo;
    }

    public ArrayList<Usuario> registrarAcompañantes(int cantAcompañantes) { //Registra todos los acompañantes
        ArrayList<Usuario> acompañantes = new ArrayList<Usuario>();
        int aux = 1;
        while (cantAcompañantes != 0) {
            System.out.println("Acompañante " + aux);
            Usuario nuevo = registrarUsuario();
            acompañantes.add(nuevo);
            clearScreen();
            cantAcompañantes--;
            aux++;
        }
        return acompañantes;   //Registra como usuario a cada acompañante y retorna la lista de los acompañantes del usuario
    }


    public void imprimirOpcMenuPrincipal() {
        System.out.println("\n1. Contratar vuelo");
        System.out.println("2. Cancelar vuelo");
        System.out.println("3. Ver mi lista de vuelos");
        System.out.println("4. Listar vuelos disponibles de AEROTAXI");
        System.out.println("5. Listar aviones disponibles de AEROTAXI");
        System.out.println("0. Salir");
        System.out.println("\nElija una opción:");
    }

    public void imprimirOpcMenuContratar() {
        imprimirTitulo();
        System.out.println("\n************* MENU USUARIO *************\n");
        System.out.println("1. Contratar vuelo por origen/destino");
        System.out.println("2. Contratar vuelo por fecha");
        System.out.println("0. Volver al menu anterior");
    }


    public void imprimirCiudades() {
        System.out.println("1. Buenos Aires");
        System.out.println("2. Córdoba");
        System.out.println("3. Santiago");
        System.out.println("4. Montevideo");

        System.out.println("\nIngrese una opcion: ");
    }

    public void imprimirTitulo() {
        System.out.println("      _       ________  _______      ___    _________     _       ____  ____  _____  ");
        System.out.println("     / \\     |_   __  ||_   __ \\   .'   `. |  _   _  |   / \\     |_  _||_  _||_   _| ");
        System.out.println("    / _ \\      | |_ \\_|  | |__) | /  .-.  \\|_/ | | \\_|  / _ \\      \\ \\  / /    | |   ");
        System.out.println("   / ___ \\     |  _| _   |  __ /  | |   | |    | |     / ___ \\      > `' <     | |   ");
        System.out.println(" _/ /   \\ \\_  _| |__/ | _| |  \\ \\_\\  `-'  /   _| |_  _/ /   \\ \\_  _/ /'`\\ \\_  _| |_  ");
        System.out.println("|____| |____||________||____| |___|`.___.'   |_____||____| |____||____||____||_____| ");
    }


    public String elegirCiudad(int elegido) {
        String ciudad = "";
        switch (elegido) {
            case 1:
                ciudad = "Buenos Aires";
                break;
            case 2:
                ciudad = "Córdoba";
                break;
            case 3:
                ciudad = "Santiago";
                break;
            case 4:
                ciudad = "Montevideo";
                break;
            default:
                ciudad = null;
                break;
        }
        return ciudad;
    }

    public void clearScreen() {
        for (int i = 0; i < 80 * 300; i++)
            System.out.println("\b");
    }
}


