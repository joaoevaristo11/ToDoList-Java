package classes;

import interfaces.UserInterface;

import java.util.List;
import java.util.Scanner;

public class TaskManager implements UserInterface {
    private static final TaskService taskService = new TaskService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        int option = 0;

        while (option != 5) {
            System.out.println("\n-----------------\n🧾 TASK MENU 🧾\n-----------------\n");
            System.out.println("1. Adicionar Tarefa\n2. Mostrar Tarefas\n3. Editar Tarefa\n4. Apagar Tarefa\n5. Sair\n");
            System.out.print("Digite a sua resposta: ");
            option = scanner.nextInt();
            scanner.nextLine();
            if (option >= 1 && option <= 5) {
                switch (option) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        System.out.println("👋 Saindo do programa...");
                        break;
                }
            } else {
                System.out.println("❌ Opção inválida, digite um número de 1 a 5...");
            }
        }
    }

    @Override
    public void showTasks() {
        List<Task> tasks = new FileHandler().readTasks();
        if (tasks.isEmpty()) {
            System.out.println("📭 Não há tarefas registradas.");
        } else {
            tasks.forEach(Task::displayTask);
        }
    }
}
