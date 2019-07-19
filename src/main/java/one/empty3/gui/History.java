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
}
