package com.xiaomi.alice;

import bitoflife.chatterbean.AliceBot;
import bitoflife.chatterbean.AliceBotMother;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by qinbin on 2016/6/21.
 */
public class Main {
    public static final String END = "bye";

    public static String input()
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try
        {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;

    }
    
    public static String get_slot(AliceBot bot,String sentence) {
    	return bot.respond(sentence);
    }
    
    public static void main(String[] args) throws Exception {
        System.getProperties().load(Main.class.getClassLoader().getResourceAsStream("my.properties"));
		long end = 0,begin=0;
		boolean bTime = false;
        AliceBotMother mother = new AliceBotMother();
        mother.setUp();
        AliceBot bot = mother.newInstance();
        begin =System.currentTimeMillis();
        System.out.println("begin:"  +  String.valueOf(begin));
        //begin = System.nanoTime();
        //System.out.println(bot.respond("WHAT IS YOUR VERSION"));
        //System.out.println(bot.respond("好烦呢"));
        
        //System.out.println(bot.respond("怎么进行推广呀"));

        //String res = bot.respond("我怎么进行充值我的账号");
        String sentence = "我怎么进行充值我的账号";
        //System.out.println(res);
        //String res = sentence;
        System.out.println(get_slot(bot,sentence));
        end = System.currentTimeMillis();
        //end = System.nanoTime();
        System.out.println("end:"  +  String.valueOf(end));
        System.out.println((end-begin));
        //System.out.println(res);
        /*System.err.println("Alice>" + bot.respond("welcome"));
        while (true) {
            String input = input();
            if (END.equalsIgnoreCase(input)) {
                break;
            }
            System.err.println("Alice>" + bot.respond(input));
        }*/
    }
}
