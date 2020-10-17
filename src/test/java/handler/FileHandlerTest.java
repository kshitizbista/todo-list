package handler;


import model.Status;
import model.Task;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FileHandlerTest {

    @Test
    public void testReadAndWriteFile() throws IOException, ClassNotFoundException {
        String fileName = "test-todolist.txt";
        Set<Task> tasks = new HashSet<>();

        Task task1 = new Task(1,
                "Read History Book",
                "Habits", LocalDate.now(),
                LocalDate.now().plusMonths(1),
                Status.OPEN);
        Task task2 = new Task(1,
                "Do Morning Yoga",
                "Habits", LocalDate.now(),
                LocalDate.now().plusMonths(2),
                Status.OPEN);
        Task task3 = new Task(1,
                "Buy Running Shoes",
                "Shopping", LocalDate.now(),
                LocalDate.now().plusMonths(5),
                Status.DONE);
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);


        FileHandler.writeFile(tasks, fileName);
        Set<Task> returnedTask = FileHandler.readFile(fileName);

        assertEquals(tasks, returnedTask);

    }

}