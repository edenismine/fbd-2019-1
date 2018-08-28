package mx.unam.ciencias.fbd.repository;

import mx.unam.ciencias.fbd.util.Validate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
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
    private final static Logger LOGGER = Logger.getLogger(AbstractCSVCrudRepository.class.getName());
    /**
     * File where the repository if physically stored.
     */
    private final URL REPO_HOME;
    /**
     * CSV format.
     */
    private final CSVFormat CSV_FORMAT;

    /**
     * Initializes repo.
     *
     * @param path   path to repo's physical location.
     * @param schema an enum with the entity's schema.
     */
    AbstractCSVCrudRepository(String path, Class<? extends Enum<?>> schema) {
        this.REPO_HOME = AbstractCSVCrudRepository.class.getClassLoader().getResource(path);
        this.CSV_FORMAT = CSVFormat.DEFAULT.withHeader(schema);
    }

    @Override
    public S save(S entity) throws IOException {
        LOGGER.info("Persisting entity: " + entity);
        Validate.notNull(entity);
        // read all the records
        Collection<CSVRecord> records = readRecords();

        // look for record with matching id and if found, delete it.
        boolean needOverwrite = records.removeIf(record -> getId(entity).toString().equals(record.get("ID")));

        // if overwrite is needed recreate the file.
        recreateFileIfNeeded(needOverwrite);

        // Create buffered writer
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(REPO_HOME.getFile())));
        CSVPrinter out = new CSVPrinter(writer, CSV_FORMAT);

        // if file was recreated, add all the records
        if (needOverwrite) {
            out.printRecords(records);
        }

        // save the entity
        out.printRecord(asRecord(entity));

        // close the file
        out.close();

        // return the persisted entity
        return entity;
    }

    @Override
    public Optional<S> findById(ID id) throws IOException {
        Validate.notNull(id);
        // read the file
        Collection<CSVRecord> records = readRecords();

        // look for record with matching id and if found, fetch it.
        CSVRecord target = null;
        for (CSVRecord record : records) {
            if (id.toString().equals(record.get("ID"))) {
                target = record;
                break;
            }
        }
        S result = ofRecord(target);
        return Optional.ofNullable(result);
    }

    @Override
    public Stream<S> findAll() throws IOException {
        // read the file
        Collection<CSVRecord> records = readRecords();
        // map the records to entities
        return records.stream().map(this::ofRecord);
    }

    @Override
    public boolean deleteById(ID id) throws IOException {
        // read the file
        Collection<CSVRecord> records = readRecords();

        // look for record with matching id and if found, delete it.
        boolean needOverwrite = records.removeIf(record -> id.toString().equals(record.get("ID")));

        // if overwrite is needed recreate the file.
        recreateFileIfNeeded(needOverwrite);

        BufferedWriter writer = new BufferedWriter(new FileWriter(REPO_HOME.getFile()));
        CSVPrinter out = new CSVPrinter(writer, CSV_FORMAT);

        // if file was recreated, add all the records
        if (needOverwrite) {
            out.printRecords(records);
        }
        // close the file
        out.close();
        // return modified state
        return needOverwrite;
    }

    /**
     * Loads all the records from disk.
     *
     * @return all the records.
     * @throws IOException if file not found or unavailable.
     */
    private Collection<CSVRecord> readRecords() throws IOException {
        InputStream in = REPO_HOME.openStream();
        List<CSVRecord> result =
                CSVParser.parse(
                        in,
                        Charset.defaultCharset(),
                        CSVFormat.DEFAULT
                                .withFirstRecordAsHeader()
                                .withIgnoreHeaderCase()
                                .withTrim()
                ).getRecords();
        in.close();
        return result;
    }

    /**
     * Recreates the repo file if needed.
     *
     * @param needOverwrite flag that signals if the file should be recreated.
     * @throws IOException if the file needs to be recreated but we can't.
     */
    private void recreateFileIfNeeded(boolean needOverwrite) throws IOException {
        if (needOverwrite) {
            File data = new File(REPO_HOME.getFile());
            if (data.delete() && data.createNewFile()) {
                LOGGER.severe("File " + REPO_HOME + " was recreated.");
            } else {
                String msg = "File " + REPO_HOME + "was not recreated.";
                LOGGER.severe(msg);
                throw new IOException(msg);
            }
        }
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
