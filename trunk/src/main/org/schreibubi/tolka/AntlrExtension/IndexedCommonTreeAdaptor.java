/**
 * Copyright (C) 2009 jwerner <schreibubi@gmail.com>
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
package org.schreibubi.tolka.AntlrExtension;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

/**
 * @author Jörg Werner
 * 
 */
public class IndexedCommonTreeAdaptor extends CommonTreeAdaptor {

	/**
	 * 
	 */
	public IndexedCommonTreeAdaptor() {
		super();
	}

	@Override
	public Object create(Token payload) {
		return new IndexedCommonTree(payload);
	}

	public int getIndex(Object t) {
		return ((IndexedCommonTree) t).getIndex();
	}

	public void setIndex(Object t, int i) {
		((IndexedCommonTree) t).setIndex(i);
	}

}
