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
package org.schreibubi.symbol;

import java.util.LinkedHashSet;

import org.antlr.runtime.RecognitionException;
import org.schreibubi.visitor.Visitor;


/**
 * Class for storing Strings.
 * 
 * @author Jörg Werner
 * 
 */
public class SymbolString extends Symbol {
	private String	value	= "";

	/**
	 * Constructor
	 * 
	 * @param value
	 *            String for this symbol
	 */
	public SymbolString(String value) {
		super("", SymType.STRING);
		setValue(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            String for this symbol
	 */
	public SymbolString(String name, String value) {
		super(name, SymType.STRING);
		setValue(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param s
	 *            Symbol
	 */
	public SymbolString(SymbolString s) {
		super(s.getName(), SymType.STRING);
		setValue(s.getValue());
	}

	/**
	 * @param v
	 * @throws Exception
	 */
	public void accept(Visitor<Symbol> v) throws Exception {
		v.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#add(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol add(Symbol s) throws RecognitionException {
		if (s instanceof SymbolString) {
			String o = ((SymbolString) s).getValue();
			this.value = this.value + o;
			return this;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#and(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol and(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/**
	 * Assign symbol
	 * 
	 * @param s
	 *            Symbol
	 */
	public void assign(SymbolString s) {
		setValue(s.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#clone()
	 */
	@Override
	public Symbol clone() {
		return new SymbolString(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convert(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol convert(Symbol s) throws RecognitionException {
		return s.convertToString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToFloat()
	 */
	@Override
	public SymbolDouble convertToDouble() throws RecognitionException {
		return new SymbolDouble(getName(), this.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToInt()
	 */
	@Override
	public SymbolInteger convertToInt() throws RecognitionException {
		return new SymbolInteger(getName(), this.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToString()
	 */
	@Override
	public SymbolString convertToString() throws RecognitionException {
		return new SymbolString(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#div(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol div(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	@Override
	public boolean eq(Symbol s) throws RecognitionException {
		if (s instanceof SymbolString) {
			SymbolString os = (SymbolString) s;
			String o = os.getValue();
			return o.equals(os);
		} else
			throw new RecognitionException();
	}

	@Override
	public boolean ge(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	@Override
	public Symbol getBitList(LinkedHashSet<Symbol> bits) {
		return null;
	}

	/**
	 * Returns string value stored in this class
	 * 
	 * @return string value stored
	 */
	public String getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString()
	 */
	@Override
	public String getValueString() {
		return value;
	}

	@Override
	public boolean gt(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	@Override
	public boolean le(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	@Override
	public boolean lt(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	@Override
	public Symbol max(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	@Override
	public Symbol min(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#mul(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol mul(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#not()
	 */
	@Override
	public Symbol not() throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#or(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol or(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/**
	 * Sets string value stored in this class
	 * 
	 * @param value
	 *            string value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sub(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol sub(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "=\"" + getValueString() + "\"";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uminus()
	 */
	@Override
	public Symbol uminus() throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uplus()
	 */
	@Override
	public Symbol uplus() throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#xor(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol xor(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

}
