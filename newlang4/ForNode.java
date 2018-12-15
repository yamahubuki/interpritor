package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class ForNode extends Node {

	Node init=null;			//初期化
	Node max=null;			//継続条件の上限値
	Node operation=null;	//処理内容
	Node step=null;		//更新対象

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.FOR
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private ForNode(Environment in){
		env=in;
		type=NodeType.FOR_STMT;
	}

	public static Node getHandrar(Environment envIn){
		return new ForNode(envIn);
	}

	public void parse() throws Exception {
		//forとわかっているのでスルー
		env.getInput().get();

		//substのはず
		if (SubstNode.isMatch(env.getInput().peek(1).getType())){
			init=SubstNode.getHandrar(env);
			init.parse();
		} else {
			throw new SyntaxException("FOR文の構成が不正です。初期化条件の指定が誤っています。"+env.getInput().getLine()+"行目");
		}

		//TOの確認
		if (env.getInput().get().getType()!=LexicalType.TO){
			throw new SyntaxException("for文の構成が不正です。TOがありません。"+env.getInput().getLine()+"行目");
		}

		//継続上限となるINTVAL
		if (env.getInput().peek(1).getType()==LexicalType.INTVAL){
			max=ConstNode.getHandrar(env,env.getInput().get().getValue());
		} else {
			throw new SyntaxException("for文の構成が不正です。継続条件の上限値がありません。"+env.getInput().getLine()+"行目");
		}

		//NL確認
		if (env.getInput().get().getType()!=LexicalType.NL){
			throw new SyntaxException("for文の構成が不正です。継続条件の後のNLがありません。"+env.getInput().getLine()+"行目");
		}

		//処理内容
		if (StmtListNode.isMatch(env.getInput().peek(1).getType())){
			operation=StmtListNode.getHandrar(env);
			operation.parse();
		} else {
			throw new SyntaxException("for文の構成が不正です。処理内容の記述を検出できません。"+env.getInput().getLine()+"行目");
		}

		if (env.getInput().get().getType()!=LexicalType.NL){
			throw new SyntaxException("for文の構成が不正です。処理内容の後のNLがありません。"+env.getInput().getLine()+"行目");
		}

		//NEXTの確認
		if (env.getInput().get().getType()!=LexicalType.NEXT){
			throw new SyntaxException("for文の構成が不正です。NEXTがありません。"+env.getInput().getLine()+"行目");
		}

		//更新内容
		if (env.getInput().peek(1).getType()==LexicalType.NAME){
			step=VariableNode.getHandrar(env,env.getInput().get().getValue());
		} else {
			throw new SyntaxException("for文の構成が不正です。更新対象がありません。"+env.getInput().getLine()+"行目");
		}
	}


	public String toString(int indent) {
		String ret="";
		ret+="FOR：初期化＝"+init+" 継続上限＝"+max+"　処理内容：[\n";
		ret+=operation.toString(indent+1)+"\n";
		for(int i=0;i<indent;i++){
			ret+="\t";
		}
		ret+="]　更新対象："+step;
		return ret;
	}
}
