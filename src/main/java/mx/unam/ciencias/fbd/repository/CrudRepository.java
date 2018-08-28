package mx.unam.ciencias.fbd.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    /**
     * Creates or saves an entity to the repository.
     *
     * @param entity the entity that will be persisted, must not be null.
     * @return the saved entity.
     * @throws IOException if something goes wrong when reading or writing the persisted data.
     */
    T save(T entity) throws IOException;

    /**
     * Retrieves an entity from the repository using its id.
     *
     * @param id the entity's id, must not be null.
     * @return the entity with a matching id or {@link Optional#empty()} if no such entity exists.
     */
    Optional<T> findById(ID id);

    /**
     * @return all the persisted entities.
     */
    List<T> findAll();

    /**
     * Deletes an entity from the repository using its id.
     *
     * @param id the entity's id, must not be null.
     */
    void deleteById(ID id);
}
