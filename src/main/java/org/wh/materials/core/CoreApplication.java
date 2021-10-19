package org.wh.materials.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CoreApplication {

  private static String applicationName;
  private static String applicationVersion;
  private static String organizationDomain;
  private static String organizationName;

  public static String getApplicationName() {
    return applicationName;
  }

  public static void setApplicationName(String applicationName) {
    CoreApplication.applicationName = applicationName;
  }

  public static String getApplicationVersion() {
    return applicationVersion;
  }

  public static void setApplicationVersion(String applicationVersion) {
    CoreApplication.applicationVersion = applicationVersion;
  }

  public static String getOrganizationDomain() {
    return organizationDomain;
  }

  public static void setOrganizationDomain(String organizationDomain) {
    CoreApplication.organizationDomain = organizationDomain;
  }

  public static String getOrganizationName() {
    return organizationName;
  }

  public static void setOrganizationName(String organizationName) {
    CoreApplication.organizationName = organizationName;
  }

  public static String getBasePath() {
    return System.getProperty("user.home") + "/" + organizationName + "/" + applicationName;
  }

  /**
   * Accesseur du fichier journal principal.
   *
   * @return Le fichier journal principal.
   */
  public static String getLogPath() {
    return getBasePath() + "/logs/log.txt";
  }

  /**
   * Accesseur du fichier journal d'erreurs.
   *
   * @return Le fichier journal d'erreurs.
   */
  public static String getErrLogPath() {
    return getBasePath() + "/logs/logErr.txt";
  }

  /**
   * Accesseur du fichier temporaire du journal principal.
   *
   * @return Le fichier temporaire du journal principal.
   */
  public static String getTempLogPath() {
    return getBasePath() + "/logs/logTemp.txt";
  }

  /**
   * Accesseur du fichier temporaire du journal d'erreurs.
   *
   * @return Le fichier temporaire du journal d'erreurs.
   */
  public static String getTempErrLogPath() {
    return getBasePath() + "/logs/logErrTemp.txt";
  }

  /**
   * Accesseur du fichier de paramètres.
   *
   * @return le chamin d'accès du fichier de paramètres.
   */
  public static String getSettingsPath() {
    return getBasePath() + "/settings.whset";
  }

  /**
   * Crée tous les répertoires nécessaire pour les stockages secondaires de
   * l'application.
   */
  public static void createAllDirs() {
    File path = new File(getBasePath() + "/logs");
    if (!path.exists()) {
      path.mkdirs();
    }
    path = new File(getBasePath() + "/bdd");
    if (!path.exists()) {
      path.mkdirs();
    }
  }

  /**
   * Join logs temporary files with the min logs files.
   *
   * @throws java.io.IOException Si les chemins ne sont pas correctement crées.
   */
  public static void joinLogs() throws IOException {
    //normal et normal temp.
    Path fromPath = Paths.get(CoreApplication.getTempLogPath());
    Path toPath = Paths.get(CoreApplication.getLogPath());
    Charset utf8 = Charset.forName("UTF-8");
    if (Files.notExists(toPath)) Files.createFile(toPath);

    try (BufferedReader reader = Files.newBufferedReader(fromPath, utf8);
         BufferedWriter writer = Files.newBufferedWriter(toPath, utf8, StandardOpenOption.APPEND)) {
      String line;
      while ((line = reader.readLine()) != null) {
        writer.write(line + "\n");
      }
    } catch (IOException ex) {
      Log.writeError("Erreur lors de la fusion du fichier de journal principal : " + ex.getMessage());
      System.err.println("Erreur lors de la fusion du fichier de journal principal : " + ex.getMessage());
    }

    //erreurs et errues temp.
    fromPath = Paths.get(CoreApplication.getTempErrLogPath());
    toPath = Paths.get(CoreApplication.getErrLogPath());
    if (Files.notExists(toPath)) Files.createFile(toPath);

    try (BufferedReader reader = Files.newBufferedReader(fromPath, utf8);
         BufferedWriter writer = Files.newBufferedWriter(toPath, utf8, StandardOpenOption.APPEND)) {
      String line;
      while ((line = reader.readLine()) != null) {
        writer.write(line + "\n");
      }
    } catch (IOException ex) {
      Log.writeError("Erreur lors de la fusion du fichier de journal d'erreurs : " + ex.getMessage());
      System.err.println("Erreur lors de la fusion du fichier de journal d'erreurs : " + ex.getMessage());
    }
  }

  public static void deleteLogs() {
    File file = new File(CoreApplication.getLogPath());
    file.deleteOnExit();
    file = new File(CoreApplication.getErrLogPath());
    file.deleteOnExit();
  }
}
