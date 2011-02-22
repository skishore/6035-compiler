package edu.mit.compilers.le02.symboltable;

import java.util.Hashtable;

public class SymbolTable {
	private SymbolTable parent;
	private Hashtable<String,Descriptor> table;
	
	public SymbolTable(SymbolTable parent){
		this.parent = parent;
		this.table = new Hashtable<String,Descriptor>();
	}

	/**
	 * Add a new entry to the symbol table.  Verify that it does not already 
	 * exist in this table or any ancestor
	 * @param id The identifier of the new entry
	 * @param descriptor The descriptor of the new entry
	 * @return True if entry was successful
	 */
	public boolean put(String id, Descriptor descriptor){
		boolean unique = true;
		SymbolTable st = this;
		while(st!=null){
			if(st.contains(id)){
				unique = false;
			}
			st = st.getParent();
		}
		if(!unique){
			//TODO: Throw Exception Here
			System.out.println("Duplicate identifier");
		}else{
				if(descriptor!=null){
					this.table.put(id, descriptor);
				}
		}
		return unique;
		
	}
	
	public Descriptor get(String id){
		return this.table.get(id);
	}
	
	public boolean contains(String id){
		return this.table.containsKey(id);
	}
	
	public SymbolTable getParent(){
		return this.parent;
	}
	
	public String toString(){
		String output = "";
		for(String k: this.table.keySet()){
			output+=k+":["+table.get(k).toString()+"],";
		}
		if(output.length()>0){
			output = output.substring(0, output.length()-1);
		}
		return output;
	}
	
	
	

}
