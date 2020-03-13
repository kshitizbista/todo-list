package service;

import model.Task;

import java.util.*;

/**
 * This class provides implementation of the {@code ITask} interface.
 *
 * @author Kshitiz Bista
 * @see ITask
 */
public class TaskService implements ITask {

    /**
     * Map for storing instances of tasks as key value pair. Key is id of the task while value is task object.
     */
    private Map<Integer, Task> map = new HashMap<>();

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation sets instance of task to {@link TaskService#map} field using task id as key. If
     * param {@code task} id is{@literal null} then creates new id using {@link TaskService#getNextId()}. If id is
     * already present then replaces the old value.
     */
    @Override
    public Task addOrUpdate(Task task) {
        if (task != null) {
            if (task.getId() == null) {
                task.setId(getNextId());
            }
            map.put(task.getId(), task);
        } else {
            throw new RuntimeException("Object cannot be null");
        }
        return task;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation removes task from {@link TaskService#map}
     */
    @Override
    public void deleteById(int id) {
        map.remove(id);
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation returns all entities from {@link TaskService#map} as HashSet type
     */
    @Override
    public Set<Task> findAll() {
        return new HashSet<>(map.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task findById(int id) {
        return map.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int count() {
        return map.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(int id) {
        if (findById(id) != null) return true;
        return false;
    }

    /**
     * Checks maximum element of {@link TaskService#map} and retrieves the next number.
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
