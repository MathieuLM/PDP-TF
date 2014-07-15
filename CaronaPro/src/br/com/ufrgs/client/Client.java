package br.com.ufrgs.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This class is the interface between the application
 * main activity (main thread) and the server. It extends
 * Observable because the main activity as to be notify to
 * modify the UI when data from the server is received. It
 * is a Singleton class too because there is only one interface
 * between the server and the main thread.
 */
public class Client extends Observable {

	private static Client INSTANCE;
    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> objects;
    private static String SERVER_HOST = "10.0.2.2";
    private static int SERVER_PORT = 8888;

    private Client() {
        new NetworkTask(this).execute();
    }
    
    /*
     * Singleton instantiation
     */
    public static Client getInstance() {
    	if(INSTANCE == null) {
			INSTANCE = new Client();
		}
		return INSTANCE;
    }

    /*
     * Subclass that defines the Thread who constantly wait and read
     * the Objects sent by the server.
     */
    private class ConnectionToServer {
        private ObjectInputStream in;
        private ObjectOutputStream out;
        
        ConnectionToServer(Socket socket) throws IOException {
        	this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            
            Thread read = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object object = in.readUnshared();
                            objects.add(object);
                            
                        } catch(Exception exception) { 
                        	exception.printStackTrace();
                        	break;
                        }
                    }
                }
            };
            read.start();
        }

        private void write(Object object) {
            try{
                this.out.writeUnshared(object);

                Log.d("Trace", "Object sended: " + object);
                
            } catch(IOException exception) { 
            	exception.printStackTrace(); 
            }
        }
    }

    /*
     * Send the object to the server to be saved in the
     * database, the data sending is executed into an 
     * AsyncTask to avoid the problems of networking on the
     * main thread.
     */
    public void send(Object object) {
        new DistributeTask().execute(object);
    }
    
    /*
     * AsyncTask that execute all the Networking treatment except
     * the sending of the object. This class too is used to avoid
     * the problems of networking on the main thread.
     */
    private class NetworkTask extends AsyncTask<Void, Object, Object> {

    	private Client client;
    	
    	NetworkTask(Client client) {
    		this.client = client;
    	}
    	
    	/*
    	 * (non-Javadoc)
    	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
    	 * 
    	 * This is the method who is executed asynchronously when
    	 * the AsyncTask.execute method is called. It creates the
    	 * the queue where are added the objects received from the
    	 * socket. Creates the socket and creates the Threads for
    	 * reading on the socket and for updating the Model and the UI
    	 * when the queue contains a new object.
    	 */
		@Override
		protected Object doInBackground(Void... voids) {
			objects = new LinkedBlockingQueue<Object>();
			try {
				Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
				server = new ConnectionToServer(socket);
			} catch(Exception exception) {
				exception.printStackTrace();
			}
	        
	        Thread messageHandling = new Thread() {
	            public void run(){
	                while(true){
	                	if(objects.size() > 0) {
	                		Object object = objects.poll();
	                		Log.d("Trace", "Object received: " + object);
	                		
	                		publishProgress(object);
	                	}
	                }
	            }
	        };
	        messageHandling.start();
	        
	        return null;
		}
		
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 * 
		 * Method called when AsyncTask.publishProgress() is called,
		 * notify the observers to update the UI with the object
		 * casted as parameter.
		 */
		@Override
		protected void onProgressUpdate(Object...objects) {
			this.client.setChanged();
			this.client.notifyObservers(objects[0]);
		}
    }
    
    /*
     * Asynchronous task for writing an object on the socket
     * to the server.
     */
    private class DistributeTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... objects) {
			server.write(objects[0]);
			
			return null;
		}
    }

}
