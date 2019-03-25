
/*
Приоритеты операций:
1.Скобки
2.Унарное сложение вычитание
3.Возведение в степень, функции
4.Умножение, деление
5.Сложение, вычитание
*/


import java.util.ArrayList;
import java.util.Stack;

public class FunctionCalculator {
    private String function; // This is our function f(x) which will be calculated
    private double x; // This is our value of x  - argument of function f(x)
    private ArrayList<String> error; //Errors
    private int offset;
    private double result;
    private char op;
    private double object;
    private char[] expression; // Function f(x) presented as array of chars
    private boolean noObj;
    private Stack<Character> opStack;
    private Stack<Double> objectStack;
    private Stack<Integer> offsetStack;

    public FunctionCalculator(String function, double x) {
        this.function = function;
        this.x = x;
        error = new ArrayList<>();
    }

    public double Calculator() {
        function=function.toLowerCase();
        function=function.replaceAll(" ","");
        result = 0.0;
        object = 0.0;
        offset = 0;
        expression = function.toCharArray();
        op = '+';
        noObj = true;
        opStack = new Stack<>();
        objectStack = new Stack<>();
        offsetStack = new Stack<>();
        System.out.println(expression);
        return Calculate();
    }

    public double Calculate() {

        result = ExpressionPriority5();
        return result;
    }
    public boolean CheckBraces() {
        boolean ok=false;
        int braces=0;
        for(int i=0;i<function.length();i++) {
            switch (function.charAt(i)) {
                case '(':
                    braces++;
                    break;
                case ')':
                    braces--;
                    break;
            }
        }
        if (braces==0) ok=true;

        return ok;
    }
    public double GetNumber() {
        double result = 0.0;
        int k = 0;
        while (Character.isDigit(expression[offset + k]) || expression[offset + k] == '.') {
            k++;
            if (offset + k > expression.length - 1) break;
        }
        try {
            result = Double.parseDouble(function.substring(offset, offset + k));
            offset = offset + k;
            System.out.println("Number=" + result + " Offset " + offset);
        } catch (Exception e) {
            error.add("Invalid number");
            System.out.println("Error");
            offset = function.length() - 1;
        }
        return result;
    }

    public double GetX() {
        double result = x;
        offset++;
        System.out.println("X is " + result);
        return result;
    }

    public void GetObject() {
        System.out.println("Offset " + offset);
        if (offset <= function.length() - 1) {
            if (Character.isDigit(expression[offset])) {
                object = GetNumber();
                noObj = false;
            } else
                switch (expression[offset]) {
                    case '+':
                        op = '+';
                        offset++;
                        System.out.println("Case +");
                        break;
                    case '-':
                        op = '-';
                        offset++;
                        System.out.println("Case -");
                        break;
                    case '*':
                        op = '*';
                        offset++;
                        System.out.println("Case *");
                        break;
                    case '/':
                        op = '/';
                        offset++;
                        System.out.println("Case /");
                        break;
                    case '^':
                        op = '^';
                        //offset++;
                        System.out.println("Case ^");
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
                            } else error.add("Invalid function");
                        } else {
                            error.add("Invalid function");
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
                }

        }

    }


    public double ExpressionPriority1() {
        double result = 0.0;
        if (offset <= function.length() - 1) {
            if (expression[offset] == '(') {
                offset++;
                opStack.push(op);
                objectStack.push(object);
                op = '+';
                object = 0.0;
                System.out.println("push");
                result = ExpressionPriority5();
                op = opStack.pop();
                object = objectStack.pop();
                offset = offsetStack.pop();
                System.out.println("pop, offset =" + offset + " object =" + object + " op =" + op + " result =" + result);
                GetObject();

            } else if (expression[offset] == ')') {
                offsetStack.push(++offset);
                offset = expression.length;
                result = object;

            } else {

                GetObject();
                result = object;
            }
        } else result = object;
        return result;
    }

    public double ExpressionPriority2() {
        //double result = ExpressionPriority1();
        if (noObj && op == '-') {
            GetObject();
            return -ExpressionPriority1();
        }

        return ExpressionPriority1();
    }

    public double ExpressionPriority3() {
        double result = ExpressionPriority2();
        System.out.println("^");
        while ((op == '^') || op == 'f') {
            if (offset > function.length() - 1) break;
            if (expression[offset] == '^') {
                offset++;
                GetObject();
                double temp = ExpressionPriority2();
                result = Math.pow(result, temp);
                System.out.println("^, result=" + result + " Offset " + offset);
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
                }
            } else error.add("Invalid function");


        }
        return result;
    }

    public double ExpressionPriority4() {
        double result = ExpressionPriority3();

        System.out.println("*/");
        while (op == '*' || op == '/') {
            if (offset > function.length() - 1) break;
            switch (op) {
                case '*':
                    GetObject();
                    result = result * ExpressionPriority3();
                    System.out.println("Multiply, result=" + result + " Offset " + offset);
                    break;
                case '/':
                    GetObject();
                    result = result / ExpressionPriority3();
                    System.out.println("Division, result=" + result + " Offset " + offset);
                    break;
            }

        }

        return result;
    }

    public double ExpressionPriority5() {
        double result = 0.0;
        noObj = true; //Есть ли число. Используется для определения унарного минуса
        GetObject();
        result = ExpressionPriority4();
        while (offset <= function.length() - 1) {
            System.out.println("Offset=" + offset + " Length=" + function.length());
            System.out.println("+-");
            while (op == '+' || op == '-') {
                if (offset > function.length() - 1) break;
                switch (op) {
                    case '+':
                        GetObject();
                        result = result + ExpressionPriority4();
                        System.out.println("Sum, result=" + result + " Offset " + offset);
                        break;
                    case '-':
                        GetObject();
                        result = result - ExpressionPriority4();
                        System.out.println("Minus, result=" + result + " Offset " + offset);
                        break;

                }

            }
        }
        return result;
    }
}