/*
Copyleft (C) 2005 H�lio Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documents/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean;

import bitoflife.chatterbean.text.Sentence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bitoflife.chatterbean.text.Sentence.ASTERISK;

/**
 * Contains information about a match operation, which is needed by the classes
 * of the <code>bitoflife.chatterbean.aiml</code> to produce a proper response.
 */
public class Match implements Serializable { // Math为什么定义为可序列化呢？
	/*
	 * Inner Classes
	 */

	public enum Section {
		PATTERN, THAT, TOPIC;
	}

	/*
	 * Attributes
	 */

	/**
	 * Version class identifier for the serialization engine. Matches the number
	 * of the last revision where the class was created / modified.
	 */
	private static final long serialVersionUID = 8L;

	private final Map<Section, List<String>> sections = new HashMap<Section, List<String>>();

	private AliceBot callback;

	private Sentence input;

	private Sentence that;

	private Sentence topic;

	private String[] matchPath;
	
	private boolean bSlot = false;

	// 发现这里的列表长度都是2的，是不是说只支持2个通配符？？？列表里面应该是存的是通配符所匹配的内容吧？
	// 代码快。代码块的作用？
	{
		sections.put(Section.PATTERN, new ArrayList<String>(2)); // Pattern
																	// wildcards
		if(!bSlot) {
		sections.put(Section.THAT, new ArrayList<String>(2)); // That wildcards
		sections.put(Section.TOPIC, new ArrayList<String>(2)); // Topic
																// wildcards
		}
	}

	/*
	 * Constructor
	 */

	public Match() {
	}
	
	public Match(AliceBot callback, Sentence input) {
		this.callback = callback;
		this.input = input;
        setUpMatchPath(input.getSplittedOfSentence());
	}
	public Match(AliceBot callback, Sentence input, Sentence that,
			Sentence topic) {
		this.callback = callback;
		this.input = input;
		if(bSlot) {
			setUpMatchPath(input.getSplittedOfSentence());
		}else {
		this.that = that;
		this.topic = topic;
		setUpMatchPath(input.getSplittedOfSentence(),
				that.getSplittedOfSentence(), topic.getSplittedOfSentence());
		}
	}

	public Match(Sentence input) {
		this(null, input, ASTERISK, ASTERISK);
	}

	/*
	 * Methods
	 */

	private void appendWildcard(List<String> section, Sentence source,
			int beginIndex, int endIndex) {
		if (beginIndex == endIndex) {
			section.add(0, "");
		} else
			try {
				section.add(0, source.original(beginIndex, endIndex)); // 原始输入字符串的一部分。
			} catch (Exception e) {
				// throw new RuntimeException("Source: {\"" +
				// source.getOriginal() + "\", \"" + source.getNormalized() +
				// "\"}\n" +
				// "Begin Index: " + beginIndex + "\n" +
				// "End Index: " + endIndex, e);
			}
	}

	private void setUpMatchPath(String[] pattern, String[] that, String[] topic) {
		int m = pattern.length, n = that.length, o = topic.length;
		matchPath = new String[m + 1 + n + 1 + o];
		matchPath[m] = "<THAT>";
		matchPath[m + 1 + n] = "<TOPIC>";

		System.arraycopy(pattern, 0, matchPath, 0, m);
		System.arraycopy(that, 0, matchPath, m + 1, n);
		System.arraycopy(topic, 0, matchPath, m + 1 + n + 1, o);
	}

	private void setUpMatchPath(String[] pattern) {
		int m = pattern.length;
		matchPath = new String[m ];
		System.arraycopy(pattern, 0, matchPath, 0, m);
	
	}
	// 根据部分的长度分级处理。
	public void appendWildcard(int beginIndex, int endIndex) {
		int inputLength = input.length();
		if (beginIndex <= inputLength) {
			appendWildcard(sections.get(Section.PATTERN), input, beginIndex,
					endIndex);
			return;
		}

		beginIndex = beginIndex - (inputLength + 1);
		endIndex = endIndex - (inputLength + 1);

		int thatLength = that.length();
		if (beginIndex <= thatLength) {
			appendWildcard(sections.get(Section.THAT), that, beginIndex,
					endIndex);
			return;
		}

		beginIndex = beginIndex - (thatLength + 1);
		endIndex = endIndex - (thatLength + 1);

		int topicLength = topic.length();
		if (beginIndex < topicLength)
			appendWildcard(sections.get(Section.TOPIC), topic, beginIndex,
					endIndex);
	}

	/**
	 * Gets the contents for the (index)th wildcard in the matched section.
	 */
	// 从这里可以看出程序只能保存2个'*'号的内容。
	public String wildcard(Section section, int index) {
		List<String> wildcards = sections.get(section);

		// fixed by lcl
		if (wildcards.size() == 0)// 如果是我，我是不会想到加这个保险的。因为我会觉得他的长度肯定就是2了。
			return "";
		int i = index - 1;
		if (i < wildcards.size() && i > -1)
			return wildcards.get(i);
		else
			return "";
	}

	/*
	 * Properties
	 */

	public AliceBot getCallback() {
		return callback;
	}

	public void setCallback(AliceBot callback) {
		this.callback = callback;
	}

	public String[] getMatchPath() {
		return matchPath;
	}

	public String getMatchPathByIndex(int index) {
		return matchPath[index];
	}

	public int getMatchPathLength() {
		return matchPath.length;
	}
}
