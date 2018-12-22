package newlang4;

import java.util.Hashtable;
import newlang3.*;
import newlang5.*;

public class Environment {
	LexicalAnalyzer input;
	Hashtable<String,Function> library;
	Hashtable<String,VariableNode> var_table;

	public Environment(LexicalAnalyzer my_input) {
		input = my_input;
		library = new Hashtable<>();
		library.put("PRINT", new PrintFunction());
		var_table = new Hashtable<>();
	}

	public LexicalAnalyzer getInput() {
		return input;
	}		

	public Function getFunction(String fname) {
		return (Function) library.get(fname);
	}

	public VariableNode getVariable(String vname) {
		VariableNode v;
		v =var_table.get(vname);
		if (v == null) {
			v = new VariableNode(vname);
			var_table.put(vname, v);
		}
		return v;
	}
}

