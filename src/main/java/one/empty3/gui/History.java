package one.empty3.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manue on 17-07-19.
 */
public class History {
    List<RPropertyDetailsRow> history = new ArrayList<>();

    private int current = 0;

    public List<RPropertyDetailsRow> getHistory() {
        return history;
    }


    public void addToHistory(RPropertyDetailsRow row)
    {
        current++;
        history.add(current, row);
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
                current = i;
                RPropertyDetailsRow rPropertyDetailsRow = history.get(i);
                history.set(current, new RPropertyDetailsRow(rPropertyDetailsRow));

                return history.get(current);
        }
        return null;
    }
}
