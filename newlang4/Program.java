package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import newlang3.*;

public class Program extends Node {

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.IF,
		LexicalType.WHILE,
		LexicalType.DO,
		LexicalType.NAME,
		LexicalType.FOR,
		LexicalType.END
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private Program(Environment in){
		env=in;
		type=NodeType.PROGRAM;
	}

	public static Node getHandler(Environment envIn){
		return StmtListNode.getHandler(envIn);
	}

	public void parse() throws Exception {
		throw new InternalError("ProgramNodeのparseは実行できません。");
	}

	public String toString() {
		return "start Program\n";
	}
}

