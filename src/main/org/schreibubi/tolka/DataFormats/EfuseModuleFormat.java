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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class EfuseModuleFormat implements ImportDataInterface {

	public static boolean testForMagic(byte[] start) {
		int magic = (((start[0] & 0xff) << 24) | ((start[1] & 0xff) << 16) | ((start[2] & 0xff) << 8) | (start[3] & 0xff));
		if (magic == 0x45465553)
			return true;
		else
			return false;
	}

	private int	efBitsNum	= 0;

	public int getBitLength() {
		return efBitsNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.tolka.ImportInterface#readFile(java.io.InputStream)
	 */
	public VArrayList<DutBitstream> readData(InputStream instream) throws IOException, Exception {
		DataInputStream in = new DataInputStream(instream);
		VArrayList<DutBitstream> bitstreams = new VArrayList<DutBitstream>();

		byte[] id = new byte[16];
		in.read(id);
		// if (id != 0x45465553) {
		// throw new Exception("Wrong file type!");
		// }

		int version1 = Chunk.readInt32(in);
		int version2 = Chunk.readInt32(in);
		if ((version1 != 1) | (version2 != 0))
			throw new Exception("Wrong version " + version1 + "." + version2 + "!");

		efBitsNum = Chunk.readInt32(in); /*
										 * number of eFuse bits per device
										 */
		int devNum = Chunk.readInt32(in); /* number of devices per module */

		int devOrga = Chunk.readInt32(in); /*
											 * organization on module (x4/x8/x16)
											 */
		int ranks = Chunk.readInt32(in); /* number of ranks per module */
		int duts = Chunk.readInt32(in); /* number of duts */
		int readoutNum = Chunk.readInt32(in); /*
											 * readout number => final file name
											 */
		byte useSockets[] = new byte[16];
		in.read(useSockets);

		VArrayList<Integer> usedDuts = new VArrayList<Integer>();
		for (int dutCnt = 0; dutCnt < duts; dutCnt++)
			if (useSockets[dutCnt] != ' ') {
				usedDuts.add(dutCnt);
			}

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern("00");

		for (int d = 0; d < usedDuts.size() * ranks * devNum; d++) {
			int dut = (usedDuts.get(d / (ranks * devNum)) + 1);
			int rank = ((d % (ranks * devNum)) / devNum + 1);
			int device = (d % (ranks * devNum) + 1);
			bitstreams.add(new DutBitstream(BigInteger.ZERO, myFormatter.format(dut) + ":" + myFormatter.format(rank)
					+ ":" + myFormatter.format(device), dut, ""));
		}

		byte reserved[] = new byte[16];
		in.read(reserved);

		byte buffer[] = new byte[8];
		for (int rankCnt = 0; rankCnt < ranks; rankCnt++) {
			in.read(buffer);
			if (buffer[7] != rankCnt + 1)
				throw new Exception("rank is wrong!");
			for (int bitCnt = efBitsNum - 1; bitCnt >= 0; bitCnt--) {
				in.read(buffer);
				if (buffer[7] != bitCnt)
					throw new Exception("bitcount is wrong!");
				byte failBits[] = new byte[12 * duts];
				in.readFully(failBits);
				for (int d = 0; d < usedDuts.size(); d++) {
					int dutCnt = usedDuts.get(d);
					for (int devCnt = 0; devCnt < devNum; devCnt++) {
						int result = 0;
						switch (devOrga) {
						case 4:
							int mask = 0xF0 >> (devCnt % 2) * 4;
							result = failBits[12 * dutCnt + devCnt / 2] & mask;
							break;
						case 8:
							result = failBits[12 * dutCnt + devCnt];
							break;
						case 16:
							result = (failBits[12 * dutCnt + 2 * devCnt] << 8) | failBits[12 * dutCnt + 2 * devCnt + 1];
							break;
						}
						BigInteger bI = bitstreams.get(d * ranks * devNum + rankCnt * devNum + devCnt).getBitstream(0);
						if (result != 0) {
							bI = bI.setBit(bitCnt);
						} else {
							bI = bI.clearBit(bitCnt);
						}
						bitstreams.get(d * ranks * devNum + rankCnt * devNum + devCnt).setBitstream(bI, 0);

					}
				}

			}

		}

		return bitstreams;
	}
}
