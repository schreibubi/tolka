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
public class BitstreamChunk extends Chunk {

	private int								bitstreamLength;

	private final VArrayList<BigInteger>	bitStreams	= new VArrayList<BigInteger>();

	public BitstreamChunk(DataInput instream) throws IOException {
		setChunkType(ChunkTypes.BITSTREAM);
		read(instream);
	}

	@Override
	public void print(PrintStream out) {
		for (int i = 0; i < 8 * 64; i++) {
			out.println("DUT " + i + ":" + getBitstreamAsText(i));
		}
	}

	@Override
	public void read(DataInput instream) throws IOException {
		setVersion(readInt16(instream));
		setBitstreamLength(readInt16(instream));
		byte[] rawData = null;
		rawData = new byte[getBitstreamLength() * 8 * 8];
		instream.readFully(rawData);
		final int dutConst = 512;
		for (int dut = 0; dut < dutConst; dut++) {
			bitStreams.add(BigInteger.ZERO);
		}
		for (int bit = 0; bit < getBitstreamLength(); bit++) {
			// bits
			for (int dut = 0; dut < dutConst; dut++) {
				BigInteger bI = bitStreams.get(dut);
				int mask = 1 << (dut % 8);
				int byt = dut / 8;
				int b = rawData[bit * 8 * 8 + (byt / 8 + 1) * 8 - 1 - (byt % 8)] & 0xFF;
				if ((b & mask) > 0) {
					bI = bI.setBit(bit);
				} else {
					bI = bI.clearBit(bit);
				}
				bitStreams.set(dut, bI);
			}
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
				if (i.testBit(bit)) {
					str += "1";
				} else {
					str += "0";
				}
			} else {
				str += "0";
			}
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
