package br.com.andreylh.friends;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String line = null;
		Main process = new Main();
		while ((line = br.readLine()) != null) {
			process.execute(line);
		}
	}

	private BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

	public void execute(String operation) throws IOException {
		String result = processGroup(operation);
		result = sortAlphabetically(result);
		bw.write(result);
		bw.newLine();
		bw.flush();
	}

	private String sortAlphabetically(String result) {
		String onlyLetters = result.substring(1, result.length() - 1);

		char[] letters = onlyLetters.toCharArray();

		Arrays.sort(letters);

		return "{" + String.valueOf(letters) + "}";
	}

	private String processGroup(String operation) {
		int openGroupIndex = operation.indexOf("(");

		String result = operation;
		while (openGroupIndex >= 0) {
			int closeGroupIndex = findCloseParenthesesIndex(operation);
			String group = operation.substring(openGroupIndex + 1, closeGroupIndex);
			String processedGroup = processGroup(group);
			StringBuilder sb = new StringBuilder(operation);
			result = sb.replace(openGroupIndex, closeGroupIndex + 1, processedGroup).toString();

			operation = result;
			openGroupIndex = result.indexOf("(");
		}

		result = calculate(result);

		return result;
	}

	private int findCloseParenthesesIndex(String operation) {
		int openParentheses = 0;
		for (int i = 0; i < operation.length(); i++) {
			char c = operation.charAt(i);
			if (c == '(') {
				++openParentheses;
			}

			if (c == ')') {
				if (openParentheses == 1) {
					return i;
				}

				--openParentheses;
			}
		}
		return 0;
	}

	private String calculate(String operation) {
		String result = operation;

		result = calculateIntersection(result);

		result = calculateUnionsAndDifferences(result);

		return result;
	}

	private String calculateIntersection(String result) {
		int intersectionIndex = result.indexOf("*");

		while (intersectionIndex >= 0) {
			int elementStartIndex = result.substring(0, intersectionIndex).lastIndexOf("{");
			int elementEndIndex = result.indexOf("}", intersectionIndex + 1) + 1;
			StringBuilder sb = new StringBuilder(result);
			String intersection = intersection(sb.substring(elementStartIndex, elementEndIndex));
			result = sb.replace(elementStartIndex, elementEndIndex, intersection).toString();

			intersectionIndex = result.indexOf("*");
		}
		return result;
	}

	private String calculateUnionsAndDifferences(String result) {
		Matcher m = getMatcher(result);
		while (m.find()) {
			int index = m.start();
			int elementStartIndex = result.substring(0, index).lastIndexOf("{");
			int elementEndIndex = result.indexOf("}", index + 1) + 1;
			StringBuilder sb = new StringBuilder(result);
			String s = "";
			if (result.charAt(index) == '+') {
				s = union(result.substring(elementStartIndex, elementEndIndex));
			} else {
				s = difference(result.substring(elementStartIndex, elementEndIndex));
			}
			result = sb.replace(elementStartIndex, elementEndIndex, s).toString();
			m.reset(result);
		}
		return result;
	}

	private Pattern operationPattern = Pattern.compile("\\+|-");
	private Matcher matcher = null;

	private Matcher getMatcher(String result) {
		if (matcher == null) {
			matcher = operationPattern.matcher(result);
		} else {
			matcher.reset(result);
		}
		return matcher;
	}

	private String getFirstGroup(String operation) {
		int openGroupIndex = operation.indexOf("{");
		int closeGroupIndex = operation.indexOf("}");

		if (openGroupIndex >= 0 && closeGroupIndex >= 0) {
			return operation.substring(openGroupIndex + 1, closeGroupIndex);
		}
		return "";
	}

	private String getLastGroup(String operation) {
		int openGroupIndex = operation.lastIndexOf("{");
		int closeGroupIndex = operation.lastIndexOf("}");

		if (openGroupIndex >= 0 && closeGroupIndex >= 0) {
			return operation.substring(openGroupIndex + 1, closeGroupIndex);
		}
		return "";
	}

	private String union(String operation) {
		String first = getFirstGroup(operation);
		String second = getLastGroup(operation);

		Set<Character> chars = new LinkedHashSet<>();

		for (char c : first.toCharArray()) {
			chars.add(c);
		}

		for (char c : second.toCharArray()) {
			chars.add(c);
		}

		StringBuilder result = new StringBuilder();

		for (char c : chars) {
			result.append(c);
		}

		return "{" + result.toString() + "}";
	}

	private String difference(String operation) {
		String first = getFirstGroup(operation);
		String second = getLastGroup(operation);

		StringBuilder result = new StringBuilder();

		for (char c : first.toCharArray()) {
			if (second.indexOf(c) < 0) {
				result.append(c);
			}
		}

		return "{" + result.toString() + "}";
	}

	private String intersection(String operation) {
		String first = getFirstGroup(operation);
		String second = getLastGroup(operation);

		StringBuilder result = new StringBuilder();

		for (char c : first.toCharArray()) {
			if (second.indexOf(c) >= 0) {
				result.append(c);
			}
		}

		return "{" + result.toString() + "}";
	}

}
