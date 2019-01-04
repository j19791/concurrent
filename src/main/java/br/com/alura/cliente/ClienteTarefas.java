package br.com.alura.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteTarefas {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 12345);// passando o ip e porta q se deseja conectar.
		System.out.println("Conexão Estabelecida");

		// enviar comandos para o servidor
		PrintStream saida = new PrintStream(socket.getOutputStream());
		saida.println("c1");

		// travar o nosso cliente
		Scanner teclado = new Scanner(System.in);
		teclado.nextLine();

		saida.close();
		teclado.close();
		socket.close();// depois q conectar, para o cliente e servidor.
		System.out.println("Conexão Fechada");

	}

}
