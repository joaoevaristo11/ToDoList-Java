package classes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import interfaces.TaskStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler implements TaskStorage {
    private static final String FILE_PATH = "C:\\Programming\\Java\\Nova pasta\\ToDo\\src\\tasks.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void writeTask(Task task) {
        List<Task> tasks = readTasks();
        tasks.add(task);
        saveTasksToFile(tasks);
    }

    @Override
    public List<Task> readTasks() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteTask(int taskId) {
        List<Task> tasks = readTasks();
        tasks.removeIf(task -> task.getTaskId() == taskId);
        saveTasksToFile(tasks);
    }

    @Override
    public void updateTaskInFile(Task updatedTask) {
        List<Task> tasks = readTasks();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId() == updatedTask.getTaskId()) {
                tasks.set(i, updatedTask);
                break;
            }
        }
        saveTasksToFile(tasks);
    }

    private void saveTasksToFile(List<Task> tasks) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Task getTaskById(int taskId) {
        List<Task> tasks = readTasks(); // Lê todas as tarefas do arquivo
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null; // Retorna null se não encontrar a tarefa
    }

}

