package mx.unam.ciencias.fbd;

import mx.unam.ciencias.fbd.service.StaffService;
import mx.unam.ciencias.fbd.service.VehicleService;
import mx.unam.ciencias.fbd.service.WeaponService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class App {
    /**
     * App's root directory.
     */
    public final static Path ROOT = FileSystems.getDefault().getPath("data").toAbsolutePath();
    /**
     * Logger.
     */
    private final static Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final StaffService staffService = StaffService.getInstance();
    private static final VehicleService vehicleService = VehicleService.getInstance();
    private static final WeaponService weaponService = WeaponService.getInstance();
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String tables = Arrays.stream(Table.values()).map(Object::toString).collect(Collectors.joining("|"));
        boolean close = false;
        String userInput = "";
        System.out.println("SSP-CSV-DB @Version alpha0.0.1");
        usage();
        do {
            userInput = scan.next();
            System.out.printf("Got %s", userInput);
            if (userInput.equals(BasicOperation.EXIT.toString())) {
                close = true;
            }
        } while (!close);
    }

    private static void usage() {
        System.out.println("\nUso:");
        System.out.println("\tUSE [Tabla]");
        System.out.println("\t  Selecciona la tabla para trabajar sobre ella.");
        System.out.println("\tEXIT");
        System.out.println("\t  Termina la ejecución del programa.\n");
    }

    private enum BasicOperation {
        USE, // USE [Table]
        EXIT
    }

    private enum CrudOperation {
        LIST,   // LIST
        NEW,    // NEW
        DELETE, // DELETE [ID]
        EDIT,   // EDIT [ID]
        GET     // GET [ID]
    }

    private enum StaffOperation {
        SUBORDINATES,   // SUBORDINATES [ID]
        SENIORITY,      // SENIORITY [ID]
        AGE            // AGE [ID]
    }

    private enum Table {
        STAFF, VEHICLE, WEAPON
    }
}
