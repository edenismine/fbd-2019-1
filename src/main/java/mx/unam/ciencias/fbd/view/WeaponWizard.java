package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.domain.Weapon;
import mx.unam.ciencias.fbd.repository.WeaponRepository;
import mx.unam.ciencias.fbd.service.StaffService;
import mx.unam.ciencias.fbd.util.ConsoleUtils;

import java.util.*;
import java.util.stream.Collectors;

public class WeaponWizard implements Wizard<Weapon> {
    private final Scanner scanner;

    public WeaponWizard(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Weapon> create() {
        Weapon.Type type = getValidType();
        String description = getValidDescription();
        Optional<UUID> driverId = getValidUserID();
        if (driverId.isPresent()) {
            return Optional.of(new Weapon(type, description, driverId.get()));
        } else {
            System.out.println("No se encontró ningún elemento, no se puede crear un arma.");
            return Optional.empty();
        }
    }

    @Override
    public Optional<Weapon> edit(Weapon entity) {
        // Obtain entity schema and create regex
        String vehicleSchemaRegex =
                Arrays.stream(WeaponRepository.Schema.values()).map(Enum::toString).collect(Collectors.joining("|"));
        // Create edit panel
        Panel editor = new Panel("WEAPON:EDIT", scanner, vehicleSchemaRegex);
        // Create help information
        editor.setHelp(() -> {
            System.out.println("\nUso:");
            System.out.println("\t" + WeaponRepository.Schema.TYPE);
            System.out.println("\t  Edita el tipo de arma.");
            System.out.println("\t" + WeaponRepository.Schema.DESCRIPTION);
            System.out.println("\t  Edita la descripción del arma.");
            System.out.println("\t" + WeaponRepository.Schema.USER_ID);
            System.out.println("\t  Edita el elemento responsable del arma.");
            System.out.println("\t" + Panel.EOE);
            System.out.println("\t  Regresa al menú anterior.\n");
        });
        // Handle commands
        editor.setHandler(command -> {
            if (!command.equals(Panel.EOE)) {
                WeaponRepository.Schema routine = WeaponRepository.Schema.valueOf(command);
                switch (routine) {
                    case TYPE:
                        System.out.println("Tipo actual: " + entity.getType());
                        entity.setType(getValidType());
                        break;
                    case DESCRIPTION:
                        System.out.println("Descripción actual: " + entity.getDescription());
                        entity.setDescription(getValidDescription());
                        break;
                    case USER_ID:
                        System.out.println("Responsable actual: " + entity.getUserId());
                        Optional<UUID> driverId = getValidUserID();
                        if (driverId.isPresent()) {
                            entity.setUserId(driverId.get());
                        } else {
                            System.out.println("Ningún elemento puede asignarse como responsable.");
                        }
                        break;
                    case ID:
                        System.out.println("ID del elemento actual: " + entity.getId());
                        break;
                }
            }
        });
        // Run editor panel
        editor.run();
        // return entity
        return Optional.of(entity);
    }

    private String getValidDescription() {
        return ConsoleUtils.getValidString(".{3,280}", scanner, "Descripción", "Mínimo 3 caracteres, máximo 280");
    }

    private Weapon.Type getValidType() {
        String roleRegex = Arrays.stream(Weapon.Type.values()).map(Objects::toString).collect(Collectors.joining("|"));
        return Weapon.Type.valueOf(ConsoleUtils.getValidString(roleRegex, scanner, "Tipo",
                "Debe ser uno de " + roleRegex));
    }

    private Optional<UUID> getValidUserID() {
        UUID result = null;
        List<UUID> drivers = StaffService.getInstance().findAll()
                .map(Staff::getId)
                .collect(Collectors.toList());
        if (!drivers.isEmpty()) {
            System.out.println("Selecciona el ID del responsable de la siguiente lista usando el índice:");
            for (int i = 0; i < drivers.size(); i++)
                System.out.println(String.format("%d\t%s", i, drivers.get(i)));
            int validIndex = ConsoleUtils.getValidInt(scanner, "Usuario",
                    integer -> integer >= 0 && integer < drivers.size());
            result = drivers.get(validIndex);
        }
        return Optional.ofNullable(result);
    }
}
