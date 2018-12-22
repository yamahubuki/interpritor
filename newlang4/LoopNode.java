package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class LoopNode extends Node {

	Node cond=null;				//条件
	Node operation=null;		//trueの時の処理
	boolean isDoMust=false;		//条件に関わらず一度は必ず実行するか
	boolean isUntill=false;		//condの判定条件を逆にするか

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.WHILE,
		LexicalType.DO
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private LoopNode(Environment in){
		env=in;
		type=NodeType.LOOP_BLOCK;
	}

	public static Node getHandler(Environment envIn){
		return new LoopNode(envIn);
	}

	public void parse() throws Exception {
		if (env.getInput().peek(1).getType()==LexicalType.WHILE){
			//WHILEを読み飛ばす
			env.getInput().get();

			if (CondNode.isMatch(env.getInput().peek(1).getType())){
				cond=CondNode.getHandler(env);
				cond.parse();
			} else {
				throw new SyntaxException("WHILE文の構成が不正です。WHILEの直後が条件式ではありません。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().get().getType()!=LexicalType.NL){
				throw new SyntaxException("WHILE文の構成が不正です。条件式の直後には改行文字が必要です。"+env.getInput().getLine()+"行目");
			}

			if (StmtListNode.isMatch(env.getInput().peek(1).getType())){
				operation=StmtListNode.getHandler(env);
				operation.parse();
			} else {
				throw new SyntaxException("WHILE文の構成が不正です。処理内容を検出できません。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().get().getType()!=LexicalType.NL){
				throw new SyntaxException("WHILE文の構成が不正です。処理内容の後には改行文字が必要です。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().get().getType()!=LexicalType.WEND){
				throw new SyntaxException("WHILE文の構成が不正です。終端のWENDが見つかりません。"+env.getInput().getLine()+"行目");
			}
		} else if (env.getInput().peek(1).getType()==LexicalType.DO){
			//DOを読み飛ばす
			env.getInput().get();

			isDoMust=true;
			getDoBlockCond();

			if (env.getInput().get().getType()!=LexicalType.NL){
				throw new SyntaxException("DOブロックの構成が不正です。処理内容の前には改行文字が必要です。"+env.getInput().getLine()+"行目");
			}

			if (StmtListNode.isMatch(env.getInput().peek(1).getType())){
				operation=StmtListNode.getHandler(env);
				operation.parse();
			} else {
				throw new SyntaxException("DOブロックの構成が不正です。処理内容を検出できません。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().get().getType()!=LexicalType.NL){
				throw new SyntaxException("DOブロックの構成が不正です。処理内容の後には改行文字が必要です。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().get().getType()!=LexicalType.LOOP){
				throw new SyntaxException("DOブロックの構成が不正です。処理内容の後には改行文字が必要です。"+env.getInput().getLine()+"行目");
			}

			if (cond==null){
				if (!getDoBlockCond()){
					throw new SyntaxException("DOブロックに必要なWHILEまたはUNTILで始まる条件文がありません。");
				}
			}
		} else {
			throw new InternalError("LOOPBLOCKの開始字句ではありません。");
		}

		if (env.getInput().get().getType()!=LexicalType.NL){
			throw new SyntaxException("LOOPBLOCKの構成が不正です。終端の改行文字を検出できません。"+env.getInput().getLine()+"行目");
		}
	}


	private boolean getDoBlockCond() throws Exception{
		switch (env.getInput().peek(1).getType()){
			case UNTIL:
				isUntill=true;
			case WHILE:
				env.getInput().get();
				if (CondNode.isMatch(env.getInput().peek(1).getType())){
					cond=CondNode.getHandler(env);
					cond.parse();
				} else {
					throw new SyntaxException("DOブロックの構成が不正です。WHILEまたはUNTILの直後が条件式ではありません。"+env.getInput().getLine()+"行目");
				}
				break;
			default:
				return false;
		}
		return true;
	}

	public Value getValue() throws Exception{
		if (isDoMust){		//初回の強制実行
			operation.getValue();
		}

		while(true){
			if (!judge()){
				return null;
			}
			operation.getValue();
		}
	}

	private boolean judge() throws Exception {	//処理を継続するか判定
		if ((cond.getValue().getBValue()==true && isUntill==false) ||
		(cond.getValue().getBValue()==false && isUntill==true)){
			return true;
		} else {
			return false;
		}
	}

	public String toString(int indent) {
		String ret="";
		ret+="LOOPBlock：継続条件＝";
		if (isUntill){
			ret+="!(";
		}
		ret+=cond;
		if (isUntill){
			ret+=")";
		}
		ret+="　";
		if (isDoMust){
			ret+="初回強制実行";
		}
		ret+="[\n"+operation.toString(indent+1);
		for(int i=0;i<indent;i++){
			ret+="\t";
		}
		ret+="]";
		return ret;
	}
}
