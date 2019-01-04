package br.com.alura.servidor;

import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {

	private Socket socket;

	public DistribuirTarefas(Socket socket) {
		this.socket = socket;
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

			while (entradaCliente.hasNextLine()) {
				String comando = entradaCliente.nextLine();
				System.out.println(comando);
			}
			entradaCliente.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
