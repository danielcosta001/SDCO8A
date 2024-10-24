import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Scanner;

public class Principal {

    public final static Path FILE_PATH = Paths.get("fortune-br.txt");
    private int totalFortunes = 0;

    public class FortuneFileHandler {

        // Conta quantas fortunas (separadas por "%") existem no arquivo
        public int countFortunes() throws FileNotFoundException {
            int fortuneCount = 0;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(new FileInputStream(FILE_PATH.toString()))))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals("%")) fortuneCount++;
                }
                System.out.println(fortuneCount);

            } catch (IOException e) {
                System.out.println("SHOW: Exceção na leitura do arquivo.");
            }
            return fortuneCount;
        }

        // Faz o parsing do arquivo e armazena as fortunas em um HashMap
        public void parseFile(HashMap<Integer, String> fortuneMap) throws FileNotFoundException {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(new FileInputStream(FILE_PATH.toString()))))) {

                int fortuneIndex = 0;
                String line;

                while ((line = reader.readLine()) != null) {

                    StringBuilder fortune = new StringBuilder();
                    while (line != null && !line.equals("%")) {
                        fortune.append(line).append("\n");
                        line = reader.readLine();
                    }

                    fortuneMap.put(fortuneIndex, fortune.toString());
                    fortuneIndex++; // Incrementa o índice para a próxima fortuna
                }

            } catch (IOException e) {
                System.out.println("SHOW: Exceção na leitura do arquivo.");
            }
        }

        // Lê uma fortuna aleatória do HashMap
        public void readRandomFortune(HashMap<Integer, String> fortuneMap) {
            Object[] keys = fortuneMap.keySet().toArray();
            int randomIndex = new SecureRandom().nextInt(keys.length);
            Object randomKey = keys[randomIndex];

            String fortune = fortuneMap.get(randomKey);

            System.out.println("\n\nChave: " + randomKey);
            System.out.println("Fortuna:\n" + fortune);
        }

        // Escreve uma nova fortuna no HashMap
        public void addFortune(HashMap<Integer, String> fortuneMap) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Digite a fortuna que desejas adicionar na base de dados: ");
            String newFortune = scanner.nextLine();

            int newKey = fortuneMap.size(); // Usa o tamanho do mapa para gerar uma nova chave
            fortuneMap.put(newKey, newFortune);

            System.out.println("Fortuna adicionada!\nChave: " + newKey + "\nFortuna: " + newFortune);
        }
    }

    // Método principal para iniciar o processo
    public void iniciar() {
        FortuneFileHandler fileHandler = new FortuneFileHandler();
        try {
            totalFortunes = fileHandler.countFortunes();
            HashMap<Integer, String> fortuneMap = new HashMap<>();
            fileHandler.parseFile(fortuneMap);
            fileHandler.readRandomFortune(fortuneMap);
            fileHandler.addFortune(fortuneMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Principal().iniciar();
    }
}
