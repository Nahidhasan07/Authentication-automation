
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import test_mac.port_re;
import static test_mac.port_re.port_rel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nahid32081
 */
public class PrevPort_mac_find {

    Session session;
    String hostString;
    String macString;
    List<String> list;

    public PrevPort_mac_find(String host, String mac) throws JSchException, FileNotFoundException {
        hostString = host;
        macString = mac;
        connection(host);
        String result = executeCommand("show run");
        int f = port_rel(macString, result);
    }

    public String getlist() {
        if (list != null) {
            return list.get(0);
        } else {
            return "null";
        }
    }

    public int port_rel(String mac, String result) throws FileNotFoundException {
        List<String> list = new LinkedList<>();
//        File file = new File("config.txt");
//        Scanner scanner = new Scanner(file);
        Scanner scanner = new Scanner(result);
        int f = 0;
        String line = "";
        StringTokenizer st;
        String interfaceString = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
//            System.out.println(line);
            if (line.contains("interface") && !(line.contains("Vlan")) && f == 0) {
                interfaceString = line.replace("interface ", "");
                list.add(interfaceString);
                f = 1;
                continue;
            }
            if (f == 1 && !(line.equals(("!")))) {
                list.add(line);
                continue;
            } else if (f == 1 && line.equals("!")) {
                int x = mac_search(list, mac);
                if (x == 1) {
                    //System.out.println(list);
                    return 1;
                }
                list.clear();
                f = 0;
//                break;
            }
        }
        return 0;
    }

    public int mac_search(List<String> list, String mac) {
//        System.out.println(list);
        String line = "";
        for (int a = 0; a < list.size(); a++) {
            line = list.get(a);
            if (line.contains("switchport port-security mac-address sticky") && line.contains(mac)) {
                this.list = list;
                return 1;
            }
        }
        return 0;
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
