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
package org.wh.materials.core.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Wilson
 */
public class AvatarController implements Initializable {

  @FXML
  private ImageView avatar;

  @FXML
  private Label name;

  @FXML
  private PasswordField passField;

  @FXML
  private HBox advancePassField;

  private LoginController loginController;

  /**
   * Initializes the controller class.
   *
   * @param url l'url.
   * @param rb  la bundle.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

  }

  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
  }

  public ImageView getAvatar() {
    return avatar;
  }

  public Label getName() {
    return name;
  }

  public PasswordField getPassField() {
    return passField;
  }

  public HBox getAdvancePassField() {
    return advancePassField;
  }

  @FXML
  void connect() {
    System.out.println("CONNECT");
    loginController.tryConnect();
  }
}
