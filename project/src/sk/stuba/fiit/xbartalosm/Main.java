package sk.stuba.fiit.xbartalosm;

import sk.stuba.fiit.xbartalosm.hashtables.closed.ClosedHashTable;
import sk.stuba.fiit.xbartalosm.layered.DecisionDiagram;

import java.util.Random;

public class Main {


    public static void main(String[] args) {

        System.out.println("---------Testing performance---------");
        for (int i = 1; i < 26; i++) {
            System.out.println("Case #" + i);
           testPerformance(i);
        }
        System.out.println("######################################");

        System.out.println("---------Testing correctness---------");
        for (int i = 1; i < 16; i++) {
            System.out.println("Case #" + i);
            testCorrectness(i);
        }

        //Test for testing lots of different cases
       /* System.out.println("---------Testing performance bulk---------");
        for (int i = 1; i < 10000; i++) {
            System.out.println("Case #" + i);
            testPerformance(25);
        }
        System.out.println("######################################");*/

        /*while(!testCorrectness(15)){

        }*/


        System.out.println("-----------Testing sample-----------");
        sampleCorrectnessTest("ABCD+BCD+D+B","ABCD");

        System.out.println();
    }

    private static boolean testCorrectness(int n){
        final String abc =  "ABCDEFGHIJKLMONPQRSTUVWXYZ";
        String testingOrder = abc.substring(0,n);
        Random rand = new Random();
        int parts = rand.nextInt(20)+5;

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
    }

    private static void sampleCorrectnessTest(String expression, String order){

        System.out.println("Expression: " + expression);
        System.out.println("Character order: " + order);

        DecisionDiagram testDiagram = DecisionDiagram.createBDD(expression,order);
        testDiagram.printStatistics();

        ClosedHashTable<TesterInput> testInput = new Tester().generateTestingVector(expression,order);
        TesterInput[] inputs = testInput.getAllItems(TesterInput.class);

        for (int i = 0; i < inputs.length; i++) {
            int result = DecisionDiagram.BDDuse(testDiagram,inputs[i].getKey());
                System.out.println(inputs[i].getKey() + ": expected " + inputs[i].getValue() + " result: " + result +
                        (inputs[i].getValue() == result ? " - correct" : " - error"));



        }
    }

}
