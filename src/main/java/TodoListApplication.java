import handler.TodoHandler;

public class TodoListApplication {
    public static void main(String[] args) {
        TodoHandler service = new TodoHandler();
        service.display();
    }
}
