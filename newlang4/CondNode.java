package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class CondNode extends Node {

	Node left=null;				//左側
	LexicalType operator=null;	//演算子
	Node right=null;			//右側
	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.NAME,
		LexicalType.SUB,
		LexicalType.LP,
		LexicalType.INTVAL,
		LexicalType.DOUBLEVAL,
		LexicalType.LITERAL
	));

	private final static Set<LexicalType> OPERATOR=new HashSet<>(Arrays.asList(
		LexicalType.EQ,
		LexicalType.LT,
		LexicalType.LE,
		LexicalType.GT,
		LexicalType.GE,
		LexicalType.NE
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private CondNode(Environment in){
		env=in;
		type=NodeType.COND;
	}

	public static Node getHandrar(Environment envIn){
		return new CondNode(envIn);
	}

	public void parse() throws Exception {
		if (ExprNode.isMatch(env.getInput().peek(1).getType())){
			left=ExprNode.getHandrar(env);
			left.parse();
		} else {
			throw new SyntaxException("条件文の開始が不正です。"+env.getInput().getLine()+"行目");
		}

		if (OPERATOR.contains(env.getInput().peek(1).getType())){
			operator=env.getInput().get().getType();
		} else {
			throw new SyntaxException("条件文中に不正な文字がありました。"+env.getInput().getLine()+"行目");
		}

		if (ExprNode.isMatch(env.getInput().peek(1).getType())){
			right=ExprNode.getHandrar(env);
			right.parse();
		} else {
			throw new SyntaxException("条件文中に不正な記号がありました。"+env.getInput().getLine()+"行目");
		}
	}

	public String toString() {
		return "COND："+left+" "+operator+" "+right;
	}
}



