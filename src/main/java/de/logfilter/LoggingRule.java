package de.logfilter;

import java.util.regex.Pattern;

public class LoggingRule {
	
	private String rule;
	private String replacement;
	private boolean replace;
	private Pattern pattern;
	
	public LoggingRule(String rule, boolean replace, String replacement){
		this.rule = rule;
		this.pattern = Pattern.compile(rule);
		this.replace = replace;
		this.replacement = replacement;
	}
	
	public LoggingRule(String rule) {
		this.rule = rule;
		this.pattern = Pattern.compile(rule);
		this.replace = false;
		this.replacement = null;
	}
	
	public String getRule() {
		return this.rule;
	}
	
	public String getReplacement() {
		return this.replacement;
	}
	
	public Pattern getPattern() {
		return this.pattern;
	}
	
	public boolean shouldReplace() {
		return this.replace;
	}
}