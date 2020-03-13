package service;

import model.Task;

import java.util.Set;

/**
 * ITask provide method signature for crud operation of Task for TodoList program.
 *
 * @author Kshitiz Bista
 */
public interface ITask {
    /**
     * Saves(Add or Update) a given task. Use the returned task instance for further operations.
     *
     * @param task must not be {@literal null}.
     * @return the saved task.
     * @throws RuntimeException if {@code task} is {@literal null}.
     */
    Task addOrUpdate(Task task);

    /**
     * Deletes the task with given id.
     *
     * @param id must not be {@literal null}.
     */
    void deleteById(int id);

    /**
     * Returns all instances of the type.
     *
     * @return all tasks
     */
    Set<Task> findAll();

    /**
     * Retrieves a task by its id.
     *
     * @param id must not be {@literal null}.
     * @return the task with the given id or {@literal null} if none found
     */
    Task findById(int id);

    /**
     * Returns the number of tasks available.
     *
     * @return the number of total tasks
     */
    int count();

    /**
     * Returns whether a task with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if a task with the given id exists, {@literal false} otherwise.
     */
    boolean existsById(int id);
}
