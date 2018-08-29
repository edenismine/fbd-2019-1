package mx.unam.ciencias.fbd;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.domain.Vehicle;
import mx.unam.ciencias.fbd.domain.Weapon;
import mx.unam.ciencias.fbd.service.StaffService;
import mx.unam.ciencias.fbd.service.VehicleService;
import mx.unam.ciencias.fbd.service.WeaponService;
import mx.unam.ciencias.fbd.util.ConsoleUtils;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


public class App {
    /**
     * App's root directory.
     */
    public final static Path ROOT = FileSystems.getDefault().getPath("data").toAbsolutePath();
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
     * Regex that matches all table names.
     *
     * @see Table
     */
    private static final String TABLES = Arrays.stream(Table.values())
            .map(Object::toString).collect(Collectors.joining("|"));
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
     * Initializes the app and runs the main panel.
     *
     * @param args console arguments.
     */
    public static void main(String[] args) {
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

    private static void initMainPanel() {
        MAIN_PANEL = new Panel(APP_NAME, BASIC_COMMANDS);
        MAIN_PANEL.setHelp(() -> {
            usage();
            basicOperationsUsage();
        });
        MAIN_PANEL.setManager(command -> {
            String selection = ConsoleUtils.getGroup(BASIC_COMMANDS, command, 1);
            Table table = Table.valueOf(selection);
            switch (table) {
                case STAFF:
                    STAFF_PANEL.run();
                    break;
                case VEHICLE:
                    VEHICLE_PANEL.run();
                    break;
                default:
                    WEAPON_PANEL.run();
                    break;
            }
        });
    }

    private static void initWeaponPanel() {
        WEAPON_PANEL = new Panel("WEAPON", TABLE_COMMANDS, ID_COMMANDS);
        WEAPON_PANEL.setHelp(() -> {
            usage();
            crudUsage();
        });

        WEAPON_PANEL.setManager(command -> {
            if (command.matches(TABLE_COMMANDS)) {
                TableOperation tableOperation = TableOperation.valueOf(command.trim());
                switch (tableOperation) {
                    case NEW:
                        // TODO implement weapon wizard
                        System.out.println("Launch weapon wizard");
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
                            // TODO implement weapon wizard
                            System.out.println("Launch weapon wizard - edit mode");
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

    private static void initVehiclePanel() {
        VEHICLE_PANEL = new Panel("VEHICLE", TABLE_COMMANDS, ID_COMMANDS);
        VEHICLE_PANEL.setHelp(() -> {
            usage();
            crudUsage();
        });

        VEHICLE_PANEL.setManager(command -> {
            if (command.matches(TABLE_COMMANDS)) {
                TableOperation tableOperation = TableOperation.valueOf(command.trim());
                switch (tableOperation) {
                    case NEW:
                        // TODO implement vehicle wizard
                        System.out.println("Launch vehicle wizard");
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
                            // TODO implement vehicle wizard
                            System.out.println("Launch vehicle wizard - edit mode");
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

    private static void initStaffPanel() {
        STAFF_PANEL = new Panel("STAFF", TABLE_COMMANDS, ID_COMMANDS, STAFF_COMMANDS);
        STAFF_PANEL.setHelp(() -> {
            usage();
            staffUsage();
            crudUsage();
        });
        STAFF_PANEL.setManager(command -> {
            if (command.matches(TABLE_COMMANDS)) {
                TableOperation tableOperation = TableOperation.valueOf(command.trim());
                switch (tableOperation) {
                    case NEW:
                        // TODO implement staff wizard
                        System.out.println("Launch staff wizard");
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
                            // TODO implement staff wizard
                            System.out.println("Launch staff wizard - edit mode");
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

    private static void usage() {
        System.out.println("\nUso:");
    }

    private static void basicOperationsUsage() {
        System.out.println(String.format("\t" + BasicOperation.USE + " (%s)", TABLES));
        System.out.println("\t  Selecciona la tabla para trabajar sobre ella.");
        System.out.println("\tEXIT");
        System.out.println("\t  Termina la ejecución del programa.\n");
    }

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

    private static void staffUsage() {
        System.out.println("\t" + StaffOperation.SENIORITY + " \"ID\"");
        System.out.println("\t  Calcula la anitgüedad del elemento con el ID dado.");
        System.out.println("\t" + StaffOperation.AGE + " \"ID\"");
        System.out.println("\t  Calcula la edad del elemento con el ID dado.");
        System.out.println("\t" + StaffOperation.SUBORDINATES + " \"ID\"");
        System.out.println("\t  Regresa la lista de subordinados del elemento con el ID dado.");
    }

    private enum BasicOperation implements IRegex {
        USE(String.format("\\s+(%s)", TABLES)), // USE [Table]
        EXIT(""); // EXIT

        String argument;

        BasicOperation(String argument) {
            this.argument = argument;
        }

        @Override
        public String regex() {
            return this.name() + this.argument;
        }
    }

    private enum TableOperation implements IRegex {
        LIST,   // LIST
        NEW;    // NEW

        @Override
        public String regex() {
            return this.name();
        }
    }

    private enum IdOperation implements IRegex {
        DELETE, // DELETE [ID]
        EDIT,   // EDIT [ID]
        GET;     // GET [ID]

        @Override
        public String regex() {
            return this.name() + "\\s+\"(.+)\"";
        }
    }

    private enum StaffOperation implements IRegex {
        SUBORDINATES,   // SUBORDINATES [ID]
        SENIORITY,      // SENIORITY [ID]
        AGE;            // AGE [ID]

        @Override
        public String regex() {
            return this.name() + "\\s+\"(.+)\"";
        }
    }

    private enum Table {
        STAFF, VEHICLE, WEAPON
    }

    interface TableManager {
        void handle(String command);
    }

    interface IPrintHelp {
        void printHelp();
    }

    private interface IRegex {
        String regex();
    }

    /**
     * A command line panel.
     */
    static class Panel {
        /**
         * The panel's title. It precedes all user input.
         */
        private String title;
        /**
         * The manager that handles user input.
         */
        private TableManager manager;
        /**
         * All regex sequences the panel recognizes as commands.
         */
        private List<String> commands;
        /**
         * The panel's help printer.
         */
        private IPrintHelp help;

        Panel(String title, String... commands) {
            this.title = title;
            ArrayList<String> temp = new ArrayList<>();
            temp.add(BasicOperation.EXIT.name());
            temp.addAll(Arrays.asList(commands));
            this.commands = temp;
        }

        void setManager(TableManager manager) {
            this.manager = manager;
        }

        void setHelp(IPrintHelp help) {
            this.help = help;
        }

        void run() {
            boolean close = false;
            String userInput;
            String regex = String.join("|", commands);
            help.printHelp();
            do {
                userInput = ConsoleUtils.getValidString(regex, SCANNER, title);
                if (userInput.matches(BasicOperation.EXIT.regex())) {
                    close = true;
                } else {
                    manager.handle(userInput);
                }
            } while (!close);
        }
    }
}
