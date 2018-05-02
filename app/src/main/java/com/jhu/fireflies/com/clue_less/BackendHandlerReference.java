package com.jhu.fireflies.com.clue_less;

/**
 * Created by Shawn on 5/1/18.
 */

public class BackendHandlerReference {
    private static  BackendHandler backendHandler;

    public static  synchronized  BackendHandler getBackendHandler(){
        return backendHandler;
    }

    public static synchronized void setBackendHandler(BackendHandler backendHandlerIn){
        BackendHandlerReference.backendHandler = backendHandlerIn;
    }
}
