import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.lang.String;

public class Client implements Runnable {
	
	private ClientSocket client_socket;
	private Scanner scanner;
	private BufferedWriter out;
	
	public Client() {
		scanner = new Scanner(System.in);
	}
	

	/* Método da thread para receber as mensagens de outros clientes */
	@Override
	public void run() {
		String str;
		while((str = client_socket.getMessage()) != null) {

			System.out.println(str);
			
		}
	}
	
	/* Registra o cliente, podendo escrever e ler mensagens vindas de outros */
	public void start() throws UnknownHostException, IOException {
		String usr;
		System.out.print("Insira seu usuario: ");
		usr = scanner.nextLine();
		client_socket = new ClientSocket();
		/* Receber o fluxo de caracteres */
		System.out.println("Cliente conectado ao servidor em " + client_socket.IP + ":" + Server.PORT);
		
		new Thread(this).start(); /* Thread que irá receber as mensagens dos outros clientes antes mesmo que este mande uma mensagem */
		envioMensagem(usr);
	}
	
	/* Lê do terminal pelo Scanner(System.in) e faz o fluxo da mensagem para o servidor */
	private void envioMensagem(String usr) throws IOException {
		String str;
		do {
			System.out.println("Digite uma mensagem ou /exit para finalizar:");
			str = scanner.nextLine();
			str = usr + ": " + str;
			client_socket.sendMessage(str);			
			
		} while(!str.equalsIgnoreCase(usr + ": /exit"));
	}
	
	
	/* ------------------------------------- */
	public static void main(String[] args) {
		
		try {
			Client client = new Client();
			client.start();
		} catch (IOException e) {
			System.out.println("Erro ao iniciar o cliente: " + e.getMessage());
		}
		
		System.out.println("Cliente finalizado.");
	}
	
}
