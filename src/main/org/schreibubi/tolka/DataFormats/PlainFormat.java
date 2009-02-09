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
package org.schreibubi.tolka.DataFormats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class PlainFormat implements ImportDataInterface {

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
		BufferedReader inreader = new BufferedReader(new InputStreamReader(in));
		VArrayList<DutBitstream> chipIds = new VArrayList<DutBitstream>();
		String s = null;
		int c = 1;
		while ((s = inreader.readLine()) != null) { // Read line
			String r = new StringBuffer(s.trim()).reverse().toString();
			maxLength = Math.max(maxLength, r.length());
			chipIds.add(new DutBitstream(new BigInteger(r, 2), "" + c, c, ""));
			c++;
		}
		return chipIds;
	}
}
