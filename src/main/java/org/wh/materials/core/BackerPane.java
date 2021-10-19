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

import com.jfoenix.controls.JFXTabPane;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * @author Wilson
 */
public class BackerPane extends StackPane {
  private JFXTabPane panes;
  private int pos;

  public BackerPane(Region initialRegion) {
    if (initialRegion == null) {
      throw new UnsupportedOperationException("le noeud initial ne doit pas être null");
    }
    initialize(initialRegion);
  }

  public BackerPane(Region initialRegion, boolean sizer) {
    if (initialRegion == null) {
      throw new UnsupportedOperationException("le noeud initial ne doit pas être null");
    }
    if (sizer) {
      AnchorPane pane = new AnchorPane(initialRegion);
      AnchorPane.setBottomAnchor(initialRegion, .0);
      AnchorPane.setRightAnchor(initialRegion, .0);
      AnchorPane.setLeftAnchor(initialRegion, .0);
      AnchorPane.setTopAnchor(initialRegion, .0);
      initialize(pane);
    } else {
      initialize(initialRegion);
    }
  }

  private void initialize(Region initialRegion) {
    pos = 0;
    panes = new JFXTabPane();
    Tab first = new Tab("first", initialRegion);
    panes.getTabs().add(first);
    panes.getSelectionModel().selectFirst();

    this.getStylesheets().add(getClass().getResource("org/wh/materials/core/views/backer.css").toString());
    panes.getStyleClass().add("WH-backer");
    this.getChildren().setAll(panes);

    this.panes.setOnKeyPressed(event -> event.consume());
  }

  public void popFirst() {
    if (panes.getTabs().size() < 2) {
      throw new UnsupportedOperationException("Impossible de supprimer la première vue quand le nombre de vues est 1");
    }
    panes.getTabs().remove(0, 1);
    panes.getSelectionModel().selectFirst();
    pos = 0;
  }

  public void next(Region nextRegion) {
    if (nextRegion == null) {
      throw new UnsupportedOperationException("le noeud suivant ne doit pas être null");
    }
    int size = panes.getTabs().size();
    if (pos < size - 1) {
      panes.getTabs().remove(pos + 2, size);
      panes.getTabs().set(++pos, new Tab("", nextRegion));
    } else {
      panes.getTabs().add(new Tab("", nextRegion));
      pos++;
    }
    panes.getSelectionModel().selectLast();

  }

  public void next(Region nextRegion, boolean sizer) {
    if (nextRegion == null) {
      throw new UnsupportedOperationException("le noeud suivant ne doit pas être null");
    }
    if (sizer) {
      AnchorPane pane = new AnchorPane(nextRegion);
      AnchorPane.setBottomAnchor(nextRegion, .0);
      AnchorPane.setRightAnchor(nextRegion, .0);
      AnchorPane.setLeftAnchor(nextRegion, .0);
      AnchorPane.setTopAnchor(nextRegion, .0);
      next(pane);
    } else {
      next(nextRegion);
    }
  }

  public void next() {
    if (hasNext()) {
      panes.getSelectionModel().selectNext();
      pos++;
    }
  }

  public void previous() {
    if (hasPrevious()) {
      panes.getSelectionModel().selectPrevious();
      pos--;
    }
  }

  public boolean hasNext() {
    return pos < panes.getTabs().size() - 1;
  }

  public boolean hasPrevious() {
    return pos > 0;
  }

  public Node getLast() {
    return panes.getTabs().get(panes.getTabs().size() - 1).getContent();
  }

  public Node getCurrent() {
    return panes.getTabs().get(pos).getContent();
  }

  public Node getPrevious() {
    if (hasPrevious()) {
      return panes.getTabs().get(pos - 1).getContent();
    }
    return null;
  }

  public Node getNext() {
    if (hasNext()) {
      return panes.getTabs().get(pos + 1).getContent();
    }
    return null;
  }
}
