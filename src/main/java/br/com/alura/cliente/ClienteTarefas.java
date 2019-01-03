package br.com.alura.cliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteTarefas {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 12345);// passando o ip e porta q se deseja conectar.
		System.out.println("Conexão Estabelecida");

		// travar o nosso cliente
		Scanner teclado = new Scanner(System.in);
		teclado.nextLine();

		socket.close();// depois q conectar, para o cliente e servidor.
		System.out.println("Conexão Fechada");

	}

}
