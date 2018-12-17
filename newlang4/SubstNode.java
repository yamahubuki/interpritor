package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class SubstNode extends Node {

	String leftVar=null;
	Node expr=null;

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.NAME
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private SubstNode(Environment in){
		env=in;
		type=NodeType.ASSIGN_STMT;
	}

	public static Node getHandrar(Environment envIn){
		return new SubstNode(envIn);
	}

	public void parse() throws Exception {
		//Nameが来ることを事前確認済み
		leftVar=env.getInput().get().getValue().getSValue();

		//「＝」を取得するが、ここが＝である事はStmtのgethandrer()で確認済み
		LexicalUnit lu=env.getInput().get();

		if (ExprNode.isMatch(env.getInput().peek(1).getType())){
			expr=ExprNode.getHandrar(env);
			expr.parse();
		} else {
			throw new SyntaxException("代入文の後半が式として評価できません。("+env.getInput().getLine()+"行目)");
		}
	}

	public Value getValue() throws Exception{
		env.getVariable(leftVar).setValue(expr.getValue());
		return null;
	}

	public String toString(int indent) {
		return "SUBST:"+expr.toString()+"→"+leftVar.toString()+"";
	}
}
