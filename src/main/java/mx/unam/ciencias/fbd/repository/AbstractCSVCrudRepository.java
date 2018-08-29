package mx.unam.ciencias.fbd.repository;

import mx.unam.ciencias.fbd.App;
import mx.unam.ciencias.fbd.util.Safe;
import mx.unam.ciencias.fbd.util.Validate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Abstract CSV CRUD repository, implements all the basic crud functions. Implementors must provide entity schema and
 * object transformation.
 *
 * @param <S>  The type of the resources served by this repository.
 * @param <ID> The type of the identifier of the server resources.
 */
public abstract class AbstractCSVCrudRepository<S, ID> implements CrudRepository<S, ID> {
    /**
     * Logger.
     */
    private final Logger LOGGER;
    /**
     * File where the repository if physically stored.
     */
    private final Path REPO_HOME;
    /**
     * CSV format.
     */
    private final CSVFormat CSV_PARSE_FORMAT;

    /**
     * Initializes repo.
     *
     * @param csvFile repo's file name.
     * @param schema  an enum with the entity's schema.
     */
    AbstractCSVCrudRepository(String csvFile, Class<? extends Enum<?>> schema, Logger logger) {
        this.LOGGER = logger;
        this.REPO_HOME = Paths.get(App.ROOT.toString(), csvFile);
        this.CSV_PARSE_FORMAT = CSVFormat.DEFAULT
                .withHeader(schema)
                .withIgnoreHeaderCase()
                .withTrim();
    }

    @Override
    public S save(S entity) {
        Validate.notNull(entity);
        LOGGER.fine("Persisting entity: " + entity);
        S result = entity;
        // delete record if it exists
        try {
            Collection<CSVRecord> records = readRecords();
            if (records.removeIf(r -> r.get("ID").equals(Safe.safeToString(getId(entity)))))
                writeRecords(records);
        } catch (IOException e) {
            result = null;
            LOGGER.severe(e.getMessage());
        }
        // append record
        if (result != null) {
            try {
                appendRecord(asRecord(result));
            } catch (IOException e) {
                result = null;
                LOGGER.severe(e.getMessage());
            }
        }

        // return the persisted entity or null if failed.
        return result;
    }

    @Override
    public Optional<S> findById(ID id) {
        Validate.notNull(id);
        try {
            // read the file
            List<CSVRecord> records = readRecords();
            // look for record with matching id and if found, fetch it.
            for (CSVRecord record : records) {
                if (id.toString().equals(record.get("ID"))) {
                    return Optional.ofNullable(ofRecord(record));
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Stream<S> findAll() {
        // read the file
        Collection<CSVRecord> records = null;
        try {
            records = readRecords();
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        // map the records to entities
        return records == null ? Stream.empty() : records.stream().map(this::ofRecord);
    }

    @Override
    public boolean deleteById(ID id) {
        boolean changed = false;
        try {
            Collection<CSVRecord> records = readRecords();
            changed = records.removeIf(r -> r.get("ID").equals(Safe.safeToString(id)));
            if (changed)
                writeRecords(records);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return changed;
    }

    /**
     * Loads all the records from disk.
     *
     * @return all the records.
     * @throws IOException if file not found or unavailable.
     */
    private List<CSVRecord> readRecords() throws IOException {
        Reader reader = Files.newBufferedReader(REPO_HOME);
        List<CSVRecord> result = CSVParser.parse(reader, CSV_PARSE_FORMAT).getRecords();
        reader.close();
        return result;
    }

    /**
     * Overwrites the repository file with a collection of records.
     *
     * @param records records to be written to the repository file.
     * @throws IOException if the file cannot be written.
     */
    private void writeRecords(Collection<CSVRecord> records) throws IOException {
        File file = new File(REPO_HOME.toString());
        if (file.delete() && file.createNewFile()) {
            Writer writer = Files.newBufferedWriter(REPO_HOME, StandardOpenOption.WRITE);
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            printer.printRecords(records);
            writer.close();
        }
    }

    /**
     * Appends a record to the repository file.
     *
     * @param record a record as a list of strings.
     * @throws IOException if the file cannot be written.
     */
    private void appendRecord(List<String> record) throws IOException {
        Writer writer = Files.newBufferedWriter(REPO_HOME, StandardOpenOption.APPEND);
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord(record);
        writer.close();
    }

    /**
     * Retrieves a list of the entity's attributes as strings.
     *
     * @param entity the entity.
     * @return the list of the entity's attributes as strings.
     */
    abstract List<String> asRecord(S entity);

    /**
     * Retrieves the id of the provided entity.
     *
     * @param entity the entity.
     * @return the id of the provided entity.
     */
    abstract ID getId(S entity);

    /**
     * Generates an entity object from a csv record.
     *
     * @param record the given record.
     * @return the entity.
     */
    public abstract S ofRecord(CSVRecord record);
}
