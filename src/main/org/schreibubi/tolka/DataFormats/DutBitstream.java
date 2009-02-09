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
/**
 *
 */
package org.schreibubi.tolka.DataFormats;

import java.math.BigInteger;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class DutBitstream {

	private VArrayList<BigInteger>	bitstream;

	private String					info;

	private int						dut;

	private String					tname;

	public DutBitstream(BigInteger bitstream, String info, int dut, String tname) {
		super();
		VArrayList<BigInteger> temp = new VArrayList<BigInteger>();
		temp.add(bitstream);
		init(info, dut, tname, temp);
	}

	public DutBitstream(VArrayList<BigInteger> bitstream, String info, int dut, String tname) {
		super();
		init(info, dut, tname, bitstream);
	}

	public void appendBitstream(BigInteger bitstream) {
		this.bitstream.add(bitstream);
	}

	public VArrayList<BigInteger> getBitstream() {
		return bitstream;
	}

	public BigInteger getBitstream(int index) {
		return bitstream.get(index);
	}

	public int getDut() {
		return dut;
	}

	public String getInfo() {
		return info;
	}

	public int getStreamCount() {
		return this.bitstream.size();
	}

	public String getTname() {
		return tname;
	}

	public void setBitstream(BigInteger bitstream, int index) {
		this.bitstream.set(index, bitstream);
	}

	public void setBitstream(VArrayList<BigInteger> bitstream) {
		this.bitstream = bitstream;
	}

	public void setDut(int dut) {
		this.dut = dut;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	/**
	 * @param info
	 * @param dut
	 * @param tname
	 * @param bitstream
	 */
	private void init(String info, int dut, String tname, VArrayList<BigInteger> bitstream) {
		this.bitstream = bitstream;
		this.info = info;
		this.dut = dut;
		this.tname = tname;
	}

}
