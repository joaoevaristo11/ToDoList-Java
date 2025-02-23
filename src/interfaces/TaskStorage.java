package interfaces;
import classes.Task;

import java.util.List;

public interface TaskStorage {
    void writeTask(Task task);
    List<Task> readTasks();
    void deleteTask(int taskId);
    void updateTaskInFile(Task updatedTask);
}

