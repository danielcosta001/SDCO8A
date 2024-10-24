import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Servidor {

    private static Socket socket;
    private static ServerSocket server;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    private int porta = 1025;

    public void iniciar() {
        FileHandler fileHandler = new FileHandler();
        HashMap<Integer, String> fortuneMap = new HashMap<>();
        try {
            fileHandler.parseFortunes(fortuneMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            server = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta: " + porta);

            while (true) {
                socket = server.accept();
                System.out.println("Cliente conectado.");
                entrada = new DataInputStream(socket.getInputStream());
                saida = new DataOutputStream(socket.getOutputStream());

                try {
                    while (true) {
                        String mensagemEntrada = entrada.readUTF();
                        HashMap<String, String> request = parserRequest(mensagemEntrada);
                        String comando = request.get("method");

                        switch (comando) {
                            case "read":
                                if (fortuneMap.isEmpty()) {
                                    saida.writeUTF("{\"result\":\"Erro: Nenhuma fortuna disponível.\"}");
                                } else {
                                    String mensagemSaida = fileHandler.readRandomFortune(fortuneMap);
                                    saida.writeUTF("{\"result\":\"" + mensagemSaida + "\"}");
                                }
                                break;

                            case "write":
                                String newFortune = request.get("args").replaceAll("[\\[\\]\"]", "");
                                fileHandler.addFortune(fortuneMap, newFortune);
                                saida.writeUTF("{\"result\":\"" + newFortune + "\"}");
                                break;

                            default:
                                saida.writeUTF("{\"result\":\"Erro: Comando desconhecido.\"}");
                                break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Cliente desconectado.");
                } finally {
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> parserRequest(String mensagem) {
        mensagem = mensagem.substring(1, mensagem.length() - 1);
        String[] pares = mensagem.split(",,");
        HashMap<String, String> map = new HashMap<>();

        for (String par : pares) {
            String[] entrada = par.replace('"', ' ').split("::");
            map.put(entrada[0].trim(), entrada[1].trim());
        }
        return map;
    }

    public static void main(String[] args) {
        new Servidor().iniciar();
    }
}
