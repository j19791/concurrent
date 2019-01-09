package br.com.alura.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefas {

	public static void main(String[] args) throws Exception {
		final Socket socket = new Socket("localhost", 12345);// passando o ip e porta q se deseja conectar.
		System.out.println("Conexão Estabelecida");

		Thread threadEnviaComando = new Thread(new Runnable() {

			public void run() {
				try {
					// enviar comandos para o servidor
					PrintStream saida = new PrintStream(socket.getOutputStream());

					// ler os comandos do teclado e essa entrada enviar para o servidor
					Scanner teclado = new Scanner(System.in);
					while (teclado.hasNextLine()) {
						String linha = teclado.nextLine();

						// condição de saída. Quando digitamos apenas ENTER, sairemos do laço
						if (linha.trim().equals("")) {
							break;
						}

						saida.println(linha);

					}

					// fechando recursos
					saida.close();
					teclado.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			}
		});

		Thread threadRecebeResposta = new Thread(new Runnable() {

			public void run() {
				try {
					System.out.println("Recebendo dados do servidor");
					Scanner respostaServidor = new Scanner(socket.getInputStream());

					while (respostaServidor.hasNextLine()) {
						String linha = respostaServidor.nextLine();
						System.out.println(linha);
					}

					respostaServidor.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}
			}
		});

		threadRecebeResposta.start();
		threadEnviaComando.start();

		// thread main vai esperar
		threadEnviaComando.join(); // thread main espera enquanto o usuario enviar comandos. A thread main ficará
									// esperando até a outra thread acabar.

		socket.close();// depois q conectar, para o cliente e servidor. Fechado na thread main
		System.out.println("Conexão Fechada");

	}

}
