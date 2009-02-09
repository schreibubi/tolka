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
package org.schreibubi.symbol;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashSet;

import org.antlr.runtime.RecognitionException;
import org.schreibubi.visitor.Host;


/**
 * Symbol class. The parent class for all symbols. A symbol has a name and supports the visitor pattern
 * 
 * @author Jörg Werner
 */
public abstract class Symbol implements Host<Symbol>, Cloneable {
	/**
	 * Symboltype enum
	 */
	public enum SymType {
		/**
		 * String symbol
		 */
		STRING,
		/**
		 * Double symbol
		 */
		DOUBLE,
		/**
		 * Integer symbol
		 */
		INTEGER
	}

	/**
	 * Create a symbol of type
	 * 
	 * @param type
	 *            type of symbol
	 * @return Symbol
	 */
	public static Symbol createSymbolFactory(int type) {
		if (type == SymType.STRING.ordinal())
			return new SymbolString("");
		else if (type == SymType.DOUBLE.ordinal())
			return new SymbolDouble(BigDecimal.ZERO);
		else if (type == SymType.INTEGER.ordinal())
			return new SymbolInteger(BigInteger.ZERO);
		else
			return null;
	};

	/**
	 * Gets Symboltype from String
	 * 
	 * @param s
	 *            String
	 * @return SymbolType
	 */
	public static SymType getTypeFromString(String s) {
		return SymType.valueOf(s);
	}

	private String			name;

	private final SymType	type;

	Symbol(String name, SymType type) {
		setName(name);
		this.type = type;
	}

	/**
	 * Add symbols
	 * 
	 * @param s
	 *            symbol to add
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol add(Symbol s) throws RecognitionException;

	/**
	 * and symbols
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol and(Symbol s) throws RecognitionException;

	/**
	 * clones the object
	 */
	@Override
	public abstract Symbol clone();

	/**
	 * Convert another Symbol to this symbols subclass
	 * 
	 * @param s
	 * @return a symbol of the same type as this class
	 * @throws RecognitionException
	 */
	public abstract Symbol convert(Symbol s) throws RecognitionException;

	/**
	 * Convert Symbol to SymbolDouble
	 * 
	 * @return SymbolString
	 * @throws RecognitionException
	 */
	public abstract SymbolDouble convertToDouble() throws RecognitionException;

	/**
	 * Convert Symbol to SymbolInteger
	 * 
	 * @return SymbolString
	 * @throws RecognitionException
	 */
	public abstract SymbolInteger convertToInt() throws RecognitionException;

	/**
	 * Convert Symbol to SymbolString
	 * 
	 * @return SymbolString
	 * @throws RecognitionException
	 */
	public abstract SymbolString convertToString() throws RecognitionException;

	/**
	 * Divide symbols
	 * 
	 * @param s
	 *            symbol to divide through
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol div(Symbol s) throws RecognitionException;

	/**
	 * test if symbol is equal to other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract boolean eq(Symbol s) throws RecognitionException;

	/**
	 * test if symbol is greater or equal than other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract boolean ge(Symbol s) throws RecognitionException;

	/**
	 * @param bits
	 * @return
	 * @throws RecognitionException
	 */
	public abstract Symbol getBitList(LinkedHashSet<Symbol> bits) throws RecognitionException;

	/**
	 * Gets the name of a symbol
	 * 
	 * @return symbol name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get symbol type
	 * 
	 * @return symbol type
	 */
	public SymType getType() {
		return this.type;
	}

	/**
	 * Get string representation of symboltype
	 * 
	 * @return Symboltype string
	 */
	public String getTypeString() {
		switch (this.type) {
		case STRING:
			return "STRING";
		case DOUBLE:
			return "DOUBLE";
		case INTEGER:
			return "INTEGER";
		default:
			return "Unknown type!";
		}
	}

	/**
	 * Get symbol value as string
	 * 
	 * @return value as string
	 */
	public abstract String getValueString();

	/**
	 * test if symbol is greater than other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract boolean gt(Symbol s) throws RecognitionException;

	/**
	 * test if symbol is less than or equal to other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract boolean le(Symbol s) throws RecognitionException;

	/**
	 * test if symbol is less than other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract boolean lt(Symbol s) throws RecognitionException;

	/**
	 * max of symbol
	 * 
	 * @param s
	 *            to the power of
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol max(Symbol s) throws RecognitionException;

	/**
	 * min of symbol
	 * 
	 * @param s
	 *            to the power of
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol min(Symbol s) throws RecognitionException;

	/**
	 * Multiply symbols
	 * 
	 * @param s
	 *            symbol to multiply
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol mul(Symbol s) throws RecognitionException;

	/**
	 * not symbol
	 * 
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol not() throws RecognitionException;

	/**
	 * or symbols
	 * 
	 * @param s
	 *            symbol to or
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol or(Symbol s) throws RecognitionException;

	/**
	 * Sets the name of the symbol
	 * 
	 * @param s
	 *            name of the symbol
	 */
	public void setName(String s) {
		this.name = s;
	}

	/**
	 * Subtract symbols
	 * 
	 * @param s
	 *            symbol to subract
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol sub(Symbol s) throws RecognitionException;

	/**
	 * unary minus symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol uminus() throws RecognitionException;

	/**
	 * unary plus symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol uplus() throws RecognitionException;

	/**
	 * xor symbols
	 * 
	 * @param s
	 *            symbol to xor
	 * @return result
	 * @throws RecognitionException
	 */
	public abstract Symbol xor(Symbol s) throws RecognitionException;

}
