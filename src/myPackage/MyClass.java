package myPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import sat.SATSolver;
import sat.env.Environment;
import sat.env.Variable;



public class MyClass {
    public static void main(String[] args) throws java.io.FileNotFoundException {
        File file = new File("F:\\Personal\\Term4\\infoSysProg\\Project-2D-starting\\sampleCNF\\largeSat.cnf");
        //System.out.println("File read");
        Scanner input = new Scanner(file);
        ArrayList<String> list = new ArrayList<String>();

        while (input.hasNextLine()) {
            list.add(input.nextLine());
        }
        //System.out.println("list created");

        int numberOfLines = list.size();

        Boolean startParsing = Boolean.FALSE;

        //ArrayList<sat.formula.Clause> clauses = new ArrayList<sat.formula.Clause>();
        sat.formula.Formula myFormula = new sat.formula.Formula();
        ArrayList<sat.env.Variable> variables = new ArrayList<sat.env.Variable>();
        ArrayList<sat.formula.PosLiteral> posLiterals = new ArrayList<sat.formula.PosLiteral>();
        ArrayList<sat.formula.NegLiteral> negLiterals = new ArrayList<sat.formula.NegLiteral>();
        //ArrayList<sat.formula.Literal> oneClause = new ArrayList<sat.formula.Literal>;
        sat.formula.Clause myClause = new sat.formula.Clause();
        
        
        for(int i=0; i<numberOfLines; i++){
            if(list.get(i).length()>0){ //skip empty lines
                //System.out.println(list.get(i));
                char a = list.get(i).charAt(0); //for comment lines, a = "c"
                String preamble = Character.toString(a);

                if(preamble.equals("p")){  //problem line
                    String[] splitP = list.get(i).split("\\s+");

                    //read info from problem line
                    String type = splitP[1];
                    int numOfVariables = Integer.parseInt(splitP[2]);
                    int numOfClauses = Integer.parseInt(splitP[3]);

                    //add variables & posLiterals & negLiterals
                    for(int j=0; j<numOfVariables; j++){
                    	sat.env.Variable v = new sat.env.Variable(""+(j+1));
                    	System.out.println("v:"+v);
                        variables.add(v); //ÕâÀï£¡+j+1£¡£¡£¡
                        posLiterals.add(sat.formula.PosLiteral.make(variables.get(j)));
                        negLiterals.add(sat.formula.NegLiteral.make(variables.get(j)));
                    }
                    System.out.println("negLiterals:"+negLiterals);
                    System.out.println("posLiterals:"+posLiterals);
                    startParsing = Boolean.TRUE;
                } else if (startParsing) {   //start parsing after problem line
                    String tempString = list.get(i);
                    String[] temp = tempString.split("\\s+");
                    for(String x:temp){
                    	int currentLiteral = Integer.parseInt(x);
                    	System.out.println("currentLiteral:"+x);
                        if(currentLiteral<0){
                        	System.out.println("adding neg");
                        	//System.out.print(negLiterals.get((-1)*currentLiteral-1));
                            myClause = myClause.add(negLiterals.get((-1)*currentLiteral-1));
                            System.out.println(myClause);
                        } else if(currentLiteral>0) {
                        	System.out.println("adding pos");
                            myClause = myClause.add(posLiterals.get(currentLiteral-1));
                            System.out.println(myClause);
                        } else{
                            //System.out.println("0");
                        }
                    }
                    //System.out.println(myClause);
                    myFormula = myFormula.addClause(myClause);
                    myClause = new sat.formula.Clause();
                    //int tempSize = temp.size
                    //System.out.println(tempSize);
                }
            }

        }
        System.out.println("SAT solver starts!");
        long started = System.nanoTime();
        Environment result = SATSolver.solve(myFormula);
        System.out.println(myFormula);
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("The result is:"+result);
        System.out.println("Time:"+(timeTaken/1000000.0)+"ms");
        //System.out.println(myFormula);

    }
}

