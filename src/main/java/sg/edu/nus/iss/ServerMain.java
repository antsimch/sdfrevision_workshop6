package sg.edu.nus.iss;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class ServerMain 
{
    public static void main( String[] args ) throws IOException 
    {
        int port = 12345;
        String fileName = "cookie_file.txt";
        
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            fileName = args[1];
        } else {
            System.out.println("args[0]: port, args[1]: file");
            System.exit(1);
        }      

        ServerSocket server = new ServerSocket(port);
        System.out.println("Server started. Listening on port " + port);

        ExecutorService thrPool = Executors.newFixedThreadPool(2);
        

        while (true) {
            System.out.println("Waiting for new client connection...");
            Socket socket = server.accept();

            System.out.printf("Got a new connection: %s\n\n", socket);

            CookieClientHandler handler = new CookieClientHandler(socket, fileName);

            System.out.println("Dispatching client to thread pool");
            thrPool.submit(handler);
        }  
    }
}
