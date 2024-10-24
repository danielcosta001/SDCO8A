import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Cliente {

    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    private int porta = 1025;

    public HashMap<String, String> parser(String mensagem) {
        HashMap<String, String> hashMap = new HashMap<>();
        mensagem = mensagem.substring(1, mensagem.length() - 1);

        String[] pares = mensagem.split(",,");
        for (String par : pares) {
            String[] entrada = par.replace('"', ' ').split("::");
            hashMap.put(entrada[0].trim(), entrada[1].trim());
        }
        return hashMap;
    }

    public void iniciar() {
        System.out.println("Cliente iniciado na porta: " + porta);

        try {
            socket = new Socket("127.0.0.1", porta);
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("Menu \n R to read - W to write - E to get out \n");
                String comando = br.readLine();

                switch (comando) {
                    case "R":
                        saida.writeUTF("{\"method\":\"read\",,\"args\":\"[]\"}");
                        HashMap<String, String> response = parser(entrada.readUTF());
                        if (response.containsKey("result")) {
                            System.out.println("Read:\n{result:" + response.get("result") + "\n}");
                        } else {
                            System.out.println("Erro na leitura.");
                        }
                        break;

                    case "W":
                        System.out.println("Escreva a nova fortuna:");
                        String novaFortuna = br.readLine();
                        saida.writeUTF("{\"method\":\"write\",,\"args\":\"[" + novaFortuna + "\"]\"}");
                        response = parser(entrada.readUTF());
                        System.out.println("Write:\n{result:" + response.get("result") + "\n}");
                        break;

                    case "E":
                        System.out.println("Saindo...");
                        socket.close();
                        return;

                    default:
                        System.out.println("Comando desconhecido.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cliente().iniciar();
    }
}
