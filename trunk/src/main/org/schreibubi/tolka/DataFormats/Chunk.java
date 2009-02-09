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

/**
 * @author Jörg Werner
 * 
 */
public abstract class Chunk {

	public enum ChunkTypes {
		HEADER, TOUCHDOWN, BITSTREAM, MPR
	};

	public static String readATLString(DataInput instream) throws IOException {
		int length = instream.readInt();
		String accumulate = "";
		for (int j = 0; j < length; j++) {
			int e = instream.readUnsignedByte();
			accumulate += (char) e;
		}
		return accumulate;
	}

	public static Chunk readChunk(DataInput instream) throws IOException {
		int id = readInt32(instream);
		switch (id) {
		case 0x48454452: // HEDR
			return new HeaderChunk(instream);
			// case 0x41464844: // AFHD
			// return new AfmHeader(instream);
		case 0x5444574E: // TDWN
			return new TouchdownChunk(instream);
		case 0x45465553: // EFUS
			return new BitstreamChunk(instream);
		case 0x42495352: // BISR
			return new BitstreamChunk(instream);
		case 0x4D505244: // MPRD
			return new BitstreamChunk(instream);
		case 0x57495352: // WISR
			return new MPRChunk(instream);
		default:
			return null;
		}
	}

	public static int readInt16(DataInput instream) throws IOException {
		return instream.readShort();
	}

	public static int readInt32(DataInput instream) throws IOException {
		return instream.readInt();
	}

	public static long readInt64(DataInput instream) throws IOException {
		return instream.readLong();
	}

	protected ChunkTypes	chunkType	= null;

	protected int			version;

	public ChunkTypes getChunkType() {
		return chunkType;
	}

	public int getVersion() {
		return version;
	}

	public abstract void print(PrintStream out) throws IOException;

	public abstract void read(DataInput instream) throws IOException;

	public void setChunkType(ChunkTypes chunkType) {
		this.chunkType = chunkType;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public abstract void write(DataOutput outstream) throws IOException;

}
