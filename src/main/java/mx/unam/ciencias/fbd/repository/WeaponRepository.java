package mx.unam.ciencias.fbd.repository;

import mx.unam.ciencias.fbd.domain.Weapon;
import mx.unam.ciencias.fbd.util.Safe;
import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Weapon repository.
 */
public class WeaponRepository extends AbstractCSVCrudRepository<Weapon, UUID> {
    /**
     * Physical location of the repository.
     */
    private static final String CSV_FILE = "weapon.csv";
    /**
     * Singleton instance.
     */
    private static final WeaponRepository singleton = new WeaponRepository();

    /**
     * Constructs a weapon repository.
     */
    private WeaponRepository() {
        super(CSV_FILE, Schema.class, Logger.getLogger(WeaponRepository.class.getName()));
    }

    /**
     * @return the unique instance.
     */
    public static WeaponRepository getInstance() {
        return singleton;
    }

    @Override
    List<String> asRecord(Weapon entity) {
        return Arrays.asList(
                Safe.safeToString(entity.getId()),
                Safe.safeToString(entity.getType()),
                Safe.safeToString(entity.getDescription()),
                Safe.safeToString(entity.getUserId())
        );
    }

    @Override
    UUID getId(Weapon entity) {
        return entity.getId();
    }

    @Override
    public Weapon ofRecord(CSVRecord record) {
        UUID id = UUID.fromString(record.get(Schema.ID));
        Weapon.Type type = Weapon.Type.valueOf(record.get(Schema.TYPE));
        String description = record.get(Schema.DESCRIPTION);
        UUID userId = UUID.fromString(record.get(Schema.USER_ID));
        Weapon result = new Weapon(type, description, userId);
        result.setId(id);
        return result;
    }

    /**
     * Weapon schema (columns).
     */
    public enum Schema {
        ID, TYPE, DESCRIPTION, USER_ID
    }
}
