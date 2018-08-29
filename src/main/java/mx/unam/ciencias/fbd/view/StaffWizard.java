package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.domain.Staff;

import java.util.Optional;
import java.util.Scanner;

public class StaffWizard implements Wizard<Staff> {
    private final Scanner scanner;

    public StaffWizard(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Staff> create() {
        // TODO implement
        return Optional.empty();
    }

    @Override
    public Optional<Staff> edit(Staff entity) {
        // TODO implement
        return Optional.empty();
    }
}
