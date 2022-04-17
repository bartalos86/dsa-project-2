package sk.stuba.fiit.xbartalosm;

public class Main {


    public static void main(String[] args) {
        String binaryExpression = "ABC+AC+ac";
        //binaryExpression = "AB+AC+BC";
        BDDTree tree = new BDDTree();

        tree.BDD_create(binaryExpression,"ABC");
        tree.printTree();
    }

}
