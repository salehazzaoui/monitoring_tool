package csp;// Generated from C:/SALEH/m2 il/PFE_2023_MONITORING_TOOL/src\CSPProcessAlgebra.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CSPProcessAlgebraParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		ID=10, WS=11;
	public static final int
		RULE_program = 0, RULE_process = 1, RULE_statements = 2, RULE_statement = 3, 
		RULE_event = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "process", "statements", "statement", "event"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'[]'", "'||'", "';'", "'='", "'->'", "'skip'", "'stop'", "'!'", 
			"'?'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "ID", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CSPProcessAlgebra.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CSPProcessAlgebraParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public List<ProcessContext> process() {
			return getRuleContexts(ProcessContext.class);
		}
		public ProcessContext process(int i) {
			return getRuleContext(ProcessContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CSPProcessAlgebraVisitor ) return ((CSPProcessAlgebraVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			setState(23);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(10);
				process();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(11);
				process();
				setState(12);
				match(T__0);
				setState(13);
				process();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(15);
				process();
				setState(16);
				match(T__1);
				setState(17);
				process();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(19);
				process();
				setState(20);
				match(T__2);
				setState(21);
				process();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProcessContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(CSPProcessAlgebraParser.ID, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public ProcessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_process; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).enterProcess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).exitProcess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CSPProcessAlgebraVisitor ) return ((CSPProcessAlgebraVisitor<? extends T>)visitor).visitProcess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProcessContext process() throws RecognitionException {
		ProcessContext _localctx = new ProcessContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_process);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			match(ID);
			setState(26);
			match(T__3);
			setState(27);
			statements();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementsContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).exitStatements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CSPProcessAlgebraVisitor ) return ((CSPProcessAlgebraVisitor<? extends T>)visitor).visitStatements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statements);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			statement();
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(30);
				match(T__4);
				setState(31);
				statement();
				}
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(CSPProcessAlgebraParser.ID, 0); }
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CSPProcessAlgebraVisitor ) return ((CSPProcessAlgebraVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_statement);
		try {
			setState(41);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(37);
				match(ID);
				setState(38);
				event();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(39);
				match(T__5);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(40);
				match(T__6);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(CSPProcessAlgebraParser.ID, 0); }
		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CSPProcessAlgebraListener ) ((CSPProcessAlgebraListener)listener).exitEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CSPProcessAlgebraVisitor ) return ((CSPProcessAlgebraVisitor<? extends T>)visitor).visitEvent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_event);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_la = _input.LA(1);
			if ( !(_la==T__7 || _la==T__8) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(44);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u000b/\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0003\u0000\u0018\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002!\b\u0002\n\u0002"+
		"\f\u0002$\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003"+
		"\u0003*\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0000"+
		"\u0000\u0005\u0000\u0002\u0004\u0006\b\u0000\u0001\u0001\u0000\b\t/\u0000"+
		"\u0017\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004"+
		"\u001d\u0001\u0000\u0000\u0000\u0006)\u0001\u0000\u0000\u0000\b+\u0001"+
		"\u0000\u0000\u0000\n\u0018\u0003\u0002\u0001\u0000\u000b\f\u0003\u0002"+
		"\u0001\u0000\f\r\u0005\u0001\u0000\u0000\r\u000e\u0003\u0002\u0001\u0000"+
		"\u000e\u0018\u0001\u0000\u0000\u0000\u000f\u0010\u0003\u0002\u0001\u0000"+
		"\u0010\u0011\u0005\u0002\u0000\u0000\u0011\u0012\u0003\u0002\u0001\u0000"+
		"\u0012\u0018\u0001\u0000\u0000\u0000\u0013\u0014\u0003\u0002\u0001\u0000"+
		"\u0014\u0015\u0005\u0003\u0000\u0000\u0015\u0016\u0003\u0002\u0001\u0000"+
		"\u0016\u0018\u0001\u0000\u0000\u0000\u0017\n\u0001\u0000\u0000\u0000\u0017"+
		"\u000b\u0001\u0000\u0000\u0000\u0017\u000f\u0001\u0000\u0000\u0000\u0017"+
		"\u0013\u0001\u0000\u0000\u0000\u0018\u0001\u0001\u0000\u0000\u0000\u0019"+
		"\u001a\u0005\n\u0000\u0000\u001a\u001b\u0005\u0004\u0000\u0000\u001b\u001c"+
		"\u0003\u0004\u0002\u0000\u001c\u0003\u0001\u0000\u0000\u0000\u001d\"\u0003"+
		"\u0006\u0003\u0000\u001e\u001f\u0005\u0005\u0000\u0000\u001f!\u0003\u0006"+
		"\u0003\u0000 \u001e\u0001\u0000\u0000\u0000!$\u0001\u0000\u0000\u0000"+
		"\" \u0001\u0000\u0000\u0000\"#\u0001\u0000\u0000\u0000#\u0005\u0001\u0000"+
		"\u0000\u0000$\"\u0001\u0000\u0000\u0000%&\u0005\n\u0000\u0000&*\u0003"+
		"\b\u0004\u0000\'*\u0005\u0006\u0000\u0000(*\u0005\u0007\u0000\u0000)%"+
		"\u0001\u0000\u0000\u0000)\'\u0001\u0000\u0000\u0000)(\u0001\u0000\u0000"+
		"\u0000*\u0007\u0001\u0000\u0000\u0000+,\u0007\u0000\u0000\u0000,-\u0005"+
		"\n\u0000\u0000-\t\u0001\u0000\u0000\u0000\u0003\u0017\")";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}