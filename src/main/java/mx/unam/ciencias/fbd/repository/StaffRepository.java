package mx.unam.ciencias.fbd.repository;

import mx.unam.ciencias.fbd.domain.Staff;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StaffRepository implements CrudRepository<Staff, UUID> {
    private static final String STAFF_HOME = "staff.csv";

    @Override
    public Staff save(Staff entity) throws IOException {
        // read the file
        File data = new File(STAFF_HOME);
        Collection<CSVRecord> records = CSVParser.parse(data, Charset.defaultCharset(), CSVFormat.DEFAULT).getRecords();
        // look for record with matching id
        // if record is found, set flag to true
        records.removeIf(record -> entity.getId().toString().equals(record.get("ID")));

        // open the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(data));
        CSVPrinter out = new CSVPrinter(writer, CSVFormat.DEFAULT);
        // close the file
        // return the persisted entity
        return entity;
    }

    @Override
    public Optional<Staff> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<Staff> findAll() {
        return null;
    }

    @Override
    public void deleteById(UUID uuid) {

    }
}
