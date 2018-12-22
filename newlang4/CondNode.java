package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;
import newlang5.*;

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

	public static Node getHandler(Environment envIn){
		return new CondNode(envIn);
	}

	public void parse() throws Exception {
		if (ExprNode.isMatch(env.getInput().peek(1).getType())){
			left=ExprNode.getHandler(env);
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
			right=ExprNode.getHandler(env);
			right.parse();
		} else {
			throw new SyntaxException("条件文中に不正な記号がありました。"+env.getInput().getLine()+"行目");
		}
	}

	public Value getValue() throws Exception{
		Value val1=left.getValue();
		Value val2=right.getValue();
		if (val1==null || val2==null){
			throw new CalcurateException("nullに対して演算を試みました。");
		}
		if (val1.getType()==ValueType.STRING || val2.getType()==ValueType.STRING){
			if (operator==LexicalType.EQ){
				return new ValueImpl(val1.getSValue().equals(val2.getSValue()));
			} else if(operator==LexicalType.NE){
				return new ValueImpl(val1.getSValue()!=val2.getSValue());
			} else {
				throw new CalcurateException("文字列に対して無効な演算子が指定されています。");
			}
		}

		if (operator==LexicalType.LT){
			return new ValueImpl(val1.getDValue()<val2.getDValue());
		} else if (operator==LexicalType.LE){
			return new ValueImpl(val1.getDValue()<=val2.getDValue());
		} else if (operator==LexicalType.GT){
			return new ValueImpl(val1.getDValue()>val2.getDValue());
		} else if (operator==LexicalType.GE){
			return new ValueImpl(val1.getDValue()>=val2.getDValue());
		} else if (operator==LexicalType.EQ){
			return new ValueImpl(val1.getDValue()==val2.getDValue());
		} else if (operator==LexicalType.NE){
			return new ValueImpl(val1.getDValue()!=val2.getDValue());
		} else {
			throw new InternalError("不正な演算子で条件判断を試みました。");
		}
	}

	public String toString() {
		return "COND："+left+" "+operator+" "+right;
	}
}



