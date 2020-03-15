package model;

import java.time.LocalDate;

public class Task extends BaseEntity {

    private String title;
    private LocalDate createdDate;
    private LocalDate dueDate;
    private Status status;
    private String project;

    public Task() {
    }

    public Task(Integer id, String title, String project, LocalDate createdDate, LocalDate dueDate, Status status) {
        super(id);
        this.title = title;
        this.project = project;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
