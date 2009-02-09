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
package org.schreibubi.tolka.UserInterface;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.prefs.Preferences;

import org.schreibubi.tolka.DataFormats.BsreadFormat;
import org.schreibubi.tolka.DataFormats.ChipIdFormat;
import org.schreibubi.tolka.DataFormats.DutBitstream;
import org.schreibubi.tolka.DataFormats.EfuseModuleFormat;
import org.schreibubi.tolka.DataFormats.ImportDataInterface;
import org.schreibubi.tolka.DataFormats.PlainFormat;
import org.schreibubi.visitor.VArrayList;

import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.QTextStream;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QSyntaxHighlighter;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextDocument;

/**
 * @author Jörg Werner
 * 
 */
public class MainWindow extends QMainWindow {

	private class DecodingHighlighter extends QSyntaxHighlighter {

		public class HighlightingRule {
			public QTextCharFormat	format;
			public QRegExp			pattern;

			public HighlightingRule(QRegExp pattern, QTextCharFormat format) {
				this.pattern = pattern;
				this.format = format;
			}
		}

		QRegExp						commentStartExpression;
		QRegExp						commentEndExpression;

		QTextCharFormat				classFormat			= new QTextCharFormat();
		QTextCharFormat				commentFormat		= new QTextCharFormat();
		QTextCharFormat				functionFormat		= new QTextCharFormat();
		QTextCharFormat				keywordFormat		= new QTextCharFormat();
		QTextCharFormat				quotationFormat		= new QTextCharFormat();

		Vector<HighlightingRule>	highlightingRules	= new Vector<HighlightingRule>();

		public DecodingHighlighter(QTextDocument parent) {

			super(parent);

			HighlightingRule rule;
			QBrush brush;
			QRegExp pattern;

			brush = new QBrush(QColor.darkBlue, Qt.BrushStyle.SolidPattern);
			keywordFormat.setForeground(brush);
			keywordFormat.setFontWeight(QFont.Weight.Bold.value());

			// All the java keywords
			String[] keywords = { "print", "println", "char", "hex", "bin" };

			for (String keyword : keywords) {
				pattern = new QRegExp("\\b" + keyword + "\\b");
				rule = new HighlightingRule(pattern, keywordFormat);
				highlightingRules.add(rule);
			}

			// Comment starting with //
			brush = new QBrush(QColor.gray, Qt.BrushStyle.SolidPattern);
			pattern = new QRegExp("//[^\n]*");
			commentFormat.setForeground(brush);
			rule = new HighlightingRule(pattern, commentFormat);
			highlightingRules.add(rule);

			// String
			brush = new QBrush(QColor.blue, Qt.BrushStyle.SolidPattern);
			pattern = new QRegExp("\"[^\"]*\"");
			quotationFormat.setForeground(brush);
			rule = new HighlightingRule(pattern, quotationFormat);
			highlightingRules.add(rule);

			// Function
			brush = new QBrush(QColor.darkGreen, Qt.BrushStyle.SolidPattern);
			pattern = new QRegExp("\\b[A-Za-z0-9_]+(?=\\[)");
			functionFormat.setForeground(brush);
			rule = new HighlightingRule(pattern, functionFormat);
			highlightingRules.add(rule);

			// Block comment
			commentStartExpression = new QRegExp("/\\*");
			commentEndExpression = new QRegExp("\\*/");
		}

		@Override
		public void highlightBlock(String text) {

			for (HighlightingRule rule : highlightingRules) {
				QRegExp expression = rule.pattern;
				int index = expression.indexIn(text);
				while (index >= 0) {
					int length = expression.matchedLength();
					setFormat(index, length, rule.format);
					index = expression.indexIn(text, index + length);
				}
			}
			setCurrentBlockState(0);

			int startIndex = 0;
			if (previousBlockState() != 1)
				startIndex = commentStartExpression.indexIn(text);

			while (startIndex >= 0) {
				int endIndex = commentEndExpression.indexIn(text, startIndex);
				int commentLength;
				if (endIndex == -1) {
					setCurrentBlockState(1);
					commentLength = text.length() - startIndex;
				} else
					commentLength = endIndex - startIndex + commentEndExpression.matchedLength();
				setFormat(startIndex, commentLength, commentFormat);
				startIndex = commentStartExpression.indexIn(text, startIndex + commentLength);
			}
		}
	}

	private enum whoHasFocusEnum {
		DECODING, INPUT_DATA, OUTPUT_DATA
	}

	private class ZeroOneHighlighter extends QSyntaxHighlighter {

		QTextCharFormat	oneFormat	= new QTextCharFormat();
		QTextCharFormat	zeroFormat	= new QTextCharFormat();

		public ZeroOneHighlighter(QTextDocument parent) {

			super(parent);

			QBrush brush;

			brush = new QBrush(QColor.darkBlue, Qt.BrushStyle.SolidPattern);
			zeroFormat.setForeground(brush);

			brush = new QBrush(QColor.darkRed, Qt.BrushStyle.SolidPattern);
			oneFormat.setForeground(brush);

		}

		@Override
		public void highlightBlock(String text) {
			{
				QRegExp expression = new QRegExp("1+");
				int index = expression.indexIn(text);
				while (index >= 0) {
					int length = expression.matchedLength();
					setFormat(index, length, oneFormat);
					index = expression.indexIn(text, index + length);
				}
			}
			{
				QRegExp expression = new QRegExp("0+");
				int index = expression.indexIn(text);
				while (index >= 0) {
					int length = expression.matchedLength();
					setFormat(index, length, zeroFormat);
					index = expression.indexIn(text, index + length);
				}
			}
			setCurrentBlockState(0);

		}
	}

	private static final String	DECODING_DIR_PREF		= "decodingDir";

	private static final String	INPUT_DATA_DIR_PREF		= "inputDataDir";

	private static final String	OUTPUT_DATA_DIR_PREF	= "outputDataDir";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("OS Name: " + System.getProperty("os.name"));
		System.out.println("OS Architecture: " + System.getProperty("os.arch"));
		System.out.println("OS Version: " + System.getProperty("os.version"));
		QApplication.initialize(args);
		MainWindow mainw = new MainWindow();
		mainw.show();
		QApplication.exec();
	}

	Ui_MainWindow	mainWindowUi	= new Ui_MainWindow();

	public MainWindow() {
		// Place what you made in Designer onto the main window.
		mainWindowUi.setupUi(this);

		new ZeroOneHighlighter(mainWindowUi.inputEdit.document());
		new DecodingHighlighter(mainWindowUi.programEdit.document());
		// setWindowIcon(new
		// QIcon("classpath:com/trolltech/images/qt-logo.png"));
	}

	public whoHasFocusEnum checkFocus() {
		if (mainWindowUi.outputResult.hasFocus())
			return whoHasFocusEnum.OUTPUT_DATA;
		else if (mainWindowUi.inputEdit.hasFocus())
			return whoHasFocusEnum.INPUT_DATA;
		else if (mainWindowUi.programEdit.hasFocus())
			return whoHasFocusEnum.DECODING;
		return whoHasFocusEnum.DECODING;
	}

	public void on_actionCopy_triggered() {
		System.out.println("laod data");
	}

	public void on_actionCut_triggered() {
		System.out.println("laod data");
	}

	public void on_actionLoad_Data_triggered() {
		final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		String dirString = prefs.get(INPUT_DATA_DIR_PREF, null);
		if (dirString == null)
			dirString = "C:\\";
		String fileName = QFileDialog.getOpenFileName(this, tr("Open File"), dirString, new QFileDialog.Filter(
				"Data File (*.*)"));

		// QMessageBox.warning(this, tr("Application"), String.format(tr("Cannot
		// read file %1$s:\n%2$s."), fileName, file.errorString()));
		// return;

		BufferedInputStream fileInputStream;
		try {
			fileInputStream = new BufferedInputStream(new FileInputStream(fileName));
			fileInputStream.mark(1100);
			byte[] start = new byte[1024];
			fileInputStream.read(start);
			fileInputStream.reset();
			ImportDataInterface in = null;
			if (EfuseModuleFormat.testForMagic(start))
				in = new EfuseModuleFormat();
			else if (BsreadFormat.testForMagic(start))
				in = new BsreadFormat();
			else {
				FormatSelect f = new FormatSelect(this);
				if (f.exec() == 1) {
					if (f.getSelection() == 1)
						in = new ChipIdFormat();
					else if (f.getSelection() == 2)
						in = new PlainFormat();
				} else
					return;
			}
			QApplication.setOverrideCursor(new QCursor(Qt.CursorShape.WaitCursor));
			VArrayList<DutBitstream> list = null;
			try {
				list = in.readData(fileInputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileInputStream.close();

			String v = "";
			for (DutBitstream i : list) {
				v += i.getInfo() + ":";
				for (int bit = 0; bit < in.getBitLength(); bit++)
					if (bit < i.getBitstream().bitLength()) {
						if (i.getBitstream().testBit(bit))
							v += "1";
						else
							v += "0";
					} else
						v += "0";
				v += "\n";
			}

			QApplication.restoreOverrideCursor();
			mainWindowUi.inputEdit.setPlainText(v);
			prefs.put(INPUT_DATA_DIR_PREF, fileName);

			statusBar().showMessage(tr("Input Data loaded"), 2000);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void on_actionLoad_decoding_triggered() {
		final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		String dirString = prefs.get(DECODING_DIR_PREF, null);
		if (dirString == null)
			dirString = "C:\\";

		String fileName = QFileDialog.getOpenFileName(this, tr("Load Tolka file"), dirString,
				new QFileDialog.Filter("Tolka File (*.fbd)"));
		QFile file = new QFile(fileName);
		if (!file.open(new QFile.OpenMode(QFile.OpenModeFlag.ReadOnly, QFile.OpenModeFlag.Text))) {
			QMessageBox.warning(this, tr("Application"), String.format(tr("Cannot read file %1$s:\n%2$s."), fileName,
					file.errorString()));
			return;
		}
		QTextStream in = new QTextStream(file);
		QApplication.setOverrideCursor(new QCursor(Qt.CursorShape.WaitCursor));
		mainWindowUi.programEdit.setPlainText(in.readAll());
		QApplication.restoreOverrideCursor();
		prefs.put(DECODING_DIR_PREF, fileName);

		statusBar().showMessage(tr("File loaded"), 2000);
	}

	public void on_actionPaste_triggered() {
	}

	public void on_actionQuit_triggered() {
		close();
	}

	public void on_actionSave_decoding_triggered() {
		final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		String dirString = prefs.get(DECODING_DIR_PREF, null);
		if (dirString == null)
			dirString = "C:\\";

		String fileName = QFileDialog.getSaveFileName(this, tr("Save Tolka file"), dirString,
				new QFileDialog.Filter("Tolka File (*.fbd)"));

		QFile file = new QFile(fileName);
		if (!file.open(new QFile.OpenMode(QFile.OpenModeFlag.WriteOnly, QFile.OpenModeFlag.Text))) {
			QMessageBox.warning(this, tr("Application"), String.format(tr("Cannot write file %1$s:\n%2$s."), fileName,
					file.errorString()));
			return;
		}

		QApplication.setOverrideCursor(new QCursor(Qt.CursorShape.WaitCursor));
		QTextStream out = new QTextStream(file);
		out.writeString(mainWindowUi.programEdit.toPlainText());
		file.close();
		QApplication.restoreOverrideCursor();
		prefs.put(DECODING_DIR_PREF, fileName);
		statusBar().showMessage(tr("File saved"), 2000);
	}

	public void on_actionSave_results_triggered() {
		final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		String dirString = prefs.get(OUTPUT_DATA_DIR_PREF, null);
		if (dirString == null)
			dirString = "C:\\";

		String fileName = QFileDialog.getSaveFileName(this, tr("Save result file"), dirString, new QFileDialog.Filter(
				"Result File (*.*)"));
		QFile file = new QFile(fileName);
		if (!file.open(new QFile.OpenMode(QFile.OpenModeFlag.WriteOnly, QFile.OpenModeFlag.Text))) {
			QMessageBox.warning(this, tr("Application"), String.format(tr("Cannot write file %1$s:\n%2$s."), fileName,
					file.errorString()));
			return;
		}

		QApplication.setOverrideCursor(new QCursor(Qt.CursorShape.WaitCursor));
		QTextStream out = new QTextStream(file);
		out.writeString(mainWindowUi.outputResult.toPlainText());
		file.close();
		QApplication.restoreOverrideCursor();
		prefs.put(OUTPUT_DATA_DIR_PREF, fileName);
		statusBar().showMessage(tr("Result file saved"), 2000);
	}

	public void on_runButton_clicked() {
		System.out.println("run");
	}

}
