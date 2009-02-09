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
import java.math.BigInteger;
import java.util.LinkedHashSet;

import org.antlr.runtime.RecognitionException;
import org.schreibubi.visitor.Visitor;


/**
 * Class for storing integer.
 */
public class SymbolInteger extends Symbol {
	private BigInteger	value;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(BigInteger value) {
		super(null, SymType.INTEGER);
		setValue(value);
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(int value) {
		super(null, SymType.INTEGER);
		setValue(BigInteger.valueOf(value));
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(String value) {
		super(null, SymType.INTEGER);
		parseValueString(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(String name, BigInteger value) {
		super(name, SymType.INTEGER);
		setValue(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name of the symbol
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(String name, String value) {
		super(name, SymType.INTEGER);
		parseValueString(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param s
	 *            Symbol
	 */
	public SymbolInteger(SymbolInteger s) {
		super(s.getName(), SymType.INTEGER);
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
	public SymbolInteger add(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
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
	public SymbolInteger and(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			this.value = this.value.and(o);
			return this;
		} else
			throw new RecognitionException();
	}

	/**
	 * Assign symbol
	 * 
	 * @param s
	 *            Symbol
	 */
	public void assign(SymbolInteger s) {
		setValue(s.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#clone()
	 */
	@Override
	public Symbol clone() {
		return new SymbolInteger(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convert(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol convert(Symbol s) throws RecognitionException {
		return s.convertToInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToFloat()
	 */
	@Override
	public SymbolDouble convertToDouble() throws RecognitionException {
		return new SymbolDouble(getName(), new BigDecimal(this.value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToInt()
	 */
	@Override
	public SymbolInteger convertToInt() throws RecognitionException {
		return new SymbolInteger(this);
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
	public SymbolInteger div(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			this.value = this.value.divide(o);
			return this;
		} else
			throw new RecognitionException();
	}

	@Override
	public boolean eq(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			return this.value.compareTo(o) == 0;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#gt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean ge(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			return this.value.compareTo(o) >= 0;
		} else
			throw new RecognitionException();
	}

	@Override
	public Symbol getBitList(LinkedHashSet<Symbol> bits) throws RecognitionException {
		BigInteger n = BigInteger.ZERO;
		int bc = 0;
		for (Symbol i : bits) {
			int b = i.convertToInt().getValue().intValue();
			if (value.testBit(b)) {
				n = n.setBit(bc);
			}
			bc++;
		}
		return new SymbolInteger(n);
	}

	/**
	 * Returns int value stored in this class
	 * 
	 * @return int value stored
	 */
	public BigInteger getValue() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#gt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean gt(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			return this.value.compareTo(o) == 1;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#lt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean le(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			return this.value.compareTo(o) <= 0;
		} else
			throw new RecognitionException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#lt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean lt(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			return this.value.compareTo(o) == -1;
		} else
			throw new RecognitionException();
	}

	@Override
	public Symbol max(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			BigInteger o = ((SymbolInteger) s).getValue();
			this.value = this.value.min(o);
			return this;
		} else
			throw new RecognitionException();
	}

	@Override
	public Symbol min(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			BigInteger o = ((SymbolInteger) s).getValue();
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
	public SymbolInteger mul(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
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
	public SymbolInteger not() {
		for (int i = 0; i < this.value.bitLength(); i++) {
			this.value = this.value.flipBit(i);
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#or(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger or(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			this.value = this.value.or(o);
			return this;
		} else
			throw new RecognitionException();
	}

	/**
	 * Sets int value stored in this class
	 * 
	 * @param value
	 *            int value to set
	 */
	public void setValue(BigInteger value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sub(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger sub(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uminus()
	 */
	@Override
	public Symbol uminus() throws RecognitionException {
		this.value = this.value.negate();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uplus()
	 */
	@Override
	public Symbol uplus() throws RecognitionException {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#xor(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger xor(Symbol s) throws RecognitionException {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			BigInteger o = os.getValue();
			this.value = this.value.xor(o);
			return this;
		} else
			throw new RecognitionException();
	}

	private void parseValueString(String value) {
		setValue(new BigInteger(value));
	}

}
