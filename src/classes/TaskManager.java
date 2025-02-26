package classes;

import java.util.Scanner;

public class TaskManager {
    private static final TaskService taskService = new TaskService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;

        do {
            System.out.println("\n-----------------\n🧾 TASK MENU 🧾\n-----------------\n");
            System.out.println("1. Adicionar Tarefa\n2. Mostrar Tarefas\n3. Editar Tarefa\n4. Apagar Tarefa\n5. Sair\n");
            System.out.print("Digite a sua resposta: ");

            while (!scanner.hasNextInt()) {
                System.out.println("❌ Entrada inválida! Digite um número entre 1 e 5.");
                scanner.next();
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    taskService.createTask();
                    break;
                case 2:
                    System.out.print("Pretende ordenar as tarefas por prioridade ou data? ");
                    String sortOption = scanner.nextLine();
                    taskService.sortTask(sortOption);

                    System.out.print("Pretende pesquisar alguma tarefa ou categoria (s/n)? ");
                    String searchChoice = scanner.nextLine().trim().toLowerCase();

                    if (searchChoice.equals("s")) {
                        System.out.print("Digite o nome da tarefa ou a sua categoria: ");
                        String answer = scanner.nextLine();
                        taskService.searchTasks(answer);
                    }
                    break;

                case 3:
                    System.out.print("Digite o ID da tarefa: ");
                    int taskId = scanner.nextInt();
                    scanner.nextLine();
                    taskService.editTask(taskId);
                    break;
                case 4:
                    System.out.print("Digite o ID da tarefa a eliminar: ");
                    int taskIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    taskService.deleteTask(taskIdToDelete);
                    break;
                case 5:
                    System.out.println("👋 Saindo do programa...");
                    break;
                default:
                    System.out.println("❌ Opção inválida, digite um número de 1 a 5...");
            }
        } while (option != 5);
    }
}
