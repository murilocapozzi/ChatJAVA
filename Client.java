package entities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {
	
	private ClientSocket client_socket;
	private Scanner scanner;
	private BufferedWriter out;
	
	public Client() {
		scanner = new Scanner(System.in);
	}
	
	@Override
	public void run() { /* Método da thread para receber as mensagens de outros clientes */
		String str;
		while((str = client_socket.getMessage()) != null) {

			System.out.println("Mensagem: " + str);
			
		}
	}
	
	public void start() throws UnknownHostException, IOException { /* Registra o cliente, podendo escrever e ler mensagens vindas de outros */
		client_socket = new ClientSocket();
		
		/* Receber o fluxo de caracteres */
		System.out.println("Cliente conectado ao servidor em " + client_socket.IP + ":" + Server.PORT);
		
		new Thread(this).start(); /* Thread que irá receber as mensagens dos outros clientes antes mesmo que este mande uma mensagem */
		envioMensagem();
	}
	
	private void envioMensagem() throws IOException { /* Lê do terminal pelo Scanner(System.in) e faz o fluxo da mensagem para o servidor */
		String str;
		do {
			System.out.println("Digite uma mensagem ou /exit para finalizar:");
			str = scanner.nextLine();

			client_socket.sendMessage(str);			
			
		} while(!str.equalsIgnoreCase("/exit"));
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
