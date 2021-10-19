/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.materials.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wilson
 */
public final class TimeDisplayer extends VBox implements AutoCloseable {
  private Updater<Void> updater;
  private final Text time;
  private final Text date;

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM");
  private static final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

  public TimeDisplayer() {
    super();
    final Date date2 = new Date();
    time = new Text(hourFormat.format(date2));
    date = new Text(dateFormat.format(date2));
    time.setFont(Font.font("segoe UI Semilight", FontWeight.NORMAL, 90));
    time.setFill(Color.WHITE);
    date.setFill(Color.WHITE);
    date.setFont(Font.font("segoe UI", FontWeight.NORMAL, 40));

    this.getChildren().addAll(time, date);
    this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
  }

  public void start() {
    if (updater != null) stop();
    updater = new Updater<>();
    Thread t = new Thread(updater);
    t.start();

    time.textProperty().bind(updater.timeProperty);
    date.textProperty().bind(updater.dateProperty);
  }

  public void stop() {
    if (updater.cancel) return;
    updater.cancel = true;
    updater.cancel();
    time.textProperty().unbind();
    time.textProperty().unbind();
  }

  @Override
  public void close() {
    this.stop();
  }

  private class Updater<V> extends Task<Void> {

    public StringProperty dateProperty = new SimpleStringProperty();
    public StringProperty timeProperty = new SimpleStringProperty();
    public boolean cancel = false;

    @Override
    protected Void call() throws Exception {
      try {
        int remainder;
        while (!cancel) {
          final Date date2 = new Date();
          dateProperty.set(dateFormat.format(date2));
          timeProperty.set(hourFormat.format(date2));
          remainder = 60 - date2.getSeconds();
          if (remainder > 30) remainder /= 2;
          Thread.sleep(remainder * 1000);
        }
      } catch (InterruptedException ex) {
        System.err.println("Echec de compatage : " + ex.getMessage());
      }
      return null;
    }

  }

}
