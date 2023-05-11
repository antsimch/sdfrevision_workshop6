package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CookieClientHandler implements Runnable {
    
    private final Socket socket;
    private String fileName;

    public CookieClientHandler(Socket socket, String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        
        File cookieFile = new File(fileName);

        if (!cookieFile.exists()) {
            System.out.println("File not found");
            System.exit(2);;
        }

        Cookie cookie = new Cookie();

        try {
            cookie.populateCookieList(cookieFile);

            InputStream is = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            String serverIncoming = "";

            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            while (!serverIncoming.equalsIgnoreCase("close")) {
                serverIncoming = dis.readUTF();

                if (serverIncoming.equalsIgnoreCase("get-cookie")) {
                    dos.writeUTF("cookie-text " + cookie.getCookie());
                    dos.flush();
                }
            }

            dos.close();
            bos.close();
            os.close();
            dis.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
