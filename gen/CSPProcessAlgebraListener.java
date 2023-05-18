// Generated from C:/SALEH/m2 il/PFE_2023_MONITORING_TOOL/src\CSPProcessAlgebra.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CSPProcessAlgebraParser}.
 */
public interface CSPProcessAlgebraListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CSPProcessAlgebraParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(CSPProcessAlgebraParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSPProcessAlgebraParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(CSPProcessAlgebraParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSPProcessAlgebraParser#process}.
	 * @param ctx the parse tree
	 */
	void enterProcess(CSPProcessAlgebraParser.ProcessContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSPProcessAlgebraParser#process}.
	 * @param ctx the parse tree
	 */
	void exitProcess(CSPProcessAlgebraParser.ProcessContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSPProcessAlgebraParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(CSPProcessAlgebraParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSPProcessAlgebraParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(CSPProcessAlgebraParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSPProcessAlgebraParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CSPProcessAlgebraParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSPProcessAlgebraParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CSPProcessAlgebraParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CSPProcessAlgebraParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(CSPProcessAlgebraParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link CSPProcessAlgebraParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(CSPProcessAlgebraParser.EventContext ctx);
}