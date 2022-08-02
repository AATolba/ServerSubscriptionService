package com.subscription.server.Error;

import org.luaj.vm2.ast.Str;

public class Error {

    public String invalidName(){
        return "please enter a name of 3 or more characters ";
    }
    public String invalidId(){
        return "please enter an Id between 0 and 4 billion";
    }
    public String invalidEmail(){
        return "please enter a valid email";
    }
    public String UserDoesNotExist(){
        return "The user you are trying to get does not exist";
    }
    public String NullUser(){
        return "The user data is missing please try again";
    }
    public String  USerNotSaved(){
        return "The user was not saved ";
    }
    public String InternalServerError(){
        return "Internal server Error  please try again later";
    }
    public String ServerCapacityError(){
        return "You only can allocate up to 100 Gigs per request";
    }


}
