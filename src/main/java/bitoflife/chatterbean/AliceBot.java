/*
Copyleft (C) 2004 H�lio Perroni Filho
xperroni@yahoo.com
ICQ: 2490863

This file is part of ChatterBean.

ChatterBean is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

ChatterBean is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with ChatterBean (look at the Documentos/ directory); if not, either write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA, or visit (http://www.gnu.org/licenses/gpl.txt).
 */

package bitoflife.chatterbean;

import bitoflife.chatterbean.aiml.Category;
import bitoflife.chatterbean.text.Request;
import bitoflife.chatterbean.text.Response;
import bitoflife.chatterbean.text.Sentence;
import bitoflife.chatterbean.text.Transformations;

public class AliceBot {
	/*
	 * Attribute Section
	 */

	/** Context information for this bot current conversation. */
	private Context context;

	/** The Graphmaster maps user requests to AIML categories. */
	private Graphmaster graphmaster;

	/*
	 * Constructor Section
	 */

	/**
	 * Default constructor.
	 */
	public AliceBot() {
	}

	/**
	 * Creates a new AliceBot from a Graphmaster.
	 * 
	 * @param graphmaster
	 *            Graphmaster object.
	 */
	public AliceBot(Graphmaster graphmaster) {
		setContext(new Context());
		setGraphmaster(graphmaster);
	}

	/**
	 * Creates a new AliceBot from a Context and a Graphmaster.
	 * 
	 * @param context
	 *            A Context.
	 * @param graphmaster
	 *            A Graphmaster.
	 */
	public AliceBot(Context context, Graphmaster graphmaster) {
		setContext(context);
		setGraphmaster(graphmaster);
	}

	/*
	 * Method Section
	 */

	private void respond(Sentence sentence, Sentence that, Sentence topic,
			Response response) {
		if (sentence.length() > 0) {
			Match match = new Match(this, sentence, that, topic);// match把AliceBot,sentence,that,topic进行封装。
			Category category = graphmaster.match(match);
			if (category == null)
				System.out.println("category==null");
			response.append(category.process(match));
		}
	}

	/**
	 * Responds a request.
	 * 
	 * @param request
	 *            A Request.
	 * @return A response to the request.
	 */
	public Response respond(Request request) {
		
		long end = 0;
		boolean bTime = false;
		boolean bSlot = true;
		if(bTime) {
		   end = System.currentTimeMillis();
           //end = System.nanoTime();
           System.out.println("1:"  +  String.valueOf(end));
		}
		String original = request.getOriginal();
		if (original == null || "".equals(original.trim()))// 如果输入的字符串是空的，那就直接返回空的字符串。
			return new Response("");
		if(bTime) {
		   end = System.currentTimeMillis();
		   System.out.println("2:"  +  String.valueOf(end));
		}
		Sentence that = context.getThat();
		Sentence topic = context.getTopic();

		//System.out.println("原始Request:");// ###########
		//System.out.println(request.toString());

		transformations().normalization(request);
		//
		//System.out.println("规范Request:");// ###########
		//System.out.println(request.toString());
		if(bTime) {
		  end = System.currentTimeMillis();
		  System.out.println("3:"  +  String.valueOf(end));
		}
		context.appendRequest(request);

		Response response = new Response();
		for (Sentence sentence : request.getSentences())
			respond(sentence, that, topic, response);
		    if(bTime) {
		      end = System.currentTimeMillis();
		      System.out.println(":" + String.valueOf(end));
		    }
		if(!bSlot)
		    context.appendResponse(response);
		if(bTime) {
		  end = System.currentTimeMillis();
	      System.out.println("last:"  +  String.valueOf(end));
		}
		return response;
	}

	/**
	 * Responds a request.
	 * 
	 * @param A
	 *            request string.
	 * @return A response to the request string.
	 */
	public String respond(String input) {
		long end = 0;
		boolean bTime = false;
		Response response = respond(new Request(input));
		if(bTime) {
		  end = System.currentTimeMillis();
		  System.out.println("respond:"  +  String.valueOf(end));
		}
		return response.trimOriginal();
	}

	/*
	 * Accessor Section
	 */

	public Transformations transformations() {
		return context.getTransformations();
	}

	/*
	 * Property Section
	 */

	/**
	 * Returns this AliceBot's Context.
	 * 
	 * @return The Context associated to this AliceBot.
	 */
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Graphmaster getGraphmaster() {
		return graphmaster;
	}

	public void setGraphmaster(Graphmaster graphmaster) {
		this.graphmaster = graphmaster;
	}
}
