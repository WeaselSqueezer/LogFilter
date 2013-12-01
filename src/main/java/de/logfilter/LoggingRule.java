package de.logfilter;

import java.util.regex.Pattern;

import lombok.Getter;

public class LoggingRule {
	
	private @Getter String rule;
	private @Getter String replacement;
	private boolean replace;
	private @Getter Pattern pattern;
	
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
	
	public boolean shouldReplace() {
		return this.replace;
	}
}