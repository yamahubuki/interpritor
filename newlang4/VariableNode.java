package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class VariableNode extends Node {

	String name=null;

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.NAME
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private VariableNode(Environment envIn,Value v){
		env=envIn;
		type=NodeType.VARIABLE;
		name=v.getSValue();
	}

	private VariableNode(Environment envIn){
		env=envIn;
		type=NodeType.VARIABLE;
	}

	public static Node getHandrar(Environment envin,Value v){
		return new VariableNode(envin,v);
	}

	public static Node getHandrar(Environment envin){
		return new VariableNode(envin);
	}

	public void parse() throws Exception {
		if (env.getInput().peek(1).getType()==LexicalType.NAME){
			name=env.getInput().get().getValue().getSValue();
		} else {
			throw new InternalError("変数名として不適切な文字列です。"+env.getInput().getLine()+"行目");
		}
	}

	public String toString() {
		return "変数："+name;
	}
}

