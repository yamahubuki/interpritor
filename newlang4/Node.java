package newlang4;

import java.io.IOException;
import newlang3.*;

public class Node {
	NodeType type;
	Environment env;

	/** Creates a new instance of Node */
	public Node() {
	}

	public NodeType getType() {
		return type;
	}

	//文法解析の実行。戻り値で成功or失敗を返す
	public void parse() throws Exception {
	}

	//プログラムの実行
	public Value getValue() throws Exception {
		return null;
	}

	public String toString() {
		if (type == NodeType.END) return "END";
		else return "Node";
	}

	public String toString(int indent) {
		if (type == NodeType.END) return "END";
		else return "Node";
	}

}
