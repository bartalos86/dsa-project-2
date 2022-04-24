package sk.stuba.fiit.xbartalosm;

import sk.stuba.fiit.xbartalosm.deprecated.BDDTree;
import sk.stuba.fiit.xbartalosm.layered.DecisionDiagram;

public class Main {


    public static void main(String[] args) {
        String binaryExpression = "ABC+AC+ac";
        binaryExpression = "ABD+ACd+ABCDd+ABC+abc+ABD";
        //binaryExpression = "ABCD+BCD+D+B";
        binaryExpression = "ABCdD";
        System.out.println("binaryExpression".hashCode());

        BDDTree tree = new BDDTree();
        DecisionDiagram diagram = DecisionDiagram.createBDD(binaryExpression,"ABCD");

        diagram.printTree();
        diagram.printTreeNorm();

        System.out.println(DecisionDiagram.BDDuse(diagram,"1100"));


        //tree.BDD_create(binaryExpression,"ABC");
       // tree.printTree();
    }

}
