package newlang4;

public enum NodeType {
	PROGRAM,
	STMT_LIST,
	STMT,
	FOR_STMT,
	ASSIGN_STMT,		//ë„ì¸ï∂(subst)
	BLOCK,
	IF_BLOCK,
	LOOP_BLOCK,
	COND,				//ifÇ‚whileÇ≈égÇ§èåèîªíË
    EXPR_LIST,
	EXPR,				//éÆ
	FUNCTION_CALL,
    STRING_CONSTANT,
    INT_CONSTANT,
    DOUBLE_CONSTANT,
    BOOL_CONSTANT,
	VARIABLE,
    END,
}
