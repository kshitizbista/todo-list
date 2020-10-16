package handler;

import model.Status;
import model.Task;
import service.MapService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    private MapService<Task, Integer> mapService;

    public TodoHandler(MapService<Task, Integer> mapService) {
        this.mapService = mapService;
    }

    public void showList(Scanner scanner) {
        if (mapService.count() > 0) {
            List<Task> tasks = sortAndGetList(mapService.findAll(), scanner);
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

    public void addTask(Scanner scanner) {
        String project = getProject(scanner);
        String title = getTitle(scanner);
        LocalDate deuDate = getDeuDate(scanner);
        Task task = new Task();
        task.setProject(project);
        task.setTitle(title);
        task.setStatus(Status.OPEN);
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(deuDate);

        mapService.save(task);
        System.out.println("'Task Added Successfully'");
    }

    public void updateTask(Scanner scanner) {
        System.out.println("Enter the task id you want to edit");
        int taskId = scanInt(scanner);
        Task taskToEdit = mapService.findById(taskId);
        if (taskToEdit != null) {
            System.out.println("Enter the field you want to edit");
            System.out.println("1.Title");
            System.out.println("2.Project");
            System.out.println("3.Status");
            System.out.println("4.Deu Date");
            int field = scanInt(scanner);
            while (!(field >= 1 && field <= 4)) {
                System.out.println("Please Enter Correct Option:");
                field = scanInt(scanner);
            }
            switch (field) {
                case 1: {
                    Task task = new Task(
                            taskToEdit.getId(),
                            getTitle(scanner),
                            taskToEdit.getProject(),
                            taskToEdit.getCreatedDate(),
                            taskToEdit.getDueDate(),
                            taskToEdit.getStatus());
                    mapService.save(task);
                    break;
                }

                case 2: {
                    System.out.println("Note: Editing the project name will affect all the tasks under this project");
                    System.out.println("Enter Project Name:");
                    String projectName = scanString(scanner);
                    List<Task> taskList = mapService.findAll().stream().collect(Collectors.toList());
                    final String name = taskToEdit.getProject();
                    for (Task item : taskList) {
                        if (item.getProject().equals(name)) {
                            Task task = new Task(
                                    taskToEdit.getId(),
                                    taskToEdit.getTitle(),
                                    projectName,
                                    taskToEdit.getCreatedDate(),
                                    taskToEdit.getDueDate(),
                                    taskToEdit.getStatus());
                            mapService.save(task);
                        }
                    }
                    break;
                }

                case 3: {
                    Task task = new Task(
                            taskToEdit.getId(),
                            taskToEdit.getTitle(),
                            taskToEdit.getProject(),
                            taskToEdit.getCreatedDate(),
                            taskToEdit.getDueDate(),
                            getStatus(scanner));
                    mapService.save(task);
                    break;
                }

                case 4: {
                    Task task = new Task(
                            taskToEdit.getId(),
                            taskToEdit.getTitle(),
                            taskToEdit.getProject(),
                            taskToEdit.getCreatedDate(),
                            getDeuDate(scanner),
                            taskToEdit.getStatus());
                    mapService.save(task);
                    break;
                }
            }
            System.out.println("'Task Updated Successfully'");
        } else {
            System.out.println(">>ERROR: Cannot find task with id: " + taskId);
        }

    }

    public void deleteTask(Scanner scanner) {
        System.out.println("Enter the Task Id to remove:");
        int taskId = scanInt(scanner);
        if (mapService.existsById(taskId)) {
            mapService.deleteById(taskId);
            System.out.println("'Task Deleted Successfully'");
        } else {
            System.out.println(">>ERROR: Cannot find task with id: " + taskId);
        }
    }

    public void showStatus() {
        if (mapService.count() > 0) {
            int open = countStatus(Status.OPEN);
            int done = countStatus(Status.DONE);
            System.out.printf("Total task: %s, No. of Tasks To Do: %s , No. of Tasks Done: %s %n", mapService.count(), open, done);
        }
    }

    private String getProject(Scanner scanner) {
        List<String> project = mapService.findAll().stream().map(task -> task.getProject()).distinct().collect(Collectors.toList());
        String selectedProject = "";
        if (project.size() > 0) {
            System.out.println("To add task to existing project choose the id from the list below or press enter to create new project");
            int index = 0;
            for (String name : project) {
                index = index + 1;
                System.out.printf("%s. %s %n", index, name);
            }
            selectedProject = scanString(scanner);
        }
        String projectName;
        if (selectedProject.isEmpty()) {
            System.out.println("Enter Project Name:");
            projectName = scanString(scanner);
            // loop for re-entering input until project with new name is provided
            while (project.indexOf(projectName) != -1) {
                System.out.printf("Project with name: '%s' already exist. Enter another name: %n", projectName);
                projectName = scanString(scanner);
            }
        } else if (Integer.parseInt(selectedProject) <= project.size()) {
            projectName = project.get(Integer.parseInt(selectedProject) - 1);
        } else {
            //recursive
            System.out.println(">>ERROR: Entered option doesn't exist !");
            return getProject(scanner);
        }
        return projectName;
    }

    private String getTitle(Scanner scanner) {
        System.out.println("Enter Task Title:");
        return scanString(scanner);
    }

    private LocalDate getDeuDate(Scanner scanner) {
        System.out.println("Enter Due Date(yyyy-MM-dd):");
        String dateInput = scanString(scanner);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dateInput, formatter);
        } catch (DateTimeParseException ex) {
            System.out.println(">>ERROR: Date must be in yyyy-MM-dd format");
            // recursive
            return getDeuDate(scanner);
        }
        if (dueDate.compareTo(LocalDate.now()) < 0) {
            System.out.println(">>ERROR: The due date cannot be less than today's date");
            //recursive
            return getDeuDate(scanner);
        }
        return dueDate;
    }

    private Status getStatus(Scanner scanner) {
        System.out.println("Choose Status:");
        System.out.println("1." + Status.OPEN);
        System.out.println("2." + Status.DONE);
        int statusInput = scanInt(scanner);
        while (!(statusInput == 1 || statusInput == 2)) {
            System.out.println("Please Enter Correct Option:");
            statusInput = scanInt(scanner);
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

    private int countStatus(Status status) {
        return mapService.findAll().stream().filter(task -> task.getStatus() == status).collect(Collectors.toSet()).size();
    }

    private List<Task> sortAndGetList(Set<Task> list, Scanner scanner) {
        System.out.println("Enter your choice for sorting:");
        System.out.println("1.Sort by Project");
        System.out.println("2.Sort by DeuDate");

        List<Task> tasks = new ArrayList<>(list);

        int sortOption = scanInt(scanner);
        while (!(sortOption == 1 || sortOption == 2)) {
            System.out.println("Please Enter Correct Option:");
            sortOption = scanInt(scanner);
        }

        if (sortOption == 1) {
            Collections.sort(tasks, Comparator.comparing(Task::getProject));
        }
        if (sortOption == 2) {
            Collections.sort(tasks, Comparator.comparing(Task::getDueDate));
        }
        return tasks;
    }

    private int scanInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.println("Please enter integer only. Try again: ");
        }
        int result = sc.nextInt();

        // nextInt only reads the integer and cursor after reading mains just after it. To consuming the leftover new line
        // use the nextLine()
        sc.nextLine();
        return result;
    }

    private String scanString(Scanner sc) {
        return sc.nextLine().trim();
    }
}
