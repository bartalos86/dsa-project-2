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
        //binaryExpression = "AB+BC+AD";
        System.out.println(binaryExpression.hashCode());

        BDDTree tree = new BDDTree();
        DecisionDiagram diagram = DecisionDiagram.createBDD(binaryExpression,"ABCD");

        diagram.printTree();
        diagram.printTreeNorm();

      /*  System.out.println(DecisionDiagram.BDDuse(diagram,"1001"));
        //DecisionDiagram testDiagram = DecisionDiagram.createBDD(new Tester().generateExpression(5,2,20,"ABCDEFGHIJKLMONPQRST"),"ABCDEFGHIJKLMONPQRST");
        //testDiagram.printTree();
       //
        String testingExpression = new Tester().generateExpression(5,2,20,"ABCDEFGHIJKLMONPQRST");
        String testingOrder = "ABCDEFGHIJKLMONPQRST";

        ClosedHashTable<TesterInput> testInput = new Tester().generateTestingVector(testingExpression,testingOrder);
        TesterInput[] inputs = testInput.getAllItems(TesterInput.class);

        DecisionDiagram testDiagram = DecisionDiagram.createBDD(testingExpression,testingOrder);

        for (int i = 0; i < inputs.length; i++) {
            int expected = DecisionDiagram.BDDuse(testDiagram,inputs[i].getKey());
            if(inputs[i].getValue() != expected)
            System.out.println(inputs[i].getKey() + " " + inputs[i].getValue() + " expected: " + expected +
                    (inputs[i].getValue() == expected ? " correct" : " error"));


        }*/

        testPerformance(20);

        System.out.println();
    }

    private static void testPerformance(int n){
        final String abc =  "ABCDEFGHIJKLMONPQRSTUVWXYZ";
        String testingOrder = abc.substring(0,n);

        String testingExpression = new Tester().generateExpression(n*5,Math.min(2,n),n,testingOrder);
        System.out.println(testingExpression);

        double startTime = System.nanoTime();
        DecisionDiagram testDiagram = DecisionDiagram.createBDD(testingExpression,testingOrder);
        double endTime = System.nanoTime();

        testDiagram.printStatistics();
        System.out.println("Time: " + (endTime - startTime)/100000);


        //Correctness test
        ClosedHashTable<TesterInput> testInput = new Tester().generateTestingVector(testingExpression,testingOrder);
        TesterInput[] inputs = testInput.getAllItems(TesterInput.class);

        for (int i = 0; i < inputs.length; i++) {
            int expected = DecisionDiagram.BDDuse(testDiagram,inputs[i].getKey());
            if(inputs[i].getValue() != expected)
                System.out.println(inputs[i].getKey() + " " + inputs[i].getValue() + " expected: " + expected +
                        (inputs[i].getValue() == expected ? " correct" : " error"));


        }

    }

}
