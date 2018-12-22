package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class VariableNode extends Node {
	String var_name;
	Value v=null;

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.NAME
	));

	public VariableNode(String name) {
		var_name = name;
	}

	public VariableNode(String name,Value value) {
		var_name = name;
		v=value;
	}

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	public void parse(){
	}

	public void setValue(Value my_v) {
		v = my_v;
	}

	public Value getValue() {
		return v;
	}

	public String toString() {
		return "変数："+var_name;
	}
}
