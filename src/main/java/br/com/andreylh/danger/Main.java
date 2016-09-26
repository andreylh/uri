package br.com.andreylh.danger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String line = null;
		while (!(line = br.readLine()).equals("00e0")) {
			String zerosStr = line.substring(3);
			zerosStr = zerosStr.equals("0") ? "" : String.format("%0" + zerosStr + "d", 0);

			int value = Integer.valueOf(line.substring(0, 2) + zerosStr);

			String binary = Integer.toBinaryString(value);

			binary = binary.substring(1, binary.length()).concat(binary.substring(0, 1));

			System.out.println(Integer.parseInt(binary, 2));
		}
	}

}
