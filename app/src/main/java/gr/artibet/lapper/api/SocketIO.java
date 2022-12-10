package gr.artibet.lapper.api;

import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import java.net.URISyntaxException;

// Socket.io SINGLETON class
public class SocketIO {

    //private static final String SOCKET_URL = "http://lapper-ws.herokuapp.com/";
    //private static final String SOCKET_URL = "http://10.0.2.2:3000/";
    private static final String SOCKET_URL = "https://lapper-ws.artibet.gr";
    private static SocketIO mInstance;
    private Socket mSocket;

    // private constructor - not to be called from outside
    private SocketIO() {
        try {
            mSocket = IO.socket(SOCKET_URL);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();

    }

    // Get SocketIO instance
    public static synchronized SocketIO getInstance() {
        if (mInstance == null) {
            mInstance = new SocketIO();
        }
        return mInstance;
    }

    // Get single socket object
    public Socket getSocket() {
        return mSocket;
    }

}
