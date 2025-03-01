package classes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class SubTask {
    private static final Random random = new Random();

    @JsonProperty("subTaskId") // Garante que o Jackson reconhece este campo
    private int subTaskId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("state")
    private boolean state;

    // 🔥 Construtor vazio necessário para desserialização
    public SubTask() {
    }

    public SubTask(String name) {
        this.subTaskId = generateRandomId();
        this.name = name;
        this.state = false;
    }

    public int getSubTaskId() {
        return subTaskId;
    }

    public String getName() {
        return name;
    }

    public boolean getState() {
        return state;
    }

    public void markAsDone() {
        this.state = true;
    }

    private int generateRandomId() {
        return random.nextInt(9000) + 1000;
    }

    public void setName(String name){
        this.name = name;
    }

    public void displaySubTask() {
        System.out.println("Id: " + this.getSubTaskId());
        System.out.println("Nome: " + this.getName());
        System.out.println("Estado: " + (this.getState() ? "Concluída ✅" : "Pendente ❌"));
        System.out.println();
    }
}
