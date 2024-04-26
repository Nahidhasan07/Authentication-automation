
import com.jcraft.jsch.JSchException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nahid
 */
public class Control {

    /**
     * @param args the command line arguments
     * @throws com.jcraft.jsch.JSchException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws JSchException, IOException {
        // TODO code application logic here
        Set<String> all_switch = router();
        All_Switch(all_switch);
        System.out.println("done_0");
    }

    public static void All_Switch(Set<String> all_switch) throws JSchException, IOException {
        Map<String, String> map = new HashMap<>();
        List<String> telnetList = new LinkedList<>();
//        System.out.println(all_switch);
        String command1 = "show log";
        String command2 = "show interfaces description | in ";
        command1 = command1.trim();
        command2 = command2.trim();
        int count = all_switch.size();
        Iterator switch_ip = all_switch.iterator();
        String host;
        level_2:
        while (switch_ip.hasNext()) {
            host = (String) switch_ip.next();
            host = host.trim();
            String result = "";
//            if (host.contains("11") || host.contains("12")) {
//                continue;
//            }
//            System.out.println(host);
            try {
                result = command(host, command1);
//                System.out.println(result);
            } catch (Exception e) {
                if (count != 0) {
                    telnetList.add(host);
                    System.out.println("telnet");
                    continue level_2;
                }
            }
            count--;
//            System.out.println(result + "\n\n");
            Violation violation = new Violation(result);
            map = violation.getMap();
            System.out.println(map);
            map = map_update_uplink(host, map);
            System.out.println(map);
            if (!map.isEmpty()) {
                ///do something in switch_command
                switch_command swc = new switch_command(map, host);
                System.out.println("done_1");
            }
        }
    }

    public static Map<String, String> map_update_uplink(String host, Map<String, String> map) throws JSchException, FileNotFoundException {
        String des = command(host, "show int description");
        List<String> list = new LinkedList<>();
//        File file = new File("des.txt");
        Scanner scanner = new Scanner(des);
        int f = 0, count = 0;
        String line = "";
        StringTokenizer st;
        String interfaceString = "";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            st = new StringTokenizer(line);
            if ((count != 0) && (st.countTokens() == 4)) {
                list.add(st.nextToken());
                count++;
                continue;
            }
            count++;
        }
//        System.out.println(list);
        String currport, s_currport, keyString;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            keyString = entry.getKey().trim();
            currport = entry.getValue().trim();
            s_currport = short_form(currport);
            if (list.contains(s_currport)) {
                map.remove(keyString, currport);
            }
        }
        return map;
    }

    public static Set<String> router() throws JSchException, IOException {
        String x = "10.24.33.";
        String host = x + "1";
        host = host.trim();
//        String command = "show arp | in " + x + "[11-20]";
        String command = "show arp";
        command = command.trim();
        String result = "";
        try {
            result = command(host, command);
        } catch (Exception e) {
            host = x + "254";
            host = host.trim();
            result = command(host, command);
        }
//        System.out.println(result);
        Switch_filter switch_filter = new Switch_filter(host, result);
        Set<String> all_switch = switch_filter.getSwitch();
        System.out.println(all_switch);
        return all_switch;
    }

    public static String command(String host, String command) throws JSchException {
        execution ex = new execution(host);
        ex.command(command);
        String result = ex.getResult();
        ex.disconnect();
        return result;
    }

    public static String short_form(String name) {
        String newName = "", newName1 = "";;
        for (int a = 0; a < name.length(); a++) {
            newName += name.charAt(a);
            if (a == 1) {
                break;
            }
        }
        int count = 0;
        for (int a = name.length() - 1; a >= 0; a--) {
            newName1 += name.charAt(a);
            if (count == 2) {
                break;
            }
            count++;
        }
        for (int a = newName1.length() - 1; a >= 0; a--) {
            newName += newName1.charAt(a);
        }
        return newName;
    }
}
