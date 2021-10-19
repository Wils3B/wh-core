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

import com.jfoenix.transitions.CachedTransition;
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * @author Wilson Gouanet
 */
public class UnderPane extends StackPane {

  private final String DEFAULT_STYLE_CLASS = "wh-under-pane";

  /**
   * Enumération de transition pour l'affichage du pane.
   */
  public enum UnderPaneTransition {
    TOP,
    LEFT,
    CENTER,
    BOTTOM,
    RIGHT
  }

  private double offsetX;
  private double offsetY;
  private double dX;
  private double dY;

  private StackPane contentHolder;
  private StackPane paneContainer;
  private Region content;
  private Pos position;
  private ObjectProperty<UnderPaneTransition> transitionType;
  private final BooleanProperty overlayClose;
  private Transition animation;
  private final BooleanProperty open;

  EventHandler<? super MouseEvent> closeHandler = e -> close();

  //Contructeurs
  public UnderPane() {
    this(null, null, Pos.CENTER);
  }

  public UnderPane(StackPane paneContainer, Region content, Pos position) {
    this(paneContainer, content, position, null);
  }

  public UnderPane(StackPane paneContainer, Region content, Pos position, UnderPaneTransition transitionType) {
    this(paneContainer, content, position, transitionType, false);
  }

  public UnderPane(StackPane paneContainer, Region content, Pos position, UnderPaneTransition transitionType, final boolean overlayClose) {
    this.overlayClose = new SimpleBooleanProperty(true);
    this.transitionType = new SimpleObjectProperty<>(UnderPaneTransition.CENTER);
    this.open = new SimpleBooleanProperty(false);

    this.setOverlayCLose(overlayClose);
    this.initialize();
    this.setPosition(position);
    this.setContent(content);
    this.setPaneContainer(paneContainer);
    this.setTransitionType(transitionType);

  }

  //--------------------------------------------------------------------------
  //initialiseur et méthodes privées
  //--------------------------------------------------------------------------
  private void initialize() {
    this.setVisible(false);
    this.getStyleClass().add(DEFAULT_STYLE_CLASS);
    this.transitionType.addListener((o, oldVal, newVal) -> {
      animation = this.getShowTransition();
    });

    this.open.addListener((o, oldVal, newVal) -> {
      this.open(newVal);
    });

    contentHolder = new StackPane();
    contentHolder.setPickOnBounds(false);
    contentHolder.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(2), null)));

    contentHolder.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    this.getChildren().add(contentHolder);
    StackPane.setAlignment(contentHolder, Pos.CENTER);
    this.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 10, 0.2), null, null)));

    if (this.overlayClose.get()) {
      this.addEventHandler(MouseEvent.MOUSE_PRESSED, closeHandler);
    }
    // prevent propagating the events to overlay pane
    contentHolder.addEventHandler(MouseEvent.ANY, e -> e.consume());
  }

  private UnderPaneTransition getDefaultTransitionType() {
    switch (this.position) {
      case TOP_LEFT:
      case CENTER_LEFT:
      case BOTTOM_LEFT:
      case BASELINE_LEFT:
        return UnderPaneTransition.RIGHT;

      case TOP_CENTER:
        return UnderPaneTransition.BOTTOM;

      case TOP_RIGHT:
      case CENTER_RIGHT:
      case BOTTOM_RIGHT:
      case BASELINE_RIGHT:
        return UnderPaneTransition.LEFT;

      case CENTER:
        return UnderPaneTransition.CENTER;

      case BOTTOM_CENTER:
      case BASELINE_CENTER:
        return UnderPaneTransition.TOP;

      default:
        return UnderPaneTransition.CENTER;
    }
  }

  private void initChangeListeners() {
    overlayClose.addListener((o, oldVal, newVal) -> {
      if (newVal) {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, closeHandler);
      } else {
        this.removeEventHandler(MouseEvent.MOUSE_PRESSED, closeHandler);
      }
    });
  }

  private void resetProperties() {
    this.setVisible(false);
    contentHolder.setTranslateX(0);
    contentHolder.setTranslateY(0);
    contentHolder.setScaleX(1);
    contentHolder.setScaleY(1);
  }

  //--------------------------------------------------------------------------
  // Getters et Setters
  //--------------------------------------------------------------------------
  public final void setPaneContainer(StackPane paneContainer) {
    if (paneContainer != null) {
      this.paneContainer = paneContainer;
      if (this.paneContainer.getChildren().indexOf(this) == -1
          || this.paneContainer.getChildren().indexOf(this) != this.paneContainer.getChildren().size() - 1) {
        this.paneContainer.getChildren().remove(this);
        this.paneContainer.getChildren().add(this);
      }
      // FIXME: need to be improved to consider only the parent boundary
      offsetX = this.getParent().getBoundsInLocal().getWidth();
      offsetY = this.getParent().getBoundsInLocal().getHeight();
      animation = getShowTransition();
    }
  }

  public final void setContent(Region content) {
    if (content != null) {
      this.content = content;
      this.content.setPickOnBounds(false);
      contentHolder.getChildren().setAll(content);
      StackPane.setAlignment(contentHolder, position);
    }
  }

  public final void setPosition(Pos position) {
    this.position = position == null ? Pos.CENTER : position;
  }

  public final void setTransitionType(UnderPaneTransition transitionType) {
    if (transitionType == null) {
      transitionType = this.getDefaultTransitionType();
    }
    this.transitionType.set(transitionType);
  }

  public final void setOverlayCLose(final boolean overlayClose) {
    this.overlayClose.set(overlayClose);
  }

  public StackPane getPaneContainer() {
    return paneContainer;
  }

  public Region getContent() {
    return content;
  }

  public Pos getPosition() {
    return position;
  }

  public UnderPaneTransition getTransitionType() {
    return transitionType.get();
  }

  public boolean getOverlayClose() {
    return overlayClose.get();
  }

  public void setDecalage(double dX, double dY) {
    this.dX = dX;
    this.dY = dY;
  }

  public void setExpandable(boolean expX, boolean expY) {
    if (expX) {
      contentHolder.setMaxWidth(USE_COMPUTED_SIZE);
    } else {
      contentHolder.setMaxWidth(USE_PREF_SIZE);
    }
    if (expY) {
      contentHolder.setMaxHeight(USE_COMPUTED_SIZE);
    } else {
      contentHolder.setMaxHeight(USE_PREF_SIZE);
    }
  }

  public StackPane getContentHolder() {
    return contentHolder;
  }

  public BooleanProperty openProperty() {
    return open;
  }

  public boolean isOpen() {
    return open.get();
  }

  //--------------------------------------------------------------------------
  // Interactions
  //--------------------------------------------------------------------------
  public void show() {
    this.open.set(true);
  }

  public void show(StackPane paneContainer) {
    this.setPaneContainer(paneContainer);
    show();
  }

  public void close() {
    this.open.set(false);
  }

  public void mutate() {
    this.open.set(!this.open.get());
  }

  private void open(boolean op) {
    if (op) {
      if (this.paneContainer == null) {
        System.err.println("The underPane container is not set!");
        return;
      }
      this.setPaneContainer(paneContainer);
      animation.play();
    } else {
      animation.setRate(-1);
      animation.play();
      animation.setOnFinished(e -> {
        resetProperties();
        paneContainer.getChildren().remove(this);
      });
    }
  }

  //--------------------------------------------------------------------------
  // Transitions
  //--------------------------------------------------------------------------
  private Transition getShowTransition() {
    double tX, tY;
    switch (this.transitionType.get()) {
      case TOP:
        tY = offsetY;
        tX = dX;
        animation = new TRBLTransition(tX, tY);
        break;
      case LEFT:
        tY = dY;
        tX = offsetX;
        animation = new TRBLTransition(tX, tY);
        break;
      case CENTER:
        animation = new CenterTransition();
        break;
      case BOTTOM:
        tY = -offsetY;
        tX = dX;
        animation = new TRBLTransition(tX, tY);
        break;
      case RIGHT:
        tY = dY;
        tX = -offsetX;
        animation = new TRBLTransition(tX, tY);
        break;
      default:
        throw new AssertionError(this.transitionType.get().name());

    }
    return animation;
  }

  private class CenterTransition extends CachedTransition {

    public CenterTransition() {
      super(contentHolder, new Timeline(
          new KeyFrame(Duration.ZERO,
              new KeyValue(contentHolder.scaleXProperty(), 0, Interpolator.EASE_BOTH),
              new KeyValue(contentHolder.scaleYProperty(), 0, Interpolator.EASE_BOTH),
              new KeyValue(contentHolder.translateXProperty(), dX, Interpolator.EASE_BOTH),
              new KeyValue(contentHolder.translateYProperty(), dY, Interpolator.EASE_BOTH),
              new KeyValue(UnderPane.this.visibleProperty(), false, Interpolator.EASE_BOTH)
          ),
          new KeyFrame(Duration.millis(10),
              new KeyValue(UnderPane.this.visibleProperty(), true, Interpolator.EASE_BOTH),
              new KeyValue(UnderPane.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
          ),
          new KeyFrame(Duration.millis(1000),
              new KeyValue(contentHolder.scaleXProperty(), 1, Interpolator.EASE_BOTH),
              new KeyValue(contentHolder.scaleYProperty(), 1, Interpolator.EASE_BOTH),
              new KeyValue(UnderPane.this.opacityProperty(), 1, Interpolator.EASE_BOTH)
          ))
      );
      // reduce the number to increase the shifting , increase number to reduce shifting
      setCycleDuration(Duration.seconds(0.4));
      setDelay(Duration.seconds(0));
    }
  }

  private class TRBLTransition extends CachedTransition {

    public TRBLTransition(double tX, double tY) {
      super(contentHolder, new Timeline(
          new KeyFrame(Duration.ZERO,
              new KeyValue(contentHolder.translateXProperty(), tX, Interpolator.EASE_BOTH),
              new KeyValue(contentHolder.translateYProperty(), tY, Interpolator.EASE_BOTH),
              new KeyValue(UnderPane.this.visibleProperty(), false, Interpolator.EASE_BOTH)
          ),
          new KeyFrame(Duration.millis(10),
              new KeyValue(UnderPane.this.visibleProperty(), true, Interpolator.EASE_BOTH),
              new KeyValue(UnderPane.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
          ),
          new KeyFrame(Duration.millis(1000),
              new KeyValue(contentHolder.translateXProperty(), dX, Interpolator.EASE_BOTH),
              new KeyValue(contentHolder.translateYProperty(), dY, Interpolator.EASE_BOTH),
              new KeyValue(UnderPane.this.opacityProperty(), 1, Interpolator.EASE_BOTH)))
      );
      // reduce the number to increase the shifting , increase number to reduce shifting
      setCycleDuration(Duration.seconds(0.4));
      setDelay(Duration.seconds(0));
    }
  }

}
