package handler;

import model.Project;
import model.Status;
import model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

//todo: exit from the current step
public class TodoHandler {

    private boolean breakFlag = false;
    private int option;
    private List<Project> projects;
    private FileHandler fileHandler;
    private static final String FILE_NAME = "todolist.txt";

    public TodoHandler() {
        fileHandler = new FileHandler();
        projects = fileHandler.readFileAndGetList(FILE_NAME);
    }

    public void display() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(String.format("%50s", "Welcome to Todo List "));
        chooseOption();

        while (!breakFlag) {
            switch (option) {
                case 1:
                    showList();
                    break;
                case 2:
                    confirmProject();
                    break;
                case 3:
                case 4:
                case 5:
                    fileHandler.writeListToFile(projects, FILE_NAME);
                    breakFlag = true;
                    break;
            }
        }
    }


    private void confirmProject() {
        String format = "%-5s %-50s";
        int index = 0;
        if (projects.size() > 0) {
            System.out.println("Choose Existing Project from the list below or Press Enter to Create new Project");
            for (Project project : projects) {
                index = index + 1;
                System.out.println(String.format(format, index, project.getName()));
            }

            String selectedOption = scanString();
            if (selectedOption.isEmpty()) {
                addNewProject();
            } else if (Integer.parseInt(selectedOption) <= projects.size()) {
                Task task = addNewTask();
                projects.get(Integer.parseInt(selectedOption) - 1).addTask(task);
            } else {
                confirmProject();
            }
        } else {
            addNewProject();
        }
    }

    private void addNewProject() {
        System.out.println("Enter Project Name");
        String projectName = scanString();
        if (checkProjectName(projectName)) {
            System.out.printf("Project with name '%s' already exist. Please choose another name %n", projectName);
            addNewProject();
        }
        Project newProject = new Project();
        newProject.setName(projectName);
        Task newTask = addNewTask();
        newProject.addTask(newTask);
        projects.add(newProject);
    }

    private boolean checkProjectName(String name) {
        return projects.stream().anyMatch(project -> project.getName().equals(name));
    }


    //todo: check duedate to currentdate
    private Task addNewTask() {
        System.out.println("Enter Task Description:");
        String taskDesc = scanString();

        System.out.println("Enter Due Date(yyyy-MM-dd):");
        String dateInput = scanString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate = LocalDate.parse(dateInput, formatter);

        Task task = new Task();
        task.setDescription(taskDesc);
        task.setStatus(Status.OPEN);
        task.setCreatedDate(LocalDate.now());
        task.setDueDate(dueDate);
        chooseOption();
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
        if (projects.size() > 0) {
            String format = "%-10s %-50s %-30s %-15s %-15s %-15s";
            System.out.println(String.format(format, "Task No", "Task", "Project Name", "Status", "Created Date", "Due Date"));
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            projects.forEach(project -> {
                int index = 0;
                for (Task task : project.getTasks()) {
                    index = index + 1;
                    System.out.println(String.format(format,
                            index,
                            task.getDescription(),
                            project.getName(),
                            task.getStatus(),
                            task.getCreatedDate(),
                            task.getDueDate()));
                }
            });
        } else {
            System.out.println("{ --- Todo List is Empty --- }");
        }
        chooseOption();
    }

    private int scanInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    private String scanString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }


//    private Status checkStatus() {
//        System.out.println("Choose Status:");
//        System.out.println("1-" + Status.OPEN);
//        System.out.println("2-" + Status.CLOSED);
//        int statusInput = scanInt();
//        Status status = null;
//        if (statusInput == 1) {
//            status = Status.OPEN;
//        } else if (statusInput == 2) {
//            status = Status.CLOSED;
//        } else {
//            System.out.println("Please select either 1 or 2");
//            checkStatus();
//        }
//        return status;
//    }

    private void chooseOption() {
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(String.format("%45s", "1- Display List"));
        System.out.println(String.format("%45s", "2- Add New Task"));
        System.out.println(String.format("%45s", "3- Edit Task"));
        System.out.println(String.format("%45s", "4- Delete Task"));
        System.out.println(String.format("%45s", "5- Save & Exit"));
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Enter correct option");
        option = scanInt();
        //todo:
        if (option > 5) chooseOption();
    }

}
