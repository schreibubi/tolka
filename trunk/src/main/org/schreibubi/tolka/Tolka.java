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
package org.schreibubi.tolka;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.runtime.tree.TreeWizard;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolInteger;
import org.schreibubi.tolka.AntlrExtension.IndexedCommonTree;
import org.schreibubi.tolka.AntlrExtension.IndexedCommonTreeAdaptor;
import org.schreibubi.tolka.AntlrExtension.IndexedCommonTreeNodeStream;
import org.schreibubi.tolka.DataFormats.BsreadFormat;
import org.schreibubi.tolka.DataFormats.CBMDatFormat;
import org.schreibubi.tolka.DataFormats.ChipIdFormat;
import org.schreibubi.tolka.DataFormats.DutBitstream;
import org.schreibubi.tolka.DataFormats.EfuseModuleFormat;
import org.schreibubi.tolka.DataFormats.ImportDataInterface;
import org.schreibubi.tolka.DataFormats.PlainFormat;
import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 *         Version history: 0.1 (Haching) initial release
 * 
 */
public class Tolka {

	private static int	dcCount	= 0;

	public static void addDclogHeader(PrintWriter pw) {
		pw
				.print("Rev=\"01\"\n"
						+ "LotNumber=\"ZA34234    \"\n"
						+ "BatchNumber=\"           \"\n"
						+ "TraceCode=\"           \"\n"
						+ "SpdCode=\"000\"\n"
						+ "Design=\"T70 DD1   512M  \"\n"
						+ "ChipType=\"A----\"\n"
						+ "ComponentType=\"11---\"\n"
						+ "TesterName=\"AMBTES-12       \"\n"
						+ "HandlerName_1=\"PFMPRO.23/P11   \"\n"
						+ "PbId_1=\"0001\"\n"
						+ "MbIdA_1=\"----\"\n"
						+ "MbIdB_1=\"----\"\n"
						+ "DsaIdA_1=\"----\"\n"
						+ "DsaIdB_1=\"----\"\n"
						+ "DsaIdC_1=\"----\"\n"
						+ "DsaIdD_1=\"----\"\n"
						+ "HandlerName_2=\"PFMPRO.24/P12   \"\n"
						+ "PbId_2=\"0000\"\n"
						+ "MbIdA_2=\"----\"\n"
						+ "MbIdB_2=\"----\"\n"
						+ "DsaIdA_2=\"----\"\n"
						+ "DsaIdB_2=\"----\"\n"
						+ "DsaIdC_2=\"----\"\n"
						+ "DsaIdD_2=\"----\"\n"
						+ "PrgName=\"A2UH6XBC       \"\n"
						+ "PrgRev=\"AT70  \"\n"
						+ "MeasKey=\"A2    \"\n"
						+ "MeasType=\"0----\"\n"
						+ "Temp=\"+095C\"\n"
						+ "Offset1=\"+3.750NS\"\n"
						+ "Offset2=\"+0.350NS\"\n"
						+ "Date=\"20070420\"\n"
						+ "Time=\"102958\"\n"
						+ "                                                                                                          ;A-FLOW_DC\n"
						+ "DATASET :TDWN :R:DEVCNT :MR:CN:DN:DB:TNAME :DUT:SERIALNUMBER    :LOTID   :DESIGN          :WF:X :Y :PS:LOC;LTNAME                          ,RESULT\n");
	}

	public static void addDclogPrefix(PrintWriter pw, int dut, String chipid, String tname) {
		dcCount++;
		DecimalFormat f8 = new DecimalFormat("00000000");
		DecimalFormat f7 = new DecimalFormat("0000000");
		DecimalFormat f3 = new DecimalFormat("000");
		pw.print(f8.format(dcCount) + ":00001:0:" + f7.format(dcCount) + ":  :  :  :  :" + tname + ":"
				+ f3.format(dut + 1) + ":                :" + chipid + ";" + tname + "                          ,");
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		try {
			CommandLineParser CLparser = new PosixParser();

			options.addOption(OptionBuilder.withLongOpt("format").withDescription("format, either plain or chipid")
					.hasArg().withArgName("format").create('f'));
			options.addOption(OptionBuilder.withLongOpt("dclog").withDescription("output results in dclog format")
					.hasArg().withArgName("file").create('d'));
			options.addOption(OptionBuilder.withLongOpt("help").withDescription("help").create('h'));
			options.addOption(OptionBuilder.withLongOpt("version").withDescription("version").create('v'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("h")) {
				printHelpAndExit(options);
			}

			if (line.hasOption("v")) {
				Info.printVersion("Tolka");
				Runtime.getRuntime().exit(0);
			}

			String dclogFile = null;
			if (line.hasOption("d")) {
				dclogFile = line.getOptionValue("d");
			}

			String[] leftargs = line.getArgs();

			if (leftargs.length < 2) {
				System.err.println("Not enough arguments, see -h or --help");
				Runtime.getRuntime().exit(0);
			} else {
				try {
					BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(leftargs[0]));
					fileInputStream.mark(1100);
					byte[] start = new byte[1024];
					fileInputStream.read(start);
					fileInputStream.reset();

					ImportDataInterface in = null;
					if (EfuseModuleFormat.testForMagic(start)) {
						System.out.println("Detected e-fuse modules format");
						in = new EfuseModuleFormat();
					} else if (BsreadFormat.testForMagic(start)) {
						System.out.println("Detected generic BE bitstream format");
						in = new BsreadFormat();
					} else if (line.hasOption("f")) {
						String format = line.getOptionValue("f");
						if (format.contains("chipid")) {
							System.out.println("BE chipid format");
							in = new ChipIdFormat();
						} else if (format.contains("plain")) {
							System.out.println("Plain format");
							in = new PlainFormat();
						} else if (format.contains("cbm")) {
							System.out.println("CBM format");
							in = new CBMDatFormat();
						} else {
							System.err.println("Unknown file format specified");
							Runtime.getRuntime().exit(0);
						}
					} else {
						System.err.println("File format could not be autodetected, please use --format");
						Runtime.getRuntime().exit(0);
					}
					VArrayList<DutBitstream> bitstreamList = null;
					try {
						bitstreamList = in.readData(fileInputStream);
					} catch (Exception e) {
						e.printStackTrace();
					}
					fileInputStream.close();

					System.out.println("Raw data (MSB..LSB):");
					for (DutBitstream bitstream : bitstreamList) {
						System.out.print(bitstream.getInfo() + ":");
						for (int stream = 0; stream < bitstream.getStreamCount(); stream++) {
							for (int bit = in.getBitLength() - 1; bit >= 0; bit--) {
								if (bit < bitstream.getBitstream(stream).bitLength()) {
									if (bitstream.getBitstream(stream).testBit(bit)) {
										System.out.print("1");
									} else {
										System.out.print("0");
									}
								} else {
									System.out.print("0");
								}
							}
							System.out.print(":");
						}
						System.out.println();
					}

					CharStream input = new ANTLRFileStream(leftargs[1]);
					TolkaGrammarLexer lex = new TolkaGrammarLexer(input);
					CommonTokenStream tokens = new CommonTokenStream(lex);
					TolkaGrammarParser parser = new TolkaGrammarParser(tokens);
					TreeAdaptor adaptor = new IndexedCommonTreeAdaptor();
					String[] tokenNames = parser.getTokenNames();
					parser.setTreeAdaptor(adaptor);
					TolkaGrammarParser.rules_return r = parser.rules();
					// System.out.println("tree: " + ((Tree)
					// r.getTree()).toStringTree());

					IndexedCommonTree t = (IndexedCommonTree) r.getTree();

					// ASTFrame af = new ASTFrame("Tree", t);
					// af.setVisible(true);

					// Construct the tree.

					System.out.println("Interpreted data:");

					IndexedCommonTreeNodeStream nodes = new IndexedCommonTreeNodeStream(t);
					int dummy = nodes.size(); // do not delete!
					TreeWizard wiz = new TreeWizard(adaptor, tokenNames);
					final LinkedHashMap<String, Integer> rules = new LinkedHashMap<String, Integer>();
					wiz.visit(t, wiz.getTokenType("RULE"), new TreeWizard.Visitor() {
						@Override
						public void visit(Object t) {
							String name = ((IndexedCommonTree) t).getChild(0).getText();
							rules.put(name, ((IndexedCommonTree) t).getIndex());
						}
					});

					TolkaTreeWalker walker = new TolkaTreeWalker(nodes);
					walker.rulesLookup = rules;
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					if (dclogFile != null) {
						addDclogHeader(pw);
					}
					for (DutBitstream bitstream : bitstreamList) {
						nodes.rewind();
						if (dclogFile != null) {
							addDclogPrefix(pw, bitstream.getDut(), bitstream.getInfo(), bitstream.getTname());
						}
						ArrayList<Symbol> params = new ArrayList<Symbol>();
						for (int stream = 0; stream < bitstream.getStreamCount(); stream++) {
							params.add(new SymbolInteger(bitstream.getBitstream(stream)));
						}
						walker.rulestart(pw, params);
					}
					System.out.println(sw.toString());
					// Write also to dclog-file
					if (dclogFile != null) {
						PrintWriter dcw = new PrintWriter(new BufferedWriter(new FileWriter(dclogFile)));
						dcw.print(sw.toString());
						dcw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (RecognitionException e) {
					e.printStackTrace();
				}
			}
		} catch (ParseException e) {
			printHelpAndExit(options);
		} catch (Exception e) {
			System.out.println("Tolka error: " + e);
		}

	}

	/**
	 * @param options
	 */
	public static void printHelpAndExit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(Info.getVersionString("Tolka input-file config-file"), options);
		Runtime.getRuntime().exit(0);
	}
}
