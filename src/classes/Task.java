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
    private static final Scanner scanner = new Scanner(System.in);


    @JsonProperty("subtasks")
    private List<SubTask> subtasks = new ArrayList<>();

    private int taskId;

    @JsonProperty("taskName")
    private String taskName;

    @JsonProperty("category")
    private String category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("priority")
    private int priority;

    @JsonProperty("state")
    private boolean state = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    public Task(){}
    // Construtor para NOVAS tarefas (gera ID aleatório)
    public Task(String taskName,String category, String description, LocalDateTime endDateTime, int priority) {
        this.taskName = taskName;
        this.category = category;
        this.taskId = generateRandomId();
        this.description = description;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.state = false;
    }

    public String getTaskName(){return this.taskName;}
    public String getCategory()
    {
        return this.category;
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
    public LocalDateTime getEndDateTime() {return this.endDateTime;}

    public void markAsDone()
    {
        this.state=true;
        for(SubTask subtask:subtasks){
            subtask.markAsDone();
        }
    }

    public void addSubTask(SubTask subtask){
        this.subtasks.add(subtask);
        System.out.println("✅ Subtarefa adicionada com sucesso!");
    }

    public void removeSubTask(int subTaskId){
       boolean remove = subtasks.removeIf(subtask->subtask.getSubTaskId()==subTaskId);
        if (remove) {
            System.out.println("✅ Subtarefa removida com sucesso!");
        } else {
            System.out.println("❌ Nenhuma subtarefa encontrada com o ID: " + subTaskId);
        }
    }

    private int generateRandomId()
    {
        List<Task> tasks = filehandler.readTasks();
        Set<Integer> usedIds = new HashSet<>();
        for(Task task:tasks) usedIds.add(task.getTaskId());

        int id;
        do{id = random.nextInt(9000)+1000;}while(usedIds.contains(id));

        usedIds.add(id);
        return id;
    }

    public void setEndDate(LocalDate newDate, LocalTime time, boolean isNew) {
        if (newDate == null) {
            return;
        }

        if (time == null) {
            time = LocalTime.MIDNIGHT;
        }

        LocalDateTime newDateTime = LocalDateTime.of(newDate, time);
        if (this.endDateTime != null && this.endDateTime.equals(newDateTime)) {
            return;
        }

        if (isNew && newDateTime.isBefore(LocalDateTime.now())) {
            System.out.println("❌ Erro: A data e hora já passaram! Escolha um momento futuro.");
            return;
        }

        this.endDateTime = newDateTime;
        System.out.println("✅ Data de vencimento definida para: " + this.endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
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
        System.out.println("Data de vencimento: " + (endDateTime != null ? endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "Não definida"));
        System.out.println("Prioridade: " + priority);
        System.out.println("Estado: " + (state ? "Concluída ✅" : "Pendente ❌"));

        if(!subtasks.isEmpty()){
            System.out.println("\n-----Subtarefas associadas-----");
            for(SubTask subtask:subtasks){
                subtask.displaySubTask();
            }
            System.out.println("-------------------------------");
        }
        System.out.println("-----------------------------\n");
    }

    public void editSubTask(){
        if(this.subtasks.isEmpty()){
            System.out.println("Esta tarefa não possui sub-tarefas associadas...");
            return;
        }

        System.out.println("-----Lista de sub-tarefas-----");
        for(SubTask subtask:subtasks){
            subtask.displaySubTask();
        }

        System.out.print("Digite o id da sub-tarefa: ");
        int subtaskIdtoEdit = scanner.nextInt();
        scanner.nextLine();

        SubTask subTaskExist = null;
        for(SubTask subtask:this.subtasks){
            if(subtask.getSubTaskId()==subtaskIdtoEdit){
                subTaskExist = subtask;
                break;
            }
        }

        if(subTaskExist==null){
            System.out.println("❌ Nenhuma subtarefa encontrada com esse ID.");
            return;
        }

        System.out.println("\nO que deseja modificar na subtarefa?");
        System.out.println("1. Nome");
        System.out.println("2. Estado (Concluir subtarefa)");
        System.out.println("0. Cancelar edição");
        System.out.print("\nEscolha uma opção (0-2): ");

        int suboption = scanner.nextInt();
        scanner.nextLine();
        switch(suboption){
            case 0:
                System.out.println("Exiting...");
                break;
            case 1:
                System.out.println("Nome atual: "+subTaskExist.getName());
                System.out.print("Novo nome: ");
                String newName = scanner.nextLine();
                subTaskExist.setName(newName);
                System.out.println("✅ Nome da subtarefa atualizado!");
                break;
            case 2:
                subTaskExist.markAsDone();
                System.out.println("✅ Subtarefa marcada como concluída!");
                break;
            default:
                System.out.println("❌ Opção inválida! Tente novamente.");
        }
    }

    public void editTask()
    {
        displayTask();
        boolean continuee = true;
        while(continuee) {
            System.out.println("Qual o campo deseja modificar?");
            System.out.println("1. Nome");
            System.out.println("2. Descrição");
            System.out.println("3. Categoria");
            System.out.println("4. Data de vencimento");
            System.out.println("5. Prioridade");
            System.out.println("6. Estado (Concluir tarefa)");
            System.out.println("7. Modificar sub-tarefa");
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
                    System.out.println("Nome atual: "+this.getTaskName());
                    System.out.print("Novo nome: ");
                    this.taskName = scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Descrição atual: "+this.description);
                    System.out.print("Nova descrição: ");
                    this.description = scanner.nextLine();
                    break;
                case 3:
                    System.out.println("Categoria atual: "+this.getCategory());
                    System.out.println("Nova categoria: ");
                    this.category = scanner.nextLine();
                    break;
                case 4:
                    System.out.println("Data de Vencimento atual: " + endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    try {
                        System.out.print("Nova Data de Vencimento (no formato yyyy-MM-dd): ");
                        LocalDate newDate = LocalDate.parse(scanner.nextLine());

                        System.out.print("Digite a hora de vencimento ou Enter para 00:00 (HH:mm): ");
                        String timeInput = scanner.nextLine().trim();
                        LocalTime newTime = timeInput.isEmpty() ? LocalTime.MIDNIGHT : LocalTime.parse(timeInput);

                        setEndDate(newDate, newTime, true);
                        System.out.println("✅ Data de vencimento atualizada para: " + newDate + " " + newTime);
                    } catch (Exception e) {
                        System.out.println("❌ Erro: Formato inválido! Use o formato correto.");
                    }
                    break;
                case 5:
                    System.out.println("Prioridade atual: " + this.priority);
                    int newPriority;
                    do {
                        System.out.print("Nova prioridade (1- Baixa, 2- Média, 3- Alta): ");
                        newPriority = scanner.nextInt();
                        scanner.nextLine();
                    } while (!setPriority(newPriority));
                    break;
                case 6:
                    markAsDone();
                    System.out.println("✅ Tarefa marcada como concluída!");
                    break;
                case 7:
                    editSubTask();
                    break;
                default:
                    System.out.println("❌ Opção inválida! Tente novamente.");
            }
        }
    }

    public static void createTask() {
        System.out.println("\n🔹 Criar Nova Tarefa 🔹");

        System.out.print("Nome da tarefa: ");
        String taskName = scanner.nextLine();
        System.out.print("Descrição: ");
        String description = scanner.nextLine();
        System.out.print("Categoria da tarefa(ex: Trabalho, Estudos, Pessoal)   ): ");
        String category = scanner.nextLine();

        //implementar Data
        LocalDate newDate = null;
        LocalTime newTime = LocalTime.MIDNIGHT;
        boolean validate = false;

        while(!validate){
            System.out.print("Data de vencimento (yyyy-MM-dd): ");
            try{
                newDate = LocalDate.parse(scanner.nextLine());
                validate = true;
            }catch(Exception e){
                System.out.println("❌ Formato inválido! Use yyyy-MM-dd.");
            }
        }

        System.out.print("Pretende adicionar uma hora à tarefa? (s/n): ");
        String choice = scanner.nextLine();

        if(choice.equalsIgnoreCase("s")){
            boolean validateHour = false;
            while(!validateHour){
                System.out.print("Digite a hora de vencimento da tarefa (HH:mm): ");
                try{
                    newTime = LocalTime.parse(scanner.nextLine());
                    validateHour = true;
                } catch (Exception e) {
                    System.out.println("❌ Formato inválido! Use HH:mm");
                }
            }
        }

        LocalDateTime newDateTime = LocalDateTime.of(newDate,newTime);
        System.out.println("✅ Data de vencimento definida para: " + newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        int priority;
        do {
            System.out.print("Prioridade (1- Baixa, 2- Média, 3- Alta): ");
            priority = scanner.nextInt();
            scanner.nextLine();
        } while (priority < 1 || priority > 3);

        Task newTask = new Task(taskName,category, description, newDateTime, priority);

        System.out.print("Pretende adicionar alguma subtarefa(s/n)? : ");
        String answer = scanner.nextLine();
        if(answer.equalsIgnoreCase("s")){
            boolean next = true;
            while(next){
                System.out.print("Nome da subtarefa: ");
                String subTaskName = scanner.nextLine();
                SubTask subtask = new SubTask(subTaskName);
                newTask.addSubTask(subtask);
                System.out.print("Adicionar outra subtarefa? (s/n): ");
                next = scanner.nextLine().equalsIgnoreCase("s");
            }
        }

        filehandler.writeTask(newTask);

        System.out.println("✅ Tarefa criada e guardada com sucesso!\n");
    }

    public static void searchTasks(String string) {
        List<Task> tasks = filehandler.readTasks();
        List<Task> result = new ArrayList<>();

        for (Task task : tasks) {
            String category = (task.getCategory() != null) ? task.getCategory() : ""; // Evita NullPointerException
            String name = (task.getTaskName() != null) ? task.getTaskName() : "";

            if (category.equalsIgnoreCase(string) || name.equalsIgnoreCase(string)) {
                result.add(task);
            }
        }

        if (result.isEmpty()) {
            System.out.println("🔍 Nenhuma tarefa encontrada para: " + string);
        } else {
            for (Task task : result) {
                task.displayTask();
            }
        }
    }

    public static void sortTask(String option) {
        List<Task> tasks = filehandler.readTasks();

        if (tasks.isEmpty()) {
            System.out.println("📭 Não há tarefas para ordenar.");
            return;
        }

        if (option.equalsIgnoreCase("prioridade")) {
            tasks.sort(Comparator.comparingInt(Task::getPriority).reversed());
        } else if (option.equalsIgnoreCase("data")) {
            tasks.sort(Comparator.comparing(Task::getEndDateTime));
        } else {
            System.out.println("❌ Opção inválida. Escolha 'prioridade' ou 'data'.");
            return;
        }

        // Mostrar as tarefas ordenadas
        for (Task task : tasks) {
            task.displayTask();
        }
    }
}
