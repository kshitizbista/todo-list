package model;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task obj = (Task) o;
        return (Objects.equals(this.getId(), obj.getId()) &&
                Objects.equals(this.title, obj.title) &&
                Objects.equals(this.project, obj.project) &&
                Objects.equals(this.createdDate, obj.createdDate) &&
                Objects.equals(this.dueDate, obj.dueDate) &&
                Objects.equals(this.status, obj.status));
    }
}
