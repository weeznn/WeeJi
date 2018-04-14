package com.weeznn.jieba1.baidu_nlp.res_entry;

/**
 * Created by weeznn on 2018/4/13.
 */

public class Lexer {

    public String word;
    public String wordStype;
    public String sentence;

    public Lexer(String word,String wordStype,String sentence){
        this.sentence=sentence;
        this.word=word;
        this.wordStype=wordStype;
    }
}
