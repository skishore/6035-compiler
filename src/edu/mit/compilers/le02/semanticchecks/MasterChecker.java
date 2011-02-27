package edu.mit.compilers.le02.semanticchecks;

import edu.mit.compilers.le02.ast.ASTNode;

public class MasterChecker {
    /** Holds the MasterChecker singleton. */
    private static MasterChecker instance;

    /**
     * Retrieves the MasterChecker singleton, creating if necessary.
     */
    public static MasterChecker getInstance() {
        if (instance == null) {
            instance = new MasterChecker();
        }
        return instance;
    }

    /**
     * Runs all currently implemented semantic checks. 
     */
    public static void checkAll(ASTNode root) {
        CheckDeclarations.check(root);
        CheckMain.check(root);
        CheckArrayLengths.check(root);
        CheckMethodCalls.check(root);
        CheckExprTypes.check(root);
        CheckBreakContinue.check(root);
    }
}
