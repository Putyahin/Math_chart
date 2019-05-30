package com.mathChart;
/*
Priority of operations:
1.Braces
2.Unary minus and plus
3.Math power and functions like sine etc.
4.Multiply, divide
5.Plus, minus
*/

import java.util.ArrayList;
import java.util.Stack;

/**
 * Calculates the value of mathematical function f(x) with the
 * parameter x. 
 *
 */
public class FunctionCalculator {
	private String function; // This is our function f(x) which will be calculated
	private double x; // This is our value of x - argument of function f(x)
	public ArrayList<String> error; // Errors
	private int offset;
//	private double result;
	private char op;
	private double object;
	private char[] expression; // Function f(x) presented as array of chars
	private boolean noObj;
	private Stack<Character> opStack;
	private Stack<Double> objectStack;
	private Stack<Integer> offsetStack;

//	public FunctionCalculator() {
//		error = new ArrayList<>();
//	}

	/**  The constructor which sets values of fields function and x*/
	public FunctionCalculator(String function, double x) {
		this.function = function;
		this.x = x;
		error = new ArrayList<>();

	}

	/** The setter for field function - mathematical function f(x)*/
	public void setFunction(String function) {
		this.function = function;
	}

	/** The setter for x argument of function f(x)*/
	public void setX(double x) {
		this.x = x;
	}

	/** The getter for field function - mathematical function f(x)*/
	public String getFunction() {
		return this.function;
	}

	/** The getter for x argument of function f(x)*/
	public double getX() {
		return this.x;
	}

	/** The starting point in calculating the mathematical function */
	public double Calculator() {
		function = function.toLowerCase();
		function = function.replace(" ", "");
		function = function.replace(",", ".");
//		result = 0.0;
		object = 0.0;
		offset = 0;
		expression = function.toCharArray();
		op = '+';
		noObj = true;
		opStack = new Stack<>();
		objectStack = new Stack<>();
		offsetStack = new Stack<>();
		return ExpressionPriority5();
	}

	
//	private double Calculate() {
//
//		result = ExpressionPriority5();
//		return result;
//	}

	/** Checks the correctness of braces in the mathematical function*/
	public boolean CheckBraces() {
		boolean ok = false;
		int braces = 0;
		for (int i = 0; i < function.length(); i++) {
			switch (function.charAt(i)) {
			case '(':
				braces++;
				break;
			case ')':
				braces--;
				break;
			}
		}
		if (braces == 0)
			ok = true;

		return ok;
	}

	/** Checks the correctness of operations in the mathematical function*/
	public boolean CheckOp() {
		boolean ok = true;
		char[] exp;
		exp = function.toCharArray();
		offset = 0;
		object = 0.0;
		for (int i = 0; i < exp.length - 1; i++) {
			op = '0';
			if (exp[i] == '+' || exp[i] == '-' || exp[i] == '/' || exp[i] == '*' || exp[i] == '^') {
				offset = i + 1;
				GetObject();
				if (op != '0' && op != 'f')
					ok = false;
			}
			if (exp[i] == '(') {
				offset = i + 1;
				GetObject();
				if (op != '0' && op != '-' && op != 'f')
					ok = false;
			}
			if (exp[i] == 'x') {
				offset = i + 1;
				GetObject();
				if (op == '0')
					ok = false;
			}

		}
		return ok;
	}

	/** This method is used to extract the number and parse it to double*/
	private double GetNumber() {
		double result = 0.0;
		int k = 0;
		while (Character.isDigit(expression[offset + k]) || expression[offset + k] == '.') {
			k++;
			if (offset + k > expression.length - 1)
				break;
		}
		try {
			result = Double.parseDouble(function.substring(offset, offset + k));
			offset = offset + k;
		} catch (NullPointerException | NumberFormatException e) {
			error.add("invalid number");
			offset = function.length();
			offsetStack.push(offset);
		}
		return result;
	}

	/** Returns the value of x argument*/
	private double GetX() {
		double result = x;
		offset++;
		return result;
	}

	/** Extracts the next object in field function. According to the type of the object,
	 * method detects mathematical operation, mathematical function, x, number*/
	private void GetObject() {
		if (offset <= function.length() - 1) {
			if (Character.isDigit(expression[offset])) {
				object = GetNumber();
				noObj = false;
			} else
				switch (expression[offset]) {
				case '+':
					op = '+';
					offset++;
					break;
				case '-':
					op = '-';
					offset++;
					break;
				case '*':
					op = '*';
					offset++;
					break;
				case '/':
					op = '/';
					offset++;
					break;
				case '^':
					op = '^';
					break;
				case 's':
					op = 'f';
					break;
				case 'c':
					op = 'f';
					break;
				case 't':
					op = 'f';
					break;
				case 'l':
					op = 'f';
					break;
				case 'p':
					offset++;
					if (offset <= function.length() - 1) {
						if (expression[offset] == 'i') {
							object = Math.PI;
							offset++;
							noObj = false;
						} else {
							error.add("error in math expression");
							offset = function.length();
							offsetStack.push(offset);
						}
					} else {
						error.add("error in math expression");
						offset = function.length();
						offsetStack.push(offset);
					}
					break;
				case 'e':
					object = Math.E;
					offset++;
					noObj = false;
					break;
				case '(':
					break;
				case ')':
					op = '+';
					object = 0.0;
					break;
				case 'x':
					object = GetX();
					noObj = false;
					break;
				default:
					error.add("error in math expression");
					offset = function.length();
					offsetStack.push(offset);
					break;
				}

		}

	}

	/** Method works with braces*/
	private double ExpressionPriority1() {
		double result = 0.0;
		if (offset <= function.length() - 1) {
			if (expression[offset] == '(') {
				offset++;
				opStack.push(op);
				objectStack.push(object);
				op = '+';
				object = 0.0;
				result = ExpressionPriority5();
				op = opStack.pop();
				object = objectStack.pop();
				offset = offsetStack.pop();
				GetObject();

			} else if (expression[offset] == ')') {
				offsetStack.push(++offset);
				offset = expression.length;
				result = object;

			} else {

				GetObject();
				result = object;
			}
		} else
			result = object;
		return result;
	}

	/** Method works with unary plus or minus*/
	private double ExpressionPriority2() {
		if (noObj && op == '-') {
			GetObject();
			return -ExpressionPriority1();
		}
		if (noObj && op == '+') {
			GetObject();
			return ExpressionPriority1();
		}
		return ExpressionPriority1();
	}

	/** Method works with mathematical functions*/
	private double ExpressionPriority3() {
		double result = ExpressionPriority2();
		while ((op == '^') || op == 'f') {
			if (offset > function.length() - 1)
				break;
			if (expression[offset] == '^') {
				offset++;
				GetObject();
				double temp = ExpressionPriority2();
				result = Math.pow(result, temp);
			} else if (offset < function.length() - 3) {
				if ("sin".equals(function.substring(offset, offset + 3))) {
					offset += 3;
					GetObject();
					double temp = ExpressionPriority2();
					result = Math.sin(temp);
				} else if ("cos".equals(function.substring(offset, offset + 3))) {
					offset += 3;
					GetObject();
					double temp = ExpressionPriority2();
					result = Math.cos(temp);
				} else if ("tan".equals(function.substring(offset, offset + 3))) {
					offset += 3;
					GetObject();
					double temp = ExpressionPriority2();
					result = Math.tan(temp);
				} else if ("log".equals(function.substring(offset, offset + 3))) {
					offset += 3;
					GetObject();
					double temp = ExpressionPriority2();
					result = Math.log(temp);
				} else if (offset < function.length() - 4) {
					if ("sqrt".equals(function.substring(offset, offset + 4))) {
						offset += 4;
						GetObject();
						double temp = ExpressionPriority2();
						result = Math.sqrt(temp);
					}
					else {
						error.add("invalid function");
						offset = function.length();
						offsetStack.push(offset);
					}
				} else {
					error.add("invalid function");
					offset = function.length();
					offsetStack.push(offset);
				}
			} else {
				error.add("invalid function");
				offset = function.length();
				offsetStack.push(offset);
			}

		}
		return result;
	}

	/** Method works with multiplying and dividing*/
	private double ExpressionPriority4() {
		double result = ExpressionPriority3();
		while (op == '*' || op == '/') {
			if (offset > function.length() - 1)
				break;
			switch (op) {
			case '*':
				GetObject();
				result = result * ExpressionPriority3();
				break;
			case '/':
				GetObject();
				result = result / ExpressionPriority3();
				break;
			}

		}

		return result;
	}

	/** Method works with adding and subtracting*/
	private double ExpressionPriority5() {
		double result = 0.0;
		noObj = true; // This variable is used for unary minus detection
		GetObject();
		result = ExpressionPriority4();
		while (offset <= function.length() - 1) {
			while (op == '+' || op == '-') {
				if (offset > function.length() - 1)
					break;
				switch (op) {
				case '+':
					GetObject();
					result = result + ExpressionPriority4();
					break;
				case '-':
					GetObject();
					result = result - ExpressionPriority4();
					break;

				}

			}
		}
		return result;
	}
}
