package org.wh.materials.core;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public abstract class Settings implements Serializable {

  private boolean m_loaded;
  private HashMap<String, Object> m_values;

  public Settings() {
    m_values = null;
  }

  /**
   * Loads the settings from the settings file by organisation name and the application name.
   *
   * @return <code>true</code> if the settings are correctly loaded, <code>false</code> else.
   */
  public boolean loadSettings() {
    m_loaded = true;
    String path = CoreApplication.getSettingsPath();
    File file = new File(path);
    if (!file.exists()) {
      return false;
    }
    try {
      FileInputStream fichierIn = new FileInputStream(path);
      try (ObjectInputStream in = new ObjectInputStream(fichierIn)) {
        m_values = (HashMap<String, Object>) in.readObject();
        if (m_values == null) {
          m_values = new HashMap<>();
        }
      }
      Log.writeLog("Settings are loaded!");
      return true;
    } catch (FileNotFoundException e) {
      Log.writeError("Erreur de presence de fichier lors du chargement des settings : " + e.getMessage());
    } catch (IOException e) {
      Log.writeError("Erreur I/O lors du chargement des settings : " + e.getMessage());
    } catch (ClassNotFoundException e) {
      Log.writeError("Erreur Class Not Found lors du chargement des settings : " + e.getMessage());
    }
    return false;
  }

  /**
   * Store settings on a file.
   */
  public void storeSettings() {
    String path = CoreApplication.getSettingsPath();
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
    try {
      file.createNewFile();
      FileOutputStream fichierOut = new FileOutputStream(path);
      try (ObjectOutputStream out = new ObjectOutputStream(fichierOut)) {
        out.writeObject(m_values);
        out.flush();
      }
      Log.writeLog("Settings are stored!");
    } catch (FileNotFoundException e) {
      Log.writeError("Erreur de presence de fichier lors de l'ecriture des settings : " + e.getMessage());
    } catch (IOException e) {
      Log.writeError("Erreur de création du fichier de settings : " + e.getMessage());
    }
  }

  /**
   * Add a setting to the list of settings if this one did not exists or replace them otherwise.
   *
   * @param settingName  The name of setting.
   * @param settingValue The setting value.
   */
  public void insertSetting(String settingName, Object settingValue) {
    m_values.put(settingName, settingValue);
  }

  /**
   * Read the value of a setting.
   *
   * @param settingName The name of asked setting.
   * @return The value of asked setting if they are define before, or <code>null</code>.
   */
  public Object getSetting(String settingName) {
    if (m_values == null) {
      m_values = new HashMap<>();
    }
    Object settingVal = m_values.get(settingName);
    if (settingVal == null) {
      return defaultSetting(settingName);
    } else {
      return settingVal;
    }
  }

  /**
   * The default value of a setting,
   * This value is returned by the getSetting function if the setting are not set up before but he are defined here.
   *
   * @param settingName The name of setting.
   * @return The default value of a setting referenced by his name.
   */
  protected abstract Object defaultSetting(String settingName);

  public void restoreSettings() {
    m_values.clear();
    storeSettings();
  }

  public void resetSetting(String settingName) {
    this.insertSetting(settingName, this.defaultSetting(settingName));
  }

  /**
   * Describe the settings.
   */
  public void describe() {
    if (m_values == null) {
      Log.writeLog("Settings are NULL");
    } else {
      Set<String> keys = m_values.keySet();
      Log.writeLog("---------------------------------------");
      Log.writeLog("-------Description des settings--------");
      Log.writeLog("---------------------------------------");
      keys.forEach((a) -> {
        Log.writeLog(a + " = " + m_values.get(a));
      });
      Log.writeLog("---------------------------------------");
    }
  }
}
