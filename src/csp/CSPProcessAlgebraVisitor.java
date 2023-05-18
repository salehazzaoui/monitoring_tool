package csp;// Generated from C:/SALEH/m2 il/PFE_2023_MONITORING_TOOL/src\CSPProcessAlgebra.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CSPProcessAlgebraParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CSPProcessAlgebraVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CSPProcessAlgebraParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(CSPProcessAlgebraParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSPProcessAlgebraParser#process}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcess(CSPProcessAlgebraParser.ProcessContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSPProcessAlgebraParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(CSPProcessAlgebraParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSPProcessAlgebraParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CSPProcessAlgebraParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CSPProcessAlgebraParser#event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent(CSPProcessAlgebraParser.EventContext ctx);
}