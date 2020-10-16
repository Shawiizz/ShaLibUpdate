package fr.shawiizz.shalibupdate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static fr.shawiizz.shalibupdate.UpdateUtils.*;

public class ShaLibUpdate {
    public static boolean HIDEMESSAGES = false;
    public static boolean SHOWMESSAGES = true;
    public static int filesToDownload = 0;
    public static int downloadedFiles = 0;
    public static int downloadFailed = 0;
    public static int percentage = 0;
    public static boolean updateStatus = false;
    public static boolean checkStatus = false;
    public static String link = "";
    public static String path = "";
    static Boolean log = false;
    static Boolean checkFiles = true;
    static HashMap<String, File> filesToDl = new HashMap<>();
    static ArrayList<String> filesToCheck = new ArrayList<>();

    public ShaLibUpdate(String link, String path, boolean log) {
        ShaLibUpdate.link = link;
        ShaLibUpdate.path = path;
        ShaLibUpdate.log = log;
    }

    public ShaLibUpdate(String link, String path, boolean checkFiles, boolean type) {
        new ShaLibUpdate(link, path, type);
        ShaLibUpdate.checkFiles = checkFiles;
    }

    private static void indexFiles() {
        for (String l : getPageContent(link + "/files/index.php")) {
            String v = l.replace("./", "");
            String[] args = v.split("\\|");
            if(args.length == 2) {
                File file = new File(path + "/" + args[0]);
                filesToCheck.add(args[1]);
                if((!file.exists() || !getMd5(file).equals(args[1])) && !notContains(getPageContent(link + "/IgnoreDownload.cfg"), args[0]))
                    filesToDl.put(link + "/files/" + args[0], file);
            }
        }
    }

    public static void checkFiles() throws IOException {
        log("[ShaLibUpdate] - Download process finished.");

        ArrayList<String> dontdelete = new ArrayList<>(getPageContent(link + "/IgnoreDelete.cfg"));

        log("[ShaLibUpdate] - Checking not allowed files...");
        File f = new File(path);
        try (Stream<Path> paths = Files.walk(f.toPath())) {
            paths.forEach(filePath -> {
                if(Files.isRegularFile(filePath)) {
                    String filePathName = filePath.toString().replace(f.toString(), "").replaceFirst("\\\\", "").replaceAll("\\\\", "/");
                    if(!dontdelete.contains(filePathName)) {
                        if(!filesToCheck.contains(getMd5(filePath.toFile())) && !notContains(dontdelete, filePathName)) {
                            filePath.toFile().delete();
                            log("[ShaLibUpdate] - Deleting " + filePath.toFile().getName());
                        }
                    }
                }
            });
        }
    }

    public void startUpdater() {
        log("   _____   _               _        _   _       _    _               _           _          \n" +
                "  / ____| | |             | |      (_) | |     | |  | |             | |         | |         \n" +
                " | (___   | |__     __ _  | |       _  | |__   | |  | |  _ __     __| |   __ _  | |_    ___ \n" +
                "  \\___ \\  | '_ \\   / _` | | |      | | | '_ \\  | |  | | | '_ \\   / _` |  / _` | | __|  / _ \\\n" +
                "  ____) | | | | | | (_| | | |____  | | | |_) | | |__| | | |_) | | (_| | | (_| | | |_  |  __/\n" +
                " |_____/  |_| |_|  \\__,_| |______| |_| |_.__/   \\____/  | .__/   \\__,_|  \\__,_|  \\__|  \\___|\n" +
                "                                                        | |                                 \n" +
                "                                                        |_|                                 ");
        log("[ShaLibUpdate] - Indexing files");
        try {
            indexFiles();
        } catch (Exception e) {
            System.out.println("[ShaLibUpdate] [ERROR] - " + e.getMessage());
        }
        filesToDownload = filesToDl.size();
        log("[ShaLibUpdate] - " + filesToDownload + " files are required to download.");
        updateStatus = true;
        for (String i : filesToDl.keySet()) dl(i, filesToDl.get(i));
        updateStatus = false;
        percentage = 100;
        checkStatus = true;
        try {
            if(checkFiles) checkFiles();
        } catch (IOException e) {
            System.out.println("[ShaLibUpdate] [ERROR] - " + e.getMessage());
        }
        checkStatus = false;
    }
}
