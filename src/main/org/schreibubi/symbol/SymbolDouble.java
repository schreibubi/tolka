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

import java.math.BigDecimal;
import java.util.LinkedHashSet;

import org.antlr.runtime.RecognitionException;
import org.schreibubi.visitor.Visitor;


/**
 * Class for storing doubles.
 */
public class SymbolDouble extends Symbol {
	private BigDecimal	value;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            BigDecimal value for this symbol
	 */
	public SymbolDouble(BigDecimal value) {
		super(null, SymType.DOUBLE);
		setValue(value);
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            BigDecimal value for this symbol
	 */
	public SymbolDouble(String value) {
		super(null, SymType.DOUBLE);
		parseValueString(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            BigDecimal value for this symbol
	 */
	public SymbolDouble(String name, BigDecimal value) {
		super(name, SymType.DOUBLE);
		setValue(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            symbol name
	 * 
	 * @param value
	 *            BigDecimal value for this symbol
	 */
	public SymbolDouble(String name, String value) {
		super(name, SymType.DOUBLE);
		parseValueString(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param s
	 */
	public SymbolDouble(SymbolDouble s) {
		super(s.getName(), SymType.DOUBLE);
		setValue(s.getValue());
	}

	public void accept(Visitor<Symbol> v) throws Exception {
		v.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#add(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble add(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			this.value = this.value.add(o);
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
	public SymbolDouble and(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/**
	 * Assign symbol
	 * 
	 * @param s
	 *            symbol
	 */
	public void assign(SymbolDouble s) {
		setValue(s.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#clone()
	 */
	@Override
	public Symbol clone() {
		return new SymbolDouble(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convert(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol convert(Symbol s) throws RecognitionException {
		return s.convertToDouble();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToFloat()
	 */
	@Override
	public SymbolDouble convertToDouble() throws RecognitionException {
		return new SymbolDouble(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToInt()
	 */
	@Override
	public SymbolInteger convertToInt() throws RecognitionException {
		return new SymbolInteger(getName(), this.value.toBigInteger());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToString()
	 */
	@Override
	public SymbolString convertToString() throws RecognitionException {
		return new SymbolString(getName(), getValueString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#div(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble div(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			this.value = this.value.divide(o);
			return this;
		} else
			throw new RecognitionException();
	}

	@Override
	public boolean eq(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			return this.value.compareTo(o) == 0;
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
	 * Returns BigDecimal value stored in this class
	 * 
	 * @return BigDecimal value stored
	 */
	public BigDecimal getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString()
	 */
	@Override
	public String getValueString() {
		return value.toString();
	}

	@Override
	public boolean gt(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			return this.value.compareTo(o) == 1;
		} else
			throw new RecognitionException();
	}

	@Override
	public boolean le(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			return this.value.compareTo(o) <= 0;
		} else
			throw new RecognitionException();
	}

	@Override
	public boolean lt(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			return this.value.compareTo(o) == -1;
		} else
			throw new RecognitionException();
	}

	@Override
	public Symbol max(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			BigDecimal o = ((SymbolDouble) s).getValue();
			this.value = this.value.max(o);
			return this;
		} else
			throw new RecognitionException();
	}

	@Override
	public Symbol min(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			BigDecimal o = ((SymbolDouble) s).getValue();
			this.value = this.value.min(o);
			return this;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#mul(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble mul(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			this.value = this.value.multiply(o);
			return this;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#not()
	 */
	@Override
	public SymbolDouble not() throws RecognitionException {
		throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#or(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble or(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	/**
	 * Sets BigDecimal value stored in this class
	 * 
	 * @param value
	 *            BigDecimal value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sub(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble sub(Symbol s) throws RecognitionException {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			BigDecimal o = os.getValue();
			this.value = this.value.subtract(o);
			return this;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "=" + getValueString();
	}

	@Override
	public SymbolDouble uminus() {
		this.value = this.value.negate();
		return this;
	}

	@Override
	public SymbolDouble uplus() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#xor(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble xor(Symbol s) throws RecognitionException {
		throw new RecognitionException();
	}

	private void parseValueString(String value) {
		setValue(new BigDecimal(value));
	}

}
