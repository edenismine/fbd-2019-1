package mx.unam.ciencias.fbd;

import mx.unam.ciencias.fbd.domain.Staff;
import mx.unam.ciencias.fbd.service.StaffService;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.logging.Logger;

/*
 * Main application
 */
public class App {
    /**
     * App's root directory.
     */
    public final static Path ROOT = FileSystems.getDefault().getPath("data").toAbsolutePath();
    /**
     * Logger.
     */
    private final static Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        StaffService staffService = StaffService.getInstance();
        Staff s1 = new Staff("Daniel", Staff.Sex.MALE, LocalDate.now(), LocalDate.now(), Staff.Role.OFFICER);
        Staff s2 = new Staff("Andrea", Staff.Sex.MALE, LocalDate.now(), LocalDate.now(), Staff.Role.OFFICER);

        System.out.println("Adding " + s1);
        staffService.save(s1);
        staffService.findAll().forEach(System.out::println);

        System.out.println("Adding " + s2);
        staffService.save(s2);
        staffService.findAll().forEach(System.out::println);

        System.out.println("Deleted " + s2);
        staffService.deleteById(s2.getId());
        staffService.findAll().forEach(System.out::println);
    }
}
