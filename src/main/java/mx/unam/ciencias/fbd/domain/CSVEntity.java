package mx.unam.ciencias.fbd.domain;

import org.apache.commons.csv.CSVRecord;

/**
 * Implementors can be exported as a list of strings for persisting.
 */
public interface CSVEntity<T> {
    /**
     * @return the schema of the calling entity, aka a list of the entity's fields.
     */
    String[] schema();

    /**
     * @return the calling entity as a list of strings.
     */
    String[] asRecord();

    /**
     * @return the provided record as an entity.
     */
    T asObject(CSVRecord record);

    /**
     * @return the entity's id.
     */
    Object getId();
}
