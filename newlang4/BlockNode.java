package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class BlockNode extends Node {

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.DO,
		LexicalType.WHILE,
		LexicalType.IF
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private BlockNode(Environment in){
		env=in;
		type=NodeType.BLOCK;
	}

	public static Node getHandler(Environment envIn) throws Exception{
		if (IfNode.isMatch(envIn.getInput().peek(1).getType())){
			return IfNode.getHandler(envIn);
		}else if (LoopNode.isMatch(envIn.getInput().peek(1).getType())){
			return LoopNode.getHandler(envIn);
		} else {
			throw new InternalError("BlockNodeを生成できない字句です。"+envIn.getInput().getLine()+"行目");
		}
	}

	public void parse() throws Exception {
		throw new InternalError("BlockNodeクラスのparseは実行できません。");
	}

	public String toString() {
		return "Block:";
	}
}

