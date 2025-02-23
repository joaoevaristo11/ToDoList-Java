package classes;

import interfaces.TaskOperations;

import java.util.List;
import java.util.Scanner;

public class TaskService implements TaskOperations
{
    private static final Scanner scanner = new Scanner(System.in);
    private static final FileHandler filehandler = new FileHandler();
    @Override
    public void createTask() {
        Task.createTask();
    }

    @Override
    public void editTask(int taskId) {

    }

    @Override
    public void deleteTask(int taskId) {

    }

    public static boolean validateAnswer(int option)
    {
        if(option<1 || option >5)
        {
            System.out.println("Opção inválida, digite um número de 1 a 5...");
            return false;
        }

        return true;
    }

    public static void showTasks() {
        String option = "";

        while (!option.equals("s") && !option.equals("n")) {
            System.out.println("Quer ver alguma tarefa específica (s/n)? ");
            option = scanner.nextLine().toLowerCase();

            if (option.equals("s")) {
                System.out.print("Digite o ID dessa tarefa: ");
                int idEntered = scanner.nextInt();
                scanner.nextLine();

                Task taskById = filehandler.getTaskById(idEntered);
                if (taskById != null) {
                    taskById.displayTask();
                } else {
                    System.out.println("❌ Não existe uma tarefa com o ID: " + idEntered);
                }
            } else if (option.equals("n")) {
                List<Task> tasks = filehandler.readTasks();
                if (tasks.isEmpty()) {
                    System.out.println("📭 Não há tarefas registadas.");
                    return;
                }

                for (Task task : tasks) {
                    task.displayTask();
                }
            } else {
                System.out.println("❌ Erro: Por favor, insira 's' ou 'n'.");
            }
        }
    }
}
