package com.example.travisparstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public Comment(){
        super();
    }
    public String getText() {
        return getString(KEY_TEXT);
    }
    public void setKeyText(String keyText){
        put(KEY_TEXT, keyText);
    }

    public Post getPost(){
        return (Post) getParseObject(KEY_POST);
    }
    public void setImage(ParseFile parseFile){
        put(KEY_POST,parseFile);
    }
    public _User getUser(){
        return (_User) getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser){
        put(KEY_USER,parseUser);
    }

    public static final String KEY_TEXT = "Text";
    public static final String KEY_POST = "Post";
    public static final String KEY_USER = "User";


}