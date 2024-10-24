import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

public class FileHandler {
    
    // Caminho para o arquivo "fortune-br.txt"
    private static final Path filePath = Paths.get("src\\fortune-br.txt");
    //private static final Path filePath = Paths.get("C:\\Users\\danie\\OneDrive\\Desktop\\Distribuidos Atividades\\lab2-master\\lab2-master\\a_lab2\\src\\fortune-br.txt");

    // Método para contar quantas "fortunes" existem no arquivo
    public int countFortunes() throws IOException {
        int lineCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("%")) {
                    lineCount++;
                }
            }
        }
        
        return lineCount;
    }

    // Método para fazer o parse das "fortunes" e armazená-las no HashMap
    public void parseFortunes(HashMap<Integer, String> fortuneMap) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            StringBuilder fortune = new StringBuilder();
            int fortuneIndex = 0;

            while ((line = br.readLine()) != null) {
                if (line.equals("%")) {
                    if (fortune.length() > 0) {
                        fortuneMap.put(fortuneIndex, fortune.toString().trim());
                        fortuneIndex++;
                        fortune.setLength(0);  // Limpa o StringBuilder para a próxima fortuna
                    }
                } else {
                    fortune.append(line).append("\n");
                }
            }

            // Adiciona a última fortuna (se não terminar com %)
            if (fortune.length() > 0) {
                fortuneMap.put(fortuneIndex, fortune.toString().trim());
            }
        }
    }

    // Método para selecionar uma "fortuna" aleatoriamente
    public String readRandomFortune(HashMap<Integer, String> fortuneMap) {
        Random random = new Random();
        int randomIndex = random.nextInt(fortuneMap.size());
        return fortuneMap.get(randomIndex);
    }

    // Método para adicionar uma nova "fortuna" ao arquivo
    public void addFortune(HashMap<Integer, String> fortuneMap, String newFortune) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString(), true))) {
            // Escreve a nova fortuna no arquivo, garantindo o formato correto
            writer.write("\n%\n");
            writer.write(newFortune.trim());
            writer.write("\n%");
        }
        // Atualiza o HashMap com a nova fortuna
        fortuneMap.put(fortuneMap.size(), newFortune.trim());
    }
}
