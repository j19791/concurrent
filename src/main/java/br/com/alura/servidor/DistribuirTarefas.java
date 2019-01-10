package br.com.alura.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {

	private Socket socket;
	private ServidorTarefas servidor;

	public DistribuirTarefas(Socket socket, ServidorTarefas servidor) {
		this.socket = socket;
		this.servidor = servidor;
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
					// confirma��o do o cliente
					saidaCliente.println("Confirma��o do comando c1");
					break;
				}
				case "c2": {
					saidaCliente.println("Confirma��o do comando c2");
					break;
				}

				case "fim": {
					saidaCliente.println("Desligando o servidor");
					servidor.parar();
					break;
				}

				default: {
					saidaCliente.println("Comando n�o encontrado");
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
