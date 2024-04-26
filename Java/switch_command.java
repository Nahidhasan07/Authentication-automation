
import com.jcraft.jsch.JSchException;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nahid
 */
public class switch_command {

    Map<String, String> map;
    String host;

    public switch_command(Map<String, String> map, String host) throws JSchException, IOException {
        this.map = map;
        this.host = host;
        port_mac();
        System.out.println(host);
    }

    public void port_mac() throws JSchException, IOException {
        String currport = "", mac = "", prevport = "x";
//        System.out.println(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mac = entry.getKey().trim();
            currport = entry.getValue().trim();
            //mac realse from old port search baki ase
            PrevPort_mac_find pp = new PrevPort_mac_find(host, mac);
            prevport = pp.getlist();
            System.out.println(prevport + " " + currport);
            mac_release(currport, prevport);
        }
    }

    public void mac_release(String currport, String prevport) throws JSchException, IOException {
        Runtime runtime = Runtime.getRuntime();
        String path = "C:\\Users\\nahid32081\\Documents\\python\\mac_violation\\mac_xx.py";
        replaceSelected(host, currport, prevport);
        Process process = Runtime.getRuntime().exec("python " + path);
        int flag = CMDResults(process);
        if (flag == -1) {
            System.out.println("something wrong in flag");
        }
    }

    public int CMDResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public void replaceSelected(String ip, String currport, String prevport) {
        ip = "    ip = " + '"' + ip + '"';
//        String k = "remote_conn.send(" + '"' + "int " + portString + "\\n" + '"' + ')';
        String k1 = "";
        if ((prevport!=null) || prevport.equalsIgnoreCase("")) {
            k1 = "    list = [" + '"' + prevport + '"' + "," + '"' + currport + '"' + "]";
        } else {
            k1 = "    list = [" + '"' + currport + '"' + "]";
        }
        try {
//            String path = "C:\\Users\\nahid32081\\Documents\\python\\mac_violation\\mac_x.py";
            BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\nahid32081\\Documents\\python\\mac_violation\\mac_x_1.py"));
            StringBuffer inputBuffer = new StringBuffer();
            String line;
            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();
            String inputStr = inputBuffer.toString();
//            System.out.println(inputStr); // display the original file for debugging
            String[] lines = inputBuffer.toString().split("\n"); //or use any other character for splitting      
//            System.out.println(lines[5]);
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("ip = ")) {
//                    System.out.println(lines[i]);
                    inputStr = inputStr.replace(lines[i], ip);
                }
                if (lines[i].contains("list = [")) {
//                    System.out.println(lines[i]);
                    inputStr = inputStr.replace(lines[i], k1);
                }
            }
            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\nahid32081\\Documents\\python\\mac_violation\\mac_xx.py");
            fileOut.write(inputStr.getBytes());
            fileOut.close();
        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }
}
