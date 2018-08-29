package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.domain.Vehicle;
import mx.unam.ciencias.fbd.repository.VehicleRepository;
import mx.unam.ciencias.fbd.service.StaffService;
import mx.unam.ciencias.fbd.util.ConsoleUtils;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleWizard implements Wizard<Vehicle> {
    private final Scanner scanner;

    public VehicleWizard(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Vehicle> create() {
        String plates = getValidPlates();
        Vehicle.Type type = getValidType();
        String model = getValidModel();
        String description = getValidDescription();
        Vehicle result = new Vehicle(plates, type, model, description);
        result.setZone(getValidZone());
        String addSupervisor = ConsoleUtils.getValidString("[yY][eE][sS]|[nN][oO]|[sS][iI]", scanner,
                "Agregar conductor?", "Por favor usa el formato si|no");
        if (addSupervisor.toLowerCase().charAt(0) != 'n') {
            Optional<UUID> driverId = getValidDriverID();
            if (driverId.isPresent()) {
                result.setDriverId(driverId.get());
            } else {
                System.out.println("Ningún elemento puede asignarse como conductor.");
            }
        }
        return Optional.of(result);
    }

    @Override
    public Optional<Vehicle> edit(Vehicle entity) {
        // Obtain entity schema and create regex
        String vehicleSchemaRegex =
                Arrays.stream(VehicleRepository.Schema.values()).map(Enum::toString).collect(Collectors.joining("|"));
        // Create edit panel
        Panel editor = new Panel("VEHICLE:EDIT", scanner, vehicleSchemaRegex);
        // Create help information
        editor.setHelp(() -> {
            System.out.println("\nUso:");
            System.out.println("\t" + VehicleRepository.Schema.TYPE);
            System.out.println("\t  Edita el tipo del vehículo.");
            System.out.println("\t" + VehicleRepository.Schema.MODEL);
            System.out.println("\t  Edita el modelo del vehículo.");
            System.out.println("\t" + VehicleRepository.Schema.ZONE);
            System.out.println("\t  Edita la zona del vehículo.");
            System.out.println("\t" + VehicleRepository.Schema.DESCRIPTION);
            System.out.println("\t  Edita la descripción del vehículo.");
            System.out.println("\t" + VehicleRepository.Schema.DRIVER_ID);
            System.out.println("\t  Edita el elemento responsable del vehículo.");
            System.out.println("\t" + Panel.EOE);
            System.out.println("\t  Regresa al menú anterior.\n");
        });
        // Handle commands
        editor.setHandler(command -> {
            if (!command.equals(Panel.EOE)) {
                VehicleRepository.Schema routine = VehicleRepository.Schema.valueOf(command);
                switch (routine) {
                    case TYPE:
                        System.out.println("Tipo actual: " + entity.getType());
                        entity.setType(getValidType());
                        break;
                    case MODEL:
                        System.out.println("Modelo actual: " + entity.getModel());
                        entity.setModel(getValidModel());
                        break;
                    case ZONE:
                        System.out.println("Zona actual: " + entity.getZone());
                        entity.setZone(getValidZone());
                        break;
                    case DESCRIPTION:
                        System.out.println("Descripción actual: " + entity.getDescription());
                        entity.setDescription(getValidDescription());
                        break;
                    case DRIVER_ID:
                        System.out.println("Conductor actual: " + entity.getDriverId());
                        Optional<UUID> driverId = getValidDriverID();
                        if (driverId.isPresent()) {
                            entity.setDriverId(driverId.get());
                        } else {
                            System.out.println("Ningún elemento puede asignarse como conductor.");
                        }
                        break;
                    case ID:
                        System.out.println("Placas del elemento actual: " + entity.getId());
                        break;
                }
            }
        });
        // Run editor panel
        editor.run();
        // return entity
        return Optional.of(entity);
    }

    private String getValidPlates() {
        return ConsoleUtils.getValidString("[\\dA-Z]+(-?[\\dA-Z])*", scanner,
                "Matrícula", "Sólo se permiten mayúsculas y guiones");
    }

    private Optional<UUID> getValidDriverID() {
        UUID result = null;
        List<UUID> drivers = StaffService.getInstance().findAll()
                .map(Staff::getId)
                .collect(Collectors.toList());
        if (!drivers.isEmpty()) {
            System.out.println("Selecciona el ID del conductor de la siguiente lista usando el índice:");
            for (int i = 0; i < drivers.size(); i++)
                System.out.println(String.format("%d\t%s", i, drivers.get(i)));
            int validIndex = ConsoleUtils.getValidInt(scanner, "Conductor",
                    integer -> integer >= 0 && integer < drivers.size());
            result = drivers.get(validIndex);
        }
        return Optional.ofNullable(result);
    }

    private String getValidDescription() {
        return ConsoleUtils.getValidString(".{3,280}", scanner, "Descripción", "Mínimo 3 caracteres, máximo 280");
    }

    private String getValidZone() {
        return ConsoleUtils.getValidString("[\\dA-Z]+(-?[\\dA-Z])*", scanner,
                "Zona", "Sólo se permiten mayúsculas y guiones");
    }

    private String getValidModel() {
        return ConsoleUtils.getValidString(".{1,280}", scanner, "Modelo", "Mmáximo 280 caracteres");
    }

    private Vehicle.Type getValidType() {
        String roleRegex = Arrays.stream(Vehicle.Type.values()).map(Objects::toString).collect(Collectors.joining("|"));
        return Vehicle.Type.valueOf(ConsoleUtils.getValidString(roleRegex, scanner, "Tipo",
                "Debe ser uno de " + roleRegex));
    }
}
