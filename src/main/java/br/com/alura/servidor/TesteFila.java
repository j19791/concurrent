package br.com.alura.servidor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TesteFila {

	public static void main(String[] args) {

		// Queue<String> fila = new LinkedList<>(); não é thread-safe

		BlockingQueue<String> fila = new ArrayBlockingQueue<>(3); // thread safe

		/*
		 * fila.offer("c1");// O offer() oferece um lugar numa fila limitada
		 * fila.offer("c2"); fila.offer("c3");
		 */

		try {
			fila.put("c1");
			fila.put("c2");
			fila.put("c3");
			fila.put("c4"); // se a fila estiver cheia o put esperará até que haja
			// capacidade.
			// ArrayBlockingQueue tem limite definido (3)
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * System.out.println(fila.poll());// retira da fila respeitando FIFO
		 * System.out.println(fila.poll()); System.out.println(fila.poll());
		 * System.out.println(fila.poll()); // null
		 */

		try {
			System.out.println(fila.take());
			System.out.println(fila.take());
			System.out.println(fila.take());
			System.out.println(fila.take());// O take() espera até o próximo elemento ficar disponível na fila e assim
											// trava a thread.

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(fila.size()); // irá imprimir 0, pois o pool retira os elementos da fila

	}

}
