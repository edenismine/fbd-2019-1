package mx.unam.ciencias.fbd.view;

import mx.unam.ciencias.fbd.domain.Weapon;

import java.util.Optional;
import java.util.Scanner;

public class WeaponWizard implements Wizard<Weapon> {
    private final Scanner scanner;

    public WeaponWizard(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Optional<Weapon> create() {
        // TODO implement
        return Optional.empty();
    }

    @Override
    public Optional<Weapon> edit(Weapon entity) {
        // TODO implement
        return Optional.empty();
    }
}
