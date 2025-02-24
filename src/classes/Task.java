package classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Task
{
    private static final Random random = new Random();
    private static final FileHandler filehandler = new FileHandler();
    private int taskId;
    @JsonProperty("taskName")
    private String taskName;

    @JsonProperty("category")
    private String category;

    @JsonProperty("description")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonProperty("priority")
    private int priority;

    @JsonProperty("state")
    private boolean state = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    public Task(){}
    // Construtor para NOVAS tarefas (gera ID aleatório)
    public Task(String taskName,String category, String description, LocalDate endDate,LocalDateTime endDateTime, int priority) {
        this.taskName = taskName;
        this.category = category;
        this.taskId = generateRandomId();
        this.description = description;
        this.endDate = endDate;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.state = false;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public boolean getState()
    {
        return this.state;
    }

    public int getTaskId()
    {
        return this.taskId;
    }

    public void markAsDone()
    {
        this.state=true;
    }

    private int generateRandomId()
    {
        List<Task> tasks = filehandler.readTasks();
        Set<Integer> usedIds = new HashSet<>();
        for(Task task:tasks)
        {
            usedIds.add(task.getTaskId());
        }

        int id;
        do{id = random.nextInt(9000)+1000;}while(usedIds.contains(id));

        usedIds.add(id);
        return id;
    }

    public void setEndDate(LocalDate newDate) {
        if (this.endDate != null && this.endDate.equals(newDate)) {
            return;  // Se a data não mudou, não faz nada
        }

        LocalDate now = LocalDate.now();
        int month = newDate.getMonthValue();
        int day = newDate.getDayOfMonth();
        int year = newDate.getYear();
        int maxDays = Month.of(month).length(Year.isLeap(year));

        if (newDate.isBefore(now)) {
            System.out.println("❌ Erro: A data já passou! Escolha uma data futura.");
            return;
        }

        if (day > maxDays || day < 1) {
            System.out.println("❌ Erro: O dia " + day + " não é válido no mês " + month + ".");
            return;
        }

        if (year > now.getYear() + 25) {
            System.out.println("❌ Erro: O ano " + year + " está demasiado distante no futuro.");
            return;
        }

        this.endDate = newDate;
        this.endDateTime = null;

        // Só exibe a mensagem se a data realmente tiver sido alterada
        if (!this.endDate.equals(newDate)) {
            System.out.println("✅ Data de vencimento definida para: " + this.endDate);
        }
    }

    public boolean setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("❌ Prioridade inválida! Deve ser 1, 2 ou 3.");
        }
        this.priority = priority;
        return true;
    }

    public void displayTask()
    {
        System.out.println("\n----------- Detalhes da Tarefa -----------");
        System.out.println("ID: " + taskId);
        System.out.println("Nome: " + taskName);
        if(category!=null) {System.out.println("Categoria: " + category);}
        System.out.println("Descrição: " + description);
        System.out.println("Data de vencimento: " + (endDateTime != null ? endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : endDate));
        System.out.println("Prioridade: " + priority);
        System.out.println("Estado: " + (state ? "Concluída ✅" : "Pendente ❌"));
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
            System.out.print("\nInput (0-5): ");
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
                    System.out.print("Novo nome: ");
                    this.taskName = scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Descrição atual: "+this.description);
                    System.out.print("Nova descrição: ");
                    this.description = scanner.nextLine();
                    break;
                case 3:
                    System.out.println("Data de Vencimento atual: "+this.endDate);
                    System.out.print("Nova Data de Vencimento(no formato YYYY-MM-DD): ");
                    setEndDate(LocalDate.parse(scanner.nextLine()));
                    break;
                case 4:
                    System.out.println("Prioridade atual: " + this.priority);
                    int newPriority;
                    do {
                        System.out.print("Nova prioridade (1- Baixa, 2- Média, 3- Alta): ");
                        newPriority = scanner.nextInt();
                        scanner.nextLine();
                    } while (!setPriority(newPriority));
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
        System.out.print("Categoria da tarefa(ex: Trabalho, Estudos, Pessoal)   ): ");
        String category = scanner.nextLine();

        LocalDate newDate = null;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Data de vencimento (YYYY-MM-DD): ");
            try {
                newDate = LocalDate.parse(scanner.nextLine());
                validDate = true;
            } catch (Exception e) {
                System.out.println("❌ Formato inválido! Use YYYY-MM-DD.");
            }
        }

        LocalDateTime endDateTime = null;
        System.out.print("Pretende adicionar hora à sua tarefa (s/n)? ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("s")) {
            boolean validTime = false;
            while (!validTime) {
                System.out.print("Digite a hora de vencimento da tarefa (HH:MM): ");
                try {
                    LocalTime localTime = LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));
                    endDateTime = LocalDateTime.of(newDate, localTime);
                    if (endDateTime.isBefore(LocalDateTime.now())) {
                        System.out.println("❌ A data e hora já passaram! Escolha um momento futuro.");
                    } else {
                        validTime = true;
                    }
                } catch (Exception e) {
                    System.out.println("❌ Formato inválido! Use HH:MM.");
                }
            }
        }

        int priority;
        do {
            System.out.print("Prioridade (1- Baixa, 2- Média, 3- Alta): ");
            priority = scanner.nextInt();
            scanner.nextLine();
        } while (priority < 1 || priority > 3);

        Task newTask = new Task(taskName,category, description, newDate, endDateTime, priority);

        filehandler.writeTask(newTask);

        System.out.println("✅ Tarefa criada e guardada com sucesso!\n");
    }
}
