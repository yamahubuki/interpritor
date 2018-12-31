package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class StmtNode extends Node {

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<>(Arrays.asList(
		LexicalType.NAME,
		LexicalType.FOR,
		LexicalType.END
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private StmtNode(Environment in){
		throw new InternalError("StmtNodeクラスのインスタンスは生成できません。("+in.getInput().getLine()+"行目)");
	}

	public static Node getHandler(Environment envIn) throws Exception{
		switch (envIn.getInput().peek(1).getType()){
			case NAME:
				if (envIn.getInput().peek(2).getType()==LexicalType.EQ){
					return SubstNode.getHandler(envIn);
				} else if (ExprListNode.isMatch(envIn.getInput().peek(2).getType())){
					return CallNode.getHandler(envIn);
				} else {
					throw new SyntaxException("正しい文ではありません。("+envIn.getInput().getLine()+"行目)");
				}
			case FOR:
				return ForNode.getHandler(envIn);
			case END:
				return EndNode.getHandler(envIn);
			default:
				throw new InternalError("StmtNodeに適合しない型でgetHandlerがコールされました。"+envIn.getInput().peek(1).getType());
		}
	}

	public void parse() throws Exception {
		throw new InternalError("StmtNodeクラスのparseは実行できません。");
	}

	public String toString() {
		return "Stmt";
	}
}

