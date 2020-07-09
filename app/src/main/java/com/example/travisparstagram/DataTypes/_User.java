package com.example.travisparstagram.DataTypes;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("_User")
public class _User extends ParseUser {

    public _User() {
        super();
    }




    public static final String KEY_IMAGE = "ProfileImage";
    public ParseFile getImage(){
        return(getParseFile(KEY_IMAGE));
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE,parseFile);
    }
}
