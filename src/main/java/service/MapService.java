package service;

import java.util.Set;

/**
 * This interface provide method signature for crud operation of generic type.
 *
 * @author Kshitiz Bista
 */

public interface MapService<T, ID> {
    /**
     * Saves(Add or Update) a given object. Use the returned object instance for further operations.
     *
     * @param object must not be {@literal null}.
     * @return the saved object.
     * @throws RuntimeException if {@code object} is {@literal null}.
     */
    T save(T object);

    /**
     * Deletes the object with given id.
     *
     * @param id must not be {@literal null}.
     */
    void deleteById(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return all objects
     */
    Set<T> findAll();

    /**
     * Retrieves a object by its id.
     *
     * @param id must not be {@literal null}.
     * @return the object with the given id or {@literal null} if none found
     */
    T findById(ID id);

    /**
     * Returns the number of object available.
     *
     * @return the number of total objects
     */
    int count();

    /**
     * Returns whether a object with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if a object with the given id exists, {@literal false} otherwise.
     */
    boolean existsById(ID id);

}
