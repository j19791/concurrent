package br.com.alura.servidor;

import java.io.PrintStream;

public class ComandoC1 implements Runnable {

	private PrintStream saida;

	/**
	 * Cada comando deve devolver o resultado da execu��o, ou pelo menos informar ao
	 * 
	 * 
	 * 
	 */

	public ComandoC1(PrintStream saida) {
		// recebemos no construtor a sa�da do cliente.
		// informar ao cliente que a execu��o foi finalizada.:
		this.saida = saida;
	}

	@Override
	public void run() {

		System.out.println("Executando comando c1");

		try {
			// faz algo bem demorado
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		// devolvendo resposta para o cliente
		saida.println("Comando c1 executado com sucesso!");

	}

}
