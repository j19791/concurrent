package br.com.alura.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class DistribuirTarefas implements Runnable {

	private Socket socket;
	private ServidorTarefas servidor;
	private ExecutorService threadPool;

	public DistribuirTarefas(ExecutorService threadPool, Socket socket, ServidorTarefas servidor) {
		this.socket = socket;
		this.servidor = servidor;
		this.threadPool = threadPool;
	}

	public void run() {
		System.out.println("Distribuindo as tarefas para o cliente " + socket);

		/*
		 * try { Thread.sleep(20000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */

		try {
			// recebendo comando do cliente
			Scanner entradaCliente = new Scanner(socket.getInputStream());

			PrintStream saidaCliente = new PrintStream(socket.getOutputStream());

			while (entradaCliente.hasNextLine()) {

				String comando = entradaCliente.nextLine();
				System.out.println("Comando recebido " + comando);

				switch (comando) {
				case "c1": {
					// confirmação do o cliente
					saidaCliente.println("Confirmação do comando c1");

					ComandoC1 c1 = new ComandoC1(saidaCliente);// tarefa
					this.threadPool.execute(c1);// rodando thread através de pool

					break;
				}
				case "c2": {
					saidaCliente.println("Confirmação do comando c2");
					ComandoC2 c2 = new ComandoC2(saidaCliente);
					this.threadPool.execute(c2);
					break;
				}

				case "fim": {
					saidaCliente.println("Desligando o servidor");
					servidor.parar();
					break;
				}

				default: {
					saidaCliente.println("Comando não encontrado");
				}
				}

				System.out.println(comando);
			}

			saidaCliente.close();
			entradaCliente.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
