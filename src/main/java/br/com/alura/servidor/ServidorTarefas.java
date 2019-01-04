package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTarefas {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("---- Iniciando Servidor ----");

		// socket � o ponto-final de um fluxo de comunica��o entre duas aplica��es,
		// atrav�s de uma rede: servidor - definir uma porta maior que 1023

		ServerSocket servidor = new ServerSocket(12345);// clientes fazem conex�o atrav�s da porta 12345,
		// Socket socket = servidor.accept(); // bloqueante e trava a thread principal.
		// Ao rodar, a thread main fica parada
		// at� receber uma conex�o atrav�s de um cliente e parar de rodar depois q
		// foi estabelecida.

		// aceitando v�rios clientes e ficando sempre online
		/*
		 * while (true) { Socket socket = servidor.accept(); Thread.sleep(20000);//
		 * simulando o cliente submetendo tarefas mais pesadas, demorando 20s p/terminar
		 * System.out.println("Aceitando novo cliente na porta " + socket.getPort());//
		 * toda comunica��o a partir desse // momento � feita com uma porta // dedicada
		 * pra cada cliente. // o servidor fica travado para cada cliente. aguardando
		 * inclusive o t�rmino da // execu��o de um cliente p/ q o outro cliente possa
		 * come�ar a usar o servidor. // Todos os clientes est�o sendo executados na
		 * thread principal (main)
		 * 
		 * }
		 */

		// agora utilizando concurrent
		// ExecutorService poolDeThreads = Executors.newFixedThreadPool(5); // p/ melhor
		// uso dos recursos, utilizando um
		// pool de threads (maximo 5)

		ExecutorService poolDeThreads = Executors.newCachedThreadPool();// cresce/ou diminui dinamicamente. descarta as
																		// threads que ficam ociosas por mais de 60
																		// segundos

		// newSingleThreadExecutor : apenas 1 thread

		// utilizando threads p/ q os clientes n]ao fiquem travados
		while (true) {

			Socket socket = servidor.accept();
			System.out.println("Aceitando novo cliente na porta " + socket.getPort());// as cmunica��es entre os
																						// clientes s�o feitas em portas
																						// dedicadas
			// new Thread(new DistribuirTarefas(socket)).start();// nosso servidor n�o trava
			// mais e aceita v�rios clientes.
			poolDeThreads.execute(new DistribuirTarefas(socket)); //
		}
	}

}
