package com.jhu.fireflies.com.clue_less;

import java.net.Socket;

/**
 * Created by Shawn on 5/1/18.
 */

public class BackendHandlerReference {
    private static  BackendHandler backendHandler;

    private static String serverLog;
    private static Socket socket;

    public static Socket getSocket(){return socket;}

    public static void setSocket(Socket s){BackendHandlerReference.socket = s;}

    public static String getServerLog(){
        return serverLog;
    }

    public static void addToServerLog(String command){
        BackendHandlerReference.serverLog = command + "\n" + BackendHandlerReference.serverLog;
    }

    public static  synchronized  BackendHandler getBackendHandler(){
        return backendHandler;
    }

    public static synchronized void setBackendHandler(BackendHandler backendHandlerIn){
        BackendHandlerReference.backendHandler = backendHandlerIn;
    }
}
