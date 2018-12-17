package newlang4;

import java.util.Hashtable;
import newlang3.*;
import newlang5.*;

public class Environment {
	LexicalAnalyzer input;
	Hashtable library;
	Hashtable var_table;

	public Environment(LexicalAnalyzer my_input) {
		input = my_input;
		library = new Hashtable();
		library.put("PRINT", new PrintFunction());
		var_table = new Hashtable();
	}

	public LexicalAnalyzer getInput() {
		return input;
	}		

	public Function getFunction(String fname) {
		return (Function) library.get(fname);
	}

	public Variable getVariable(String vname) {
		Variable v;
		v = (Variable) var_table.get(vname);
		if (v == null) {
			v = new Variable(vname);
			var_table.put(vname, v);
		}
		return v;
	}
}

