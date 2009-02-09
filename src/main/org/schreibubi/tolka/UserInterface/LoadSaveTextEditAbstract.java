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

/**
 * @author Jörg Werner
 * 
 */
public abstract class LoadSaveTextEditAbstract {

	abstract public boolean allowsLoad();

	abstract public boolean allowsSave();

	abstract public String getLoadInfo();

	abstract public String getSaveInfo();

	abstract public void load(String filename);

	abstract public void save(String filename);

}
