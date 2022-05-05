package sk.stuba.fiit.xbartalosm;

import sk.stuba.fiit.xbartalosm.deprecated.BDDTree;
import sk.stuba.fiit.xbartalosm.hashtables.closed.ClosedHashTable;
import sk.stuba.fiit.xbartalosm.layered.DecisionDiagram;

public class Main {


    public static void main(String[] args) {
        String binaryExpression = "ABC+AC+ac";
        binaryExpression = "ABD+ACd+ABCDd+ABC+abc+ABD";
        //binaryExpression = "ABCD+BCD+D+B";
        //binaryExpression = "ABCdD";
        //binaryExpression = "ABC+abc";
        binaryExpression = "AB+C";
        //binaryExpression = "aBcDeFGhIJkL+ABCDeFghIJkL+deFGhIJkL+BcDEFgh+AbCdefGHijkl+JkL";
        binaryExpression = "fG+aBCdefG+ABCD";
        //binaryExpression = "DE+bcDE+AbCDe";
        //ABcDEfGH+abCDEfghIJKL+Jk+ABcdeFghIJk+BC+abcDEfghIJKL
        binaryExpression = "AB+cA+bD";
        String order = "CBDA";
        System.out.println(binaryExpression.hashCode());

        BDDTree tree = new BDDTree();
        DecisionDiagram diagram = DecisionDiagram.createBDD(binaryExpression,order);

        ClosedHashTable<TesterInput> testInput = new Tester().generateTestingVector(binaryExpression,order);
        TesterInput[] inputs = testInput.getAllItems(TesterInput.class);
        //010111001100

        for (int i = 0; i < inputs.length; i++) {
            int expected = DecisionDiagram.BDDuse(diagram,inputs[i].getKey());
            if(inputs[i].getValue() != expected)
                System.out.println(inputs[i].getKey() + " " + inputs[i].getValue() + " expected: " + expected +
                        (inputs[i].getValue() == expected ? " correct" : " error"));


        }

        diagram.printTree();
        diagram.printTreeNorm();

      /*  System.out.println("---------Testing performance---------");
        for (int i = 1; i < 26; i++) {
            System.out.println("Case #" + i);
           // testPerformance(i);
        }
        System.out.println("######################################");*/

        System.out.println("---------Testing correctness---------");
        for (int i = 1; i < 50; i++) {
            //System.out.println("Case #" + i);
            //testCorrectness(12);
        }

        /*while(!testCorrectness(12)){

        }*/

        System.out.println();
    }

    private static boolean testCorrectness(int n){
        final String abc =  "ABCDEFGHIJKLMONPQRSTUVWXYZ";
        String testingOrder = abc.substring(0,n);
        int parts = Math.max(4,n/2);

        System.out.printf("Testing for %d characters, %d parts and %d min. part length \n", n,parts,Math.min(2,n));

        String testingExpression = new Tester().generateExpression(parts,Math.min(2,n),n,testingOrder);
        System.out.println(testingExpression);

        double startTime = System.nanoTime();
        DecisionDiagram testDiagram = DecisionDiagram.createBDD(testingExpression,testingOrder);
        double endTime = System.nanoTime();

        testDiagram.printStatistics();
        System.out.println("Time: " + (endTime - startTime)/100000 + " ms");



        //Correctness test
        ClosedHashTable<TesterInput> testInput = new Tester().generateTestingVector(testingExpression,testingOrder);
        TesterInput[] inputs = testInput.getAllItems(TesterInput.class);
        boolean wasIncorrect = false;
        for (int i = 0; i < inputs.length; i++) {
            int expected = DecisionDiagram.BDDuse(testDiagram,inputs[i].getKey());
            if(inputs[i].getValue() != expected){
                System.out.println(inputs[i].getKey() + " " + inputs[i].getValue() + " expected: " + expected +
                        (inputs[i].getValue() == expected ? " correct" : " error"));
                wasIncorrect = true;
            }

        }

        if(!wasIncorrect)
            System.out.println("Results: CORRECT!");

        System.out.println("--------------------");

        return wasIncorrect;

    }


    private static void testPerformance(int n){
        final String abc =  "ABCDEFGHIJKLMONPQRSTUVWXYZ";
        Tester tester = new Tester();
        String testingOrder = abc.substring(0,n);
        int parts = n*3;

        System.out.printf("Testing for %d characters, %d parts and %d min. part length \n", n,parts,Math.min(2,n));

        String testingExpression = tester.generateExpression(parts,Math.min(2,n),n,testingOrder);
        System.out.println(testingExpression);

        double startTime = System.nanoTime();
        DecisionDiagram testDiagram = DecisionDiagram.createBDD(testingExpression,testingOrder);
        double endTime = System.nanoTime();

        testDiagram.printStatistics();
        System.out.println("Creation time: " + (endTime - startTime)/100000 + " ms");

         startTime = System.nanoTime();
         DecisionDiagram.BDDuse(testDiagram,tester.generateRandomInput(n));
         endTime = System.nanoTime();
        System.out.println("BDDuse time: " + (endTime - startTime)/100000 + " ms");

        System.out.println("--------------------");


        //Correctness test
      /*  ClosedHashTable<TesterInput> testInput = new Tester().generateTestingVector(testingExpression,testingOrder);
        TesterInput[] inputs = testInput.getAllItems(TesterInput.class);

        for (int i = 0; i < inputs.length; i++) {
            int expected = DecisionDiagram.BDDuse(testDiagram,inputs[i].getKey());
            if(inputs[i].getValue() != expected)
                System.out.println(inputs[i].getKey() + " " + inputs[i].getValue() + " expected: " + expected +
                        (inputs[i].getValue() == expected ? " correct" : " error"));


        }*/
    }

}
