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

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.wh.materials.core.controllers.LoginController;

import java.io.IOException;

/**
 * @author Wilson
 */
public abstract class Login extends StackPane implements AutoCloseable {
  protected LoginController myController;
  protected Stage stage;

  public Login(Stage stage, SimpleUser first, SimpleUser... others) throws IOException {
    //Chargement de la vue de login
    FXMLLoader loader = new FXMLLoader(Login.class.getResource("views/loginView.fxml"));
    StackPane vue = loader.load();
    myController = loader.getController();
    this.getChildren().setAll(vue);
    StackPane.setAlignment(vue, Pos.TOP_LEFT);
    myController.setLogin(this);
    myController.getVueUsers().add(new VueUser(first));
    for (SimpleUser usr : others) {
      myController.getVueUsers().add(new VueUser(usr));
    }

    this.stage = stage;
    stage.setFullScreenExitHint("");
  }

  public abstract boolean verifPass(SimpleUser user);

  @Override
  public abstract void close();

  public PasswordField getPasswordField() {
    return myController.getMyAvatarController().getPassField();
  }

  public void updateScene() {
    myController.setScene(stage.getScene());
  }

  public SimpleUser getConnectedUser() {
    return myController.getSelectedUser();
  }

  public LoginController getController() {
    return myController;
  }

  public void fullScreen() {
    stage.setFullScreen(!stage.isFullScreen());
  }

  public void setImagesLocation(String base) {
    myController.setImagesLocation(base);
  }

  public Stage getStage() {
    return stage;
  }
}
