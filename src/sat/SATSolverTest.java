package sat;
import java.io.FileWriter;

import java.io.PrintWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import sat.env.*;
import sat.formula.*;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String[] args) throws IOException{
    	File file = new File("F:\\Personal\\Term4\\infoSysProg\\Project-2D-starting\\sampleCNF\\s8Sat.cnf");
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
                    	//System.out.println("v:"+v);
                        variables.add(v); //这里！+j+1！！！
                        posLiterals.add(sat.formula.PosLiteral.make(variables.get(j)));
                        negLiterals.add(sat.formula.NegLiteral.make(variables.get(j)));
                    }
                    startParsing = Boolean.TRUE;
                } else if (startParsing) {   //start parsing after problem line
                    String tempString = list.get(i);
                    String[] temp = tempString.split("\\s+");
                    for(String x:temp){
                    	int currentLiteral = Integer.parseInt(x);
                        if(currentLiteral<0){
                            myClause = myClause.add(negLiterals.get((-1)*currentLiteral-1));
                        } else if(currentLiteral>0) {
                            myClause = myClause.add(posLiterals.get(currentLiteral-1));
                        } else{
                        }
                    }
                    myFormula = myFormula.addClause(myClause);
                    myClause = new sat.formula.Clause();
                }
            }

        }
        posLiterals = new ArrayList<sat.formula.PosLiteral>();
        negLiterals = new ArrayList<sat.formula.NegLiteral>();
        String file_name = "F:\\Personal\\Term4\\infoSysProg\\Project-2D-starting\\sampleCNF\\BoolAssignment.txt";
        Writer data = new FileWriter(file_name, false);
        
        
        System.out.println("SAT solver starts!");
        long started = System.nanoTime();
        Environment result = SATSolver.solve(myFormula);
        long time = System.nanoTime();
        long timeTaken = time - started;
        if (result == null) {
        	System.out.println("not satisfiable");
        }else {
        	System.out.println("satisfiable");
        	//System.out.println(result);
        	String resultString = result.toString();
        	System.out.println(resultString);
        	int ii = resultString.length()-1;
        	String outputLineNum = "";
        	String outputLineBool = "";
        	String outputLine = "";
        	while (ii>=0) {
        		//System.out.println("hay");
        		if (Character.toString(resultString.charAt(ii)).equals(",")) {
        			//System.out.println("hay1");
        			outputLine = outputLineNum+":"+outputLineBool+"\n";
        			//System.out.println(outputLine);
        			data.write(outputLine);
        			outputLine = "";
        			outputLineNum = "";
        			outputLineBool ="";
        			ii--;
        		}else {
        			//System.out.println("hay else");
	    			if (Character.isDigit(resultString.charAt(ii))) {
	    				outputLineNum = Character.toString(resultString.charAt(ii))+outputLineNum;
	    				ii--;
	    			}else if
	    			    (Character.isLetter(resultString.charAt(ii))) {
	    				outputLineBool = Character.toString(resultString.charAt(ii))+outputLineBool;
	    				ii--;
	    			}else {
	    				ii--;
	    			}
        		}
        	}
        }
        data.close();
        System.out.println("Time:"+(timeTaken/1000000.0)+"ms");
    }
	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}
