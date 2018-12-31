package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class Program extends Node {

	Node list=null;

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.IF,
		LexicalType.WHILE,
		LexicalType.DO,
		LexicalType.NAME,
		LexicalType.FOR,
		LexicalType.END,
		LexicalType.NL
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private Program(Environment in){
		env=in;
		type=NodeType.PROGRAM;
	}

	public static Node getHandler(Environment envIn){
		return new Program(envIn);
	}

	public void parse() throws Exception {
		if (StmtListNode.isMatch(env.getInput().peek(1).getType())){
			list=StmtListNode.getHandler(env);
			list.parse();
		}

		//ファイル終端(EOF直前)にNLがあれば読み飛ばす
		while (env.getInput().expect(LexicalType.NL)){
			env.getInput().get();
		}

		//次の字句がEOFでなければおかしい
		if (!env.getInput().expect(LexicalType.EOF)){
			throw new SyntaxException("不正な字句"+env.getInput().peek(1)+"があったため、構文解析を続行できませんでした。("+env.getInput().getLine()+"行目)");
		}
	}

	public Value getValue() throws Exception{
		if (list!=null){
			return list.getValue();
		}
		return null;
	}

	public String toString(int indent) {
		if (list!=null){
			return list.toString(indent);
		}
		return "";
	}
}
