package handler;

import model.Status;
import model.Task;
import service.FileService;
import service.IFile;
import service.ITask;
import service.TaskService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>Todo Handler!</h1>
 * The TodoHandler prints the output on the screen.
 * It get input from console to create new task, edit and delete them.
 *
 * @author Kshitiz Bista
 */
public class TodoHandler {

    private static final String FILE_NAME = "todolist.txt";
    private boolean breakFlag = false;
    private int option;
    private ITask todoService;
    private IFile fileService;

    public TodoHandler() {
        todoService = new TaskService();
        fileService = new FileService();
    }

    public void start() {
        Set<Task> taskSet = null;
        try {
            taskSet = fileService.readFile(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        taskSet.forEach(task -> todoService.addOrUpdate(task));
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Welcome to Todo List ");
        showStatus();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("(1) Show Task List (by date or project)");
        System.out.println("(2) Add New Task");
        System.out.println("(3) Edit Task (update, mark as done)");
        System.out.println("(4) Remove Task");
        System.out.println("(5) Save And Quit");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        pickOptionFromTopMenu();

        while (!breakFlag) {
            switch (option) {
                case 1:
                    showList();
                    showStatus();
                    pickOptionFromTopMenu();
                    break;
                case 2:
                    addTask();
                    pickOptionFromTopMenu();
                    break;
                case 3:
                    updateTask();
                    pickOptionFromTopMenu();
                    break;
                case 4:
                    deleteTask();
                    pickOptionFromTopMenu();
                    break;
                case 5:
                    try {
                        fileService.writeFile(todoService.findAll(), FILE_NAME);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    breakFlag = true;
                    break;
            }
        }
    }

    private void showList() {
        if (todoService.count() > 0) {
            List<Task> tasks = sortAndGetList(todoService.findAll());
            String format = "%-10s %-50s %-30s %-15s %-15s %-15s";
            System.out.println(String.format(format, "Task Id", "Task", "Project Name", "Status", "Created Date", "Due Date"));
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            tasks.forEach(task -> System.out.println(String.format(format,
                    task.getId(),
                    task.getTitle(),
                    task.getProject(),
                    task.getStatus(),
                    task.getCreatedDate(),
                    task.getDueDate())));
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("There are no tasks in Todo List");
        }
    }

    private void addTask() {
        String project = getProject();
        String title = getTitle();
        LocalDate deuDate = getDeuDate();
        Task task = new Task();
        task.setProject(project);
        task.setTitle(title);
        task.setStatus(Status.OPEN);
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(deuDate);

        todoService.addOrUpdate(task);
        System.out.println("'Task Added Successfully'");
    }

    private void updateTask() {
        System.out.println("Enter the task id you want to edit");
        int taskId = scanInt();
        Task task = todoService.findById(taskId);
        if (task != null) {
            System.out.println("Enter the field you want to edit");
            System.out.println("1.Title");
            System.out.println("2.Project");
            System.out.println("3.Status");
            System.out.println("4.Deu Date");
            int field = scanInt();
            while (!(field >= 1 && field <= 4)) {
                System.out.println("Please Enter Correct Option:");
                field = scanInt();
            }
            switch (field) {
                case 1:
                    task.setTitle(getTitle());
                    break;
                case 2:
                    task.setProject(getProject());
                    break;
                case 3:
                    task.setStatus(getStatus());
                    break;
                case 4:
                    task.setDueDate(getDeuDate());
                    break;
            }
            todoService.addOrUpdate(task);
            System.out.println("'Task Updated Successfully'");
        } else {
            System.out.println(">>ERROR: Cannot find task with id: " + taskId);
        }

    }

    private void deleteTask() {
        System.out.println("Enter the Task Id to remove:");
        int taskId = scanInt();
        if (todoService.existsById(taskId)) {
            todoService.deleteById(taskId);
            System.out.println("'Task Deleted Successfully'");
        } else {
            System.out.println(">>ERROR: Cannot find task with id: " + taskId);
        }
    }

    private String getProject() {
        List<String> project = todoService.findAll().stream().map(task -> task.getProject()).distinct().collect(Collectors.toList());

        System.out.println("Choose project from the list below or press enter to create new project");
        int index = 0;
        for (String name : project) {
            index = index + 1;
            System.out.printf("%s. %s %n", index, name);
        }
        String selectedProject = scanString();
        String projectName;
        if (selectedProject.isEmpty()) {
            System.out.println("Enter Project Name:");
            projectName = scanString();
            // loop for re-entering input until project with new name is provided
            while (project.indexOf(projectName) != -1) {
                System.out.printf("Project with name: '%s' already exist. Enter another name: %n", projectName);
                projectName = scanString();
            }
        } else if (Integer.parseInt(selectedProject) <= project.size()) {
            projectName = project.get(Integer.parseInt(selectedProject) - 1);
        } else {
            //recursive
            System.out.println(">>ERROR: Entered option doesn't exist !");
            return getProject();
        }
        return projectName;
    }

    private String getTitle() {
        System.out.println("Enter Task Title:");
        return scanString();
    }

    private LocalDate getDeuDate() {
        System.out.println("Enter Due Date(yyyy-MM-dd):");
        String dateInput = scanString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate = LocalDate.parse(dateInput, formatter);
        if (dueDate.compareTo(LocalDate.now()) < 0) {
            System.out.println(">>ERROR: The due date cannot be less than today's date");
            //recursive
            return getDeuDate();
        }
        return dueDate;
    }

    private Status getStatus() {
        System.out.println("Choose Status:");
        System.out.println("1." + Status.OPEN);
        System.out.println("2." + Status.DONE);
        int statusInput = scanInt();
        while (!(statusInput == 1 || statusInput == 2)) {
            System.out.println("Please Enter Correct Option:");
            statusInput = scanInt();
        }
        Status status = null;
        if (statusInput == 1) {
            status = Status.OPEN;
        }
        if (statusInput == 2) {
            status = Status.DONE;
        }
        return status;
    }

    private void pickOptionFromTopMenu() {
        System.out.println("Pick An Option From The Menu At Top:");
        option = scanInt();
        while (!(option >= 1 && option <= 5)) {
            System.out.println("Please Enter Correct Option:");
            option = scanInt();
        }
    }

    private void showStatus() {
        if (todoService.count() > 0) {
            int openStatus = getNumberOfSelectedStatus(Status.OPEN);
            int closedStatus = getNumberOfSelectedStatus(Status.DONE);
            System.out.printf("Total task: %s, No. of Tasks To Do: %s , No. of Tasks Done: %s %n", openStatus, closedStatus, todoService.count());
        }
    }

    private int getNumberOfSelectedStatus(Status status) {
        return todoService.findAll().stream().filter(task -> task.getStatus() == status).collect(Collectors.toSet()).size();
    }

    private List<Task> sortAndGetList(Set<Task> list) {
        System.out.println("Enter your choice for sorting:");
        System.out.println("1.Sort by Project");
        System.out.println("2.Sort by DeuDate");

        List<Task> tasks = new ArrayList<>(list);

        int sortOption = scanInt();
        while (!(sortOption == 1 || sortOption == 2)) {
            System.out.println("Please Enter Correct Option:");
            sortOption = scanInt();
        }

        if (sortOption == 1) {
            Collections.sort(tasks, Comparator.comparing(Task::getProject));
        }
        if (sortOption == 2) {
            Collections.sort(tasks, Comparator.comparing(Task::getDueDate));
        }
        return tasks;
    }

    private int scanInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    private String scanString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }
}
