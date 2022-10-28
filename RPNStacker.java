
import java.util.Map;
import java.util.Stack;
import java.util.Scanner;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException; 
import java.util.function.DoubleBinaryOperator;

public class RPNStacker {
    public static Stack <Double> stack = new Stack<Double>();
    public static Map <String, DoubleBinaryOperator> exprs = new HashMap<>();
    public static Map <String, Token> tokens = new HashMap<>();
    public static Scanner in = new Scanner(System.in);

    public static String[] read_and_parse() { return in.nextLine().split(" \n"); }

    public static String readFile (String filepath) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String everything;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line + " ");
                //sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } finally {
            br.close();
        }
        return everything;
    }

    // Inicializa o map das expressões
    public static void setUpDict() {
        exprs.put("+", (a, b) -> a + b);
        exprs.put("-", (a, b) -> a - b);
        exprs.put("/", (a, b) -> a / b);
        exprs.put("*", (a, b) -> a * b);

        tokens.put("+", new Token(TokenType.PLUS, "+"));
        tokens.put("-", new Token(TokenType.MINUS, "-"));
        tokens.put("/", new Token(TokenType.SLASH, "/"));
        tokens.put("*", new Token(TokenType.STAR, "*"));
    }

    // Caso seja uma string, faz o parse e quebra em uma lista de termos
    public static Double evaluate (String expression) {
        return evaluate(expression.split(" "));
    }

    // Opera para uma lista de termos
    public static Double evaluate(String[] expr) {
        for (String s: expr) {
            if (Regex.isNum(s)) {
                try {
                    stack.push(Double.parseDouble(s));
                    System.out.println(new Token(TokenType.NUM, s.toString()).toString());
                } catch (Exception e) {
                    throw new Error("Unexpected character: " + s);
                }
            }
            else if (Regex.isOP(s)){
                Double a = stack.pop();
                Double b = stack.pop();
                try {
                    Double result = exprs.get(s).applyAsDouble(b, a);
                    stack.push(result);
                    System.out.println(tokens.get(s).toString());
                } catch (Exception e) {
                    throw new Error("Unexpected character: " + s);
                }
            }
            else {
                throw new Error("Unexpected character: " + s);
            }
        }
        System.out.println(new Token(TokenType.EOF, "EOF").toString());
        double result = stack.pop();
        if (stack.empty()) return result;
        else throw new Error("Invalid equation");
    }

    public static boolean isSignal(String s) {
        return (
            s.equals("-") || 
            s.equals("*") ||
            s.equals("+") ||
            s.equals("/")
        );
    } 

    public static void expressionsFromUser() {
        System.out.print("Insira a expressão: ");
        String expr = in.nextLine();
        Double result = -1.0;
        while (!expr.equals("")) {
            try {
                result = evaluate(expr);
                if (stack.empty()) System.out.println("O valor é: " + result);
                else System.out.println("A expressão inserida é inválida!");
            } catch (Exception e) {  
                System.out.println("A expressão inserida é inválida!");
            }
            stack.clear();
            System.out.print("Insira a expressão: ");
            expr = in.nextLine();
        }
    }

    public static void expressionsFromFile (String filepath) {
        try {
            String exprFromFile = readFile(filepath);
            System.out.println("Expressão lida: " + exprFromFile);
            System.out.println(evaluate(exprFromFile));
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao ler o arquivo. Log: " + e);    
        }
    }

    public static void main(String[] args) {
        setUpDict();
        // Lê as expressões do usuário
        // expressionsFromUser();

        // Lê a expressão de um arquivo
        expressionsFromFile("Calc1.stk");
        in.close();
    }
}

