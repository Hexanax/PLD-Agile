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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Pair;

/**
 * A layer that allows to visualise points of interest.
 */
public class PointLayer extends MapLayer {

    private final ObservableList<Pair<MapPoint, Node>> intersectionPoints = FXCollections.observableArrayList();
    private final ObservableList<Pair<MapPoint, Node>> requestPoints = FXCollections.observableArrayList();
    private static Controller controller;
    public PointLayer() {

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }




    public void addPoint(MapPoint p, Node icon) {
        intersectionPoints.add(new Pair<>(p, icon));
        this.getChildren().add(icon);
        this.markDirty();
    }

    public void addRequestPoint(MapPoint p, Node icon){
        requestPoints.add(new Pair<>(p, icon));
        this.getChildren().add(icon);
        this.markDirty();
    }

    public void clearPoints() {
        intersectionPoints.clear();
        this.getChildren().clear();
        this.markDirty();
    }

    public void clearRequestPoints(){
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


    public void activeMapIntersectionsListener(){

        for (Pair<MapPoint, Node> point : intersectionPoints) {
            point.getValue().setOnMouseClicked(event-> {
                controller.modifyClick(point.getKey().getId(),"Intersection", -1);
            });
        }
    }

    public void activeRequestIntersectionsListener(){
        for (Pair<MapPoint, Node> point : requestPoints) {
            point.getValue().setOnMouseClicked(event-> {
                controller.modifyClick(point.getKey().getRequestId(),"Intersection", point.getKey().getStepIndex());
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
