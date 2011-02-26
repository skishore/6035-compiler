package edu.mit.compilers.le02.semanticchecks;

import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.DecafType;
import edu.mit.compilers.le02.stgenerator.SymbolTableException;
import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ASTNodeVisitor;

import edu.mit.compilers.le02.ast.ArrayLocationNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.BlockNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.MethodCallNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.ScalarLocationNode;

import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;

public class CheckDeclarations extends ASTNodeVisitor<Boolean> {
    /** Holds the CheckDeclarations singleton. */
    private static CheckDeclarations instance;
    private static SymbolTable varTable;
    private static SymbolTable methodTable;

    /**
     * Retrieves the CheckDeclarations singleton, creating if necessary.
     */
    public static CheckDeclarations getInstance() {
        if (instance == null) {
            instance = new CheckDeclarations();
        }
        return instance;
    }

    /**
     * Checks that every identifier is declared before it is used.
     */
    public static void checkDeclarations(ASTNode root) {
        assert(root instanceof ClassNode);
        root.accept(getInstance());
    }

    @Override
    public Boolean visit(ClassNode node) {
        varTable = node.getDesc().getFieldSymbolTable();
        methodTable = node.getDesc().getMethodSymbolTable();

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }
        return true;
    }

    @Override
    public Boolean visit(MethodDeclNode node) {
        SymbolTable parent = varTable;
        varTable = ((MethodDescriptor)methodTable.getMap().get(node.getName())).getParamSymbolTable();

        node.getBody().accept(this);

        varTable = parent;
        return true;
    }

    @Override
    public Boolean visit(BlockNode node) {
        SymbolTable parent = varTable;
        varTable = node.getLocalSymbolTable();

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }

        varTable = parent;
        return true;
    }

    @Override
    public Boolean visit(ArrayLocationNode node) {
        SymbolTable search = varTable;
        while ((!search.contains(node.getName())) && (search.getParent() != null)) {
            search = search.getParent();
        }
        if (search == null) {
            ErrorReporting.reportError(new SymbolTableException(node.getSourceLoc(), "Undeclared variable " + node.getName()));
        }

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }

        return true;
    }

    @Override
    public Boolean visit(ScalarLocationNode node) {
        SymbolTable search = varTable;
        while ((search != null) && (!search.contains(node.getName()))) {
            search = search.getParent();
        }
        if (search == null) {
            ErrorReporting.reportError(new SymbolTableException(node.getSourceLoc(), "Undeclared variable " + node.getName()));
        }

        return true;
    }

    @Override
    public Boolean visit(MethodCallNode node) {
        if (!methodTable.contains(node.getName())) {
            ErrorReporting.reportError(new SymbolTableException(node.getSourceLoc(), "Undeclared method " + node.getName()));
        }

        for (ASTNode child : node.getChildren()) {
            child.accept(this);
        }

        return true;
    }
}
