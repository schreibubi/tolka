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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class ChipIdFormat implements ImportDataInterface {

	public static boolean testForMagic(byte[] start) {
		return true; // always true, because it has no magic!!!!
	}

	private final int	bitsConst		= 80;
	private final int	dutConst		= 128;

	private final int	dutBlockSize	= dutConst / 8 * bitsConst;

	public int getBitLength() {
		return bitsConst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.ImportInterface#readFile(java.io.InputStream)
	 */
	public VArrayList<DutBitstream> readData(InputStream in) throws IOException {
		VArrayList<DutBitstream> chipIds = new VArrayList<DutBitstream>();
		int offsetDUT = 0;
		byte[] rawData = new byte[dutBlockSize];
		while (in.read(rawData) == dutBlockSize) { // Read data for
			// first 128 DUTs
			for (int dut = 0; dut < dutConst; dut++) {
				chipIds.add(new DutBitstream(BigInteger.ZERO, "DUT " + dut, dut, ""));
			}
			for (int bit = 0; bit < bitsConst; bit++) {
				for (int dut = 0; dut < dutConst; dut++) {
					BigInteger bI = chipIds.get(dut + offsetDUT).getBitstream(0);
					int mask = 1 << dut % 8;
					int b = rawData[bit * dutConst / 8 + (dutConst / 8 - 1 - dut / 8)] & 0xFF;
					if ((b & mask) > 0) {
						bI = bI.setBit(bit);
					} else {
						bI = bI.clearBit(bit);
					}
					chipIds.get(dut + offsetDUT).setBitstream(bI, 0);
				}
			}
			offsetDUT += dutConst;
		}
		return chipIds;
	}
}
