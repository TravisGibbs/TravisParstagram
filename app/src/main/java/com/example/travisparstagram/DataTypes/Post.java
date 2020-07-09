package com.example.travisparstagram.DataTypes;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {
    public Post(){
        super();
    }
    public String getKeyDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setKeyDescription(String keyDescription){
        put(KEY_DESCRIPTION, keyDescription);
    }

    public ParseFile getImage(){
        return(getParseFile(KEY_IMAGE));
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE,parseFile);
    }
    public _User getUser(){
        return (_User) getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser){
        put(KEY_USER,parseUser);
    }

    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_USER = "User";


}
