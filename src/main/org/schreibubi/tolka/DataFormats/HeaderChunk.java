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
public class HeaderChunk extends Chunk {

	private int		Revision;

	private String	LotNumber;

	private String	BatchNumber;

	private String	TraceCode;

	private String	SpdCode;

	private String	Design;

	private String	ChipType;

	private String	ComponentType;

	private String	TesterName;

	private String	HandlerName_1;

	private String	PbId_1;

	private String	MbIdA_1;

	private String	MbIdB_1;

	private String	DsaIdA_1;

	private String	DsaIdB_1;

	private String	DsaIdC_1;

	private String	DsaIdD_1;

	private String	HandlerName_2;

	private String	PbId_2;

	private String	MbIdA_2;

	private String	MbIdB_2;

	private String	DsaIdA_2;

	private String	DsaIdB_2;

	private String	DsaIdC_2;

	private String	DsaIdD_2;

	private String	PrgName;

	private String	PrgRev;

	private String	MeasKey;

	private String	MeasType;

	private String	Temp;

	private String	Offset1;

	private String	Offset2;

	private String	Date;

	private String	Time;

	HeaderChunk(DataInput instream) throws IOException {
		setChunkType(ChunkTypes.HEADER);
		read(instream);
	}

	public String getBatchNumber() {
		return BatchNumber;
	}

	public String getChipType() {
		return ChipType;
	}

	public String getComponentType() {
		return ComponentType;
	}

	public String getDate() {
		return Date;
	}

	public String getDesign() {
		return Design;
	}

	public String getDsaIdA_1() {
		return DsaIdA_1;
	}

	public String getDsaIdA_2() {
		return DsaIdA_2;
	}

	public String getDsaIdB_1() {
		return DsaIdB_1;
	}

	public String getDsaIdB_2() {
		return DsaIdB_2;
	}

	public String getDsaIdC_1() {
		return DsaIdC_1;
	}

	public String getDsaIdC_2() {
		return DsaIdC_2;
	}

	public String getDsaIdD_1() {
		return DsaIdD_1;
	}

	public String getDsaIdD_2() {
		return DsaIdD_2;
	}

	public String getHandlerName_1() {
		return HandlerName_1;
	}

	public String getHandlerName_2() {
		return HandlerName_2;
	}

	public String getLotNumber() {
		return LotNumber;
	}

	public String getMbIdA_1() {
		return MbIdA_1;
	}

	public String getMbIdA_2() {
		return MbIdA_2;
	}

	public String getMbIdB_1() {
		return MbIdB_1;
	}

	public String getMbIdB_2() {
		return MbIdB_2;
	}

	public String getMeasKey() {
		return MeasKey;
	}

	public String getMeasType() {
		return MeasType;
	}

	public String getOffset1() {
		return Offset1;
	}

	public String getOffset2() {
		return Offset2;
	}

	public String getPbId_1() {
		return PbId_1;
	}

	public String getPbId_2() {
		return PbId_2;
	}

	public String getPrgName() {
		return PrgName;
	}

	public String getPrgRev() {
		return PrgRev;
	}

	public int getRevision() {
		return Revision;
	}

	public String getSpdCode() {
		return SpdCode;
	}

	public String getTemp() {
		return Temp;
	}

	public String getTesterName() {
		return TesterName;
	}

	public String getTime() {
		return Time;
	}

	public String getTraceCode() {
		return TraceCode;
	}

	@Override
	public void print(PrintStream out) {
		out.println("Rev=\"" + getRevision() + "\"");
		out.println("LotNumber=\"" + getLotNumber() + "\"");
		out.println("BatchNumber=\"" + getBatchNumber() + "\"");
		out.println("TraceCode=\"" + getTraceCode() + "\"");
		out.println("SpdCode=\"" + getSpdCode() + "\"");
		out.println("Design=\"" + getDesign() + "\"");
		out.println("ChipType=\"" + getChipType() + "\"");
		out.println("ComponentType=\"" + getComponentType() + "\"");
		out.println("TesterName=\"" + getTesterName() + "\"");
		out.println("HandlerName_1=\"" + getHandlerName_1() + "\"");
		out.println("PbId_1=\"" + getPbId_1() + "\"");
		out.println("MbIdA_1=\"" + getMbIdA_1() + "\"");
		out.println("MbIdB_1=\"" + getMbIdB_1() + "\"");
		out.println("DsaIdA_1=\"" + getDsaIdA_1() + "\"");
		out.println("DsaIdB_1=\"" + getDsaIdB_1() + "\"");
		out.println("DsaIdC_1=\"" + getDsaIdC_1() + "\"");
		out.println("DsaIdD_1=\"" + getDsaIdD_1() + "\"");
		out.println("HandlerName_2=\"" + getHandlerName_2() + "\"");
		out.println("PbId_2=\"" + getPbId_2() + "\"");
		out.println("MbIdA_2=\"" + getMbIdA_2() + "\"");
		out.println("MbIdB_2=\"" + getMbIdB_2() + "\"");
		out.println("DsaIdA_2=\"" + getDsaIdA_2() + "\"");
		out.println("DsaIdB_2=\"" + getDsaIdB_2() + "\"");
		out.println("DsaIdC_2=\"" + getDsaIdC_2() + "\"");
		out.println("DsaIdD_2=\"" + getDsaIdD_2() + "\"");
		out.println("PrgName=\"" + getPrgName() + "\"");
		out.println("PrgRev=\"" + getPrgRev() + "\"");
		out.println("MeasKey=\"" + getMeasKey() + "\"");
		out.println("MeasType=\"" + getMeasType() + "\"");
		out.println("Temp=\"" + getTemp() + "\"");
		out.println("Offset1=\"" + getOffset1() + "\"");
		out.println("Offset2=\"" + getOffset2() + "\"");
		out.println("Date=\"" + getDate() + "\"");
		out.println("Time=\"" + getTime() + "\"");
	}

	@Override
	public void read(DataInput instream) throws IOException {
		setVersion(readInt16(instream));
		setRevision(readInt32(instream));
		setLotNumber(readATLString(instream));
		setBatchNumber(readATLString(instream));
		setTraceCode(readATLString(instream));
		setSpdCode(readATLString(instream));
		setDesign(readATLString(instream));
		setChipType(readATLString(instream));
		setComponentType(readATLString(instream));
		setTesterName(readATLString(instream));
		setHandlerName_1(readATLString(instream));
		setPbId_1(readATLString(instream));
		setMbIdA_1(readATLString(instream));
		setMbIdB_1(readATLString(instream));
		setDsaIdA_1(readATLString(instream));
		setDsaIdB_1(readATLString(instream));
		setDsaIdC_1(readATLString(instream));
		setDsaIdD_1(readATLString(instream));
		setHandlerName_2(readATLString(instream));
		setPbId_2(readATLString(instream));
		setMbIdA_2(readATLString(instream));
		setMbIdB_2(readATLString(instream));
		setDsaIdA_2(readATLString(instream));
		setDsaIdB_2(readATLString(instream));
		setDsaIdC_2(readATLString(instream));
		setDsaIdD_2(readATLString(instream));
		setPrgName(readATLString(instream));
		setPrgRev(readATLString(instream));
		setMeasKey(readATLString(instream));
		setMeasType(readATLString(instream));
		setTemp(readATLString(instream));
		setOffset1(readATLString(instream));
		setOffset2(readATLString(instream));
		setDate(readATLString(instream));
		setTime(readATLString(instream));
	}

	public void setBatchNumber(String batchNumber) {
		BatchNumber = batchNumber;
	}

	public void setChipType(String chipType) {
		ChipType = chipType;
	}

	public void setComponentType(String componentType) {
		ComponentType = componentType;
	}

	public void setDate(String date) {
		Date = date;
	}

	public void setDesign(String design) {
		Design = design;
	}

	public void setDsaIdA_1(String dsaIdA_1) {
		DsaIdA_1 = dsaIdA_1;
	}

	public void setDsaIdA_2(String dsaIdA_2) {
		DsaIdA_2 = dsaIdA_2;
	}

	public void setDsaIdB_1(String dsaIdB_1) {
		DsaIdB_1 = dsaIdB_1;
	}

	public void setDsaIdB_2(String dsaIdB_2) {
		DsaIdB_2 = dsaIdB_2;
	}

	public void setDsaIdC_1(String dsaIdC_1) {
		DsaIdC_1 = dsaIdC_1;
	}

	public void setDsaIdC_2(String dsaIdC_2) {
		DsaIdC_2 = dsaIdC_2;
	}

	public void setDsaIdD_1(String dsaIdD_1) {
		DsaIdD_1 = dsaIdD_1;
	}

	public void setDsaIdD_2(String dsaIdD_2) {
		DsaIdD_2 = dsaIdD_2;
	}

	public void setHandlerName_1(String handlerName_1) {
		HandlerName_1 = handlerName_1;
	}

	public void setHandlerName_2(String handlerName_2) {
		HandlerName_2 = handlerName_2;
	}

	public void setLotNumber(String lotNumber) {
		LotNumber = lotNumber;
	}

	public void setMbIdA_1(String mbIdA_1) {
		MbIdA_1 = mbIdA_1;
	}

	public void setMbIdA_2(String mbIdA_2) {
		MbIdA_2 = mbIdA_2;
	}

	public void setMbIdB_1(String mbIdB_1) {
		MbIdB_1 = mbIdB_1;
	}

	public void setMbIdB_2(String mbIdB_2) {
		MbIdB_2 = mbIdB_2;
	}

	public void setMeasKey(String measKey) {
		MeasKey = measKey;
	}

	public void setMeasType(String measType) {
		MeasType = measType;
	}

	public void setOffset1(String offset1) {
		Offset1 = offset1;
	}

	public void setOffset2(String offset2) {
		Offset2 = offset2;
	}

	public void setPbId_1(String pbId_1) {
		PbId_1 = pbId_1;
	}

	public void setPbId_2(String pbId_2) {
		PbId_2 = pbId_2;
	}

	public void setPrgName(String prgName) {
		PrgName = prgName;
	}

	public void setPrgRev(String prgRev) {
		PrgRev = prgRev;
	}

	public void setRevision(int revision) {
		Revision = revision;
	}

	public void setSpdCode(String spdCode) {
		SpdCode = spdCode;
	}

	public void setTemp(String temp) {
		Temp = temp;
	}

	public void setTesterName(String testerName) {
		TesterName = testerName;
	}

	public void setTime(String time) {
		Time = time;
	}

	public void setTraceCode(String traceCode) {
		TraceCode = traceCode;
	}

	@Override
	public void write(DataOutput outstream) {
	}

}
