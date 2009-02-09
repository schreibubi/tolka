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
package org.schreibubi.tolka.DataFormats;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class CBMDatFormat implements ImportDataInterface {

	public static boolean testForMagic(byte[] start) {
		return true; // always true, because it has no magic!!!!
	}

	private int	maxLength	= 0;

	public int getBitLength() {
		return maxLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.ImportInterface#readFile(java.io.InputStream)
	 */
	public VArrayList<DutBitstream> readData(InputStream in) throws IOException {
		DataInputStream instream = new DataInputStream(in);

		VArrayList<DutBitstream> cbms = new VArrayList<DutBitstream>();

		int chunks = instream.readInt();
		for (int i = 0; i < chunks; i++) {
			int start = instream.readInt();
			int stop = instream.readInt();
			VArrayList<Integer> parallelChain = new VArrayList<Integer>();
			for (int j = start; j < stop; j++) {
				parallelChain.add(instream.readInt());
			}
			System.out.println("Chunk " + i + ": " + parallelChain);
		}

		VArrayList<BigInteger> bI = new VArrayList<BigInteger>();
		maxLength = 94;
		bI.add(new BigInteger(new StringBuffer(
				"0011010000111111111111111111111111111111111110001101001011111111111111111111111111111111111110")
				.reverse().toString(), 2));
		bI.add(new BigInteger(new StringBuffer(
				"0000000000000000000000001101011110011000000000000000000000000000000000000001100000000000000000")
				.reverse().toString(), 2));
		bI.add(new BigInteger(new StringBuffer(
				"0000000000000000000000001111111111111000000000000000000000000000000000011111100000000000000000")
				.reverse().toString(), 2));

		cbms.add(new DutBitstream(bI, "Seti", 1, ""));
		return cbms;
	}
}
