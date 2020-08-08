package com.example.AmbersAR;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<String,Void,Void>{
    private Socket socket;
    private PrintWriter writer;

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String message = strings[0];

            socket = new Socket("192.168.99.134",2530);  //TODO: provide user options for host
            writer = new PrintWriter(socket.getOutputStream());

            writer.write(message);
            writer.flush();

            writer.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
