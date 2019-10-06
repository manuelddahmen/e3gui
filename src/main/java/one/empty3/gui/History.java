/*
 *
 *  *  This file is part of Empty3.
 *  *
 *  *     Empty3 is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 2 of the License, or
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by manue on 17-07-19.
 */
public class History {
    List<RPropertyDetailsRow> history = new ArrayList<>();

    private int current = -1;

    public List<RPropertyDetailsRow> getHistory() {
        return history;
    }
    public void clear()
    {
        current = -1;
        history.clear();
    }

    public void addToHistory(RPropertyDetailsRow row)
    {
        current++;
        history.add(current, row);
        if(history.size()>=getCurrent())
        {
            for(int i=current+1; i<history.size(); i++)
                history.remove(i);
        }
    }

    public void setHistory(List<RPropertyDetailsRow> history) {
        this.history = history;
    }


    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }


    public void next() {
        if(current<history.size()-1)
            current++;
    }

    public void back() {
        if(current>0)
            current--;
    }

    public Object getCurrentRow() {
        return history.get(current);
    }

    public RPropertyDetailsRow get(int i) {
        if(current<history.size()-1)
            if(current>=0) {
                RPropertyDetailsRow rPropertyDetailsRow = history.get(i);
                return rPropertyDetailsRow;

        }
        return null;
    }
}
