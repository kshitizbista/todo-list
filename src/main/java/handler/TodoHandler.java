package handler;

import model.Status;
import model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TodoHandler {

    private boolean breakFlag = false;
    private int option;
    private List<Task> tasks;
    private FileHandler fileHandler;
    private static final String FILE_NAME = "todolist.txt";

    public TodoHandler() {
        fileHandler = new FileHandler();
        tasks = fileHandler.readFileAndGetList(FILE_NAME);
    }

    public void start() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Welcome to Todo List ");
        showStatus();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("(1) Show Task List (by date or project)");
        System.out.println("(2) Add New Task");
        System.out.println("(3) Edit Task (update, mark as done)");
        System.out.println("(4) Remove Task");
        System.out.println("(5) Exit");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        pickOptionFromTopMenu();

        while (!breakFlag) {
            switch (option) {
                case 1:
                    showList();
                    pickOptionFromTopMenu();
                    break;
                case 2:
                    addTask();
                    fileHandler.writeListToFile(tasks, FILE_NAME);
                    pickOptionFromTopMenu();
                    break;
                case 3:
                    updateTask();
                    fileHandler.writeListToFile(tasks, FILE_NAME);
                    pickOptionFromTopMenu();
                    break;
                case 4:
                    deleteTask();
                    fileHandler.writeListToFile(tasks, FILE_NAME);
                    pickOptionFromTopMenu();
                    break;
                case 5:
                    breakFlag = true;
                    break;
            }
        }
    }

    private void showList() {
        if (tasks.size() > 0) {
            sort();
            String format = "%-10s %-50s %-30s %-15s %-15s %-15s";
            System.out.println(String.format(format, "Task No", "Task", "Project Name", "Status", "Created Date", "Due Date"));
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            int index = 0;
            for (Task task : tasks) {
                index = index + 1;
                System.out.println(String.format(format,
                        index,
                        task.getTitle(),
                        task.getProject(),
                        task.getStatus(),
                        task.getCreatedDate(),
                        task.getDueDate()));
            }
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

        tasks.add(task);
    }

    private void updateTask() {
        System.out.println("Enter task no you want to edit");
        int taskNo = scanInt();
        //todo: check needed
        Task task = tasks.get(taskNo - 1);
        System.out.println("Enter the field you want to edit");
        System.out.println("1.Title");
        System.out.println("2.Project");
        System.out.println("3.Status");
        System.out.println("4.Deu Date");
        int field = scanInt();
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
            default:
                //recursive
                System.out.println(">>ERROR: Entered option doesn't exist !");
                updateTask();
                return;

        }
    }

    private void deleteTask() {
        System.out.println("Enter the Task No to remove:");
        int taskNo = scanInt();
        try {
            tasks.remove(taskNo);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(">>ERROR: Cannot delete task. Index out of range: " + taskNo);
        }
    }

    private String getTitle() {
        System.out.println("Enter Task Title:");
        return scanString();
    }

    private String getProject() {
        List<String> project = tasks.stream().map(task -> task.getProject()).distinct().collect(Collectors.toList());

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
            while (true) {
                boolean projectAlreadyExist = false;
                for (String name : project) {
                    if (name.equalsIgnoreCase(projectName)) {
                        projectAlreadyExist = true;
                        break;
                    }
                }
                if (projectAlreadyExist) {
                    System.out.printf("Project with name: '%s' already exist. Enter another name %n", projectName);
                    projectName = scanString();
                } else {
                    break;
                }
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

    private Status getStatus() {
        System.out.println("Choose Status:");
        System.out.println("1." + Status.OPEN);
        System.out.println("2." + Status.CLOSED);
        int statusInput = scanInt();
        Status status = null;
        if (statusInput == 1) {
            status = Status.OPEN;
        } else if (statusInput == 2) {
            status = Status.CLOSED;
        } else {
            System.out.println(">>ERROR: Entered option doesn't exist !");
            //recursive
            return getStatus();
        }
        return status;
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

    private int scanInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    private String scanString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    private void pickOptionFromTopMenu() {
        System.out.println("Pick An Option From The Menu At Top:");
        option = scanInt();
        //recursive
        if (option > 5) {
            System.out.println(">>ERROR: Entered option doesn't exist !");
            pickOptionFromTopMenu();
            return;
        }
    }

    private int getNumberOfSelectedStatus(Status status) {
        return tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList()).size();
    }

    private void showStatus() {
        if (tasks.size() > 0) {
            int openStatus = getNumberOfSelectedStatus(Status.OPEN);
            int closedStatus = getNumberOfSelectedStatus(Status.CLOSED);
            System.out.printf("You have %s tasks todo and %s tasks are done! %n", openStatus, closedStatus);
        }
    }

    private void sort() {
        System.out.println("Enter your choice for sorting:");
        System.out.println("1.Sort by Project");
        System.out.println("2.Sort by DeuDate");

        int sortOption = scanInt();
        if (sortOption == 1) {
            Collections.sort(tasks, Comparator.comparing(Task::getProject));
        } else if (sortOption == 2) {
            Collections.sort(tasks, Comparator.comparing(Task::getDueDate));
        } else {
            //recursive
            sort();
            return;
        }
    }
}
