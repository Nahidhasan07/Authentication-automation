
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
public class Violation {

    Map<String, String> map;

    public Violation(String sw_log) throws FileNotFoundException, IOException {
        vio_dec(sw_log);
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void vio_dec(String sw_log) throws FileNotFoundException, IOException {
        Map<String, String> map = new HashMap<>();
//        File file = new File("log.txt");
        Scanner scanner = new Scanner(sw_log);
        String line = "";
        StringTokenizer st;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.contains("vty0")) {
                map.clear();
            }
            if (line.contains("Security violation occurred")) {
                st = new StringTokenizer(line, " ");
//                System.out.print(line + "  ");
                int count = 0;
                String mac = "", port = "";
                while (st.hasMoreTokens()) {
                    if (count == 12) {
//                        System.out.println("number " + count + " " + st.nextToken());
                        mac = st.nextToken();
                    }
                    if (count == 14) {
                        port = st.nextToken();
                        port = port.replace(".", "");
//                        System.out.println("number " + count + " " + st.nextToken());
                        continue;
                    }
                    st.nextToken();
                    count++;
                }
                map.put(mac, port);
            }
        }
//        System.out.println(map);
        this.map = map;
    }
}
