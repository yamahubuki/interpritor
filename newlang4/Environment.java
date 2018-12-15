package newlang4;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import newlang3.*;

public class Environment {
	LexicalAnalyzer input;
	Map<String,Value> var_table;
	    
	public Environment(LexicalAnalyzer my_input) {
		input = my_input;
		var_table = new HashMap<String,Value>();
	}
		
	public LexicalAnalyzer getInput() {
		return input;
	}		

	public void setValue(String s,Value v){
		var_table.put(s,v);
	}

	public Value getValue(String s){
//		return value;
		return null;
	}
}

