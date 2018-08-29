package mx.unam.ciencias.fbd;

import mx.unam.ciencias.fbd.common.*;
import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.domain.Vehicle;
import mx.unam.ciencias.fbd.domain.Weapon;
import mx.unam.ciencias.fbd.service.StaffService;
import mx.unam.ciencias.fbd.service.VehicleService;
import mx.unam.ciencias.fbd.service.WeaponService;
import mx.unam.ciencias.fbd.util.ConsoleUtils;
import mx.unam.ciencias.fbd.view.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class App {
    /**
     * App's root directory.
     */
    public final static Path ROOT = FileSystems.getDefault().getPath("data").toAbsolutePath();
    /**
     * Regex that matches all table names.
     *
     * @see Table
     */
    public static final String TABLES = Arrays.stream(Table.values())
            .map(Object::toString).collect(Collectors.joining("|"));
    /**
     * Staff service.
     */
    private static final StaffService STAFF_SERVICE = StaffService.getInstance();
    /**
     * Vehicle service.
     */
    private static final VehicleService VEHICLE_SERVICE = VehicleService.getInstance();
    /**
     * Weapon service.
     */
    private static final WeaponService WEAPON_SERVICE = WeaponService.getInstance();
    /**
     * Standard in wrapped in a scanner for user input.
     */
    private static final Scanner SCANNER = new Scanner(System.in);
    /**
     * Regex that matches all basic commands.
     *
     * @see BasicOperation
     */
    private static final String BASIC_COMMANDS = Arrays.stream(BasicOperation.values())
            .map(IRegex::regex).collect(Collectors.joining("|"));
    /**
     * Regex that matches all table commands.
     *
     * @see TableOperation
     */
    private static final String TABLE_COMMANDS = Arrays.stream(TableOperation.values())
            .map(IRegex::regex).collect(Collectors.joining("|"));
    /**
     * Regex that matches all basic commands that require an ID.
     *
     * @see IdOperation
     */
    private static final String ID_COMMANDS = "(" + Arrays.stream(IdOperation.values())
            .map(Objects::toString).collect(Collectors.joining("|")) + ")" + "\\s+\"(.+)\"";
    /**
     * Regex that matches all advanced commands for the staff table.
     *
     * @see StaffOperation
     */
    private static final String STAFF_COMMANDS = "(" + Arrays.stream(StaffOperation.values())
            .map(Objects::toString).collect(Collectors.joining("|")) + ")" + "\\s+\"(.+)\"";
    /**
     * Application's name.
     */
    private static final String APP_NAME = "SSP-CSV-DB";
    /**
     * Staff panel.
     */
    private static Panel STAFF_PANEL;
    /**
     * Vehicle panel.
     */
    private static Panel VEHICLE_PANEL;
    /**
     * Weapon panel.
     */
    private static Panel WEAPON_PANEL;
    /**
     * Main panel.
     */
    private static Panel MAIN_PANEL;

    /**
     * Staff editing wizard.
     */
    private static Wizard<Staff> STAFF_WIZARD = new StaffWizard(SCANNER);
    /**
     * Vehicle editing wizard.
     */
    private static Wizard<Vehicle> VEHICLE_WIZARD = new VehicleWizard(SCANNER);
    /**
     * Weapon editing wizard.
     */
    private static Wizard<Weapon> WEAPON_WIZARD = new WeaponWizard(SCANNER);

    /**
     * Initializes the app and runs the main panel.
     *
     * @param args console arguments.
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.SEVERE);
        System.out.println(APP_NAME + " @Version alpha0.0.1");
        init();
        MAIN_PANEL.run();
    }

    private static void init() {
        initStaffPanel();
        initVehiclePanel();
        initWeaponPanel();
        initMainPanel();
    }

    /**
     * Correctly initializes the Main panel.
     */
    private static void initMainPanel() {
        MAIN_PANEL = new Panel(APP_NAME, SCANNER, BASIC_COMMANDS);
        MAIN_PANEL.setHelp(() -> {
            usage();
            basicOperationsUsage();
        });
        MAIN_PANEL.setHandler(command -> {
            String selection = ConsoleUtils.getGroup(BASIC_COMMANDS, command, 1);
            Table table = Table.valueOf(selection);
            switch (table) {
                case STAFF:
                    STAFF_PANEL.run();
                    break;
                case VEHICLE:
                    VEHICLE_PANEL.run();
                    break;
                case WEAPON:
                    WEAPON_PANEL.run();
                    break;
            }
        });
    }

    /**
     * Correctly initializes the Weapon panel.
     */
    private static void initWeaponPanel() {
        WEAPON_PANEL = new Panel("WEAPON", SCANNER, TABLE_COMMANDS, ID_COMMANDS);
        WEAPON_PANEL.setHelp(() -> {
            usage();
            crudUsage();
        });

        WEAPON_PANEL.setHandler(command -> {
            if (command.matches(TABLE_COMMANDS)) {
                TableOperation tableOperation = TableOperation.valueOf(command.trim());
                switch (tableOperation) {
                    case NEW:
                        WEAPON_WIZARD.create().ifPresent(WEAPON_SERVICE::save);
                        break;
                    case LIST:
                        WEAPON_SERVICE.findAll().forEach(System.out::println);
                }
            } else if (command.matches(ID_COMMANDS)) {
                // Parse command and id
                String operationStr = ConsoleUtils.getGroup(ID_COMMANDS, command, 1);
                String idStr = ConsoleUtils.getGroup(ID_COMMANDS, command, 2);
                IdOperation operation = IdOperation.valueOf(operationStr);
                Optional<Weapon> result = Optional.empty();
                // Attempt to get element by ID
                try {
                    UUID id = UUID.fromString(idStr);
                    result = WEAPON_SERVICE.findById(id);
                } catch (IllegalArgumentException ignored) {
                }
                // Handle operation if element is present
                if (result.isPresent()) {
                    Weapon weapon = result.get();
                    switch (operation) {
                        case GET:
                            System.out.println(weapon);
                            break;
                        case EDIT:
                            WEAPON_WIZARD.edit(weapon).ifPresent(WEAPON_SERVICE::save);
                            break;
                        case DELETE:
                            WEAPON_SERVICE.deleteById(weapon.getId());
                            break;
                    }
                } else {
                    System.out.println("No se encontró el registro con id " + idStr);
                }
            }
        });
    }

    /**
     * Correctly initializes the Vehicle panel.
     */
    private static void initVehiclePanel() {
        VEHICLE_PANEL = new Panel("VEHICLE", SCANNER, TABLE_COMMANDS, ID_COMMANDS);
        VEHICLE_PANEL.setHelp(() -> {
            usage();
            crudUsage();
        });

        VEHICLE_PANEL.setHandler(command -> {
            if (command.matches(TABLE_COMMANDS)) {
                TableOperation tableOperation = TableOperation.valueOf(command.trim());
                switch (tableOperation) {
                    case NEW:
                        VEHICLE_WIZARD.create().ifPresent(VEHICLE_SERVICE::save);
                        break;
                    case LIST:
                        VEHICLE_SERVICE.findAll().forEach(System.out::println);
                }
            } else if (command.matches(ID_COMMANDS)) {
                // Parse command and id
                String operationStr = ConsoleUtils.getGroup(ID_COMMANDS, command, 1);
                String idStr = ConsoleUtils.getGroup(ID_COMMANDS, command, 2);
                IdOperation operation = IdOperation.valueOf(operationStr);
                // Attempt to get element by ID
                Optional<Vehicle> result = VEHICLE_SERVICE.findById(idStr);
                // Handle operation if element is present
                if (result.isPresent()) {
                    Vehicle vehicle = result.get();
                    switch (operation) {
                        case GET:
                            System.out.println(vehicle);
                            break;
                        case EDIT:
                            VEHICLE_WIZARD.edit(vehicle).ifPresent(VEHICLE_SERVICE::save);
                            break;
                        case DELETE:
                            VEHICLE_SERVICE.deleteById(vehicle.getId());
                            break;
                    }
                } else {
                    System.out.println("No se encontró el registro con id " + idStr);
                }
            }
        });
    }

    /**
     * Correctly initializes the Staff panel.
     */
    private static void initStaffPanel() {
        STAFF_PANEL = new Panel("STAFF", SCANNER, TABLE_COMMANDS, ID_COMMANDS, STAFF_COMMANDS);
        STAFF_PANEL.setHelp(() -> {
            usage();
            staffUsage();
            crudUsage();
        });
        STAFF_PANEL.setHandler(command -> {
            if (command.matches(TABLE_COMMANDS)) {
                TableOperation tableOperation = TableOperation.valueOf(command.trim());
                switch (tableOperation) {
                    case NEW:
                        STAFF_WIZARD.create().ifPresent(STAFF_SERVICE::save);
                        break;
                    case LIST:
                        STAFF_SERVICE.findAll().forEach(System.out::println);
                }
            } else if (command.matches(ID_COMMANDS)) {
                // Parse command and id
                String operationStr = ConsoleUtils.getGroup(ID_COMMANDS, command, 1);
                String idStr = ConsoleUtils.getGroup(ID_COMMANDS, command, 2);
                IdOperation operation = IdOperation.valueOf(operationStr);
                Optional<Staff> result = Optional.empty();
                // Attempt to get element by ID
                try {
                    UUID id = UUID.fromString(idStr);
                    result = STAFF_SERVICE.findById(id);
                } catch (IllegalArgumentException ignored) {
                }
                // Handle operation if element is present
                if (result.isPresent()) {
                    Staff staff = result.get();
                    switch (operation) {
                        case GET:
                            System.out.println(staff);
                            break;
                        case EDIT:
                            STAFF_WIZARD.edit(staff).ifPresent(STAFF_SERVICE::save);
                            break;
                        case DELETE:
                            STAFF_SERVICE.deleteById(staff.getId());
                            break;
                    }
                } else {
                    System.out.println("No se encontró el registro con id " + idStr);
                }
            } else if (command.matches(STAFF_COMMANDS)) {
                // Parse command and id
                String operationStr = ConsoleUtils.getGroup(STAFF_COMMANDS, command, 1);
                String idStr = ConsoleUtils.getGroup(STAFF_COMMANDS, command, 2);
                StaffOperation operation = StaffOperation.valueOf(operationStr);
                Optional<Staff> result = Optional.empty();
                // Attempt to get by id
                try {
                    UUID id = UUID.fromString(idStr);
                    result = STAFF_SERVICE.findById(id);
                } catch (IllegalArgumentException ignored) {
                }
                // Handle operation if element is present
                if (result.isPresent()) {
                    Staff staff = result.get();
                    switch (operation) {
                        case AGE:
                            System.out.println(staff.getAge());
                            break;
                        case SENIORITY:
                            System.out.println(staff.getSeniority());
                            break;
                        case SUBORDINATES:
                            STAFF_SERVICE.getSubordinates(staff).forEach(System.out::println);
                    }
                } else {
                    System.out.println("No se encontró el registro con id " + idStr);
                }
            }
        });
    }

    /**
     * Prints the usage section header.
     */
    private static void usage() {
        System.out.println("\nUso:");
    }

    /**
     * Prints helpful information about basic options.
     */
    private static void basicOperationsUsage() {
        System.out.println(String.format("\t" + BasicOperation.USE + " (%s)", TABLES));
        System.out.println("\t  Selecciona la tabla para trabajar sobre ella.");
        System.out.println("\tEXIT");
        System.out.println("\t  Termina la ejecución del programa.\n");
    }

    /**
     * Prints helpful information about crud options.
     */
    private static void crudUsage() {
        System.out.println("\t" + TableOperation.LIST);
        System.out.println("\t  Lista todas las entidades de la tabla.");
        System.out.println("\t" + TableOperation.NEW);
        System.out.println("\t  Añade una nueva entidad a la tabla.");
        System.out.println("\t" + IdOperation.DELETE + " \"ID\"");
        System.out.println("\t  Borra la entidad con el ID dado.");
        System.out.println("\t" + IdOperation.EDIT + " \"ID\"");
        System.out.println("\t  Edita la entidad con el ID dado.");
        System.out.println("\t" + IdOperation.GET + " \"ID\"");
        System.out.println("\t  Busca la entidad con el ID dado.");
        System.out.println("\t" + BasicOperation.EXIT);
        System.out.println("\t  Regresa al menú anterior.\n");
    }

    /**
     * Prints helpful information about the staff options.
     */
    private static void staffUsage() {
        System.out.println("\t" + StaffOperation.SENIORITY + " \"ID\"");
        System.out.println("\t  Calcula la anitgüedad del elemento con el ID dado.");
        System.out.println("\t" + StaffOperation.AGE + " \"ID\"");
        System.out.println("\t  Calcula la edad del elemento con el ID dado.");
        System.out.println("\t" + StaffOperation.SUBORDINATES + " \"ID\"");
        System.out.println("\t  Regresa la lista de subordinados del elemento con el ID dado.");
    }

    private enum Table {
        STAFF, VEHICLE, WEAPON
    }
}
