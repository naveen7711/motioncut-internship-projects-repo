import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.*;
import java.util.Map;

public class UrlShortener {
    private Map<String, String> shortToLongMap;
    private Map<String, String> longToShortMap;
    private String domain;
    private Random random;
    

    public void saveMappings(String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(shortToLongMap);
            out.writeObject(longToShortMap);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadMappings(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            shortToLongMap = (Map<String, String>) in.readObject();
            longToShortMap = (Map<String, String>) in.readObject();
        }
    }


    public UrlShortener(String domain) {
        this.shortToLongMap = new HashMap<>();
        this.longToShortMap = new HashMap<>();
        this.domain = domain;
        this.random = new Random();
    }

    public String shortenUrl(String longUrl) {
        if (longToShortMap.containsKey(longUrl)) {
            return longToShortMap.get(longUrl);
        }

        String shortUrl = generateShortUrl();
        while (shortToLongMap.containsKey(shortUrl)) {
            shortUrl = generateShortUrl();
        }

        shortToLongMap.put(shortUrl, longUrl);
        longToShortMap.put(longUrl, shortUrl);
        return shortUrl;
    }

    public String expandUrl(String shortUrl) throws Exception {
        if (!shortToLongMap.containsKey(shortUrl)) {
            throw new Exception("Invalid short URL");
        }
        return shortToLongMap.get(shortUrl);
    }

    private String generateShortUrl() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder(domain);

        for (int i = 0; i < 6; i++) {
            shortUrl.append(chars.charAt(random.nextInt(chars.length())));
        }
        return shortUrl.toString();
    }
    
}
