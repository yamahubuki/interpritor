package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import newlang3.*;

public class StmtListNode extends Node {

	List<Node> list=new ArrayList<>();

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

	private StmtListNode(Environment in){
		env=in;
		type=NodeType.STMT_LIST;
	}

	public static Node getHandrar(Environment envIn){
		return new StmtListNode(envIn);
	}

	public void parse() throws Exception {
		while(true){
			//リスト終端以外のNLを読み飛ばす
			while(env.getInput().peek(1).getType()==LexicalType.NL &&
				StmtListNode.isMatch(env.getInput().peek(2).getType())
			){
				env.getInput().get();
			}

			Node handrar=null;
			if (StmtNode.isMatch(env.getInput().peek(1).getType())){
				handrar=StmtNode.getHandrar(env);
			} else if (BlockNode.isMatch(env.getInput().peek(1).getType())){
				handrar=BlockNode.getHandrar(env);
			} else {
				break;
			}
			handrar.parse();
			list.add(handrar);
		}
	}

	public String toString(int indent) {
		String ret="";
		for(int i=0;i<indent;i++){
			ret+="\t";
		}
		ret+="stmtList("+list.size()+"):\n";
		for(int i=0;i<list.size();i++){
			for(int j=0;j<indent+1;j++){
				ret+="\t";
			}
			ret+=list.get(i).toString(indent+1)+"\n";
		}
		return ret;
	}
}
