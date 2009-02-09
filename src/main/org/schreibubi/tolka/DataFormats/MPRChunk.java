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
import java.math.BigInteger;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class MPRChunk extends Chunk {

	private int								bitstreamLength;

	private int								dutMax;

	private final VArrayList<BigInteger>	bitStreams	= new VArrayList<BigInteger>();

	private String							tname;

	public MPRChunk(DataInput instream) throws IOException {
		setChunkType(ChunkTypes.MPR);
		read(instream);
	}

	public String getTname() {
		return tname;
	}

	@Override
	public void print(PrintStream out) {
		for (int i = 0; i < dutMax; i++)
			out.println("DUT " + i + ":" + getBitstreamAsText(i));
	}

	@Override
	public void read(DataInput instream) throws IOException {
		setVersion(readInt16(instream));
		int reads = readInt16(instream);
		dutMax = readInt16(instream);
		int dqnum = readInt16(instream);
		tname = readATLString(instream);
		setBitstreamLength(reads * dqnum);
		byte[] rawData = null;
		rawData = new byte[reads * dutMax * 4];
		instream.readFully(rawData);
		for (int dut = 0; dut < dutMax; dut++)
			bitStreams.add(BigInteger.ZERO);
		for (int read = 0; read < reads; read++)
			for (int dut = 0; dut < dutMax; dut++) {
				BigInteger bI = bitStreams.get(dut);
				for (int dq = 0; dq < dqnum; dq++) {
					int mask = 1 << (dq % 8);
					int byt = dq / 8;
					int b = rawData[(read * dutMax + dut) * 4 + 3 - byt] & 0xFF;
					if ((b & mask) > 0)
						bI = bI.setBit(dqnum * read + dqnum - 1 - dq);
					else
						bI = bI.clearBit(dqnum * read + dqnum - 1 - dq);
				}
				bitStreams.set(dut, bI);
			}
	}

	@Override
	public void write(DataOutput outstream) {
	}

	BigInteger getBitstream(int dut) {
		return bitStreams.get(dut);
	}

	String getBitstreamAsText(int dut) {
		String str = "";
		BigInteger i = bitStreams.get(dut);
		for (int bit = 0; bit < getBitstreamLength(); bit++)
			if (bit < i.bitLength()) {
				if (i.testBit(bit))
					str += "1";
				else
					str += "0";
			} else
				str += "0";
		return str;
	}

	int getBitstreamLength() {
		return bitstreamLength;
	}

	void setBitstream(int dut, BigInteger bitstream) {
	}

	void setBitstreamLength(int theValue) {
		bitstreamLength = theValue;
	}

}
