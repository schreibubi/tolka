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
/**
 *
 */
package org.schreibubi.tolka.DataFormats;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintStream;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class TouchdownChunk extends Chunk {

	int					MinDut;

	int					TouchDownCount;

	int					MaxDut;

	VArrayList<String>	chipIds	= new VArrayList<String>();

	VArrayList<Long>	dutMask	= new VArrayList<Long>();

	public TouchdownChunk(DataInput instream) throws IOException {
		setChunkType(ChunkTypes.TOUCHDOWN);
		read(instream);
	}

	public String getChipId(int i) {
		return chipIds.get(i);
	}

	public VArrayList<Long> getDutMask() {
		return dutMask;
	}

	public int getMaxDut() {
		return MaxDut;
	}

	public int getMinDut() {
		return MinDut;
	}

	public int getTouchDownCount() {
		return TouchDownCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.DataFormats.Chunk#print(java.io.PrintStream)
	 */
	@Override
	public void print(PrintStream out) {
		for (int i = getMinDut(); i <= getMaxDut(); i++)
			System.out.println(getChipId(i));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.DataFormats.Chunk#read(java.io.InputStream)
	 */
	@Override
	public void read(DataInput instream) throws IOException {
		setVersion(readInt16(instream));
		setTouchDownCount(readInt16(instream));
		int min = readInt16(instream);
		setMinDut(--min);
		int max = readInt16(instream);
		setMaxDut(--max);
		for (int i = getMinDut(); i <= getMaxDut(); i++) {
			String accumulate = "";
			for (int j = 0; j < 43; j++) {
				int e;
				e = instream.readUnsignedByte();
				accumulate += (char) e;
			}
			setChipId(i, accumulate.substring(0, accumulate.length() - 2)); // strip
			// off
			// last
			// colon and
			// linefeed
		}
		if (getVersion() > 1) { // Version 2 also contains the DUT masking
			dutMask.clear();
			for (int i = 0; i < 8; i++)
				dutMask.add(readInt64(instream));
		}

	}

	public void setChipId(int i, String s) {
		if (chipIds.size() - 1 < i)
			for (int j = chipIds.size() - 1; j < i; j++)
				chipIds.add(null);
		chipIds.set(i, s);
	}

	public void setDutMask(VArrayList<Long> dutMask) {
		this.dutMask = dutMask;
	}

	public void setMaxDut(int maxDut) {
		MaxDut = maxDut;
	}

	public void setMinDut(int minDut) {
		MinDut = minDut;
	}

	public void setTouchDownCount(int touchDownCount) {
		TouchDownCount = touchDownCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.DataFormats.Chunk#write(java.io.OutputStream)
	 */
	@Override
	public void write(DataOutput outstream) {
	}

}
