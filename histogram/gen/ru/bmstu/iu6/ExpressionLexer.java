// Generated from C:/Users/dimatroickij/PycharmProjects/searchImages/histogram/src/main/java/ru/bmstu/iu6\Expression.g4 by ANTLR 4.9
package ru.bmstu.iu6;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, OP=3, ELEMENT=4, ELEMENT1D=5, WS=6;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "OP", "ELEMENT", "ELEMENT1D", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "OP", "ELEMENT", "ELEMENT1D", "WS"
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


	public ExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\bJ\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\4\5\4\31\n\4\3\5\6\5\34\n\5\r\5\16\5\35\3\5\7\5!\n\5\f\5\16\5$\13\5"+
		"\3\6\3\6\7\6(\n\6\f\6\16\6+\13\6\3\6\3\6\7\6/\n\6\f\6\16\6\62\13\6\3\6"+
		"\3\6\7\6\66\n\6\f\6\16\69\13\6\3\6\3\6\7\6=\n\6\f\6\16\6@\13\6\3\6\3\6"+
		"\3\7\6\7E\n\7\r\7\16\7F\3\7\3\7\2\2\b\3\3\5\4\7\5\t\6\13\7\r\b\3\2\6\7"+
		"\2((,-//\61\61~~\4\2C\\c|\3\2\62;\5\2\13\f\17\17\"\"\2R\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\3\17\3\2\2"+
		"\2\5\21\3\2\2\2\7\30\3\2\2\2\t\33\3\2\2\2\13%\3\2\2\2\rD\3\2\2\2\17\20"+
		"\7*\2\2\20\4\3\2\2\2\21\22\7+\2\2\22\6\3\2\2\2\23\31\t\2\2\2\24\25\7%"+
		"\2\2\25\31\7~\2\2\26\27\7%\2\2\27\31\7\61\2\2\30\23\3\2\2\2\30\24\3\2"+
		"\2\2\30\26\3\2\2\2\31\b\3\2\2\2\32\34\t\3\2\2\33\32\3\2\2\2\34\35\3\2"+
		"\2\2\35\33\3\2\2\2\35\36\3\2\2\2\36\"\3\2\2\2\37!\t\4\2\2 \37\3\2\2\2"+
		"!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\n\3\2\2\2$\"\3\2\2\2%)\7*\2\2&(\5\r"+
		"\7\2\'&\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*,\3\2\2\2+)\3\2\2\2,\60"+
		"\5\t\5\2-/\5\r\7\2.-\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61"+
		"\63\3\2\2\2\62\60\3\2\2\2\63\67\7.\2\2\64\66\5\r\7\2\65\64\3\2\2\2\66"+
		"9\3\2\2\2\67\65\3\2\2\2\678\3\2\2\28:\3\2\2\29\67\3\2\2\2:>\5\t\5\2;="+
		"\5\r\7\2<;\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3\2\2\2@>\3\2\2\2A"+
		"B\7+\2\2B\f\3\2\2\2CE\t\5\2\2DC\3\2\2\2EF\3\2\2\2FD\3\2\2\2FG\3\2\2\2"+
		"GH\3\2\2\2HI\b\7\2\2I\16\3\2\2\2\f\2\30\33\35\")\60\67>F\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}