package classes;

import java.util.List;
import java.util.Scanner;

public class TaskService implements interfaces.TaskOperations {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FileHandler filehandler = new FileHandler();

    @Override
    public void createTask() {
        Task.createTask();
    }

    @Override
    public void editTask(int taskId) {
        Task task = filehandler.getTaskById(taskId);
        if (task != null) {
            task.editTask();
            filehandler.updateTaskInFile(task);
        } else {
            System.out.println("❌ Tarefa não encontrada!");
        }
    }

    @Override
    public void deleteTask(int taskId) {
        filehandler.deleteTask(taskId);
        System.out.println("✅ Tarefa removida com sucesso!");
    }

    @Override
    public void showTasks() {
        List<Task> tasks = filehandler.readTasks();
        if (tasks.isEmpty()) {
            System.out.println("📭 Não há tarefas registradas.");
        } else {
            tasks.forEach(Task::displayTask);
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        return filehandler.getTaskById(taskId);
    }

    @Override
    public void searchTasks(String string) {
        Task.searchTasks(string);
    }

    @Override
    public void sortTask(String option) {
        Task.sortTask(option);
    }
}
