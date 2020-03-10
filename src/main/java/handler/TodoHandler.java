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
        showToplevelMenu();
        chooseOptionFromTopMenu();

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
                case 5:
                    fileHandler.writeListToFile(tasks, FILE_NAME);
                    breakFlag = true;
                    break;
            }
        }
    }

    //todo: check duedate to currentdate
    private Task addTask() {
        System.out.println("Enter Project Name:");
        String project = scanString();

        System.out.println("Enter Task Title:");
        String taskDesc = scanString();

        Task task = new Task();
        task.setProject(project);
        task.setTitle(taskDesc);
        task.setStatus(Status.OPEN);
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(getDate());
        chooseOptionFromTopMenu();
        tasks.add(task);
        return task;
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
        chooseOptionFromTopMenu();
    }

    private int scanInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    private String scanString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    private void chooseOptionFromTopMenu() {
        System.out.println("Pick an option:");
        option = scanInt();
        //recursive
        if (option > 5) chooseOptionFromTopMenu();
    }

    private void showToplevelMenu() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("(1) Show Task List (by date or project)");
        System.out.println("(2) Add New Task");
        System.out.println("(3) Edit Task (update, mark as done)");
        System.out.println("(4) Remove Task");
        System.out.println("(5) Save & Exit");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
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
