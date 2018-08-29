package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.repository.StaffRepository;
import mx.unam.ciencias.fbd.service.StaffService;
import mx.unam.ciencias.fbd.util.ConsoleUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StaffWizard implements Wizard<Staff> {
    private final Scanner scanner;

    public StaffWizard(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Staff> edit(Staff entity) {
        String staffSchemaRegex =
                Arrays.stream(StaffRepository.Schema.values()).map(Enum::toString).collect(Collectors.joining("|"));
        Panel editor = new Panel("STAFF:EDIT", scanner, staffSchemaRegex);
        editor.setHelp(() -> {
            System.out.println("\nUso:");
            System.out.println("\t" + StaffRepository.Schema.NAME);
            System.out.println("\t  Edita el nombre del elemento.");
            System.out.println("\t" + StaffRepository.Schema.SEX);
            System.out.println("\t  Edita el sexo del elemento.");
            System.out.println("\t" + StaffRepository.Schema.DOB);
            System.out.println("\t  Edita la fecha de nacimiento del elemento.");
            System.out.println("\t" + StaffRepository.Schema.DOH);
            System.out.println("\t  Edita la fecha de contratación del elemento");
            System.out.println("\t" + StaffRepository.Schema.ROLE);
            System.out.println("\t  Edita el rol del elemento.");
            System.out.println("\t" + StaffRepository.Schema.SUPERVISOR_ID);
            System.out.println("\t  Edita el supervisor del elemento.");
            System.out.println("\t" + Panel.EOE);
            System.out.println("\t  Regresa al menú anterior.\n");
        });
        editor.setHandler(command -> {
            if (!command.equals(Panel.EOE)) {
                StaffRepository.Schema routine = StaffRepository.Schema.valueOf(command);
                switch (routine) {
                    case NAME:
                        System.out.println("Nombre actual: " + entity.getName());
                        entity.setName(getValidName());
                        break;
                    case SEX:
                        System.out.println("Sexo actual: " + entity.getSex());
                        entity.setSex(getValidSex());
                        break;
                    case DOB:
                        System.out.println("Fecha de nacimiento actual: " + entity.getDob());
                        entity.setDob(ConsoleUtils.getValidDate(scanner, "Fecha de nacimiento"));
                        break;
                    case DOH:
                        System.out.println("Fecha de contratación actual: " + entity.getDoh());
                        entity.setDob(ConsoleUtils.getValidDate(scanner, "Fecha de contratación"));
                        break;
                    case ROLE:
                        System.out.println("Rol actual: " + entity.getRole());
                        entity.setRole(getValidRole());
                        break;
                    case SUPERVISOR_ID:
                        System.out.println("Supervisor actual: " + entity.getSupervisorID());
                        Optional<String> supervisorIdStr = getValidSupervisorID(entity.getRole());
                        if (supervisorIdStr.isPresent()) {
                            entity.setSupervisorID(UUID.fromString(supervisorIdStr.get()));
                        } else {
                            System.out.println("Ningún elemento puede asigarse como superior.");
                        }
                    case ID:
                        System.out.println("Se está editando al elemento " + entity.getId());
                }
            }
        });
        editor.run();
        return Optional.of(entity);
    }

    @Override
    public Optional<Staff> create() {
        System.out.println("Se intentará crear un nuevo elemento en la base de datos.");
        String name = getValidName();
        Staff.Sex sex = getValidSex();
        LocalDate dob = ConsoleUtils.getValidDate(scanner, "Fecha de nacimiento");
        LocalDate doh = ConsoleUtils.getValidDate(scanner, "Fecha de contratación");
        Staff.Role role = getValidRole();
        Staff result = new Staff(name, sex, dob, doh, role);

        String addSupervisor = ConsoleUtils.getValidString("[yY][eE][sS]|[nN][oO]|[sS][iI]", scanner,
                "Agregar superior?", "Por favor usa el formato si|no");
        if (addSupervisor.toLowerCase().charAt(0) != 'n') {
            Optional<String> supervisorIdStr = getValidSupervisorID(role);
            if (supervisorIdStr.isPresent()) {
                result.setSupervisorID(UUID.fromString(supervisorIdStr.get()));
            } else {
                System.out.println("Ningún elemento puede asigarse como superior.");
            }
        }
        return Optional.of(result);
    }

    private Optional<String> getValidSupervisorID(Staff.Role role) {
        String result = null;
        List<String> canBeSuperior = StaffService.getInstance().findAll()
                .filter(staff -> staff.getRole().ordinal() > role.ordinal())
                .map(staff -> staff.getId().toString())
                .collect(Collectors.toList());
        if (!canBeSuperior.isEmpty()) {
            System.out.println("Selecciona la matrícula del superior de la siguiente lista usando el índice:");
            for (int i = 0; i < canBeSuperior.size(); i++)
                System.out.println(String.format("%d\t%s", i, canBeSuperior.get(i)));
            int validIndex = ConsoleUtils.getValidInt(scanner, "Superior",
                    integer -> integer >= 0 && integer < canBeSuperior.size());
            result = canBeSuperior.get(validIndex);
        }
        return Optional.ofNullable(result);
    }

    private Staff.Role getValidRole() {
        String roleRegex = Arrays.stream(Staff.Role.values()).map(Objects::toString).collect(Collectors.joining("|"));
        return Staff.Role.valueOf(ConsoleUtils.getValidString(roleRegex, scanner, "Rol",
                "Debe ser uno de " + roleRegex));
    }

    private Staff.Sex getValidSex() {
        String sexRegex = Arrays.stream(Staff.Sex.values()).map(Objects::toString).collect(Collectors.joining("|"));
        return Staff.Sex.valueOf(ConsoleUtils.getValidString(sexRegex, scanner, "Sexo",
                "Debe ser uno de " + sexRegex));
    }

    private String getValidName() {
        return ConsoleUtils.getValidString("[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*", scanner, "Nombre", "INVALID");
    }
}
