package one.empty3.gui;

import com.jgoodies.common.collect.ObservableList;

import javax.swing.event.ListDataListener;
import java.util.ArrayList;

/**
 * Created by manue on 01-07-19.
 */
public class MyObservableList<T> extends ArrayList<T> implements ObservableList<T> {
	ArrayList<ListDataListener>  listDataListeners = new ArrayList<>();

	@Override
	public int getSize() {
		return size();
	}

	@Override
	public Object getElementAt(int index) {

		return get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
			listDataListeners.add(l);
		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listDataListeners.remove(l);
		
	}

}