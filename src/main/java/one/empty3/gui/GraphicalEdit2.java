/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     Empty3 is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with Empty3.  If not, see <https://www.gnu.org/licenses/>. 2
 *
 *
 */

package one.empty3.gui;

import one.empty3.library.Representable;

/**
 * Created by manue on 19-11-19.
 */
public class GraphicalEdit2 extends Thread {
    private Main main;
    private boolean running = true;
    private boolean endSel1;
    private UpdateViewMain panel;
    private int activeSelection;

    public boolean isEndSel1() {
        return endSel1;
    }

    public boolean isStartSel1() {
        return startSel1;
    }

    public void setStartSel1(boolean startSel1) {
        this.startSel1 = startSel1;
    }

    private boolean startSel1;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setSelectingMultipleObjectsIn(boolean selectingMultipleObjectsIn) {
        this.selectingMultipleObjectsIn = selectingMultipleObjectsIn;
    }

    public boolean isSelectingMultipleObjectsIn() {
        return selectingMultipleObjectsIn;
    }

    public void setEndSel1(boolean endSel1) {
        this.endSel1 = endSel1;
    }

    public void setActiveSelection(int activeSelection) {
        this.activeSelection = activeSelection;
    }

    public int getActiveSelection() {
        return activeSelection;
    }

    public enum SelType { SelectRotate, Translate, Rotate};
    public enum Action {duplicateOnPoints,duplicateOnCurve,duplicateOnSurface,extrude};
    private SelType selTypeIn;
    private boolean isSelectingIn;
    private boolean isSelectingMultipleIn;
    private SelType selTypeOut;
    private boolean isSelectingOut;
    private boolean isSelectingMultipleOut;
    private boolean SelectMultipleIn, SelectArbitraryPointsIn, selectingMultipleObjectsIn;
    private boolean SelectMultipleOut, SelectArbitraryPointsOut;
    private Action actionToPerform;
    private MyObservableList<Representable> selectionIn;
    private MyObservableList<Representable> selectionOut;
    public GraphicalEdit2() {
    }

    public Main getMain() {
        return main;
    }

    public boolean isRunning() {
        return running;
    }

    public SelType getSelTypeIn() {
        return selTypeIn;
    }

    public void setSelTypeIn(SelType selTypeIn) {
        this.selTypeIn = selTypeIn;
    }

    public boolean isSelectingIn() {
        return isSelectingIn;
    }

    public void setSelectingIn(boolean selectingIn) {
        isSelectingIn = selectingIn;
    }

    public boolean isSelectingMultipleIn() {
        return isSelectingMultipleIn;
    }

    public void setSelectingMultipleIn(boolean selectingMultipleIn) {
        isSelectingMultipleIn = selectingMultipleIn;
    }

    public SelType getSelTypeOut() {
        return selTypeOut;
    }

    public void setSelTypeOut(SelType selTypeOut) {
        this.selTypeOut = selTypeOut;
    }

    public boolean isSelectingOut() {
        return isSelectingOut;
    }

    public void setSelectingOut(boolean selectingOut) {
        isSelectingOut = selectingOut;
    }

    public boolean isSelectingMultipleOut() {
        return isSelectingMultipleOut;
    }

    public void setSelectingMultipleOut(boolean selectingMultipleOut) {
        isSelectingMultipleOut = selectingMultipleOut;
    }

    public boolean isSelectMultipleIn() {
        return SelectMultipleIn;
    }

    public void setSelectMultipleIn(boolean selectMultipleIn) {
        SelectMultipleIn = selectMultipleIn;
    }

    public boolean isSelectArbitraryPointsIn() {
        return SelectArbitraryPointsIn;
    }

    public void setSelectArbitraryPointsIn(boolean selectArbitraryPointsIn) {
        SelectArbitraryPointsIn = selectArbitraryPointsIn;
    }

    public boolean isSelectMultipleOut() {
        return SelectMultipleOut;
    }

    public void setSelectMultipleOut(boolean selectMultipleOut) {
        SelectMultipleOut = selectMultipleOut;
    }

    public boolean isSelectArbitraryPointsOut() {
        return SelectArbitraryPointsOut;
    }

    public void setSelectArbitraryPointsOut(boolean selectArbitraryPointsOut) {
        SelectArbitraryPointsOut = selectArbitraryPointsOut;
    }

    public Action getActionToPerform() {
        return actionToPerform;
    }

    public void setActionToPerform(Action actionToPerform) {
        this.actionToPerform = actionToPerform;
    }

    public MyObservableList<Representable> getSelectionIn() {
        return selectionIn;
    }

    public void setSelectionIn(MyObservableList<Representable> selectionIn) {
        this.selectionIn = selectionIn;
    }

    public MyObservableList<Representable> getSelectionOut() {
        return selectionOut;
    }

    public void setSelectionOut(MyObservableList<Representable> selectionOut) {
        this.selectionOut = selectionOut;
    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points  sur lesquels dupliquer
     * Adapation du repère en le liant à la courbe NON
     */
    public void duplicateOnPoints()
    {

    }
    /***
     * Sel1: objets à dupliquer
     * Sel2: points de la courbe sur lesquels dupliquer
     * Adapation du repère en le liant à la courbe OUI
     */
    public void duplicateOnCurve()
    {

    }

    /***
     * Sel1: objets à dupliquer
     * Sel2: points de la surface àsur lesquelsduplqiuer
     * Adapation du repère en le liant à la surface OUI
     */
    public void duplicateOnSurface()
    {

    }

    /***
     * Sel1 est considéré comme la surface à extruder (donc que des points)
     * Sel2 le chemin d'extrusion
     */
    public void extrude()
    {

    }

    public void performAction()
    {

    }

    public UpdateViewMain getPanel() {
        return panel;
    }

    public void setPanel(UpdateViewMain panel) {
        this.panel = panel;
    }

    public void run() {

    }
}
