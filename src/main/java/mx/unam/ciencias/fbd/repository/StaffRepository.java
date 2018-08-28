package mx.unam.ciencias.fbd.repository;

import mx.unam.ciencias.fbd.domain.Staff;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Concrete Staff entities repository. Provides transformer functions from and to CSVRecords. It also set's the
 * entities' schema.
 */
public class StaffRepository extends AbstractCSVCrudRepository<Staff, UUID> {
    private static final String STAFF_HOME = "staff.csv";
    private static final String[] SCHEMA = {"ID", "NAME", "SEX", "DOB", "DOH", "ROLE", "SUPERVISOR_ID"};

    public StaffRepository() {
        super(STAFF_HOME);
    }

    @Override
    List<String> asRecord(Staff entity) {
        String supervisor = entity.getSupervisorID() == null ? "" : entity.getSupervisorID().toString();
        return Arrays.asList(
                entity.getId().toString(),
                entity.getName(),
                entity.getSex().toString(),
                entity.getDob().toString(),
                entity.getDoh().toString(),
                entity.getRole().toString(),
                supervisor);
    }

    @Override
    UUID getId(Staff entity) {
        return entity.getId();
    }

    @Override
    public Staff ofRecord(CSVRecord record) {
        UUID id = UUID.fromString(record.get("ID"));
        String name = record.get("NAME");
        Staff.Sex sex = Staff.Sex.valueOf(record.get("SEX"));
        LocalDate dob = LocalDate.parse(record.get("DOB"));
        LocalDate doh = LocalDate.parse(record.get("DOH"));
        Staff.Role role = Staff.Role.valueOf(record.get("ROLE"));
        String supervisorIdStr = record.get("SUPERVISOR_ID");
        Staff result = new Staff(name, sex, dob, doh, role);
        result.setId(id);
        if (!supervisorIdStr.isEmpty()) {
            UUID supervisorId = UUID.fromString(supervisorIdStr);
            result.setSupervisorID(supervisorId);
        }
        return result;
    }

    @Override
    protected String[] getSchema() {
        return SCHEMA;
    }
}
