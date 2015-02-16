package net.afterlifelochie.fontbox.layout.markdown;

import java.util.regex.Pattern;

public class MarkdownParser {

	private static final Pattern compilerDirective = Pattern.compile("^(\\#!{([a-z]*)})(.*?)(\\#!{end\\2})",
			Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern list = Pattern.compile(
			"^(([ ]{0,3}(?:[*+-]|\\d+[.])[ \\t]+)(?s:.+?)(\\z|\\n{2,}(?=\\S)(?![ \\t]*(?:[*+-]|\\d+[.])[ \\t]+)))",
			Pattern.MULTILINE);
	private static final Pattern listItem = Pattern.compile(
			"(\\n)?(^[ \\t]*)(?:[*+-]|\\d+[.])[ \\t]+((?s:.+?)(\\n{1,2}))(?= \\n* (\\z|\\2(?:[*+-]|\\d+[.])[ \\t]+))",
			Pattern.MULTILINE);
	private static final Pattern heading = Pattern.compile("^(\\#{1,6})[ \\t]*(.+?)[ \\t]*\\#*\\n+", Pattern.MULTILINE);

	public static MarkdownDocument parse(String blob) throws MarkdownException {
		
	}
}
