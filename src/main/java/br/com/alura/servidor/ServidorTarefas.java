package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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

	private BlockingQueue<String> filaComandos;

	public ServidorTarefas() throws IOException {
		System.out.println("---- Iniciando Servidor ----");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newCachedThreadPool(new FabricaDeThreads());// usando o tratamento de erros de
																				// thread mais adequado

		// remova as três linhas anteriores do método rodar()

		this.estaRodando.set(true);

		this.filaComandos = new ArrayBlockingQueue<>(2);// capacidade de 2 elementos na fila

		iniciarConsumidores();
	}

	public void rodar() throws IOException {

		while (this.estaRodando.get()) { // usando o atributo
			try {
				Socket socket = servidor.accept();
				System.out.println("Aceitando novo cliente na porta " + socket.getPort());

				DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool, filaComandos, socket, this);// this:
																													// passando
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

	private void iniciarConsumidores() {
		int qtdConsumidores = 2;
		for (int i = 0; i < qtdConsumidores; i++) {
			TarefaConsumir tarefa = new TarefaConsumir(filaComandos);
			this.threadPool.execute(tarefa);
		}
	}

}
