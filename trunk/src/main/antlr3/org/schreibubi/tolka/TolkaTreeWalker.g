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

tree grammar TolkaTreeWalker;

options {
    tokenVocab=TolkaGrammar;
    ASTLabelType=CommonTree;
    backtrack=true;
}

@header {
    package org.schreibubi.tolka;
    import java.util.LinkedHashSet;
    import java.util.LinkedHashMap;
    import java.util.Iterator;
    import org.schreibubi.symbol.*;
    import java.math.BigInteger;
    import java.io.PrintWriter;
    import org.schreibubi.tolka.AntlrExtension.IndexedBufferedTreeNodeStream;
}

@members {
    LinkedHashMap<String,Integer> rulesLookup;
    PrintWriter lpw;

  Stack<LinkedHashMap<String,Symbol>> scopes = new Stack<LinkedHashMap<String,Symbol>>();

  /** Get value of name.  Either on top of stack as arg or in memory. */
  public Symbol getVariableValue(String name) throws RecognitionException {
      LinkedHashMap<String,Symbol> values = scopes.get(scopes.size()-1);
      Symbol vI = values.get(name);
      if ( vI!=null ) {
           return vI;
      }
      throw new RecognitionException();
  }

  /** Put value of name.  */
  public void putVariableValue(String name, Symbol value) {
      LinkedHashMap<String,Symbol> values = scopes.get(scopes.size()-1);
      value.setName(name);
      values.put(name,value);
  }

  public void execRule(String name, ArrayList<Symbol> arg) throws RecognitionException {
      IndexedBufferedTreeNodeStream stream = (IndexedBufferedTreeNodeStream)input;
        int addr = rulesLookup.get(name);;
        if ( addr>=0 ) {
          ((IndexedBufferedTreeNodeStream)input).push(addr);
          rule(arg);
          ((IndexedBufferedTreeNodeStream)input).pop();
        }
        else {
			throw new RecognitionException();
        }
  }
}

rulestart [PrintWriter pw, ArrayList<Symbol> in]
@init {
    lpw=pw;
}
	: rule[in]
	;
	
rule [ArrayList<Symbol> in] throws Exception
@init {
    LinkedHashMap<String,Symbol> argScope = new LinkedHashMap<String,Symbol>();
    scopes.push(argScope); // push new arg scope
    ArrayList<String> parameters=new ArrayList<String>();
}
@after {
    scopes.pop();          // remove arg scope
}
    : ^(RULE name=ID (a=ID {parameters.add($a.text);})+ { 
      if (in.size()!=parameters.size()) throw new RecognitionException();
      for (int i=0;i<in.size();i++) 
      { 
        putVariableValue(parameters.get(i),in.get(i)); 
      } 
      } statement+)
    ;

statement
  : switchalt
  | command
  ;

switchalt
  : ^(SWITCHALT v=varExpression alts[$v.value])
  ;

alts [Symbol altsel]
@init{
  ArrayList<LinkedHashSet<Symbol>> intlist=new ArrayList<LinkedHashSet<Symbol>>();
  ArrayList<Integer> treelist=new ArrayList<Integer>();
  int ind=0;
}
  : ( ^(ALT i=numberOrRange { ind=input.index(); } .) { intlist.add($i.value); treelist.add(ind); } )+
    {
      Iterator<Integer> iterList = treelist.iterator();
      for (Iterator<LinkedHashSet<Symbol>> iterInt = intlist.iterator(); iterInt.hasNext();) {
        LinkedHashSet<Symbol> sel = iterInt.next();
        int pos = iterList.next();
        boolean found=false;
        for (Symbol s: sel) {
          if (s.eq(altsel)| s.lt(new SymbolInteger(BigInteger.ZERO)) ) {
			found=true;
			break;
          }
        }
        if (found) {
            ((CommonTreeNodeStream)input).push(pos);
            commands();
            ((CommonTreeNodeStream)input).pop();
			break;
        }
      }
    }
    ;

commands
  : ^(COMMANDS command+)
  ;

command : (callCommand | printCommand | printlnCommand | assignment)
  ;

assignment
  : ^(ASSIGN_COMMAND ID e=expr) {
  	putVariableValue($ID.text,$e.value);
  }
  ;


printCommand
  : ^(PRINT_COMMAND e=expr) { lpw.print($e.value.getValueString()); }
  ;

printlnCommand
  : ^(PRINTLN_COMMAND e=expr) { lpw.println($e.value.getValueString()); }
  ;

callCommand
@init {
ArrayList<Symbol> arguments=new ArrayList<Symbol>();
}
  : ^(CALL_COMMAND name=ID (e=expr {arguments.add(e); })+)
      {
          execRule($name.text,arguments);
      }
  ;

expr returns [Symbol value=null]
    :
        ^(
            PLUS e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.add(e2);
            }
        )
    |   ^(
            MINUS e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.sub(e2);
            }
        )
    |   ^(
            STAR e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.mul(e2);
            }
        )
    |   ^(
            SLASH e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.div(e2);
            }
        )
/*    |   ^(
            XOR e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.xor(e2);
            }
        )
    |   ^(
            OR e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.or(e2);
            }
        )
    |   ^(
            AND e1=expr e2=expr
            {
                if ( e1.getType().compareTo( e2.getType() ) < 0 ) {
                    e2 = e1.convert( e2 );
                } else {
                    e1 = e2.convert( e1 );
                }
                $value=e1.and(e2);
            }
        )   */
    |   ^(
            NOT e1=expr
            {
                $value=e1.not();
            }
        )
    |   ^(
            UNARY_PLUS e1=expr
            {
                $value=e1.uplus();
            }
        )
    |   ^(
            UNARY_MINUS e1=expr
            {
                $value=e1.uminus();
            }
        )
    |   ^(
            CHAR_FUNC e1=expr
            {
                $value=new SymbolString(Character.toString((char) e1.convertToInt().getValue().intValue()));
            }
        )
    |   ^(
            HEX_FUNC e1=expr
            {
                $value=new SymbolString(e1.convertToInt().getValue().toString(16).toUpperCase());
            }
        )
    |   ^(
            BIN_FUNC e1=expr
            {
                $value=new SymbolString(e1.convertToInt().getValue().toString(2));
            }
        )
    |   ^(
            MATCH_FUNC searchFor=expr searchIn=expr searchStart=expr
            {
                String strFor=searchFor.convertToInt().getValue().toString(2);
                String strIn=searchIn.convertToInt().getValue().toString(2);
                $value=new SymbolInteger(strIn.length()-1-strIn.lastIndexOf(strFor,strIn.length()-1-searchStart.convertToInt().getValue().intValue()));
            }
        )
    |   e1=varExpression { $value=e1; }
    |   e1=con { $value=e1; }
    ;

varExpression returns [Symbol value=null]
    : f=id (subRange=numberOrRange)? {
        if (subRange!=null) {
            $value=$f.value.getBitList(subRange);
        } else {
            $value=$f.value;
        }
        subRange=null;
    }
    ;

id returns [Symbol value=null]
    : f=ID { $value=getVariableValue($f.text); }
    ;

numberOrRange returns [LinkedHashSet<Symbol> value=new LinkedHashSet<Symbol>()]
    :
    (( b=number { value.add($b.value); }
    | r=range { value.addAll($r.rangelist); }
    )+
    | DEFAULT { value.add(new SymbolInteger(BigInteger.valueOf(-1))); } )
    ;

number returns [Symbol value]
    : ^(NUMBER e=expr) { $value=$e.value; }
    ;

range returns [LinkedHashSet<Symbol> rangelist=new LinkedHashSet<Symbol>()] throws Exception
    : ^(RANGE (sstart=expr sstop=expr) ) {
  	  Symbol start=$sstart.value.convertToInt();
  	  Symbol stop=$sstop.value.convertToInt();
      if (start.le(stop)) {
        Symbol i=start.clone();
      	while (i.le(stop)) {
        	$rangelist.add(i.clone());
    	    i.add(new SymbolInteger(BigInteger.valueOf(1)));
	    }
      } else {
        Symbol i=start.clone();
      	while (i.ge(stop)) {
        	$rangelist.add(i.clone());
    	    i.sub(new SymbolInteger(BigInteger.valueOf(1)));
	    }
      }
    }
    ;

con returns [Symbol value=null]
    : f=FLT_CONST { $value=new SymbolDouble( $f.text ); }
    | f=INT_CONST { $value=new SymbolInteger( $f.text ); }
    | f=HEX_CONST { $value=new SymbolInteger( BigInteger.valueOf( Integer.parseInt($f.text.substring(1),16) ) ); }
    | f=BIN_CONST { $value=new SymbolInteger( BigInteger.valueOf( Integer.parseInt($f.text.substring(1),2) ) ); }
    | f=STR_CONST { $value=new SymbolString( $f.text.substring(1,$f.text.length()-1)); }
    ;
