/*
 * Copyright (c) 2016, Gluon
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
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fr.insalyon.pldagile.view.maps;

import fr.insalyon.pldagile.controller.Controller;
import fr.insalyon.pldagile.view.Colors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A layer that allows to visualise points of interest.
 */
public class PointLayer extends MapLayer {

    private final ObservableList<Pair<MapPoint, Node>> intersectionPoints = FXCollections.observableArrayList();
    private static final ObservableList<Pair<MapPoint, Node>> requestPoints = FXCollections.observableArrayList();
    private static Controller controller;
    private static Pair<MapPoint, Node> lastHighlighted = null;
    private static final double highlightIconFactor = 1.3;

    public PointLayer() {

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public static void highlightIcon(Long id) {
        // unhighlight last listened icon when disabling the itemView listener
        if (lastHighlighted != null) {
            unHighlightIcon(lastHighlighted.getKey().getRequestId());
        }
        // looking for the mapPoints that matches the listened request
        List<Pair<MapPoint, Node>> candidates = requestPoints.stream()
                .filter(point -> point.getKey().getRequestId() == id).collect(Collectors.toList());
        candidates.forEach((candidate) -> {
            candidate.getValue().setScaleX(highlightIconFactor);
            candidate.getValue().setScaleY(highlightIconFactor);
            lastHighlighted = candidate;
        });
    }

    public static void unHighlightIcon(Long id) {
        // looking for the mapPoints that matches the listened request
        List<Pair<MapPoint, Node>> candidates = requestPoints.stream()
                .filter(point -> point.getKey().getRequestId() == id).collect(Collectors.toList());
        candidates.forEach((candidate) -> {
            candidate.getValue().setScaleX(1.0);
            candidate.getValue().setScaleY(1.0);
        });
    }

    public static Pair<MapPoint, Node> getLastHighlighted() {
        return lastHighlighted;
    }

    public void addPoint(MapPoint p, Node icon) {
        intersectionPoints.add(new Pair<>(p, icon));
        this.getChildren().add(icon);
        this.markDirty();
    }

    public void addTourPoint(MapPoint p) {
        Node circle = new Circle(2, Color.WHITE);

        // Add blue border
        DropShadow borderEffect = new DropShadow(BlurType.THREE_PASS_BOX, Colors.getTourLineColor(), 2, 3, 0, 0);
        circle.setEffect(borderEffect);

        intersectionPoints.add(new Pair<>(p, circle));
        this.getChildren().add(circle);
        this.markDirty();
    }

    public void addRequestPoint(MapPoint p, Node icon) {
        requestPoints.add(new Pair<>(p, icon));
        this.getChildren().add(icon);
        this.markDirty();
    }

    public void clearPoints() {
        intersectionPoints.clear();
        this.getChildren().clear();
        this.markDirty();
    }

    public void clearRequestPoints() {
        requestPoints.clear();
        this.getChildren().removeIf(node -> node instanceof RequestMapPin);
    }

    @Override
    protected void layoutLayer() {
        layoutLayerPoints(intersectionPoints);
        layoutLayerPoints(requestPoints);
    }

    private void layoutLayerPoints(ObservableList<Pair<MapPoint, Node>> Points) {
        for (Pair<MapPoint, Node> candidate : Points) {
            MapPoint point = candidate.getKey();
            Node icon = candidate.getValue();
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
            icon.setVisible(true);
            icon.setTranslateX(mapPoint.getX());
            icon.setTranslateY(mapPoint.getY());
        }
    }

    public void activeMapIntersectionsListener() {

        for (Pair<MapPoint, Node> point : intersectionPoints) {
            point.getValue().setOnMouseClicked(event -> {
                controller.modifyClick(point.getKey().getId(), "Intersection", -1);
            });
        }
    }

    public void activeRequestIntersectionsListener() {
        for (Pair<MapPoint, Node> point : requestPoints) {
            point.getValue().setOnMouseClicked(event -> {
                controller.modifyClick(point.getKey().getRequestId(), "Intersection", point.getKey().getStepIndex());
                System.out.println("active request:" + point.getKey().getStepIndex());
            });
        }
    }

    public void disableMapIntersectionsListener() {
        for (Pair<MapPoint, Node> point : intersectionPoints) {
            point.getValue().setOnMouseClicked(null);
        }
    }

    public void disableRequestIntersectionsListener() {
        for (Pair<MapPoint, Node> point : requestPoints) {
            point.getValue().setOnMouseClicked(null);
        }
    }

}
