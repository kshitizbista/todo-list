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
        System.out.println(String.format("%50s", "Welcome to Todo List "));
        System.out.printf("You have %s tasks todo and %s tasks are done! %n", getActualStatusNo(Status.OPEN), getActualStatusNo(Status.CLOSED));
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

        System.out.println("Enter Due Date(yyyy-MM-dd):");
        String dateInput = scanString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate = LocalDate.parse(dateInput, formatter);

        Task task = new Task();
        task.setProject(project);
        task.setTitle(taskDesc);
        task.setStatus(Status.OPEN);
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(dueDate);
        chooseOptionFromTopMenu();
        tasks.add(task);
        return task;
    }

    private void updateTask() {

    }

    private void deleteTask() {

    }

    private void deleteProject() {
    }

    //todo: show x task is done y task is remaining, sorting
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
            System.out.println("{ --- Todo List is Empty --- }");
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
        System.out.println("Enter Correct Option From Top Menu");
        option = scanInt();
        //todo:
        if (option > 5) chooseOptionFromTopMenu();
    }

    private void showToplevelMenu() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(String.format("%45s", "1- Display List"));
        System.out.println(String.format("%45s", "2- Add New Task"));
        System.out.println(String.format("%45s", "3- Edit Task"));
        System.out.println(String.format("%45s", "4- Delete Task"));
        System.out.println(String.format("%45s", "5- Save & Exit"));
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
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
            sort();
        }
    }

    private int getActualStatusNo(Status status) {
        return tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList()).size();
    }

}
