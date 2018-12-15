package newlang4;

import java.io.IOException;
import newlang3.*;

public class Node {
	NodeType type;
	Environment env;

	public NodeType getType() {
		return type;
	}

	public void parse() throws Exception {
	}

	//ÉvÉçÉOÉâÉÄÇÃé¿çs
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
