package newlang4;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import newlang3.*;

public class ExprNode extends Node {

	List result=new ArrayList();

	//自分のfirstをセットでもっておく
	private final static Set<LexicalType> FIRST=new HashSet<LexicalType>(Arrays.asList(
		LexicalType.NAME,
		LexicalType.SUB,
		LexicalType.LP,
		LexicalType.INTVAL,
		LexicalType.DOUBLEVAL,
		LexicalType.LITERAL
	));

	public static boolean isMatch(LexicalType type){
		return FIRST.contains(type);
	}

	private ExprNode(Environment in){
		env=in;
		type=NodeType.EXPR;
	}

	public static Node getHandrar(Environment in){
		return new ExprNode(in);
	}

	public void parse() throws Exception {
		List<LexicalUnit> stack = new ArrayList<>();

		add: while(true){
			switch(env.getInput().peek(1).getType()){
				case LP:
					env.getInput().get();
					Node h=ExprNode.getHandrar(env);
					h.parse();
					result.add(h);
					if (env.getInput().get().getType()!=LexicalType.RP){
						throw new SyntaxException("計算式の構成が不正です。)が見つかりません。"+env.getInput().getLine()+"行目");
					}
					break;
				case INTVAL:
				case DOUBLEVAL:
				case LITERAL:
					result.add(ConstNode.getHandrar(env,env.getInput().get().getValue()));
					break;
				case NAME:
					if (env.getInput().peek(2).getType()==LexicalType.LP){
						Node tmpNode=CallNode.getHandrar(env);
						tmpNode.parse();
						result.add(tmpNode);
					} else {
						result.add(VariableNode.getHandrar(env,env.getInput().get().getValue()));
					}
					break;
				case ADD:
					for(int i=stack.size()-1;i>=0;i--){
						boolean flg=false;
						if (stack.get(i).getType()==LexicalType.MUL ||
							stack.get(i).getType()==LexicalType.DIV ||
							stack.get(i).getType()==LexicalType.SUB
						){
							flg=true;
							result.add(stack.get(i));
							stack.remove(i);
						} else if (flg=true && stack.get(i).getType()==LexicalType.ADD){
							break;
						}
					}

				case SUB:
					for(int i=stack.size()-1;i>=0;i--){
						boolean flg=false;
						if (stack.get(i).getType()==LexicalType.MUL ||
						stack.get(i).getType()==LexicalType.DIV){
							flg=true;
							result.add(stack.get(i));
							stack.remove(i);
						} else if (flg=true &&
						(stack.get(i).getType()==LexicalType.ADD ||
						stack.get(i).getType()==LexicalType.SUB)){
							break;
						}
					}
					stack.add(env.getInput().get());
					break;
				case MUL:
					for(int i=stack.size()-1;i>=0;i--){
						boolean flg=false;
						if (stack.get(i).getType()==LexicalType.DIV){
							flg=true;
							result.add(stack.get(i));
							stack.remove(i);
						} else if (flg=true &&
							(stack.get(i).getType()==LexicalType.ADD ||
							stack.get(i).getType()==LexicalType.SUB  ||
							stack.get(i).getType()==LexicalType.MUL)
						){
							break;
						}
					}
					stack.add(env.getInput().get());
					break;
				case DIV:
					stack.add(env.getInput().get());
					break;
				default:
					break add;
			}
		}

		for(int i=stack.size()-1;i>=0;i--){
			result.add(stack.get(i));
		}
	}

	public String toString() {
		String tmp=" 式[";
		for(int i=0;i<result.size();i++){
			tmp+=result.get(i)+" ";
		}
		return tmp+"] ";
	}
}
