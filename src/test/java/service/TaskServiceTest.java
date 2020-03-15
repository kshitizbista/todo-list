package service;

import model.Status;
import model.Task;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.*;

public class TaskServiceTest {

    private TaskService taskService;
    final Integer taskId = 1;
    final String title = "Make lunch and dinner";
    final String project = "Home Project";
    final LocalDate dueDate = LocalDate.now();

    @Before
    public void setUp() {
        taskService = new TaskService();
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(title);
        task.setProject(project);
        task.setDueDate(dueDate);
        taskService.save(task);
    }

    @Test
    public void addNewTaskTest() {
        Task task = new Task();
        task.setTitle("Learn Unit Test");
        task.setProject("Bootcamp");
        task.setDueDate(LocalDate.now());

        Task savedTask = taskService.save(task);
        assertNotNull(savedTask);
        assertNotNull(savedTask.getId());
    }

    @Test()
    public void addNullTaskObjectTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> taskService.save(null));
        String expectedMessage = "Object cannot be null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateTaskTest() {
        String newTitle = "Do Homework";
        String newProject = "School Project";
        Status newStatus = Status.DONE;
        LocalDate newDueDate = dueDate.plusDays(3);

        Task newTask = new Task();
        newTask.setId(taskId);
        newTask.setTitle(newTitle);
        newTask.setProject(newProject);
        newTask.setStatus(newStatus);
        newTask.setDueDate(newDueDate);
        Task updatedTask = taskService.save(newTask);

        assertEquals(taskId , newTask.getId());
        assertEquals(newProject, updatedTask.getProject());
        assertEquals(newTitle, updatedTask.getTitle());
        assertEquals(newStatus, updatedTask.getStatus());
        assertEquals(newDueDate, updatedTask.getDueDate());
    }

    @Test
    public void deleteByIdTest() {
        taskService.deleteById(taskId);
        assertEquals(0, taskService.count());
    }

    @Test
    public void findAllTest() {
        Task task = new Task();
        task.setTitle("Learn Unit Test");
        task.setProject("Bootcamp");
        task.setDueDate(LocalDate.now());
        taskService.save(task);

        Set<Task> tasks = taskService.findAll();
        assertEquals(2, tasks.size());
    }

    @Test
    public void findByIdTest() {
        Task task = taskService.findById(taskId);
        assertNotNull(task);
        assertEquals(Integer.valueOf(1), task.getId());
    }

    @Test
    public void findByIdNullTest() {
        Task task = taskService.findById(2);
        assertNull(task);
    }

    @Test
    public void countTest() {
        int value = taskService.count();
        assertEquals(1, value);
    }

    @Test
    public void existsByIdTest() {
        boolean result = taskService.existsById(taskId);
        assertTrue(result);
    }

    @Test
    public void doesNotExistsByIdTest() {
        boolean result = taskService.existsById(2);
        assertFalse(result);
    }
}
