package classes;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Task
{
    private static final Random random = new Random();
    private static final Set<Integer> usedIds = new HashSet<>();
    private String taskName;
    private int taskId;
    private String description;
    private LocalDate endDate;
    private int priority;
    private boolean done = false;

    // Construtor principal (usado para carregar tarefas já existentes)
    public Task(String taskName, int taskId, String description, LocalDate endDate, int priority, boolean state) {
        this.taskName = taskName;
        this.taskId = taskId;
        this.description = description;
        this.endDate = endDate;
        this.priority = priority;
        this.done = state;
    }

    // Construtor para NOVAS tarefas (gera ID aleatório)
    public Task(String taskName, String description, LocalDate endDate, int priority) {
        this.taskName = taskName;
        this.taskId = generateRandomId();
        this.description = description;
        this.endDate = endDate;
        this.priority = priority;
        this.done = false;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public boolean getState()
    {
        return this.done;
    }

    public int getTaskId()
    {
        return this.taskId;
    }

    public boolean markAsDone()
    {
        return done=true;
    }

    private int generateRandomId()
    {
        int id;
        do{id = random.nextInt(9000)+1000;}while(usedIds.contains(id));

        usedIds.add(id);
        return id;
    }

    public void setEndDate(LocalDate newDate) {  // Tornar pública
        LocalDate now = LocalDate.now();

        if (newDate.isBefore(now)) {
            System.out.println("❌ Erro: A data já passou! Escolha uma data futura.");
            return;
        }

        int month = newDate.getMonthValue();
        int day = newDate.getDayOfMonth();
        int year = newDate.getYear();
        int maxDays = Month.of(month).length(Year.isLeap(year));

        if (day > maxDays || day < 1) {
            System.out.println("❌ Erro: O dia " + day + " não é válido no mês " + month + ".");
            return;
        }

        if (year > now.getYear() + 25) {
            System.out.println("❌ Erro: O ano " + year + " está demasiado distante no futuro.");
            return;
        }

        this.endDate = newDate;
        System.out.println("✅ Data de vencimento definida para: " + this.endDate);
    }

    public boolean setPriority(int priority) {
        if (priority >= 1 && priority <= 3) {
            this.priority = priority;
            return true;
        } else {
            System.out.println("❌ Prioridade inválida!");
            return false;
        }
    }



    public void displayTask()
    {
        System.out.println("\n----------- Detalhes da Tarefa -----------");
        System.out.println("ID: " + taskId);
        System.out.println("Nome: " + taskName);
        System.out.println("Descrição: " + description);
        System.out.println("Data de vencimento: " + endDate);
        System.out.println("Prioridade: " + priority);
        System.out.println("Estado: " + (done ? "Concluída ✅" : "Pendente ❌"));
        System.out.println("-----------------------------\n");
    }

    public void editTask()
    {
        Scanner scanner = new Scanner(System.in);
        displayTask();
        boolean continuee = true;
        while(continuee) {
            System.out.println("Qual o campo deseja modificar?");
            System.out.println("1. Nome");
            System.out.println("2. Descrição");
            System.out.println("3. Data de vencimento");
            System.out.println("4. Prioridade");
            System.out.println("5. Estado (Concluir tarefa)");
            System.out.println("0. Cancelar edição");
            System.out.println("\nInput (0-5): ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option)
            {
                case 0:
                    continuee = false;
                    System.out.println("Cancelando edição...");
                    break;
                case 1:
                    System.out.println("Nome atual: "+this.taskName);
                    System.out.println("Novo nome: ");
                    this.taskName = scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Descrição atual: "+this.description);
                    System.out.println("Nova descrição: ");
                    this.description = scanner.nextLine();
                    break;
                case 3:
                    System.out.println("Data de Vencimento atual: "+this.endDate);
                    System.out.println("Nova Data de Vencimento(no formato YYYY-MM-DD): ");
                    setEndDate(LocalDate.parse(scanner.nextLine()));
                    break;
                case 4:
                    System.out.println("Prioridade atual: " + this.priority);
                    int newPriority;
                    do {
                        System.out.println("Nova prioridade (1- Baixa, 2- Média, 3- Alta): ");
                        newPriority = scanner.nextInt();
                        scanner.nextLine();
                    } while (newPriority < 1 || newPriority > 3);
                    this.priority = newPriority;
                    break;

                case 5:
                    markAsDone();
                    System.out.println("✅ Tarefa marcada como concluída!");
                    break;
                default:
                    System.out.println("❌ Opção inválida! Tente novamente.");
            }
        }
    }

    public static void createTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🔹 Criar Nova Tarefa 🔹");

        System.out.print("Nome da tarefa: ");
        String taskName = scanner.nextLine();
        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        Task newTask = new Task(taskName, description, LocalDate.now(), 1);

        boolean validDate = false;
        while (!validDate) {
            System.out.print("Data de vencimento (YYYY-MM-DD): ");
            try {
                LocalDate newDate = LocalDate.parse(scanner.nextLine());
                newTask.setEndDate(newDate);
                validDate = true;
            } catch (Exception e) {
                System.out.println("❌ Formato inválido! Use YYYY-MM-DD.");
            }
        }

        // Prioridade
        int priority;
        do {
            System.out.print("Prioridade (1- Baixa, 2- Média, 3- Alta): ");
            priority = scanner.nextInt();
            scanner.nextLine();
        } while (!newTask.setPriority(priority));
        // Escrever no ficheiro JSON
        FileHandler.writeTask(newTask);

        System.out.println("✅ Tarefa criada e guardada com sucesso!\n");

    }


}
