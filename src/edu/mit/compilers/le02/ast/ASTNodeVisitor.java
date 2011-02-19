package edu.mit.compilers.le02.ast;

public abstract class ASTNodeVisitor {

	/*
	 * Helper method: visits the children of the node in order
	 */
	private void visitChildren(ASTNode node) {
		for (ASTNode child : node.getChildren()) {
			child.visit(this);
		}
	}
	
	/*
	 * Default behavior is to visit all nodes, but do nothing at each.
	 * This way you only need to override the ones you care about to make
	 * a new visitor.
	 */
	public void accept(ArrayDeclNode node) {visitChildren(node);}
	public void accept(ArrayLocationNode node) {visitChildren(node);}
	public void accept(AssignNode node) {visitChildren(node);}
	public void accept(BlockNode node) {visitChildren(node);}
	public void accept(BooleanNode node) {visitChildren(node);}
	public void accept(BoolOpNode node) {visitChildren(node);}
	public void accept(BreakNode node) {visitChildren(node);}
	public void accept(CallStatementNode node) {visitChildren(node);}
	public void accept(CharNode node) {visitChildren(node);}
	public void accept(ClassNode node) {visitChildren(node);}
	public void accept(ContinueNode node) {visitChildren(node);}
	public void accept(ForNode node) {visitChildren(node);}
	public void accept(IfNode node) {visitChildren(node);}
	public void accept(IntNode node) {visitChildren(node);}
	public void accept(MathOpNode node) {visitChildren(node);}
	public void accept(MethodCallNode node) {visitChildren(node);}
	public void accept(MethodDeclNode node) {visitChildren(node);}
	public void accept(MinusNode node) {visitChildren(node);}
	public void accept(NotNode node) {visitChildren(node);}
	public void accept(ReturnNode node) {visitChildren(node);}
	public void accept(ScalarLocationNode node) {visitChildren(node);}
	public void accept(StringNode node) {visitChildren(node);}
	public void accept(SyscallArgNode node) {visitChildren(node);}
	public void accept(SystemCallNode node) {visitChildren(node);}
	public void accept(VarDeclNode node) {visitChildren(node);}
	public void accept(VariableNode node) {visitChildren(node);}

}
