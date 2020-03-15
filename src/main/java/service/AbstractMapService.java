package service;


import model.BaseEntity;

import java.util.*;

/**
 * AbstractMapService implements methods for crud operation (addition,update and deletion) of map based entity.
 * This class provides a skeletal implementation of the {@code MapService} interface, to minimize the effort
 * required to implement this interface.
 *
 * @author Kshitiz Bista
 * @see MapService
 */
public abstract class AbstractMapService<T extends BaseEntity, ID extends Integer> implements MapService<T, ID> {
    /**
     * Map for storing instances of object as key-value pair. Key is id of the object while value is the object itself.
     */
    protected Map<Integer, T> map = new HashMap<>();

    /**
     * {@inheritDoc}
     * Sets instance of object as value to {@link AbstractMapService#map} using object
     * id as key. If param {@code object} id is {@literal null} then creates new id using
     * {@link AbstractMapService#getNextId()}.
     */
    @Override
    public T save(T object) {
        if (object != null) {
            if (object.getId() == null) {
                object.setId(getNextId());
            }
            map.put(object.getId(), object);
        } else {
            throw new RuntimeException("Object cannot be null");
        }
        return object;
    }

    /**
     * {@inheritDoc}
     * if the object with given id is present in {@link AbstractMapService#map} then removes it, otherwise do nothing.
     */
    @Override
    public void deleteById(ID id) {
        map.remove(id);
    }

    /**
     * {@inheritDoc}
     * returns all values from {@link AbstractMapService#map} as set
     */
    @Override
    public Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findById(ID id) {
        return map.get(id);
    }

    /**
     * {@inheritDoc}
     * retrieves maximum number of element in {@link AbstractMapService#map}
     */
    @Override
    public int count() {
        return map.size();
    }

    /**
     * {@inheritDoc}
     * uses {@link AbstractMapService#findById} for operation.
     */
    @Override
    public boolean existsById(ID id) {
        if (findById(id) != null) return true;
        return false;
    }

    /**
     * Checks maximum element of {@link AbstractMapService#map} and retrieves the next number.
     *
     * @return next id
     */
    private Integer getNextId() {
        Integer nextId;
        try {
            nextId = Collections.max(map.keySet()) + 1;
        } catch (NoSuchElementException ex) {
            nextId = 1;
        }
        return nextId;
    }
}
