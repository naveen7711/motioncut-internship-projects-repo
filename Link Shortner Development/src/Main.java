import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UrlShortener urlShortener = new UrlShortener("http://short.url/");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Shorten URL");
            System.out.println("2. Expand URL");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter long URL:");
                    String longUrl = scanner.nextLine();
                    String shortUrl = urlShortener.shortenUrl(longUrl);
                    System.out.println("Shortened URL: " + shortUrl);
                    break;
                case 2:
                    System.out.println("Enter short URL:");
                    String shortUrlToExpand = scanner.nextLine();
                    try {
                        String expandedUrl = urlShortener.expandUrl(shortUrlToExpand);
                        System.out.println("Expanded URL: " + expandedUrl);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
