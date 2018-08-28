package mx.unam.ciencias.fbd;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.repository.StaffRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Logger;

/*
 * Main application
 */
public class App {
    /**
     * Logger.
     */
    private final static Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        StaffRepository repo = new StaffRepository();
        try {
            System.out.println("Printing all records.");
            repo.findAll().forEach(r -> System.out.println(r.toString()));
            System.out.println("Persisting new record.");
            repo.save(new Staff("Daniel", Staff.Sex.MALE, LocalDate.now(), LocalDate.now(), Staff.Role.LIUTENANT));
            System.out.println("Persisting new record.");
            repo.save(new Staff("Julia", Staff.Sex.FEMALE, LocalDate.now(), LocalDate.now(), Staff.Role.OFFICER));
            System.out.println("Printing all records.");
            repo.findAll().forEach(s -> System.out.println(s.toString()));
        } catch (IOException e) {
            LOGGER.severe(e.getLocalizedMessage());
        }
    }
}
