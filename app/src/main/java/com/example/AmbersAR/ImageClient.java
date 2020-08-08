package com.example.AmbersAR;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ImageClient extends AsyncTask<byte[],Void,Void>{
    private Socket socket;

    @Override
    protected Void doInBackground(byte[]... bytes) {
        try {
            byte[] message = bytes[0];

            socket = new Socket("192.168.99.134",2530);  //TODO: provide user options for host
            OutputStream os = socket.getOutputStream();

            os.write(message);
//
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
