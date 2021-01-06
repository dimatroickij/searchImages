package ru.bmstu.iu6;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class Parsing {

    public Parsing() {}

    public Vector<String> parseString(String expression) {
        return parseString(expression, false);
    }

    public Vector<String> parseString(String expression, boolean isElement) {
        Vector<String> expr = new Vector<>();
        Vector<String> elements = new Vector<>();
        ExpressionLexer lexer = new ExpressionLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        // добавить проверку на ошибку
        ParseTree tree = parser.expr();

        List<Token> commonTokens;
        Stack<Token> stack = new Stack<>();
        commonTokens = tokens.getTokens();
        for (Token el: commonTokens){
            switch (el.getType()){
                case -1:
                    break;
                case 1:
                    stack.push(el);
                    break;
                case 2:
                    while (!stack.empty() && !stack.peek().getText().equals("(")){
                        expr.add(stack.pop().getText());
                    }
                    if (!stack.empty())
                        stack.pop();
                    break;
                case 3:
                    while (!stack.empty() && stack.peek().getType() >= el.getType()){
                        expr.add(stack.pop().getText());
                    }
                    stack.push(el);
                    break;
                default:
                    expr.add(el.getText());
                    if (isElement)
                        elements.add(el.getText());
                    break;
            }
        }
        while (!stack.empty())
            expr.add(stack.pop().getText());
        if (isElement)
            return elements;
        else
            return expr;
    }

    public Vector<Vector<String>> parseString1D(String expression) {
        Vector<Vector<String>> expr = new Vector<>();
        ExpressionLexer lexer = new ExpressionLexer(CharStreams.fromString(expression));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        // добавить проверку на ошибку
        ParseTree tree = parser.expr();

        List<Token> commonTokens;
        Stack<Token> stack = new Stack<>();
        commonTokens = tokens.getTokens();
        for (Token el: commonTokens){
            switch (el.getType()){
                case -1:
                    break;
                case 1:
                    stack.push(el);
                    break;
                case 2:
                    while (!stack.empty() && !stack.peek().getText().equals("(")){
                        Vector<String> elVector = new Vector<>();
                        elVector.add(stack.pop().getText());
                        expr.add(elVector);
                    }
                    if (!stack.empty())
                        stack.pop();
                    break;
                case 3:
                    while (!stack.empty() && stack.peek().getType() >= el.getType()){
                        Vector<String> elVector = new Vector<>();
                        elVector.add(stack.pop().getText());
                        expr.add(elVector);
                    }
                    stack.push(el);
                    break;
                case 5:
                    Vector<String> elVector = new Vector<>();
                    String element = el.getText();
                    element = element.replace("(", "").replace(")", "").replace(" ", "");
                    String[] vector = element.split(",");
                    for (String s : vector) {
                        elVector.add((String) s);
                    }
                    expr.add(elVector);
                    break;
                default:
                    Vector<String> elVector1 = new Vector<>();
                    elVector1.add(el.getText());
                    expr.add(elVector1);
                    break;
            }
        }
        while (!stack.empty()) {
            Vector<String> elVector = new Vector<>();
            elVector.add(stack.pop().getText());
            expr.add(elVector);
        }
        return expr;
    }

}
