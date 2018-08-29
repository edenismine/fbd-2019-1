package mx.unam.ciencias.fbd.view;

import java.util.Optional;

/**
 * Guides the user through adding or editing entries.
 */
public interface Wizard<T> {
    /**
     * Guides the user through the creation of a new enity.
     *
     * @return the new entity or {@link Optional#empty()} if the process is unsuccessful.
     */
    Optional<T> create();

    /**
     * Guides the user through the editing of a given entity.
     *
     * @param entity the entity that needs to be edited.
     * @return the modified entity or {@link Optional#empty()} if the process is unsuccessful.
     */
    Optional<T> edit(T entity);
}
