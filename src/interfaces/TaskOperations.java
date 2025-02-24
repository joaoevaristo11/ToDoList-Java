package interfaces;

import classes.Task;
import java.util.List;

public interface TaskOperations {
    void createTask();
    void editTask(int taskId);
    void deleteTask(int taskId);
    void showTasks();
    Task getTaskById(int taskId);
}