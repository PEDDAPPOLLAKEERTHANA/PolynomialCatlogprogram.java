import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolynomialConstant {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the JSON input:");

        StringBuilder jsonBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            jsonBuilder.append(scanner.nextLine());
        }
        scanner.close();

        String json = jsonBuilder.toString();
        int n = extractInt(json, "\"n\":");
        int k = extractInt(json, "\"k\":");

        List<Point> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            String baseStr = extractString(json, "\"" + i + "\":{\"base\":\"", "\"");
            String valueStr = extractString(json, "\"" + i + "\":{\"base\":\"" + baseStr + "\",\"value\":\"", "\"");
            int base = Integer.parseInt(baseStr);
            int decodedY = decodeValue(valueStr, base);
            points.add(new Point(i, decodedY));
        }

        int constantTermC = lagrangeInterpolation(points.subList(0, k), 0);
        System.out.println("The constant term c is: " + constantTermC);
    }

    private static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    private static int lagrangeInterpolation(List<Point> points, int x0) {
        double total = 0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Point point = points.get(i);
            double xi = point.x;
            double yi = point.y;
            double li = 1;

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    li *= (x0 - points.get(j).x) / (xi - points.get(j).x);
                }
            }
            total += li * yi;
        }
        return (int) Math.round(total);
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static int extractInt(String json, String key) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(",", startIndex);
        return Integer.parseInt(json.substring(startIndex, endIndex).trim());
    }

    private static String extractString(String json, String startKey, String endKey) {
        int startIndex = json.indexOf(startKey) + startKey.length();
        int endIndex = json.indexOf(endKey, startIndex);
        return json.substring(startIndex, endIndex).trim();
    }
}
