package org.wh.materials.core.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.wh.materials.core.Login;
import org.wh.materials.core.SimpleUser;
import org.wh.materials.core.TimeDisplayer;
import org.wh.materials.core.VueUser;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

  @FXML
  private StackPane rootStack;

  @FXML
  private ImageView fond;

  @FXML
  private VBox centralBox;

  @FXML
  private Label title;

  @FXML
  private VBox swBox;

  @FXML
  private HBox neBox;

  @FXML
  private Button nextImage;

  @FXML
  private HBox seBox;

  @FXML
  private Button shutdown;

  @FXML
  private ScrollPane swScrll;

  @FXML
  private HBox titleBox;

  @FXML
  private Button fullscreen;

  private int state;
  private int nbreImages;
  private TimeDisplayer td;
  private AvatarController myAvatarController;
  private VBox avatarBox;
  private ObservableList<VueUser> vueUsers;
  private WaiterService<Void> waiter;
  private Login login;
  private ToggleGroup group;
  private VBox messageFailled;
  private ConnectFailledController connectFailledController;
  private Scene scene;
  private String imagesLocation;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    state = 1;
    nbreImages = 24;

    td = new TimeDisplayer();

    //Affichage de la première fenêtre
    displayFirst();
    nextImage();

    //Chargement de la vue avatar
    try {
      FXMLLoader loader = new FXMLLoader(Login.class.getResource("views/avatarView.fxml"));
      avatarBox = loader.load();
      myAvatarController = loader.getController();
      myAvatarController.setLoginController(this);
    } catch (IOException ex) {
      System.err.println("Erreur de chargement de la vue avatar : " + ex.getMessage());
    }

    //Chargement de la vue connect failed
    try {
      FXMLLoader loader = new FXMLLoader(Login.class.getResource("views/connectFailledView.fxml"));
      messageFailled = loader.load();
      connectFailledController = loader.getController();
    } catch (IOException ex) {
      System.err.println("Erreur de chargement de la vue connectFailled : " + ex.getMessage());
    }

    connectFailledController.getOkBtn().setOnMouseClicked(e -> {
      displaySecond();
    });

    vueUsers = FXCollections.observableArrayList();

    waiter = new WaiterService<>();
    waiter.setOnSucceeded(e -> {
      displayFirst();
    });

    imagesLocation = "/images/img";

  }

  private void resizeBackground() {
    double imgW = fond.getImage().getWidth();
    double imgH = fond.getImage().getHeight();

    double w = scene != null ? scene.getWidth() : rootStack.getWidth();
    double h = scene != null ? scene.getHeight() : rootStack.getHeight();
    //System.out.println(w+" "+h);

    if (w * imgH > h * imgW) {
      fond.setFitWidth(w);
      fond.setFitHeight(w * imgH / imgW);
    } else {
      fond.setFitHeight(h);
      fond.setFitWidth(h * imgW / imgH);
    }

    double dx = (fond.getFitWidth() - w);
    double dy = (fond.getFitHeight() - h);

    //mise à jour des positions
    swScrll.setTranslateX(0);
    swScrll.setTranslateY(-dy);
    seBox.setTranslateX(-dx);
    seBox.setTranslateY(-dy);
    neBox.setTranslateX(-dx);
    neBox.setTranslateY(0);
    centralBox.setTranslateY(-dy / 2 - (h - centralBox.getHeight()) / 10);

    //Rectangle2D viewport = new Rectangle2D(0, 0, w, h);
    //fond.setViewport(viewport);
  }

  public void restartThreads() {
    td.start();
    startNewWaiter();
  }

  @FXML
  public void nextImage() {
    Random r = new Random();
    int select = r.nextInt(nbreImages) + 1;
    rootStack.setStyle("-fx-background-color: rgb(80,80,80);");
    FadeTransition f = new FadeTransition(Duration.seconds(0.5), fond);
    double df = state == 1 ? 0 : 0.5, dt = state == 1 ? 0 : 0.3;
    f.setFromValue(1 - df);
    f.setToValue(0.5 - dt);
    f.play();
    f.setOnFinished(e -> {
      fond.setImage(new Image(imagesLocation + select + ".jpg"));
      resizeBackground();
      FadeTransition f2 = new FadeTransition(Duration.seconds(0.5), fond);
      f2.setFromValue(0.5 - dt);
      f2.setToValue(1 - df);
      f2.play();
      rootStack.setStyle("-fx-background-color: rgba(0,0,0,0.9);");
    });

  }

  public void setNbreImages(int nbreImages) {
    this.nbreImages = nbreImages;
  }

  private synchronized void displayFirst() {
    if (state == 2) {
      ParallelTransition pt = upFade(0.3, centralBox, swScrll);
      pt.play();
      pt.setOnFinished(e -> {
        centralBox.getChildren().setAll(titleBox);
        swBox.getChildren().setAll(td);
        centralBox.setOpacity(1);
        centralBox.setTranslateY(0);
        swScrll.setOpacity(1);
        swScrll.setTranslateY(0);
        resizeBackground();
      });
    } else {
      centralBox.getChildren().setAll(titleBox);
      swBox.getChildren().setAll(td);
      resizeBackground();
    }
    state = 1;

    fond.setOpacity(1);

    td.start();

  }

  private synchronized void displaySecond() {

    startNewWaiter();

    if (group == null) {
      group = new ToggleGroup();
      group.getToggles().addAll(vueUsers);
    }
    group.selectedToggleProperty().addListener(this::changed);
    if (group.getSelectedToggle() == null) {
      group.getToggles().get(0).setSelected(true);
    }

    fond.setOpacity(0.5);
    if (state == 1) {
      myAvatarController.getPassField().clear();

      ParallelTransition pt = upFade(0.5, centralBox, swScrll);
      pt.play();

      pt.setOnFinished(e -> {
        swScrll.setOpacity(1);
        swScrll.setTranslateY(0);
        swBox.getChildren().clear();
        swBox.getChildren().addAll(vueUsers);
        centralBox.setOpacity(1);
        centralBox.setTranslateY(0);
        centralBox.getChildren().clear();
        centralBox.getChildren().addAll(avatarBox);
        resizeBackground();
      });
    } else {
      avatarBox.getChildren().set(2, myAvatarController.getAdvancePassField());
    }

  }

  private void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
    if (newValue != null) {
      VueUser v = (VueUser) newValue;
      myAvatarController.getName().setText(v.getUser().getNom());
      myAvatarController.getAvatar().setImage(v.getUser().getProfil());
    } else {
      oldValue.setSelected(true);
    }
  }

  private void startNewWaiter() {
    if (waiter.getState() == State.READY) {
      waiter.start();
    } else {
      waiter.restart();
    }
  }

  private ParallelTransition upFade(double seconds, Node... parties) {
    ParallelTransition pt = new ParallelTransition();
    TranslateTransition[] translates = new TranslateTransition[parties.length];
    FadeTransition[] fades = new FadeTransition[parties.length];
    for (int i = 0; i < parties.length; i++) {
      fades[i] = new FadeTransition(Duration.seconds(seconds), parties[i]);
      fades[i].setInterpolator(Interpolator.EASE_BOTH);
      translates[i] = new TranslateTransition(Duration.seconds(seconds), parties[i]);
      fades[i].setToValue(0.2);
      translates[i].setToY(-rootStack.getHeight());
      translates[i].setInterpolator(Interpolator.EASE_BOTH);
      pt.getChildren().addAll(fades[i], translates[i]);
    }
    return pt;
  }

  private synchronized void displayThird() {
    state = 3;
    avatarBox.getChildren().set(2, messageFailled);
  }

  @FXML
  public void fondClicked() {
    if (state == 1 || state == 3) {
      displaySecond();
      state = 2;
    }
  }

  @FXML
  public void close() {
    login.close();
  }

  public void stopThreads() {
    td.close();
    waiter.cancel();
  }

  public void setMyAvatarController(AvatarController myAvatarController) {
    this.myAvatarController = myAvatarController;
  }

  public void tryConnect() {

    startNewWaiter();

    avatarBox.getChildren().set(2, new ProgressIndicator());

        /*PassVerif<Void> passVerif = new PassVerif<>();
        Thread t = new Thread(passVerif);
        t.start();*/
    boolean found = false;
    if (login != null) {
      found = login.verifPass(((VueUser) group.getSelectedToggle()).getUser());
    }

    if (!found) {
      displayThird();
    } else {
      myAvatarController.getPassField().clear();
      avatarBox.getChildren().set(2, myAvatarController.getAdvancePassField());
    }
  }

  public AvatarController getMyAvatarController() {
    return myAvatarController;
  }

  public void setLogin(Login login) {
    this.login = login;
  }

  public void setScene(Scene scene) {
    this.scene = scene;
    scene.widthProperty().addListener(((observable, oldValue, newValue) -> {
      resizeBackground();
    }));
    scene.heightProperty().addListener(((observable, oldValue, newValue) -> {
      resizeBackground();
    }));
    resizeBackground();


    //fulscreen management
    login.getStage().setFullScreenExitHint(null);
    login.getStage().fullScreenProperty().addListener((o, oVal, nVal) -> {
      if (nVal) {
        fullscreen.setGraphic(new ImageView("/images/compress_32px.png"));
      } else {
        fullscreen.setGraphic(new ImageView("/images/resize_diagonal_32px.png"));
      }
    });
  }

  @FXML
  void fullScreen() {
    login.fullScreen();
  }

  @FXML
  void fondPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ALPHANUMERIC) {
      fondClicked();
    } else if (event.getCode() == KeyCode.ESCAPE) {

    }
  }

  public ObservableList<VueUser> getVueUsers() {
    return vueUsers;
  }

  public SimpleUser getSelectedUser() {
    return ((VueUser) group.getSelectedToggle()).getUser();
  }

  public void setImagesLocation(String imagesLocation) {
    this.imagesLocation = imagesLocation;
  }

  private class Waiter<E> extends Task<E> {

    public long instant = System.currentTimeMillis();
    public boolean changer = false;
    public boolean cancel = false;

    @Override
    protected E call() {
      long dt;
      while (!cancel) {
        if (instant + 120000 <= System.currentTimeMillis()) {
          changer = true;
          break;

        } else {
          dt = instant + 120000 - System.currentTimeMillis();
          if (dt > 30000) {
            dt /= 4;
          } else if (dt > 16000) {
            dt /= 2;
          }
        }
        try {
          Thread.sleep(dt);
        } catch (InterruptedException ex) {
          cancel = true;
          System.err.println("Erreur interruption dans le waiteur : " + ex.getMessage());
        }
      }
      return null;
    }

  }

  private class PassVerif<E> extends Task<E> {

    public boolean found = false;

    @Override
    protected E call() throws Exception {

      return null;
    }

  }

  private class WaiterService<E> extends Service<E> {

    @Override
    protected Task<E> createTask() {
      return new Waiter<>();
    }

  }
}
