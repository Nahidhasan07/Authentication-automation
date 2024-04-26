
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nahid
 */
public class execution {

    public Session session;
    public String result;

    public execution(String host) throws JSchException {
        connection(host);
    }

    public void command(String command) {
        String result = executeCommand(command);
        this.result = result;
    }

    public Session connecttion() {
        return session;
    }

    public void disconnect() {
        session.disconnect();
    }

    public String getResult() {
        return result;
    }

    public void connection(String host) throws JSchException {
        JSch jsch = new JSch();
        String username = "nahid32081"; // your username
        String password = "Notagain81@7x.";
        int port = 22; // your remote server port
        Session session = jsch.getSession(username, host, port);
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(2000);
        session.connect();
        this.session = session;
    }

    private String executeCommand(String command) {
        String finalResult = "";
        try {
            String result = null;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);// this method should be called before connect()
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream inputStream = channel.getInputStream();
            channel.connect();
            byte[] byteObject = new byte[10240];
            while (true) {
                while (inputStream.available() > 0) {
                    int readByte = inputStream.read(byteObject, 0, 1024);
                    if (readByte < 0) {
                        break;
                    }
                    result = new String(byteObject, 0, readByte);
                    finalResult = finalResult + result;
                }
                if (channel.isClosed()) {
                    break;
                }
            }
            channel.disconnect();
            //System.out.println("Disconnected channel " + channel.getExitStatus());
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return finalResult;
    }
}
