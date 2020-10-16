package fr.shawiizz.shalibupdate;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UpdateUtils {
    static Boolean notContains(ArrayList<String> list, String str) {
        boolean contains = false;
        for (String s : list)
            if(s.endsWith("/") && str.contains(s)) {
                contains = true;
                break;
            } else if(!s.endsWith("/") && str.equals(s)) {
                contains = true;
                break;
            }
        return contains;
    }

    public static String getMd5(File path) {
        try {
            return DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(path.toPath()))).toLowerCase();
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println("[ShaLibUpdate] [ERROR] - " + e.getMessage());
        }
        return "md5notfound";
    }

    public static void dl(String link, File path) {
        try {
            path.getParentFile().mkdirs();
            path.createNewFile();
            log("[ShaLibUpdate] Downloading " + path.getName());
            FileChannel.open(Paths.get(path.getAbsolutePath()), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING).transferFrom(Channels.newChannel(getConn(link).getInputStream()), 0L, Long.MAX_VALUE);
            ShaLibUpdate.downloadedFiles++;
            ShaLibUpdate.percentage = Math.round((float) ShaLibUpdate.downloadedFiles / ShaLibUpdate.filesToDownload * 100);
        } catch (IOException e) {
            System.out.println("[ShaLibUpdate] [ERROR] - " + e.getMessage());
            ShaLibUpdate.downloadFailed++;
            log("[ShaLibUpdate] - The file " + path.getName() + " failed to download | Link : " + link + "\nThe error : \n" + e.toString());
        }
    }

    public static ArrayList<String> getPageContent(String url) {
        ArrayList<String> content = new ArrayList<>();
        try {
            BufferedReader IgnoreDownloadFile = new BufferedReader(new InputStreamReader(getConn(url).getInputStream()));
            String line;
            while ((line = IgnoreDownloadFile.readLine()) != null) content.add(line);
        } catch (Exception e) {
            System.out.println("[ShaLibUpdate] [ERROR] - Unable to read " + url + "\nPlease check that this file exists.");
            System.exit(0);
        }
        return content;
    }

    public static URLConnection getConn(String URL) {
        URLConnection conn = null;
        try {
            conn = new URL(URL).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
            conn.setUseCaches(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void log(String message) {
        if(ShaLibUpdate.log)
            System.out.println(message);
    }
}
