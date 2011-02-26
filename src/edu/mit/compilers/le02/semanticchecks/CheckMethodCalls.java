package edu.mit.compilers.le02.semanticchecks;

import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.MethodCallNode;
import edu.mit.compilers.le02.stgenerator.SymbolTableException;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.ParamDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;

public class CheckMethodCalls extends ASTNodeVisitor<Boolean> {
    /** Holds the CheckMethodCalls singleton. */
    private static CheckMethodCalls instance;
    private static SymbolTable methodTable;

    /**
     * Retrieves the CheckMethodCalls singleton, creating if necessary.
     */
    public static CheckMethodCalls getInstance() {
        if (instance == null) {
            instance = new CheckMethodCalls();
        }
        return instance;
    }

    /**
     * Checks that every method call passes the correct number and type of arguments.
     */
    public static void check(ASTNode root) {
        assert(root instanceof ClassNode);
        root.accept(getInstance());
    }

    @Override
    public Boolean visit(ClassNode node) {
        methodTable = node.getDesc().getMethodSymbolTable();

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }

        return true;
    }

    @Override
    public Boolean visit(MethodCallNode node) {
        MethodDescriptor methodDesc = (MethodDescriptor)(methodTable.getMap().get(node.getName()));

        if (methodDesc.getParams().size() != node.getArgs().size()) {
            ErrorReporting.reportError(new SemanticException(node.getSourceLoc(),
                "Method " + node.getName() + " expects " + methodDesc.getParams().size()
                + " arguments, got " + node.getArgs().size()));
        } else {
            System.out.println(node.getName() + " " + node.getSourceLoc());
            for (int i = 0; i < methodDesc.getParams().size(); i++) {
                DecafType expected = ((ParamDescriptor)methodDesc.getParamSymbolTable().getMap().get(methodDesc.getParams().get(i))).getType();
                DecafType got = node.getArgs().get(i).getType();
                if (expected != got) {
                    ErrorReporting.reportError(new SemanticException(node.getArgs().get(i).getSourceLoc(),
                        "Argument " + i + " to method " + node.getName() + " should be of type " + expected
                        + ", but was " + got));
                }
            }
        }

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }

        return true;
    }
}
