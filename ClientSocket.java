package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClientSocket {

	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;
	public final String IP = "127.0.0.1";
	private final int PORT = 24872;
	
	/* Construtor para o cliente */
	public ClientSocket() throws UnknownHostException, IOException {
		socket = new Socket(IP, PORT);
		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	/* Construtor do cliente para o servidor */
	public ClientSocket(Socket s) throws IOException {
		this.socket = s;
		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	/* Recebe mensagem do buffer de leitura */
	public String getMessage() {
		try {
			return in.readLine();
		} catch (IOException e) {
			return null;
		}
	}
	
	/* Envia a mensagem pelo buffer de escrita */
	public void sendMessage(String str){
		try {
			out.write(str);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Erro ao enviar mensagem do cliente " + this.getRemoteSocketAddress() + e.getMessage());
		}
	}
	
	/* Fecha os descritores abertos */
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Erro ao fechar socket " + e.getMessage());
		}
	}
	
	
	/* Identificação do socket */
	public SocketAddress getRemoteSocketAddress() {
		return socket.getRemoteSocketAddress();
	}
	
}
