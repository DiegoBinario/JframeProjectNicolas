package classes;

import classes.Reserva;
import classes.Habitacion;
import classes.Persona;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Hotel {

    private String nombre;
    private Habitacion[] habitaciones;

    public Hotel(String nombre) {
        this.nombre = nombre;
        habitaciones = new Habitacion[20];
    }

    public void pregargarDatosHabitaciones() {
        Random r = new Random();

        for (int i = 0; i < habitaciones.length; i++) {//agregamos datos randoms a las habitaciones
            Habitacion habitacion = new Habitacion();
            habitacion.setNumero(i + 1);
            habitacion.setCamas(r.nextInt(2) + 1);
            if (habitacion.getCamas() == 1) {
                habitacion.setBanos(1);
            } else {
                habitacion.setBanos(r.nextInt(2) + 1);
            }
            habitacion.setEstado(false);
            if (habitacion.getCamas() == 2 && habitacion.getBanos() == 2) {
                habitacion.setTipo("VIP");
                habitacion.setCosto(100000);
            } else if (habitacion.getCamas() == 2 && habitacion.getBanos() == 1) {
                habitacion.setTipo("INTERMEDIA");
                habitacion.setCosto(80000);
            } else {
                habitacion.setTipo("NORMAL");
                habitacion.setCosto(50000);
            }

            this.habitaciones[i] = habitacion;
        }
    }

    public void hacerReservaDeHabitacion() throws ParseException {
        Scanner teclado = new Scanner(System.in);

        int numHabitacion = 0, edad = 0, costoTotal = 0;  //inicializamos variables
        String fechaI, fechaT, nombre, rut, dia, mes, anno;//inicializamos variables

        boolean opc = true;
        System.out.println("Reservando un habitacion");
        System.out.println("");
        while (opc == true) { //se hara lo que esta dentro del while hasta opc sea false
            System.out.println("Que habitacion desea reservar");
            numHabitacion = Integer.parseInt(JOptionPane.showInputDialog("Que habitacion desea reservar"));//ingresamos la habitacion por teclado
            if (numHabitacion < 1 || numHabitacion > 20) {//si el numero ingresado es menor a 1 y mayor que 20
                //seguira siendo true, y volvera a ocurrir el ciclo
                JOptionPane.showMessageDialog(null, "Habitacion no encontrada,elejir otra");
            } else if (this.habitaciones[numHabitacion - 1].isEstado() == true) {//si el estado de la habitacion es true
                //seguira siendo true, y volvera a ocurrir el ciclo
                JOptionPane.showMessageDialog(null, "Habitacion ya reserveda, elejir otra");

            } else {//si no entra a ninguna de las anteriores, entrara al else y opc sera false y salira del ciclo
                JOptionPane.showMessageDialog(null, "Habitacion elegida con exito");
                opc = false;
            }
        }

        JOptionPane.showMessageDialog(null, "Fecha inicial de la reserva: " + fechaActualAutomatica());
        fechaI = fechaActualAutomatica();//generamos una fecha actual automatica con el metodo fechaActualAutomatica()
        System.out.println("Ingrese la fecha en que termina la reserva");
        fechaT = esFechaValida();//validamos la fecha que ingresa el usuario con el metodo esFechaValida()
        costoTotal = habitaciones[numHabitacion - 1].getCosto() * diasDeLaReserva(fechaI, fechaT);
        //calculamos el dias que el usuario reservara la habitacion con el metodo diasDeLaReserva y lo
        //multiplicamos por el costo de la habitacion por dia

        System.out.println("Ingrese el nombre de la persona que reservara la habitacion");
        nombre = JOptionPane.showInputDialog("Ingrese el nombre de la persona que reservara la habitacion");
        edad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la edad de la persona"));
        rut = JOptionPane.showInputDialog("Ingrese el rut de la persona");

        Persona p = new Persona(nombre, edad, rut);//creamos un objeto Persona y le pasamos atributos
        Reserva r = new Reserva(fechaI, fechaT, costoTotal, p);//creamos un objeto Reserva y le pasamos atributos

        this.habitaciones[numHabitacion - 1].setEstado(true);//cambiamos el estado de la habitacion a true
        this.habitaciones[numHabitacion - 1].setReserva(r);//le pasamos una resarva a la habitacion
        JOptionPane.showMessageDialog(null, "Persona agregada con exito");
        System.out.println("");
    }

    public void buscarPersonaPorRut() throws ParseException {
        Scanner teclado = new Scanner(System.in);
        boolean encontrada = false;//inicializamos las variables
        String rut = JOptionPane.showInputDialog(null, "Buscando a persona por su rut\n Ingrese el rut de la persona");
        for (int i = 0; i < this.habitaciones.length; i++) {//recorremos el arreglo de habitacion
            //si la habitacion no esta reservada entrara a este if
            if (this.habitaciones[i].getReserva() != null) {
                //si se encuentra el rut entrara a este if
                if (this.habitaciones[i].getReserva().getP().getRut().equalsIgnoreCase(rut)) {
                    //mostrara los datos de la persona y la variable encontrada pasara a true
                    encontrada = true;
                    System.out.println("Nombre de la persona: " + this.habitaciones[i].getReserva().getP().getNombre());
                    System.out.println("  Habitacion reserva: " + (i + 1));
                    System.out.println("     Fecha de inicio: " + this.habitaciones[i].getReserva().getFechaInicio());
                    System.out.println("    Fecha de termino: " + this.habitaciones[i].getReserva().getFechaTermina());
                    System.out.println("     Dias reservados: " + diasDeLaReserva(habitaciones[i].getReserva().getFechaInicio(), habitaciones[i].getReserva().getFechaTermina()));
                    System.out.println(" Costo total a pagar: " + habitaciones[i].getReserva().getCostoTotal());
                    JOptionPane.showMessageDialog(null,
                            "Nombre de la persona: " + this.habitaciones[i].getReserva().getP().getNombre() + "\n"
                            + "Habitacion reserva: " + (i + 1) + "\n"
                            + "Fecha de inicio: " + this.habitaciones[i].getReserva().getFechaInicio() + "\n"
                            + "Fecha de termino: " + this.habitaciones[i].getReserva().getFechaTermina() + "\n"
                            + "Dias reservados: " + diasDeLaReserva(habitaciones[i].getReserva().getFechaInicio(), habitaciones[i].getReserva().getFechaTermina()) + "\n"
                            + "Costo total a pagar: " + habitaciones[i].getReserva().getCostoTotal());
                }
            }

        }
        //si la variable encontrada sigue siendo false, entrara a este if
        if (encontrada == false) {
            JOptionPane.showMessageDialog(null, "Persona no encontrada");
        }

    }

    public void buscarHabitacion() throws ParseException {
        Scanner teclado = new Scanner(System.in);
        boolean encontrada = false;
        int num = 0;//inicializamos las variables
        JOptionPane.showMessageDialog(null, "Buscando habitacion");

        while (encontrada == false) {//hara este ciclo hasta que encontrada sea true
            num = Integer.parseInt(JOptionPane.showInputDialog("Buscando habitacion\nIngrese el numero de la habitacion"));

            if (num < 1 || num > 20) {//si num es menos que 1 y mayor que 20 entrara a este if
                JOptionPane.showMessageDialog(null, "Habitacion no encontrada,elejir otra");
            } else {//sino entrara a este, y encontrada pasara a true y terminara el ciclo
                encontrada = true;
            }
        }

        JOptionPane.showMessageDialog(null,
                "Habitacion: " + (num) + "\n"
                + "Camas: " + habitaciones[num - 1].getCamas() + "\n"
                + "Baños: " + habitaciones[num - 1].getBanos() + "\n"
                + "Tipo: " + habitaciones[num - 1].getTipo() + "\n"
                + "Costo por noche: " + habitaciones[num - 1].getCosto() + "\n"
        );

        if (habitaciones[num - 1].isEstado() == true) {
            JOptionPane.showMessageDialog(null,
                    "Reservada por: " + habitaciones[num - 1].getReserva().getP().getNombre() + "\n"
                    + "Inicio : " + habitaciones[num - 1].getReserva().getFechaInicio() + "\n"
                    + "Termino : " + habitaciones[num - 1].getReserva().getFechaTermina() + "\n"
                    + "Dias reservados: " + diasDeLaReserva(habitaciones[num - 1].getReserva().getFechaInicio(), habitaciones[num - 1].getReserva().getFechaTermina()) + "\n"
                    + "Costo total: " + habitaciones[num - 1].getReserva().getCostoTotal()
            );
        } else {
            JOptionPane.showMessageDialog(null, "Estado: Libre");
        }
        System.out.println("");

    }

    public void mostrarHabitaciones() throws ParseException {
        System.out.println("Mostrando todas la habitaciones");
        System.out.println("");
        for (int i = 0; i < habitaciones.length; i++) {//creamos un for que recorra todo el arreglo de habitaciones
            System.out.println("     Habitacion: " + habitaciones[i].getNumero());
            System.out.println("          Camas: " + habitaciones[i].getCamas());
            System.out.println("          Baños: " + habitaciones[i].getBanos());
            System.out.println("           Tipo: " + habitaciones[i].getTipo());
            System.out.println("  Costo por dia: " + habitaciones[i].getCosto());
            if (habitaciones[i].isEstado() == true) {
                System.out.println("              Estado: Ocupada");
                System.out.println("       Reservada por: " + habitaciones[i].getReserva().getP().getNombre());
                System.out.println("              Inicio: " + habitaciones[i].getReserva().getFechaInicio());
                System.out.println("             Termino: " + habitaciones[i].getReserva().getFechaTermina());
                System.out.println("     Dias reservados: " + diasDeLaReserva(habitaciones[i].getReserva().getFechaInicio(), habitaciones[i].getReserva().getFechaTermina()));
                System.out.println("         Costo total: " + habitaciones[i].getReserva().getCostoTotal());
            } else {
                System.out.println("     Estado: Libre");
            }
            System.out.println("");
        }
    }

    public void eliminarReservaDeHabitacion() {
        Scanner teclado = new Scanner(System.in);
        boolean encontrada = false;
        int num = 0;//inicializamos las variables

        while (encontrada == false) {//hara este ciclo hasta que encontrada sea true
            num = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de la habitacion"));

            if (num < 1 || num > 20) {//si num es menos que 1 y mayor que 20 entrara a este if
                JOptionPane.showMessageDialog(null, "Habitacion no encontrada,elejir otra ");
            } else {//sino entrara a este, y encontrada pasara a true y terminara el ciclo
                encontrada = true;
            }
        }
        //si la habitacion no tiene reserva, mostrara un mensaje
        if (this.habitaciones[num - 1].isEstado() == false) {
            JOptionPane.showMessageDialog(null, "Esta habitacion no tiene una reserva acutal");
        } else {//si tiene reserva, la eliminara dejandola como null y el estado de la habitacion pasara a false
            this.habitaciones[num - 1].setEstado(false);
            this.habitaciones[num - 1].setReserva(null);
            JOptionPane.showMessageDialog(null, "Reserva eliminada con exito");
        }
    }

    //https://es.stackoverflow.com/questions/174706/como-validar-fecha
    //Como validar fecha, codigo remodelodo y adaptado
    public String esFechaValida() {
        Scanner teclado = new Scanner(System.in);
        boolean esFechaValida = false;
        int dia = 0, mes = 0, anio = 0;
        while (esFechaValida == false) {
            anio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año"));
            mes = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el mes"));
            dia = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dia"));
            try {
                LocalDate.of(anio, mes, dia);
                esFechaValida = true;
            } catch (DateTimeException e) {
                JOptionPane.showMessageDialog(null, "Fecha no valida, ingresela nuevamente");
                esFechaValida = false;
            }
        }

        return anio + "-" + mes + "-" + dia;
    }

    //http://puntocomnoesunlenguaje.blogspot.com/2013/11/obtener-fecha-actual-en-java.html
    //Como obetener fecha actual, codigo remodelodo y adaptado
    public String fechaActualAutomatica() {
        Calendar fecha = new GregorianCalendar();
        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        return anio + "-" + (mes + 1) + "-" + dia;
    }

    public int diasDeLaReserva(String fechaInicial, String fechaFinal) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//Como pasar de String a calender
        Date fecha1 = sdf.parse(fechaInicial);                  //https://es.stackoverflow.com/questions/22525/como-ingresar-una-fecha-usando-calendar
        Date fecha2 = sdf.parse(fechaFinal);                    //
        Calendar fechaI = Calendar.getInstance();               //
        Calendar fechaF = Calendar.getInstance();               //
        fechaI.setTime(fecha1);                                 //
        fechaF.setTime(fecha2);                                 //
        int diffDays = 0;
        if (fechaF.before(fechaI) || fechaI.equals(fechaF)) {//Como restar dos fechas para obtener los dias
            diffDays = 0;                                   //https://nprieto23.wordpress.com/2013/04/25/restar-dos-fechas-en-java-correctamente/
        } else {
            while (fechaI.before(fechaF) || fechaI.equals(fechaF)) {
                diffDays++;
                fechaI.add(Calendar.DATE, 1);
            }
        }
        return diffDays == 0 ? 0 : diffDays - 1;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Habitacion[] getHabitaciones() {
        return this.habitaciones;
    }
}
