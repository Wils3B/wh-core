package org.wh.materials.core;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
  /**
   * La sortie normale
   */
  private static PrintStream sortieNormale = System.out;
  private static PrintStream sortieErreurs = System.err;
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

  public static void writeLog(String text) {
    String dt = dateFormat.format(new Date());
    sortieNormale.println(dt + " : " + text);
    sortieNormale.flush();
  }

  public static void writeError(String text) {
    String dt = dateFormat.format(new Date());
    sortieErreurs.println(dt + " : " + text);
    sortieErreurs.flush();
  }

  public static void setNormalOutput(PrintStream sortie) {
    if (sortie == null) Log.sortieNormale = System.out;
    else Log.sortieNormale = sortie;
  }

  public static void setErrorsOutput(PrintStream sortie) {
    if (sortie == null) Log.sortieErreurs = System.out;
    else Log.sortieErreurs = sortie;
  }

  public static void close() {
    if (sortieNormale != System.out)
      sortieNormale.close();
    if (sortieErreurs != null)
      sortieErreurs.close();
  }
}
