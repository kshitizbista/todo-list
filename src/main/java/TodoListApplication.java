import handler.FileHandler;
import handler.TodoHandler;
import model.Task;
import service.MapService;
import service.TaskService;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class TodoListApplication {

    private static final String FILE_NAME = "todolist.txt";
    private int option;

    private MapService<Task, Integer> loadDataFromFile() {
        MapService<Task, Integer> mapService = new TaskService();
        Set<Task> taskSet = null;
        try {
            taskSet = FileHandler.readFile(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        taskSet.forEach(task -> mapService.save(task));
        return mapService;
    }

    private void printMainMenuCommand(Scanner scanner) {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Here are the commands you can use:");
        System.out.println("(1) Show Task List (by date or project)");
        System.out.println("(2) Add New Task");
        System.out.println("(3) Edit Task (update, mark as done)");
        System.out.println("(4) Remove Task");
        System.out.println("(5) Save And Quit");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");

        option = scanner.nextInt();
        while (!(option >= 1 && option <= 5)) {
            System.out.println("Please Enter Correct Option:");
            option = scanner.nextInt();
        }
        scanner.nextLine();
    }

    public void start() {
        boolean exitLoop = false;
        Scanner scanner = new Scanner(System.in);
        MapService<Task, Integer> mapService = loadDataFromFile();
        TodoHandler todoHandler = new TodoHandler(mapService);

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Welcome to Todo List ");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");

        while (!exitLoop) {
            printMainMenuCommand(scanner);
            switch (option) {
                case 1:
                    todoHandler.showList(scanner);
                    todoHandler.showStatus();
                    break;
                case 2:
                    todoHandler.addTask(scanner);
                    break;
                case 3:
                    todoHandler.updateTask(scanner);
                    break;
                case 4:
                    todoHandler.deleteTask(scanner);
                    break;
                case 5:
                    try {
                        FileHandler.writeFile(mapService.findAll(), FILE_NAME);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    exitLoop = true;
                    break;
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        TodoListApplication app = new TodoListApplication();
        app.start();
    }
}
