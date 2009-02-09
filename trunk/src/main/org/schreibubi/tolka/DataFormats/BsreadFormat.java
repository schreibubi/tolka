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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.schreibubi.tolka.DataFormats.Chunk.ChunkTypes;
import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class BsreadFormat implements ImportDataInterface {

	public static boolean testForMagic(byte[] start) {
		int magic = (((start[0] & 0xff) << 24) | ((start[1] & 0xff) << 16) | ((start[2] & 0xff) << 8) | (start[3] & 0xff));
		if ((magic == 0x45464D44) | (magic == 0x42535244))
			return true;
		else
			return false;
	}

	private int	maxBitStreamLength	= 0;

	public int getBitLength() {
		return maxBitStreamLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.ImportInterface#readFile(java.io.InputStream)
	 */
	public VArrayList<DutBitstream> readData(InputStream instream) throws IOException, Exception {
		DataInputStream in = new DataInputStream(instream);
		VArrayList<DutBitstream> bitstreams = new VArrayList<DutBitstream>();
		int id = Chunk.readInt32(in);
		if ((id != 0x45464D44L) && (id != 0x42535244L))
			throw new Exception("Wrong file type!");

		int version = Chunk.readInt16(in);
		if (version != 1)
			throw new Exception("Wrong version " + version + "!");

		Chunk header = Chunk.readChunk(in);
		if (header.getChunkType() != ChunkTypes.HEADER)
			throw new Exception("No Header found!");

		TouchdownChunk lastTouchdown = null;
		try {
			while (true) {
				Chunk c = Chunk.readChunk(in);
				if (c != null) {
					if (c.getChunkType() == ChunkTypes.TOUCHDOWN)
						lastTouchdown = (TouchdownChunk) c;
					// lastTouchdown.print(System.out);
					else if (c.getChunkType() == ChunkTypes.BITSTREAM) {
						BitstreamChunk bitstreamChunkRead = (BitstreamChunk) c;
						maxBitStreamLength = Math.max(maxBitStreamLength, bitstreamChunkRead.getBitstreamLength());
						// bitstreamChunkRead.print(System.out);
						int min = lastTouchdown.getMinDut();
						int max = lastTouchdown.getMaxDut();
						for (int dut = min; dut <= max; dut++)
							bitstreams.add(new DutBitstream(bitstreamChunkRead.getBitstream(dut), lastTouchdown
									.getChipId(dut), dut, ""));
					} else if (c.getChunkType() == ChunkTypes.MPR) {
						MPRChunk bitstreamChunkRead = (MPRChunk) c;
						maxBitStreamLength = Math.max(maxBitStreamLength, bitstreamChunkRead.getBitstreamLength());
						// bitstreamChunkRead.print(System.out);
						int min = lastTouchdown.getMinDut();
						int max = lastTouchdown.getMaxDut();
						for (int dut = min; dut <= max; dut++)
							bitstreams.add(new DutBitstream(bitstreamChunkRead.getBitstream(dut), lastTouchdown
									.getChipId(dut), dut, bitstreamChunkRead.getTname()));
					} else
						throw new Exception("Unknown Chunk found!");
				} else
					throw new Exception("Unknown Chunk found!");
			}
		} catch (EOFException e) {
			// System.out.println("finished");
		}
		return bitstreams;
	}
}
