package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTarefas {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("---- Iniciando Servidor ----");

		// socket é o ponto-final de um fluxo de comunicação entre duas aplicações,
		// através de uma rede: servidor - definir uma porta maior que 1023

		ServerSocket servidor = new ServerSocket(12345);// clientes fazem conexão através da porta 12345,
		// Socket socket = servidor.accept(); // bloqueante e trava a thread principal.
		// Ao rodar, a thread main fica parada
		// até receber uma conexão através de um cliente e parar de rodar depois q
		// foi estabelecida.

		// aceitando vários clientes e ficando sempre online
		/*
		 * while (true) { Socket socket = servidor.accept(); Thread.sleep(20000);//
		 * simulando o cliente submetendo tarefas mais pesadas, demorando 20s p/terminar
		 * System.out.println("Aceitando novo cliente na porta " + socket.getPort());//
		 * toda comunicação a partir desse // momento é feita com uma porta // dedicada
		 * pra cada cliente. // o servidor fica travado para cada cliente. aguardando
		 * inclusive o término da // execução de um cliente p/ q o outro cliente possa
		 * começar a usar o servidor. // Todos os clientes estão sendo executados na
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
			System.out.println("Aceitando novo cliente na porta " + socket.getPort());// as cmunicações entre os
																						// clientes são feitas em portas
																						// dedicadas
			// new Thread(new DistribuirTarefas(socket)).start();// nosso servidor não trava
			// mais e aceita vários clientes.
			poolDeThreads.execute(new DistribuirTarefas(socket)); //
		}
	}

}
