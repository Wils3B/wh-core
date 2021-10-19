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

import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * @author Wilson
 */
public class VueUser extends ToggleButton {
  private final SimpleUser user;

  public VueUser(SimpleUser user) {
    this.user = user;
    this.setText(user.getNom());
    ImageView img = new ImageView(user.getProfil());
    img.setPreserveRatio(true);
    img.setSmooth(true);
    img.setFitHeight(60);
    img.setFitWidth(60);
    VBox vb = new VBox(img);
    vb.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    vb.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    vb.getStyleClass().add("profil-small");
    this.setGraphic(vb);
    this.setGraphicTextGap(10);
    this.getStyleClass().add("WinUserBtn");
  }

  public SimpleUser getUser() {
    return user;
  }
}
