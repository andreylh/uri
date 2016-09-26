package br.com.andreylh.prime;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			int lines = sc.nextInt();

			List<Integer> values = new ArrayList<>();
			for (int i = 0; i < lines; i++) {
				values.add(sc.nextInt());
			}
			Main processor = new Main();
			processor.testPrimes(values);
		} finally {
			sc.close();
		}
	}

	public void testPrimes(List<Integer> values) {
		for (Integer value : values) {
			checkIfIsPrime(value);
		}
	}

	private void checkIfIsPrime(int numberToCheck) {

		int squareRoot = (int) Math.sqrt(numberToCheck);
		for (int i = 2; i <= squareRoot; i++) {
			if (numberToCheck % i == 0) {
				System.out.println("Not Prime");
				return;
			}
		}

		System.out.println("Prime");
	}
}