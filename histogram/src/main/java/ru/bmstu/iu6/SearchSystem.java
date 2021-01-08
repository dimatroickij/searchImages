package ru.bmstu.iu6;

import ru.bmstu.iu6.integrate.Indexer;
import ru.bmstu.iu6.integrate.Searcher;

import java.io.IOException;
import java.util.Scanner;

public class SearchSystem {

    public static void indexing() throws IOException {
        Indexer indexer = new Indexer("C:\\indexing");
        int i = indexer.createIndex("C:\\dataHistogram");
        indexer.close();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Индексация файлов");
//        indexing();
        System.out.println("Файлы проиндексированы\n");
        System.out.println("Введите выражение для поиска");
//        Scanner in = new Scanner(System.in);
//        String expr = in.next();
        String expr = "(e1+e2+e3)|(e2+e3+e4)";
        E E1 = new E(expr);
        Searcher searcher = new Searcher("C:\\indexing");
        searcher.search(E1.getValue());
    }
}
