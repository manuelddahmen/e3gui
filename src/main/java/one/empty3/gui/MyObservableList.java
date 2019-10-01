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