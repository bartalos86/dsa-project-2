package sk.stuba.fiit.xbartalosm;

import sk.stuba.fiit.xbartalosm.layered.DecisionDiagram;

public class Main {


    public static void main(String[] args) {
        String binaryExpression = "ABC+AC+ac";
        //binaryExpression = "AB+AC+BC";
        System.out.println("binaryExpression".hashCode());

        BDDTree tree = new BDDTree();
        DecisionDiagram diagram = DecisionDiagram.createBDD(binaryExpression,"ABC");
        diagram.printTree();


        //tree.BDD_create(binaryExpression,"ABC");
       // tree.printTree();
    }

}
