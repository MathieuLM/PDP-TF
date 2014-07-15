package br.com.ufrgs.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the main thread class for the server application,
 * it provides all the methods to read object from client,
 * write object to client and save object into database.
 */
public class Server {
	
    private ArrayList<ConnectionToClient> clients;
    private LinkedBlockingQueue<Object> objects;
    private Database database;
    private ServerSocket serverSocket;
    private static int PORT = 8888;

    /*
     * Constructor method, instantiates the server socket,
     * the list of clients, the queue of objects and the
     * database object. Starts the thread that waits for
     * clients connection and that adds the client to the
     * list of clients when it connects. Send all the data
     * in the database to the client at the connection starting.
     * Starts the thread that wait for the addon of an object
     * into the objects queue to save this object into the
     * database and then send it to all connected clients.
     */
    public Server() throws IOException {
    	this.serverSocket = new ServerSocket(PORT);
        this.clients = new ArrayList<ConnectionToClient>();
        this.objects = new LinkedBlockingQueue<Object>();
        this.database = new Database();

        Thread accept = new Thread() {
            public void run(){
                while(true){
                    try{
                        Socket socket = serverSocket.accept();
                        clients.add(new ConnectionToClient(socket));
                        sendAllDataToOne(clients.size() - 1);
                        
                    } catch(IOException exception){ 
                    	exception.printStackTrace();
                    }
                }
            }
        };
        accept.start();

        Thread messageHandling = new Thread() {
            public void run(){
                while(true){
                	if(objects.size() > 0) {
                		Object object = objects.poll();
                		database.save(object);
                		sendToAll(object);
                	}
                }
            }
        };
        messageHandling.start();
        
        System.out.println("Server started");
    }

    /*
     * Subclass that starts the thread waiting and reading
     * objects from the client. When an object is received,
     * it is added to the queue of objects. In the case that
     * the ObjectInputStream.readUnshared throws an exception,
     * which means that the client as closed the connection,
     * the client is removed from the clients list and breaks
     * the while loop.
     */
    private class ConnectionToClient {
        private ObjectInputStream in;
        private ObjectOutputStream out;

        ConnectionToClient(Socket socket) throws IOException {
        	this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            final ConnectionToClient self = this;
            
            Thread read = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object object = in.readUnshared();
                            objects.add(object);
                            
                            System.out.println("Object received: " + object);
                            
                        } catch(Exception exception){
                        	exception.printStackTrace();
                        	clients.remove(self);
                        	break;
                        }
                    }
                }
            };
            read.start();
        }

        /*
         * Write an object on the socket to the client.
         */
        public void write(Object object) {
            try{
                out.writeUnshared(object);
                
                System.out.println("Object sended: " + object);
                
            } catch(IOException e){
            	e.printStackTrace();
            }
        }
    }

    /*
     * Send the object casted as parameter to the
     * client at the index index into the clients
     * list.
     */
    public void sendToOne(int index, Object object) throws IndexOutOfBoundsException {
        clients.get(index).write(object);
    }

    /*
     * Send the object casted as parameter to all
     * the clients into the clients list.
     */
    public void sendToAll(Object object){
        for(ConnectionToClient client : clients)
            client.write(object);
    }
    
    /*
     * Send all the data into the database to the client
     * at the index index in the clients list.
     */
    public void sendAllDataToOne(int index) {
    	for(Object object : this.database) {
    		this.sendToOne(index, object);
    	}
    }
    
    /*
     * Server main thread initialization.
     */
    public static void main(String[] args) {
    	try {
			new Server();
			
		} catch (IOException exception) {
			exception.printStackTrace();
		}
    }
}
