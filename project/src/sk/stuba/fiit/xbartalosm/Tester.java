package sk.stuba.fiit.xbartalosm;

import sk.stuba.fiit.xbartalosm.hashtables.closed.ClosedHashTable;

import java.util.Random;

public class Tester {

    public String generateExpression(int parts, int minChars, int maxChars, String order){

        StringBuilder expression = new StringBuilder();

        Random random = new Random();

        for(int i = 0; i< parts; i++){

            int expLenght = random.nextInt(maxChars-minChars+1)+minChars;
            StringBuilder expressionPart = new StringBuilder();
            int orderCounter = random.nextInt(order.length() - expLenght +1);
            for (int j = 0; j < expLenght; j++) {
                if(random.nextBoolean()){
                    expressionPart.append(order.charAt(orderCounter++));
                }else{
                    expressionPart.append(Character.toLowerCase(order.charAt(orderCounter++)));
                }
            }

            if(expression.isEmpty()){
                expression.append(expressionPart);
            }else{
                expression.append("+").append(expressionPart);
            }

        }

        return expression.toString();


    }

    public ClosedHashTable<TesterInput> generateTestingVector(String expression, String order){
        ClosedHashTable<TesterInput> testingVector = new ClosedHashTable<>();//int[(int)Math.pow(2,order.length())];
        int vecCounter = 0;

        for(int i = 0; i < Math.pow(2,order.length()); i++){
            StringBuilder inputBuilder = new StringBuilder();
            inputBuilder.append(Integer.toBinaryString(i)).insert(0,"0".repeat(order.length()-inputBuilder.length()));
            String tempExpression = expression;
            for (int j = 0; j < order.length(); j++) {
                String valueCharacter = Character.toString(inputBuilder.charAt(j));
              int value = Integer.parseInt(valueCharacter);
              String currentCharacter = Character.toString(order.charAt(j));
              String currentLowercase = Character.toString(Character.toLowerCase(order.charAt(j)));

              //If the input is 1
              if(valueCharacter.equals("1")){
                  //Replace A = 1 and a = 0
                  tempExpression = tempExpression.replace( currentCharacter,"1");
                  tempExpression = tempExpression.replace( currentLowercase,"0");

              }else{
                  //Replace A = 0 and a = 1
                  tempExpression = tempExpression.replace( currentCharacter,"0");
                  tempExpression = tempExpression.replace( currentLowercase,"1");
              }
            }

            boolean hasTruePart = false;
            String[] parts = tempExpression.split("\\+");

            for (int j = 0; j < parts.length; j++) {
                if(parts[j].contains("1") && !parts[j].contains("0")){
                    hasTruePart = true;
                    break;
                }
            }
            TesterInput input = new TesterInput(inputBuilder.toString(),hasTruePart ? 1 : 0);
            testingVector.insert(input);

        }
        return testingVector;

    }

    public String generateRandomInput(int length){
        StringBuilder testingBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if(random.nextBoolean()){
                testingBuilder.append("1");
            }else{
                testingBuilder.append("1");
            }
        }

        return testingBuilder.toString();
    }

}
