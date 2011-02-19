package edu.mit.compilers.le02.ast;

public final class SourceLocation {
	private String filename;
	private int line;
	
	public String getFilename() {
		return filename;
	}

	public int getLine() {
		return line;
	}

	public int getCol() {
		return col;
	}

	private int col;
	
	public SourceLocation(String filename, int line, int col) {
		this.filename = filename;
		this.line = line;
		this.col = col;
	}

}
