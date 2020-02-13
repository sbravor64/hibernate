package com.andres;

import org.hibernate.Session;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    static Session session = HibernateUtil.getSessionFactory().openSession();

    public static void main(String[] args) {
        session.beginTransaction();
        menu();

        session.getTransaction().commit();
        HibernateUtil.shutdown();
    }

    static void menu(){
        Scanner scanner = new Scanner(System.in);

        Scanner sc = new Scanner(System.in);
        int opcion;
        do{
            System.out.println(ANSI_CYAN + "HIBERNATE" + ANSI_RESET);
            System.out.println("MENÚ ");
            System.out.println("1. Mostrar procedencia y posición de los jugadores de los Cavaliers");
            System.out.println("2. Mostrar número de jugadores españoles.");
            System.out.println("3. Añadir jugador");
            System.out.println("4. Mostrar jugadores que en la temporada 04/05 tenían una media de\n" +
                    "puntos por partido superior a 10.");
            System.out.println("5. Acabar");

            opcion= sc.nextInt();

            switch (opcion) {
                case 1:
                    mostrarProcedenciaPosicionCavaliers();
                    break;
                case 2:
                    mostrarNumJugSpain();
                    break;
                case 3:
                    System.out.println("Escribe el código del jugador; ");
                    int codigo = scanner.nextInt();
                    System.out.println("Escribe el nombre del jugador; ");
                    scanner.nextLine();
                    String nombre = scanner.nextLine();
                    System.out.println("Escribe la procedencia del jugador; ");
                    String procedencia = scanner.nextLine();;
                    System.out.println("Escribe la altura del jugador; ");
                    String altura = scanner.nextLine();;
                    System.out.println("Escribe el peso del jugador; ");
                    int peso = scanner.nextInt();
                    System.out.println("Escribe la posición del jugador; ");
                    scanner.nextLine();
                    String posicion = scanner.nextLine();;
                    System.out.println("Escribe el nombre del equipo del jugador; ");
                    String nombre_equipo = scanner.nextLine();;

                    añadirJugador(codigo,nombre,procedencia,altura,peso,posicion,nombre_equipo);
                    break;
                case 4:
                    mostrarJugTemp();
                    break;
                case 5:
                    break;
            }
        } while (opcion!=5);
    }

    private static void mostrarJugTemp() {
        List<EstadisticasEntity> list = session.createQuery("FROM EstadisticasEntity es WHERE es.temporada='04/05' and es.puntosPorPartido>10").list();
        List<JugadoresEntity> listJ = session.createQuery("FROM JugadoresEntity").list();

        AtomicInteger count= new AtomicInteger();

        list.forEach(e -> { listJ.forEach(j -> {
                if(j.getCodigo() ==e.getJugador()){
                    count.getAndIncrement();
                    System.out.println("Nombre: " + j.getNombre() + " | Media: " + e.getPuntosPorPartido() + " | Temporada: " + e.getTemporada());
                }
        }); });

        System.out.println("Hay un total de " + count.get() + " Jugadores");
    }

    private static void añadirJugador(int codigo, String nombre, String procedencia, String altura, int peso, String posicion, String nombre_equipo) {
        JugadoresEntity jugador = new JugadoresEntity();
        jugador.setNombre(nombre);
        jugador.setCodigo(codigo);
        jugador.setAltura(altura);
        jugador.setPeso(peso);
        jugador.setPosicion(posicion);
        jugador.setProcedencia(procedencia);
        jugador.setNombre_equipo(nombre_equipo);
        session.save(jugador);
        session.getTransaction().commit();

    }

    private static void mostrarNumJugSpain() {
        List<JugadoresEntity> list = session.createQuery("FROM JugadoresEntity WHERE procedencia='Spain'").list();
        long num = list.stream().count();
        System.out.println("Total Jugadores Españoles: " + num);

    }

    private static void mostrarProcedenciaPosicionCavaliers() {
        List<JugadoresEntity> list = session.createQuery("FROM JugadoresEntity WHERE Nombre_equipo='Cavaliers'").list();

        list.stream().map(jugadoresEntity -> "Procedencia: " + jugadoresEntity.getProcedencia() + ", Posición: " + jugadoresEntity.getPosicion()).forEach(System.out::println);
    }
}
