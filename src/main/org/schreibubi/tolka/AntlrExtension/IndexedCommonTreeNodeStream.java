/**
 * Copyright (C) 2009 Jörg Werner schreibubi@gmail.com
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
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeAdaptor;

/**
 * @author Jörg Werner
 * 
 */
public class IndexedCommonTreeNodeStream extends CommonTreeNodeStream {

	private int	pos	= 0;

	/**
	 * @param tree
	 */
	public IndexedCommonTreeNodeStream(Object tree) {
		this(new IndexedCommonTreeAdaptor(), tree);
	}

	/**
	 * @param adaptor
	 * @param tree
	 */
	public IndexedCommonTreeNodeStream(TreeAdaptor adaptor, Object tree) {
		super(adaptor, tree);
	}

	/**
	 * @param adaptor
	 * @param tree
	 * @param initialBufferSize
	 */
	public IndexedCommonTreeNodeStream(TreeAdaptor adaptor, Object tree, int initialBufferSize) {
		super(adaptor, tree, initialBufferSize);
	}

	@Override
	public void fillBuffer(Object t) {
		IndexedCommonTreeAdaptor adaptor = (IndexedCommonTreeAdaptor) getTreeAdaptor();
		boolean nil = adaptor.isNil(t);
		if (!nil) {
			adaptor.setIndex(t, pos++);
			nodes.add(t); // add this node
		}
		// add DOWN node if t has children
		int n = adaptor.getChildCount(t);
		if (!nil && (n > 0)) {
			addNavigationNode(Token.DOWN);
		}
		// and now add all its children
		for (int c = 0; c < n; c++) {
			Object child = adaptor.getChild(t, c);
			fillBuffer(child);
		}
		// add UP node if t has children
		if (!nil && (n > 0)) {
			addNavigationNode(Token.UP);
		}
	}

	@Override
	protected void addNavigationNode(final int ttype) {
		IndexedCommonTreeAdaptor adaptor = (IndexedCommonTreeAdaptor) getTreeAdaptor();
		Object navNode = null;
		if (ttype == Token.DOWN) {
			if (hasUniqueNavigationNodes()) {
				navNode = adaptor.create(Token.DOWN, "DOWN");
			} else {
				navNode = down;
			}
		} else if (hasUniqueNavigationNodes()) {
			navNode = adaptor.create(Token.UP, "UP");
		} else {
			navNode = up;
		}
		adaptor.setIndex(navNode, pos++);
		nodes.add(navNode);
	}

}
