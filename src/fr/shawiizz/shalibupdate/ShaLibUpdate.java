package fr.shawiizz.shalibupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static fr.shawiizz.shalibupdate.UpdateUtils.*;

public class ShaLibUpdate {
  public static int filesToDownload = 0;
  public static int downloadedFiles = 0;
  public static int downloadFailed = 0;
  public static int percentage = 0;
  public static boolean updateStatus = false;
  public static boolean checkStatus = false;
  public static String link = "";
  public static String path = "";
  static Boolean log = false;
  static HashMap<String, File> filesToDl = new HashMap<>();
  static ArrayList<String> filesToCheck = new ArrayList<>();

  public ShaLibUpdate(String link, String path, ShaLogger type) {
    ShaLibUpdate.link = link;
    ShaLibUpdate.path = path;
    log = !type.equals(ShaLogger.HIDEMESSAGES);
  }

  private static void indexFiles() throws Exception {
    String ignorestr;
    ArrayList<String> ignoreFileList = new ArrayList<>();
    ArrayList<String> ignoreFolderList = new ArrayList<>();
    BufferedReader IgnoreDownloadFile = null;
    try {
      IgnoreDownloadFile = new BufferedReader(new InputStreamReader(new URL(link + "/IgnoreDownload.cfg").openStream()));
    } catch (Exception e) {
      System.out.println("[ShaLibUpdate] [ERROR] - You need to create the file IgnoreDownload.cfg at " + link + "/IgnoreDownload.cfg");
      System.exit(0);
    }
    while ((ignorestr = IgnoreDownloadFile.readLine()) != null) {
      if(ignorestr.endsWith("/"))
        ignoreFolderList.add(ignorestr);
      else
        ignoreFileList.add(ignorestr);
    }

    String str;
    BufferedReader files = new BufferedReader(new InputStreamReader(new URL(link + "/files/index.php").openStream()));
    while ((str = files.readLine()) != null) {
      String[] args = str.split("\\|");
      if(args.length == 2) {
        File file = new File(path + "/" + args[0]);
        filesToCheck.add(args[1]);
        if((!file.exists() || !getMd5(file).equals(args[1])) && !ignoreFileList.contains(args[0]) && !notContains(ignoreFolderList, args[0]))
          filesToDl.put(link + "/files/" + args[0], file);
      }
    }
  }

  public static void checkFiles() throws IOException {
    log("[ShaLibUpdate] - Download process finished.");

    ArrayList<String> dontdelete = new ArrayList<>();
    String ignorestr;
    BufferedReader ignore = null;
    try {
      ignore = new BufferedReader(new InputStreamReader(new URL(link + "/IgnoreDelete.cfg").openStream()));
    } catch (Exception e) {
      System.out.println("[ShaLibUpdate] [ERROR] - You need to create the file IgnoreDelete.cfg at " + link + "/IgnoreDelete.cfg");
      System.exit(0);
    }
    while ((ignorestr = ignore.readLine()) != null)
      dontdelete.add(ignorestr);

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
      e.printStackTrace();
    }
    filesToDownload = filesToDl.size();
    log("[ShaLibUpdate] - " + filesToDownload + " files are required to download.");
    updateStatus = true;
    for (String i : filesToDl.keySet()) dl(i, filesToDl.get(i));
    updateStatus = false;
    percentage = 100;
    checkStatus = true;
    try {
      checkFiles();
    } catch (IOException e) {
      e.printStackTrace();
    }
    checkStatus = false;
  }
}
