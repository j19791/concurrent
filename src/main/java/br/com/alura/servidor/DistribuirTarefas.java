package br.com.alura.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DistribuirTarefas implements Runnable {

	private Socket socket;
	private ServidorTarefas servidor;
	private ExecutorService threadPool;

	private BlockingQueue<String> filaComandos;

	public DistribuirTarefas(ExecutorService threadPool, BlockingQueue<String> filaComandos, Socket socket,
			ServidorTarefas servidor) {
		this.socket = socket;
		this.servidor = servidor;
		this.threadPool = threadPool;

		this.filaComandos = filaComandos;
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

					Callable c2WS = new ComandoC2ChamaWS(saidaCliente);
					// passando os comandos para o pool, resultado é um Future
					Future<String> futureWS = this.threadPool.submit(c2WS);

					Callable c2Banco = new ComandoC2AcessaBanco(saidaCliente);
					Future<String> futureBanco = this.threadPool.submit(c2Banco);

					// classes Callable devem ser chamadas pelo ExecutorService com submit

					/**
					 * String resultadoWS = futureWS.get();
					 * 
					 * Ao chamar o método get() do Future, vamos bloquear a thread (a thread fica
					 * travada) e não queremos isso, pois o switch executa a thread para receber
					 * qualquer comando, e não queremos travar o recebimento de outros comandos.
					 */

					// passando a tarefa para juntar os resultados para o pool
					this.threadPool.submit(new JuntaResultadosFutureWSFutureBanco(futureWS, futureBanco, saidaCliente));

					break;
				}

				case "c3": {
					this.filaComandos.put(comando); // bloqueia se tiver cheia
					saidaCliente.println("Comando c3 adicionado na fila");
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
