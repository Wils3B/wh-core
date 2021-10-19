package org.wh.materials.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PreviousNext extends VBox {
  private final JFXTabPane slider;
  private final HBox subBox;
  private final JFXButton nextBtn;
  private final JFXButton previousBtn;
  private final BooleanProperty finished;

  public PreviousNext(JFXTabPane mainPane) {
    super();

    this.getStylesheets().add(getClass().getResource("org/wh/materials/core/css/style.css").toString());

    //initialize the slider
    this.slider = mainPane;
    slider.getSelectionModel().select(0);
    slider.getStyleClass().add("WHSlider");

    //initialize the finished property
    finished = new SimpleBooleanProperty(false);

    //initialise the subHBOX
    subBox = new HBox();
    nextBtn = new JFXButton("Next(1/" + slider.getTabs().size() + ")");
    previousBtn = new JFXButton("Previous");
    subBox.getChildren().addAll(nextBtn, previousBtn);
    subBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    subBox.getStyleClass().addAll("subBox");

    //setup events
    nextBtn.setOnMouseClicked(event -> next());
    previousBtn.setOnMouseClicked(event -> previous());
    nextBtn.getStyleClass().addAll("skip-next", "priority");
    previousBtn.getStyleClass().addAll("skip-next");

    //Add all components on this
    this.getChildren().addAll(slider, new Separator(Orientation.HORIZONTAL), subBox);

  }

  private void previous() {
    int index = slider.getSelectionModel().getSelectedIndex();
    int size = slider.getTabs().size();
    if (index == 0) return;
    slider.getSelectionModel().selectPrevious();
    nextBtn.setText(String.format("Next(%d/%d)", index, size));
  }

  private void next() {
    int index = slider.getSelectionModel().getSelectedIndex();
    int size = slider.getTabs().size();
    if (index == size - 1) {
      finished.set(true);
      return;
    }
    slider.getSelectionModel().selectNext();
    if (index == size - 2)
      nextBtn.setText("Enjoy!");
    else
      nextBtn.setText(String.format("Next(%d/%d)", index + 2, size));
  }

  public BooleanProperty finishedProperty() {
    return finished;
  }
}
