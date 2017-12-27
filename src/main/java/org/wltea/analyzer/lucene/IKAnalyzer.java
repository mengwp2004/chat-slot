/**
 * IK 中文分词  版本 5.0.1
 * IK Analyzer release 5.0.1
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 */
package org.wltea.analyzer.lucene;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * IK分词器，Lucene Analyzer接口实现
 * 兼容Lucene 4.0版本
 */
public final class IKAnalyzer extends Analyzer{
	
	private boolean useSmart;
	
	public boolean useSmart() {
		return useSmart;
	}

	public void setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
	}

	/**
	 * IK分词器Lucene  Analyzer接口实现类
	 * 
	 * 默认细粒度切分算法
	 */
	public IKAnalyzer(){
		this(false);
	}
	
	/**
	 * IK分词器Lucene Analyzer接口实现类
	 * 
	 * @param useSmart 当为true时，分词器进行智能切分
	 */
	public IKAnalyzer(boolean useSmart){
		super();
		this.useSmart = useSmart;
	}

	/**
	 * 重载Analyzer接口，构造分词组件
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName, final Reader in) {
		Tokenizer _IKTokenizer = new IKTokenizer(in , this.useSmart());
		return new TokenStreamComponents(_IKTokenizer);
	}
	 public static String IKAnalysis(String str) {  
		  
		   if(str.getBytes().length == str.length()) {  
		       //如果不包含中文，就直接返回。  
		       return str;  
		    }else {  
		       //由于IK分词器，不支持特殊字符，所以将 * 改为中文字符“这是星号”,中文分词以后再将“这是星号”修正为为 *  
		       //同理将 _改为中文字符串“这是下划线”，中文分词以后再将“这是下划线”修正为 _  
		        //str= str.replaceAll("\\*","星号").replaceAll("_","下划线");  
		        str= str.replaceAll("\\*","wdxinhao").replaceAll("_","xiahuaxian");  
		    }  
		  
		   StringBuffer sb =new StringBuffer();  
		   try {  
		       byte[] bt =str.getBytes();  
		        InputStream ip =new ByteArrayInputStream(bt);  
		        Reader read =new InputStreamReader(ip);  
		       //设置为智能分词  
		        IKSegmenter iks =new IKSegmenter(read,true);  
		       
		        Lexeme t;
				while ((t =iks.next()) !=null) {  
		           //在每个分词元之后添加空格  
		            sb.append(t.getLexemeText()+" ");  
		        }  
		       //sb.delete(sb.length() - 1, sb.length());  
		    }catch (IOException e) {  
		       //TODOAuto-generated catch block  
		    }  
		   //sb.toString();
		   //return sb.toString().replaceAll("星号","*").replaceAll("下划线","_"); 
		   return sb.toString().replaceAll("wdxinhao","*").replaceAll("xiahuaxian","_");
		} 
}
