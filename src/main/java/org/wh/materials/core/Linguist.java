package org.wh.materials.core;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by W3B on 22/09/2018.
 */
public class Linguist {
  private ResourceBundle bundle;
  private String path;

  /**
   * Constructor of class.
   *
   * @param path   the path of ressource property which have translations.
   * @param locale The locale wanted.
   */
  public Linguist(String path, Locale locale) {
    this.path = path;
    this.bundle = ResourceBundle.getBundle(path, locale);
  }

  /**
   * Linguist translator method.
   *
   * @param key The key of word (or expression) expected.
   * @return The word (or expression) expected, referenced by key, or null if key is not recognize.
   */
  public String tr(String key) {
    String chaine;
    try {
      chaine = bundle.getString(key);
    } catch (MissingResourceException e) {
      chaine = key;
      Log.writeError("Exception dans la méthode tr de la classe Linguist : \n" + e.getMessage());
    }
    return chaine;
  }

  /**
   * Setter of locale.
   *
   * @param locale the new locale.
   */
  public void setLocale(Locale locale) {
    bundle = ResourceBundle.getBundle(path, locale);
  }
}
