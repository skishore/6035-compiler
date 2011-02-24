package edu.mit.compilers.le02.stgenerator;

import edu.mit.compilers.le02.ast.ASTNode;
import edu.mit.compilers.le02.ast.ClassNode;
import edu.mit.compilers.le02.ast.DeclNode;
import edu.mit.compilers.le02.ast.FieldDeclNode;
import edu.mit.compilers.le02.ast.MethodDeclNode;
import edu.mit.compilers.le02.ast.SourceLocation;
import edu.mit.compilers.le02.ast.VarDeclNode;
import edu.mit.compilers.le02.symboltable.ClassDescriptor;
import edu.mit.compilers.le02.symboltable.Descriptor;
import edu.mit.compilers.le02.symboltable.MethodDescriptor;
import edu.mit.compilers.le02.symboltable.SymbolTable;
import edu.mit.compilers.le02.symboltable.TypeDescriptor;

public class SymbolTableGenerator {
	/** Holds the SymbolTableGenerator singleton. */
	private static SymbolTableGenerator instance;

	/**
	 * Retrieves the SymbolTableGenerator singleton, creating if necessary.
	 */
	public static SymbolTableGenerator getInstance() {
		if (instance == null) {
			instance = new SymbolTableGenerator();
		}
		return instance;
	}

	/**
	 * Generates an symbol table based on an input IR.
	 */
	public static SymbolTable generateSymbolTable(ASTNode root)
			throws SymbolTableException {
		assert(root instanceof ClassNode);
		return getInstance().createClassST((ClassNode)root);
	}

	/**
	 * Converts a single node into a descriptor.
	 * 
	 * @param node The AST node.
	 * @return descriptor The descriptor.
	 * @throws SymbolTableException if any errors were encountered.
	 */
	public Descriptor expand(SymbolTable parent, ASTNode node)
			throws SymbolTableException {
		Descriptor descriptor = null;
		if (node == null) {
			throw new SymbolTableException(new SourceLocation("<unknown>", -1,
					-1),
					"Attempted to visit null node. Check if if tree is malformed.");

		} else if (node instanceof ClassNode) {
			// Create a descriptor for a classNode
			
			//Create and fill fieldSymbolTable
			SymbolTable fieldSymbolTable = new SymbolTable(parent);
			for (FieldDeclNode n : ((ClassNode) node).getFields()) {
				fieldSymbolTable.put(n.getName(), 
						this.expand(fieldSymbolTable,n));
			}

			//Create and fill methodSymbolTable
			SymbolTable methodSymbolTable = new SymbolTable(fieldSymbolTable);
			for (MethodDeclNode m : ((ClassNode) node).getMethods()) {
				methodSymbolTable.put(m.getName(), 
						this.expand(methodSymbolTable, m));
			}
			
			descriptor = new ClassDescriptor(parent, 
					((ClassNode) node).getName(), fieldSymbolTable, methodSymbolTable);
		} else if (node instanceof MethodDeclNode) {
			// Create a descriptor for a MethodDeclNode
			
			//Create and fill paramSymbolTable
			SymbolTable paramSymbolTable = new SymbolTable(parent);
			for (VarDeclNode v : ((MethodDeclNode) node).getParams()) {
				paramSymbolTable.put(v.getName(), 
						this.expand(paramSymbolTable, v));
			}

			//Create and fill localSymbolTable
			SymbolTable localSymbolTable = new SymbolTable(paramSymbolTable);
			for (VarDeclNode v : ((MethodDeclNode) node).getBody().getDecls()) {
				localSymbolTable.put(v.getName(), 
						this.expand(localSymbolTable, v));
			}
			
			descriptor = new MethodDescriptor(parent,
					((MethodDeclNode) node).getName(), localSymbolTable, 
					paramSymbolTable, (MethodDeclNode) node);

		} else if (node instanceof DeclNode) {
			// Create a descriptor for a DeclNode
			descriptor = new TypeDescriptor(parent,
					((DeclNode) node).getName(), ((DeclNode) node).getType());
		}

		return descriptor;
	}

	/**
	 * Converts a ClassNode into a SymbolTable.  The root of all ASTNode trees must be a ClassNode, otherwise we should throw an exception
	 * 
	 * @param root The root of our AST tree
	 * @return SymbolTable The expanded SymbolTable
	 */
	public SymbolTable createClassST(ClassNode root) throws SymbolTableException{
		SymbolTable st = new SymbolTable(null);
		st.put(root.getName(), this.expand(st, root));
		return st;
	}
}
