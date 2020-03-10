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

//todo: exit from the current step
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

    public void display() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Welcome to Todo List ");
        showStatus();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("(1) Show Task List (by date or project)");
        System.out.println("(2) Add New Task");
        System.out.println("(3) Edit Task (update, mark as done)");
        System.out.println("(4) Remove Task");
        System.out.println("(5) Save & Exit");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        pickOptionFromTopMenu();

        while (!breakFlag) {
            switch (option) {
                case 1:
                    showList();
                    break;
                case 2:
                    addTask();
                    break;
                case 3:
                case 4:
                    deleteTask();
                case 5:
                    fileHandler.writeListToFile(tasks, FILE_NAME);
                    breakFlag = true;
                    break;
            }
        }
    }

    private void addTask() {

        String project = getProject();
        System.out.println("Enter Task Title:");
        String title = scanString();
        LocalDate deuDate = getDate();

        Task task = new Task();
        task.setProject(project);
        task.setTitle(title);
        task.setStatus(Status.OPEN);
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(deuDate);

        tasks.add(task);
    }

    private String getProject() {
        List<String> project = tasks.stream().map(task -> task.getProject()).distinct().collect(Collectors.toList());

        System.out.println("Choose Project from the list below or press enter to create new project");
        int index = 0;
        for (String name : project) {
            index = index + 1;
            System.out.printf("%s. %s %n", index, name);
        }
        String selectedProject = scanString();
        String projectName = null;
        if (selectedProject.isEmpty()) {
            System.out.println("Enter Project Name:");
            projectName = scanString();
            // loop until project with new name is entered
            while (true) {
                boolean projectAlreadyExist = false;
                for (String name : project) {
                    if (name.equalsIgnoreCase(projectName)) {
                        projectAlreadyExist = true;
                        break;
                    }
                }
                if (projectAlreadyExist) {
                    System.out.printf("Project with Name: '%s' already exist. Enter another name %n", projectName);
                    projectName = scanString();
                } else {
                    break;
                }
            }
        } else if (Integer.parseInt(selectedProject) <= project.size()) {
            projectName = project.get(Integer.parseInt(selectedProject) - 1);
        } else {
            //recursive
            getProject();
        }
        return projectName;
    }

    private LocalDate getDate() {
        System.out.println("Enter Due Date(yyyy-MM-dd):");
        String dateInput = scanString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate = LocalDate.parse(dateInput, formatter);
        if (dueDate.compareTo(LocalDate.now()) < 0) {
            System.out.println("The due date cannot be less than today's date");
            //recursive
            getDate();
        }
        return dueDate;
    }

    private void updateTask() {

    }

    private void deleteTask() {
        System.out.println("Enter the Task No to remove:");
        int taskNo = scanInt();
        try {
            tasks.remove(taskNo);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Cannot delete task. Index out of range: " + taskNo);
        }
        pickOptionFromTopMenu();
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
        pickOptionFromTopMenu();
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
        System.out.println("Pick an option:");
        option = scanInt();
        //recursive
        if (option > 5) {
            System.out.println("Please choose option between 1 and 5 from the menu");
            pickOptionFromTopMenu();
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
        System.out.println("1.Sort By Project");
        System.out.println("2.Sort By DeuDate");

        int sortOption = scanInt();
        if (sortOption == 1) {
            Collections.sort(tasks, Comparator.comparing(Task::getProject));
        } else if (sortOption == 2) {
            Collections.sort(tasks, Comparator.comparing(Task::getDueDate));
        } else {
            //recursive
            sort();
        }
    }
}
