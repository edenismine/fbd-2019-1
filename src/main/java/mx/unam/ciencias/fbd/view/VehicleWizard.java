package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.domain.Vehicle;

import java.util.Optional;
import java.util.Scanner;

public class VehicleWizard implements Wizard<Vehicle> {
    private final Scanner scanner;

    public VehicleWizard(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Vehicle> create() {
        // TODO implement
        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> edit(Vehicle entity) {
        // TODO implement
        return Optional.empty();
    }
}
