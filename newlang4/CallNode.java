package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class CallNode extends Node {

	String funcName=null;			//関数名
	ExprListNode arguments=null;	//引数

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<>(Arrays.asList(
		LexicalType.NAME
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private CallNode(Environment envIn){
		env=envIn;
		type=NodeType.FUNCTION_CALL;
	}

	public static Node getHandler(Environment in){
		return new CallNode(in);
	}

	public void parse() throws Exception {
		boolean isBracket=false;		//括弧があったか否か

		//呼出関数名
		if (env.getInput().expect(LexicalType.NAME)){
			funcName=env.getInput().get().getValue().getSValue();
		} else {
			throw new SyntaxException("有効な関数名ではありません。("+env.getInput().getLine()+"行目");
		}

		//LPの確認
		if (env.getInput().peek(1).getType()==LexicalType.LP){
			env.getInput().get();
			isBracket=true;
		}

		//引数リスト
		if (ExprListNode.isMatch(env.getInput().peek(1).getType())){
			arguments=ExprListNode.getHandler(env);
			arguments.parse();
		}

		if (isBracket){
			//閉じ括弧
			if (env.getInput().expect(LexicalType.RP)){
				env.getInput().get();
			} else {
				throw new SyntaxException("関数呼出の括弧が閉じられていません。("+env.getInput().getLine()+"行目");
			}
		}
	}

	public Value getValue() throws Exception{
		return env.getFunction(funcName).invoke(arguments);
	}

	public String toString(int indent) {
		return "関数："+funcName+" 引数リスト＝["+arguments+"]";
	}
}
