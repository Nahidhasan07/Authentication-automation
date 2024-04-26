
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
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
 * @author nahid32081
 */
public class Switch_filter {

    String network;
    Set<String> all_switch;

    public Switch_filter(String host, String arp_table) throws FileNotFoundException, IOException {
        Network(host);
        Set<String> all_switch = Filter(network, arp_table);
        this.all_switch = all_switch;
    }

    public Set<String> getSwitch() {
        return all_switch;
    }

    public Set<String> Filter(String ip_block, String arp_table) throws FileNotFoundException, IOException {
//        System.out.println("switch_filter");
        Set<String> all_sw_number = switch_number(ip_block);
//        System.out.println(all_sw_number);
        Set<String> all_switch = new HashSet<String>();
//        File file = new File("arp table.txt");
        Scanner scanner = new Scanner(arp_table);
        String line = "";
        StringTokenizer st;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            st = new StringTokenizer(line);
            int count = 0;
            while (st.hasMoreTokens()) {
                if (count == 1) {
                    String x = st.nextToken();
//                    all_switch.add(x);
//                    System.out.print(count + " " + x + "\n");
                    if (all_sw_number.contains(x)) {
                        all_switch.add(x);
//                        System.out.print(x + "\n");
                    }
                }
                if (count == 5) {
                    st.nextToken();
                    continue;
                }
                st.nextToken();
                count++;
            }
        }
//        System.out.print(all_switch + "\n");
        return all_switch;
    }

    public Set switch_number(String ip_block) {
        Set<String> set = new HashSet<String>();
        for (int a = 11; a < 21; a++) {
            set.add(ip_block + a);
        }
//        System.out.println(set);
        return set;
    }

    public void Network(String IP) {
        StringTokenizer st = new StringTokenizer(IP, ".");
        int c = 0;
        String network = "";
        while (st.hasMoreTokens()) {
            if (c == 3) {
                break;
            }
            network += st.nextToken() + ".";
            c++;
        }
        this.network = network;
//        System.out.println(this.network);
    }
}
