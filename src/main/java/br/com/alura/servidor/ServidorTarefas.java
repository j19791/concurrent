package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

	ServerSocket servidor;

	ExecutorService threadPool;

	private AtomicBoolean estaRodando = new AtomicBoolean(true);// o valor dessa variável nunca será cacheado pela
																// thread, todo o acesso
	// vai diretamente na memória dessa variável.O acesso funciona de
	// maneira atômica, como se fosse sincronizado. Volatile

	public ServidorTarefas() throws IOException {
		System.out.println("---- Iniciando Servidor ----");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newFixedThreadPool(4, new FabricaDeThreads());// usando o tratamento de erros de
																					// thread mais adequado

		// remova as três linhas anteriores do método rodar()

		this.estaRodando.set(true);
	}

	public void rodar() throws IOException {

		while (this.estaRodando.get()) { // usando o atributo
			try {
				Socket socket = servidor.accept();
				System.out.println("Aceitando novo cliente na porta " + socket.getPort());

				DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool, socket, this);// this: passando
																										// o próprio
				// servidor de Taredas
				threadPool.execute(distribuirTarefas);
			} catch (SocketException e) {
				System.out.println("SocketException, está rodando? " + this.estaRodando);
			}
		}

	}

	public void parar() throws IOException {
		this.estaRodando.set(false);
		this.threadPool.shutdown();
		this.servidor.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		ServidorTarefas servidor = new ServidorTarefas();

		servidor.rodar();

	}

}
