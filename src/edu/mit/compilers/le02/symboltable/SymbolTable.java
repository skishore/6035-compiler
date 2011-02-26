package edu.mit.compilers.le02.symboltable;

import java.util.HashMap;
import java.util.Map;

import edu.mit.compilers.le02.ErrorReporting;
import edu.mit.compilers.le02.SourceLocation;
import edu.mit.compilers.le02.stgenerator.SymbolTableException;

public class SymbolTable {
    private SymbolTable parent;
    private Map<String, Descriptor> table;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.table = new HashMap<String, Descriptor>();
    }

    /**
     * Add a new entry to the symbol table. Verify that it does not already
     * exist in this table or any ancestor
     * 
     * @param id The identifier of the new entry
     * @param descriptor The descriptor of the new entry
     * @return True if entry was successful
     */
    public boolean put(String id, Descriptor descriptor, SourceLocation sl) {
        if (table.containsKey(id)) {
            ErrorReporting.reportError(
            new SymbolTableException(sl, "Duplicate identifier " + id));
            return false;
        } else {
            if (descriptor != null) {
                this.table.put(id, descriptor);
            }
            return true;
        }
    }

    /**
     * Finds a descriptor and recurses upwards until found or at top
     * @param id The id of the descriptor
     * @param primitive Whether the string is a primitive or a method, null for either
     * @return Returns the requested descriptor, or null if not found
     */
    public Descriptor get(String id, Boolean primitive) {
        Descriptor d;
        SymbolTable st = this;
        while (st != null) {
            if (st.getMap().containsKey(id)) {
                d = st.getMap().get(id);
                if (primitive == null){
                    return d;
                }else if (primitive && d instanceof TypedDescriptor ||
                        !primitive && d instanceof MethodDescriptor) {
                    return d;
                }

            }
            st = st.getParent();
        }
        return null;
    }

    /**
     * Checks if this symbol table or any ancestor contains the query id
     * 
     * @param id The id to be searched for
     * @return True if found, false otherwise
     */
    public boolean contains(String id) {
        Boolean found = false;
        SymbolTable st = this;
        while (st != null) {
            if (st.getMap().containsKey(id)) {
                found = true;
            }
            st = st.getParent();
        }
        return found;
    }

    public SymbolTable getParent() {
        return this.parent;
    }

    public Map<String, Descriptor> getMap() {
        return table;
    }

    @Override
    public String toString() {
        String output = "";
        for (String k : this.table.keySet()) {
            output += k + ":[" + table.get(k).toString() + "],";
        }
        if (output.length() > 0) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

}
