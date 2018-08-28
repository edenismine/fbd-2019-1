package mx.unam.ciencias.fbd.repository;

import java.util.Optional;
import java.util.stream.Stream;

public interface CrudRepository<T, ID> {
    /**
     * Creates or saves an entity to the repository.
     *
     * @param entity the entity that will be persisted, must not be null.
     * @return the saved entity.
     */
    T save(T entity);

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
    Stream<T> findAll();

    /**
     * Deletes an entity from the repository using its id.
     *
     * @param id the entity's id, must not be null.
     */
    boolean deleteById(ID id);
}
