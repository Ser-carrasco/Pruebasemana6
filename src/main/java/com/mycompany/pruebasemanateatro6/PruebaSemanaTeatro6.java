/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebasemanateatro6;

/**
 *
 * @author Sergio Carrasco
 */
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class PruebaSemanaTeatro6 {

    static Timer timerReserva = null;// Temporizador para cancelar reserva en caso de no ser confirmada 
    static int reservafila = -1; // Almacena la fila reservada 
    static int reservaasiento = -1;  // Almacena el asiento reservado
    static boolean reservaPendiente = false;  // Indica si hay una reserva

   
    static int filas = 5; // Número total de filas en el teatro
    static int asientos = 10; // Número total de asientos por fila
    static int precioEntrada = 6000; // Precio de cada entrada
    static int entradaVendida = 0; // Contador de entradas vendidas

    //Disponibilidad de asientos ocupe el ejemplo dado en clases como estructura para mostrar asientos
    static boolean[] fila1 = new boolean[asientos];
    static boolean[] fila2 = new boolean[asientos];
    static boolean[] fila3 = new boolean[asientos];
    static boolean[] fila4 = new boolean[asientos];
    static boolean[] fila5 = new boolean[asientos];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        System.out.println("__:::Bienvenidos al Teatro Moro:::__");
        
       // Bucle del menú de opciones
        while (opcion != 0) {
            mostrarMenu(); // Llamar menu 
            opcion = obtenerOpcion(sc);
            //Switch opciones del menu
            switch (opcion) {
                case 1 -> verDisponibilidad();
                case 2 -> reservarAsiento(sc);
                case 3 -> confirmarReserva();
                case 4 -> generarBoleta();
                case 5 -> modificarVenta(sc);
                case 0 -> System.out.println("_:::Hasta luego, Gracias por visitar Teatro Moro:::_"); // Se sale del sistema de venta de entradas.
                default -> System.out.println("Opcion no valida");
            }
        }
        sc.close(); //cerrar scanner 
    }
      
    static void mostrarMenu() { // Muestra el menú principal
        System.out.println("_:::Menu Principal:::_");
        System.out.println("1.Disponibilidad de asientos"); // En esta opcion preferi mostrar los asientos disponibles y asi mostrar reservas para evitar errores de compra
        System.out.println("2.Reservar Entrada");
        System.out.println("3.Confirmar Reserva");
        System.out.println("4.Generar Boleta");
        System.out.println("5.Modificar Venta");
        System.out.println("0.Salir");
        System.out.println("");
        System.out.print("Escriba el numero de la opcion seleccionada: ");
    }

    static int obtenerOpcion(Scanner sc) {
         // validar la opción ingresada por el usuario
        if (sc.hasNextInt()) {
            return sc.nextInt();
        } else {
            sc.next();
            System.out.println("Por favor, ingrese un numero válido");
            return -1;
        }
    }

    static void verDisponibilidad() {  // Muestra el estado actual de los asientos en el teatro
        for (int f = 1; f <= filas; f++) {
            boolean[] filaActual = obtenerFila(f);
            System.out.print("Fila " + f + ": ");

            for (boolean asiento : filaActual) {
                System.out.print(asiento ? "[X]" : "[O]");
            }
            System.out.println();
        }
    }

    static void reservarAsiento(Scanner sc) {  // Permite reservar un asiento si está disponible
        System.out.print("Seleccione una fila (1-" + filas + ") o 0 para salir: "); //Le pide al usuario seleccionar una fila del 1 al 5
        int filaSel = sc.nextInt();
        if (filaSel == 0) return;
        if (filaSel < 1 || filaSel > filas) {
            System.out.println("Fila inválida."); // valida que el usuario elija opcion correcta 
            return;
        }

        System.out.print("Seleccione un asiento (1-" + asientos + "): "); // permite que el usario seleccione el asiento que desee.
        int asientoSel = sc.nextInt();
         System.out.println("¡¡¡Recuerde confirmar la reserva dentro de 2 minutos o sera anulada¡¡¡");
         System.out.println("");
        if (asientoSel < 1 || asientoSel > asientos) {
            System.out.println("Asiento inválido"); // valida que el usuario elija opcion correcta
            return;
        }

        boolean[] filaSeleccionada = obtenerFila(filaSel);

        if (filaSeleccionada[asientoSel - 1]) {
            System.out.println("¡¡¡Este asiento ya esta ocupado!!!");// --
        } else {
            filaSeleccionada[asientoSel - 1] = true;
            reservafila = filaSel;
            reservaasiento = asientoSel;
            reservaPendiente = true;
            System.out.println("Reserva exitosa: Fila " + filaSel + " – Asiento " + asientoSel);
             
             // Establece un temporizador para cancelar la reserva si no se confirma a tiempo
            timerReserva = new Timer();
            TimerTask cancelacionAutomatica = new TimerTask() {
                @Override
                public void run() {
                    filaSeleccionada[asientoSel - 1] = false; 
                    System.out.println("!!!Reserva cancelada por tiempo de espera!!!"); // Le inidica al usario que u reserva fue anulada por tiempo
                    System.out.println("*Vuelva a verificar su compra*"); 
                    reservaPendiente = false;
                }
            };
            timerReserva.schedule(cancelacionAutomatica, 200000); // Dos minutos y se cancela la reserva si el usario no confirma la reserva
        }
    }

    static void confirmarReserva() { // Confirma la reserva 
        if (reservaPendiente) {
            System.out.println("::Reserva confirmada::");
            System.out.println("Fila: " + reservafila);
            System.out.println("Asiento: " + reservaasiento);
            System.out.println("");
            reservaPendiente = false;
            entradaVendida++;
        } else {
            System.out.println("No hay reservas pendientes."); //valida que el usario no este confirmando una reseva que no existe
        }
    }

    static void generarBoleta() { //Muestra la boleta generada al usuario y indica el total a a pagar, fila y asiento correspondiente 
        System.out.println("_::Boleta Teatro Moro::_");
        System.out.println("");
        int totalPagar = entradaVendida * precioEntrada;
        System.out.println("Total a pagar: $" + totalPagar);

        if (reservafila != -1 && reservaasiento != -1) { 
            System.out.println("Fila: " + reservafila);
            System.out.println("Asiento: " + reservaasiento);
            System.out.println("::Que Disfrute su función :) ::");
        } else {
            System.out.println("No se registra compra de entradas :( "); //Si no existen reservas cinfirmadas aparece este mensaje al usario
        }
    }

    static void modificarVenta(Scanner sc) {  //Permite al usuario modificar entradas
        if (reservafila == -1 || reservaasiento == -1) {
            System.out.println("No hay una reserva existente para modificar.");
            return;
        }
        System.out.println("__:::Sistema de modificacion de entradas:::__");
        System.out.println("");
        System.out.println("::Reserva actual::"); //Muestra al usario la ultima reserva
        System.out.println("Fila: " + + reservafila);
        System.out.println("Asiento: " + reservaasiento);
        System.out.println("");
        System.out.print("Ingrese nueva fila (1-" + filas + "): ");// Se le solicita al usario la nueva fila que desa
        int nuevaFila = sc.nextInt();
        if (nuevaFila < 1 || nuevaFila > filas) {
            System.out.println("Fila no vida.");
            return;
        }
        System.out.print("Ingrese nuevo asiento (1-" + asientos + "): "); //Se le pide al usuario ungresar el nuevo asiento que desea
        int nuevoAsiento = sc.nextInt();
        if (nuevoAsiento < 1 || nuevoAsiento > asientos) {
            System.out.println("Asiento no valido.");
            return;
        }

        boolean[] nuevaFilaSeleccionada = obtenerFila(nuevaFila);
        if (nuevaFilaSeleccionada[nuevoAsiento - 1]) {
            System.out.println("El asiento ya esta ocupado, elija otro");
        } else {
            obtenerFila(reservafila)[reservaasiento - 1] = false;
            nuevaFilaSeleccionada[nuevoAsiento - 1] = true;
            reservafila = nuevaFila;
            reservaasiento = nuevoAsiento;
            System.out.println("::Modificación exitosa::"); //Muestra al usario su nuevo asiento y nueva fila seleccionada
            System.out.println("");
            System.out.println("Nueva Fila: " + nuevaFila);
            System.out.println("Nuevo Asiento: " + nuevoAsiento );
            System.out.println("!!! No tiene que volver a reservar la entrada :) ¡¡¡");
        }
    }

    static boolean[] obtenerFila(int fila) { // Devuelve disponibilidad de la fila seleccionada
        return switch (fila) {
            case 1 -> fila1;
            case 2 -> fila2;
            case 3 -> fila3;
            case 4 -> fila4;
            default -> fila5;
        };
    }

}
