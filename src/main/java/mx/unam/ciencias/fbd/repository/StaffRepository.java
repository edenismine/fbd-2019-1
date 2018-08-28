package mx.unam.ciencias.fbd.repository;

import mx.unam.ciencias.fbd.domain.Staff;
import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.util.UUID;

public class StaffRepository extends AbstractCSVCrudRepository<Staff, UUID> {
    private static final String STAFF_HOME = "staff.csv";

    public StaffRepository() {
        super(STAFF_HOME);
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
        return Staff.getSCHEMA();
    }
}
