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
package org.schreibubi.tolka;

/**
 * @author Jörg Werner
 * 
 */
public class Info {

	/**
	 * Version string, naming after towns around Munich
	 */
	public static final String	version		= "Version 0.15.2 (Codename: \"Riem\", SVN: $Rev: 126 $)";

	/**
	 * Authors
	 */
	public static final String	authors		= "Jörg Werner";

	/**
	 * Copyright
	 */
	public static final String	copyright	= "(C) 2009 Jörg Werner";

	/**
	 * Returns the version string
	 * 
	 * @param name
	 *            of the program
	 * @return version string for the program
	 */
	public static String getVersionString(String name) {
		return name + "\n" + Info.version + "\nWritten by " + Info.authors + "\n" + Info.copyright + "\n";
	}

	/**
	 * Prints out the version message
	 * 
	 * @param name
	 *            of the program
	 */
	public static void printVersion(String name) {
		System.out.println(getVersionString(name));
	}

}
