import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class Server {

	public static final int PORT = 24872;
	private ServerSocket server_socket;
	
	private final List<ClientSocket> clients = new LinkedList<>();
	
	/* Inicia o servidor, esperando que clientes conectem-se a ele */
	public void start() throws IOException {
		
		System.out.println("Servidor iniciado na porta " + PORT);
		server_socket = new ServerSocket(PORT);
		esperaConexaoCliente();
		
	}
	
	/* Conectado um cliente, é inserido na lista e inicia uma thread */
	private void esperaConexaoCliente() throws UnknownHostException, IOException{
		while(true) {
			ClientSocket client_socket = new ClientSocket(server_socket.accept());
			clients.add(client_socket);
			
			System.out.println("Cliente " + client_socket.getRemoteSocketAddress() + " conectado");
			
			new Thread(() -> {
				try {
					chatClienteLoop(client_socket);
				} catch (IOException e) {
					System.out.println("Erro ao conversar com o cliente: " + e.getMessage());
				}
			}).start();
			
		}
	}
	
	/* Método executado pela thread para pegar e redirecionar mensagens dele aos outros */
	public void chatClienteLoop(ClientSocket cs) throws IOException {
		String str;
		try {
			while((str = cs.getMessage()) != null) {
				
				if(str.equalsIgnoreCase("/exit"))
					return;
				
				redirecionaMensagem(cs, str);
				
			}
		} finally { /* Fecha os descritores abertos e remove da lista */
			cs.close();
			clients.remove(cs);
		}
	}
	
	/* Método para que os outros clientes leiam a mensagem */
	public void redirecionaMensagem(ClientSocket sender, String str) {
		for(ClientSocket cs: clients) {
			if(sender != cs)
				cs.sendMessage(str);
		}
	}
	
	
	
	/* ------------------------------------- */
	public static void main(String [] args) {
		
		try {
			Server serv = new Server();
			serv.start();
		} catch(IOException e){
			System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
		}
		
		System.out.println("Servidor finalizado");
	}
	
}
