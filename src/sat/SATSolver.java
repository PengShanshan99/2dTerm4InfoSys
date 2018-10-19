package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
	static int counter = 0;
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.
        ImList<Clause> formulaClauses = formula.getClauses();
        Environment environment = new Environment();
        return solve(formulaClauses,environment);
        //throw new RuntimeException("not yet implemented.");
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
    	counter += 1;
        // TODO: implement this.
        if (clauses.size()==0)
        {
        	return env;} //env has no clauses, return this environment that make the formula be true
        Clause smallest_Clause = clauses.first();
        for(Clause i: clauses) {
            if (i.isEmpty()) { //if there is empty class, it is unsatisfiable, return null
                return null;
            }
            if (smallest_Clause.size()>i.size()) { //keep track the smallest clause
                smallest_Clause = i;
            }
        }
        Literal literal = smallest_Clause.chooseLiteral(); // Randomly picking a literal
        if (smallest_Clause.isUnit()) { //if smallest only contains 1 literal, set that literal to be true
            if(literal instanceof PosLiteral) {
                Environment new_env_true = env.putTrue(literal.getVariable());
                return solve(substitute(clauses,literal), new_env_true);
                }
            else {
                Environment new_env_false = env.putFalse(literal.getVariable());
                return solve(substitute(clauses,literal), new_env_false);
                }
        }
        else {//for multi-literal clause, put the randomly picked literal to be true, if not successful trace back and try false
            Environment new_env_true = env.putTrue(literal.getVariable());
            ImList<Clause> new_temp_clauses = substitute(clauses, literal);
            Environment possible_solution = solve(new_temp_clauses,new_env_true);
            if (possible_solution == null){
                Environment new_env_false = env.putFalse(literal.getVariable());
                return solve(substitute(clauses,literal.getNegation()), new_env_false);
            }
            else{
                return possible_solution;
            }

        }
        //throw new RuntimeException("not yet implemented.");
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // TODO: implement this.
        ImList<Clause> new_clauses = new EmptyImList<Clause>();
        for (Clause i:clauses) {
            Clause inserted_clause = i.reduce(l);
            if(inserted_clause!= null) {
            	new_clauses = new_clauses.add(inserted_clause);
            }
        }
        return new_clauses;
    }

}
