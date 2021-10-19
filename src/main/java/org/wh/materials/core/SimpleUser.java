/*
 * Copyright (C) 2019 Wilson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wh.materials.core;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * @author Wilson
 */
public class SimpleUser {

  private final Image profil;
  private final String nom;

  public SimpleUser(Image profil, String nom) {
    if (profil == null) {
      profil = new Image(getClass().getResourceAsStream("images/avatar222.png"));
    }
    int w = (int) profil.getWidth(), h = (int) profil.getHeight();
    w = w % 2 == 0 ? w : w - 1;
    h = h % 2 == 0 ? h : h - 1;
    int wh = Math.min(w, h), r = wh / 2, cx = w / 2, cy = h / 2;
    r *= r;
    if (w == wh) {
      w = 0;
      h = (h - wh) / 2;
    } else {
      h = 0;
      w = (w - wh) / 2;
    }
    this.profil = new WritableImage(wh, wh);
    PixelReader reader = profil.getPixelReader();
    PixelWriter writer = ((WritableImage) this.profil).getPixelWriter();
    for (int x = 0; x < wh; x++, w++) {
      for (int y = 0, y2 = h; y < wh; y++, y2++) {
        if ((y2 - cy) * (y2 - cy) + (w - cx) * (w - cx) <= r) {
          writer.setColor(x, y, reader.getColor(w, y2));
        } else {
          writer.setColor(x, y, Color.TRANSPARENT);
        }
      }
    }
    this.nom = nom;
  }

  public Image getProfil() {
    return profil;
  }

  public String getNom() {
    return nom;
  }

}
