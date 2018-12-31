package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import newlang3.*;

public class IfNode extends Node {

	Node cond=null;				//条件
	Node operation=null;		//trueの時の処理
	Node elseOperation=null;	//elseの時の処理

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<>(Arrays.asList(
		LexicalType.IF
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private IfNode(Environment in){
		env=in;
		type=NodeType.IF_BLOCK;
	}

	public static Node getHandler(Environment in){
		return new IfNode(in);
	}

	public void parse() throws Exception {
		boolean isELSEIF=false;			//ELSEIFの時はENDIFがいらない

		//IFまたはELSEIF
		if (env.getInput().expect(LexicalType.ELSEIF)){
			isELSEIF=true;
			env.getInput().get();
		} else if (env.getInput().expect(LexicalType.IF)){
			env.getInput().get();
		} else {
			throw new InternalError("IFまたはIFELSE以外の箇所でIFNodeのgetHandlerがコールされました。");
		}

		//Cond
		if (CondNode.isMatch(env.getInput().peek(1).getType())){
			cond=CondNode.getHandler(env);
			cond.parse();
		} else {
			throw new SyntaxException("IF文の構成が不正です。条件文を検出できません。"+env.getInput().getLine()+"行目");
		}

		//THENの確認
		if (env.getInput().expect(LexicalType.THEN)){
				env.getInput().get();
		} else {
			throw new SyntaxException("IF文の構成が不正です。THENがありません。"+env.getInput().getLine()+"行目");
		}

		//パターン１　NLの場合＝ブロック
		//パターン２　stmt+NL
		//パターン３　stmt+ELSE+stmt+NL
		if (StmtNode.isMatch(env.getInput().peek(1).getType())){
			operation=StmtNode.getHandler(env);
			operation.parse();

			if (env.getInput().expect(LexicalType.ELSE)){
				env.getInput().get();

				if (StmtNode.isMatch(env.getInput().peek(1).getType())){
					elseOperation=StmtNode.getHandler(env);
					elseOperation.parse();
				} else {
					throw new SyntaxException("ELSE文の構成が不正です。"+env.getInput().getLine()+"行目");
				}
			}
		} else if (env.getInput().expect(LexicalType.NL)){
			//NL
			env.getInput().get();

			//StmtList
			if (StmtListNode.isMatch(env.getInput().peek(1).getType())){
				operation=StmtListNode.getHandler(env);
				operation.parse();
			} else {
				throw new SyntaxException("IF文の構成が不正です。処理内容を検出できません。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().expect(LexicalType.NL)){
				env.getInput().get();
			} else {
				throw new SyntaxException("IF文の構成が不正です。trueの場合の処理の終端NLを検出できません。"+env.getInput().getLine()+"行目");
			}

			if (env.getInput().expect(LexicalType.ELSEIF)){
				elseOperation=IfNode.getHandler(env);
				elseOperation.parse();
			} else if (env.getInput().expect(LexicalType.ELSE)){
				//ELSE
				env.getInput().get();

				if (env.getInput().expect(LexicalType.NL)){
					env.getInput().get();

					if (StmtListNode.isMatch(env.getInput().peek(1).getType())){
						elseOperation=StmtListNode.getHandler(env);
						elseOperation.parse();
					} else {
						throw new SyntaxException("ELSE文の構成が不正です。"+env.getInput().getLine()+"行目");
					}

					if (env.getInput().expect(LexicalType.NL)){
						env.getInput().get();
					} else {
						throw new SyntaxException("IF文の構成が不正です。elseの場合の処理の終端NLを検出できません。"+env.getInput().getLine()+"行目");
					}
				} else {
					throw new SyntaxException("ELSE文の構成が不正です。キーワードELSEの直後には改行文字が必要です。"+env.getInput().getLine()+"行目");
				}
			}

			if (!isELSEIF){
				if (env.getInput().expect(LexicalType.ENDIF)){
					env.getInput().get();
				} else {
					throw new SyntaxException("IF文の構成が不正です。ENDIFがありません。"+env.getInput().getLine()+"行目");
				}
			}
		} else {
			throw new SyntaxException("IF文の構成が不正です。"+env.getInput().getLine()+"行目");
		}

		if (!isELSEIF){
			if (!env.getInput().expect(LexicalType.NL)){
				throw new SyntaxException("IF文の構成が不正です。終端のNLを検出できません。"+env.getInput().getLine()+"行目");
			}
		}
	}

	public Value getValue() throws Exception{
		if (cond.getValue().getBValue()==true){
			operation.getValue();
		} else if (elseOperation!=null){
			elseOperation.getValue();
		}
		return null;
	}

	public String toString(int indent) {
		String ret="";
		ret+="IF：判定条件＝"+cond+"　trueの場合：[\n";
		if (operation.getType()!=NodeType.STMT_LIST){
			for(int i=0;i<indent+1;i++){
				ret+="\t";
			}
		}
		ret+=operation.toString(indent+1);
		if (operation.getType()!=NodeType.STMT_LIST){
			ret+="\n";
		}
		for(int i=0;i<indent;i++){
			ret+="\t";
		}
		ret+="] else：[\n";
		if (elseOperation!=null){
			if (elseOperation.getType()!=NodeType.STMT_LIST){
				for(int i=0;i<indent+1;i++){
					ret+="\t";
				}
			}
			ret+=elseOperation.toString(indent+1);
			if (elseOperation.getType()!=NodeType.STMT_LIST){
				ret+="\n";
			}
		} else {
			for(int i=0;i<indent+1;i++){
				ret+="\t";
			}
			ret+="処理なし\n";
		}
		for(int i=0;i<indent;i++){
			ret+="\t";
		}
		ret+="]";
		return ret;
	}
}
