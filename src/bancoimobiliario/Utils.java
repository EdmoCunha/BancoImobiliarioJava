package bancoimobiliario;

import java.util.Random;
import java.util.Scanner;

public class Utils {

    public static int lerInt() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (Exception e) { System.out.print("Inválido, tente de novo: "); }
        }
    }

    public static double lerDouble() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try { return Double.parseDouble(scanner.nextLine().replace(",", ".").trim()); }
            catch (Exception e) { System.out.print("Inválido, tente de novo: "); }
        }
    }

    public static int rolarDado() {
        return 1 + new Random().nextInt(6);
    }
}
