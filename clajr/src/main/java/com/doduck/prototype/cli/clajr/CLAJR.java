package com.doduck.prototype.cli.clajr;

/* CLAJR - Command Line Arguments with Java Reflection
 * By Marco Tonti 2006 - tonti@cs.unibo.it - lindoro@gmail.com
 * 
 * I WAS LOOKING for a command line argument parser, when I realized that
 * the simpler ones were way too simple, and the complex ones were very
 * complex, forcing me, a very lazy programmer, to learn too many new things.
 * In both cases, these solutions would force anyone to learn a new "language",
 * the language of classes and objects and enums defined not by the actual user,
 * but by someone else. I hate that. Also, if the library was powerful and complex,
 * still the possible ways of defining ties, conditions, uses, data types, were
 * at the same time much cumbersome and quite lacking flexibility.
 * 
 * PERFECTION DOESN'T BELONG TO THIS WORLD, I thought. Or maybe yes? I felt that
 * a (nearly) perfect solution was out there... but where? The main problem was 
 * the one of describing the data structure: how one could define the structure
 * without the need for additional classes, objects, constants, enums? 
 * 
 * JAVA REFLECTION, I was sure, would have been useful! If I define the fields
 * of the command line arguments in a class, maybe I can fill in the values 
 * automagically. But Java Reflection couldn't help for that, because its methods
 * returns the fields of a class "in no particular order". So I couldn't use them.
 * But exploring the documentation, I found that the  parameters of a method
 * ARE returned in the original order. And also methods are overloadable, and can
 * receive arrays. Exactly what one is expected to express within a command line.
 * 
 * BUT THERE WAS MORE, because methods are actions. Thinking to methods in this way,
 * I asked myself how one can define an argument with a method. The key of the argument
 * of course must have something to do with the name of the method. But "-" are not
 * allowed. Ok, replace them with "_".
 * 
 * _p__print(String text)
 * _p__print(int times, String text)
 * 
 * DETAILS ARE IMPORTANT, but I don't want to write here their biography. ;) So if
 * someone wants other datails, just write me, or look in the source code.
 * 
 * INSTRUCTIONS
 * 
 * CLAJR.parse(args, modules)   or   CLAJR.parse(argString, modules)
 * 
 * args is the String array with the actual parameters.
 * argString is a string containing the parameters.
 * modules is an instance of a class defining the methods-key. Actually, one can pass 
 * 	a (vararg) array of objects as modules. This allows a full modularization of
 * 	the management (the I/O module, the XML parameters module, the actions module...)
 *    One can also define the methods directly in the classes that will be influenced
 *    by the parameters passed by the command line.
 * 
 * A module does not have to implement any kind of interface! It is just an object
 * with some public methods starting with a "_". Subclassing is allowed as well.
 * 
 * The methods implementing the keys must start with an underline "_" character, and can
 * define more than one key, e.g. _s_secs__seconds. This name matches all of the three
 * keys: -s -secs --seconds. The "tail", the unnamed parameter or parameters list,
 * is defined by the method named just with an underline char "_". Method overloading
 * is allowed also in this case. 
 * 
 * The managed data types are: String, boolean, Boolean, int, Integer, long, Long
 * float, Float, double, Double and any program-defined enum. Methods can also
 * receive arrays of these types of data.
 * 
 * A call to CLAJR.parse can raise three classes of exceptions:
 * - Throwable: simply reflects an exception thrown in a method, for example
 * 		void _age(int age) throws Exception {
 * 			if (age < 0)
 * 				throw new Exception("Age can't be a negative value.");
 * 		}
 * - ParseException: an error of the parsing engine. 
 * 		(A method like _p(ListResourceBundle list) would except that that data type
 * 		is not available... of course! Try to pass this kind of value in command line!)
 * 		This exception is raised also when there isn't a matching method.
 * - HelpNeededException: when the parser meets a "-?" key, CLAJR throws the exception
 * 		setting the message field to "-? " + the rest of the line (until the following key)
 * 		It's possible to throw this exception also in the body of the user defined methods,
 * 		In this way one can implement methods like 
 * 		void _h__help(String keyword) throws CLAJR.HelpNeededException
 * 		and fill in the message field with the approprate text.
 * 		As a subclass of this Exception, EmptyArgumentListException is also present.
 * 
 * CLAJR defines two interfaces that one can implement, if wanted or needed.
 * - Unmatched: if a module implements this interface, it needs to implement the method
 * 		boolean unmatched(String token)
 * 		The meaning of this is that if CLAJR doesn't find a perfecly matching method,
 * 		this is not necessarily an error. Try to think to gcc compiler. CLAJR gives
 * 		the possibility to manage these unmatched tokens after a partially matching method.
 * 		CLAJR calls each module implementing the Unmatched interface, passing it the
 * 		residual tokens, one at time. If the method can manage that, it must return a true value.
 * 		If no module can catch an unmatching token, or no one of them can or want to handle it,
 * 		a ParseException is thrown. The matched methods and the unmatched ones are 
 *			called strictly in the order of the command line, allowing an "action sequence" politic.
 * - Info: The Info interface has only one method: String help(). This method, if called,
 * 		must return the help string describing the use of the module. This is used by
 * 		CLAJR to build the help string, that I'm going shortly to describe.
 * 
 * The help system
 * While executing the parse method, the engine collects some help informations
 * from the modules. There are two ways to do that. The first is to implement the
 * Info interface, returning the help string formatted as you want. But there's
 * a smarter way. If one has the method _p__print, he can add a method
 * String help_p__print() returning the help string for that method. Notice that the
 * help string is only one for all of the methods sharing the same name, even in case of
 * different parameter signatures. (different methods with the same name should do 
 * very similar things!)
 * The problem with this approach is that Java Reflection, again, lists the methods in
 * no particular order, so they are simply sorted using a lexicographic order.
 * But is possible to combine these two ways of implementing help. CLAJR has a static
 * method allowing that: getMethodHelp(module, methodName).
 * This method returns an object of type MethodHelp, containing the keys of the
 * method, the signatures IN THE ORDER OF MATCHING PRIORITY (described later), and the
 * text retrieved from the "help"+methodName+"()" method of the specified module.
 * In this way is simple to define the help near the actual implementation of the
 * key-method, but giving it the preferred order in the help() method.
 * the static method GetModuleHelp(modules) retrieves the help from the module list
 * in the given order. If the module implements the Info interface, the help is
 * the string given by the invocation of the "help()" method. Otherwise the help is
 * built as described before.
 * The whole help text can be subsequently retrieved with a call to the static
 * getHelp() method:
 * 
 * try {
 * 	CLAJR.parse(args, new Manager1(), new Manager2());
 * } catch (CLAJR.EmptyArgumentListException e) { 
 * 	//if one wants to behave differently in case of empty argument
 * 	System.out.println("Usage: blah blah blah");
 * } catch (CLAJR.HelpNeededException e) {
 * 	System.out.println(CLAJR.getHelp());
 * } catch (CLAJR.ParseException e) {
 * 	System.err.println(e.getMessage());
 * 	System.out.println(CLAJR.getHelp());
 * }
 * 
 * Simple and clear, isn't it? Actually, this is the meaning of "clair" in french :) If you are
 * wondering why an Italian should give to his library a french name... no reason! I
 * thought just that is was nice :) 
 * 
 * The matching priority
 * An important question is the order the system should follow trying to match the
 * signatures of a overloaded method. For example:
 * void _p__print(int number)
 * void _p__print(float number)
 * void _p__print(String text)
 * If the string is --print 10, all methods matches it, because "10" matches a string,
 * a float and an int. The system tries first with the stricter types. This is a quite
 * an issue, because in case of signatures with more than one parameter is hard to decide
 * which one is the "stricter". Look into the class MethodHolder, in its constructor and
 * in the implementation of the Comparable interface to find out how it works.
 * In the case that no method can match perfectly the string, the ones with longer signatures
 * are tried first, in order to minimize the presence of unmatched tokens.
 * Arrays are also in a lower position, the why is explained with this example:
 * void _p__print(String key, String value)
 * void _p__print(String[] texts)
 * If the string is "--print elephant pink" is better to match it with the first of
 * the two methods, even if one should NEVER define methods conflicting like that.
 * This matter is open to advices and improvements, of course.
 * 
 * The matching algorithm
 * CLAJR builds a regular expression describing the signature of each method. The
 * methods are then sorted following the criteria described before, and tried in that
 * order. This is the real powerful (and clever ;) ) idea: I haven't tried to parse the
 * command line (except for the <key><parameters> string), but rather I try each
 * signature to see if the sequence of characters is compatible with it! Now you can
 * understand fully the importance of a correct order of the methods.
 * The last fragment of the argument list is matched with its corresponding key mehod,
 * and also with the regular expression describing all the possible tails. The best
 * combination is found by the regex engine.
 * As you know, regular expressions have the annoying attitude of becoming unreadable
 * and incomprehensible in the exact moment when they become longer than 10 characters!
 * For this reason, a double check of them would be appreciated. If you find a bug,
 * please tell me.
 * 
 * Final thoughts
 * The aspect I like most in this approach, is that is SIMPLE. One can build a very simple
 * implementation, maybe even without help, or a very complex one.
 * The second thing is that isn't needed to learn someone else's language, isn't needed
 * to enter in another's logic. The ties and the conditions are expressed in Java,
 * following the developer's personal style of coding.
 * This is the reason why I would prefer to not add too many features. The more the data 
 * types (one would like to add Date or Path) the more the use becomes complex.
 * So I certainly would appreciate advices, bug signalations, new ideas...
 * ... but keep in mind that one must feel free to use this class in the way he prefer.
 * Is it CLAJR? :)
 * 
 * Copyright (C) 2006 Marco Tonti
 *  
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with 
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA 02111-1307 USA
 */

import java.util.regex.*;
import java.util.*;
import java.lang.reflect.*;

public class CLAJR {
	
	public interface Unmatched {
		boolean unmatched(String token);
	}
	
	public interface Info {
		String help();
	}
	
	public static class ParseException extends Exception {
		public ParseException() {
			super();
		}
		
		public ParseException(String message) {
			super(message);
		}
		
		public ParseException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	public static class HelpNeededException extends Exception {
		
		public HelpNeededException() {
			super();
		}
		
		public HelpNeededException(String message) {
			super(message);
		}
		
		public HelpNeededException(String message, Throwable cause) {
			super(message, cause);
		}		
	}
	
	public static class EmptyArgumentListException extends HelpNeededException {
		
	}
	
	//To add new data types, one should add here the corresponding regexp,
	//then the rule of conversion from string to that type in the
	//addParameter method of the local class MethodHolder, in both its array
	//form and simple form. At last one should add a literal to represent
	//this new type in the level of order among the other types, in the
	//constructor of the MethodHolder class. This will be used to sort the
	//methods.
	
	private static final String BOOLEAN = "(?:true|false)";
	private static final String INT = "(?:[+-]?[0-9]+)";
	private static final String FLOAT = "(?:" + INT + "|[+-]?[0-9]*\\.[0-9]+(?:[eE][+-]?[0-9]+)?)";
	private static final String STRING = "(?:\"(?:[\\\\][\\\\]|[\\\\]\"|.)*?\"|'(?:[\\\\][\\\\]|[\\\\]'|.)*?'|(?:-[0-9.]|[^- ])\\S*)";
	//NOTE: the string pattern may need some revision
	private static final String key = "(?:-(?:[-a-zA-Z][a-zA-Z0-9]*|\\?))";
	
	private static final Pattern paramPat = Pattern.compile("(" + key + "?) +((?:" + STRING + " *)*)");
	private static final Pattern stringPat = Pattern.compile(STRING);
	private static final Pattern intPat = Pattern.compile(INT);
	private static final Pattern floatPat = Pattern.compile(FLOAT);
	private static final Pattern booleanPat = Pattern.compile(BOOLEAN);
	
	private static final Pattern methodName = Pattern.compile(key);
	
	private static String helpString = "";
	private static String helpStringWithEmpty = "";
	
	// 'ciao \' mondo' sdfsdf 
	
	private static Vector<Unmatched> unmatchedManagers = new Vector<Unmatched>();
	private static Map<String, MethodHelp> help = new TreeMap<String, MethodHelp>();
	
	public static class MethodHelp {
		private String methodName;
		private String keys = "";
		private String signatures = "";
		private String helpText = null;
		private int sigsCount = 0;
		private Object module = null;

		MethodHelp(Info module) {
			helpText = ((Info)module).help();
			methodName = "";
			this.module = module;
		}
		
		MethodHelp(Object module, String methodName) {
			this.methodName = methodName;
			this.module = module;
			
			if (methodName.equals("_")) {
				this.keys = "-";
			} else if(methodName.length() > 0){
				String s = "";
				
				Matcher m = CLAJR.methodName.matcher(methodName.replace((CharSequence)"_", "-"));
				
				while (m.find()) {
					s += " | " + m.group();
				}
				
				this.keys = s.substring(3);
			}
			
			if (methodName.length() > 0) {
				try {
					helpText = (String)module.getClass().getMethod("help" + methodName).invoke(module, new Object[0]);
				} catch(Exception e) {
				}
			}
		}
		
		void addSignature(Method method) {
			
			String meth = "";
			sigsCount++;
			
			for (Class type: method.getParameterTypes()) {
				if (type.isArray()) {
					type = type.getComponentType();
					
					if (type.isEnum()) {
						meth += " (" + enumValues(type) + ")[]";
					} else {
						meth += " " + type.getSimpleName() + "[]";
					}
				} else {
					if (type.isEnum()) {
						meth += " " + enumValues(type);
					} else {
						meth += " " + type.getSimpleName();
					}					
				}
			}
			
			if (meth.length() > 0)
				signatures += "\t" + meth.substring(1) + "\n";
			else
				signatures += "\t(empty)\n";
		}
		
		public String getSignatures() {
			return signatures;
		}
		
		public String getKeys() {
			return keys;
		}
		
		public String getMethodName() {
			return methodName;
		}
		
		public String getHelpText() {
			return helpText;
		}
		
		public String getHelp() {
			
			if (helpText == null) {
				if (sigsCount <= 1) {
					return keys + signatures;					
				} else {
					return keys + "\n" + signatures;
				}
			}
			
			if (sigsCount <= 1) {
				//just one parameter... can inline it!
				return keys + signatures + helpText;
			}
			
			return keys + "\n" + signatures + helpText;
		}
		
		public Object getModule() {
			return module;
		}
		
		//default
		String getUniqueKey() {
			return module.hashCode() + "_" + methodName;
		}
		
		
		private static final String enumValues(Class enumClass) {
			String vals = "";
			
			for (Object constant: enumClass.getEnumConstants()) {
				vals += "|" + ((Enum)constant).name();
			}
			
			return vals.substring(1);
		}
	}
	
	private static class MethodHolder implements Comparable<MethodHolder> {
		private Method method = null;
		private Object object = null;
		private String signature = "";
		private String sig = "";
		private Pattern pattern = null;
		private String name = "";
		private int objectId;
		
		MethodHolder(Object object, Method method) throws ParseException {
			this.method = method;
			this.object = object;
			name = method.getName();
			objectId = object.hashCode();
			
			//Builds a regex to recognize the parameters structure
			//like ((int)(int)(string*)(int))

			signature = "";
			
			for (Class p: method.getParameterTypes()) {
				boolean isArray = p.isArray();
				
				String param = "";
				
				if (isArray) {
					p = p.getComponentType();
				}
				
				//The sig value is uset to quickly sort the signatures of the methods
				//Because the program has to match (1) the largest signature compatible
				//with the passed parameters, and (2) the one with the strictest types
				//so that if a method matches a float, and another an int, if the value
				//is 42 it matches always as an int, and just when it is 42.5 matches as
				//a float.
				//in this implementation each type in the signature is replaced by a character
				//representing the "level of specification" of the type, e.g. a String,
				//being universal, is the last type that should be matched, so its literal
				//is a "d". For arrays it's hard to think a very good way of doing it,
				//because their length is unknown. Maybe one could sort the
				//methods collection to reflect the actual values of the parameters, but I think
				//that it would be an exaggeration. Arrays should be used as less as possible,
				//so I used uppercase letters to denote them, as in a lexicographic order
				//they will be towards the end.
				
				if (p == String.class) {
					sig += isArray? "d": "D";
					param = STRING;
				} else if (p == Float.class || p == Double.class || p == float.class || p == double.class) {
					sig += isArray? "c": "C";
					param = FLOAT;
				} else if(p == Integer.class || p == Long.class || p == int.class || p== long.class) {
					sig += isArray? "b": "B";
					param = INT;
				} else if(p == Boolean.class || p == boolean.class) {
					sig += isArray? "a": "A";
					param = BOOLEAN;
				} else if (p.isEnum()) {
					sig += isArray? "a": "A";
					param = enumRegExp(p);
				} else {
					throw new ParseException ("Data type not available: " + p.getName());
				}
				
				if (isArray) {
					//zero or one param, followed by zero or more (one or more spaces followed by param)
					//to avoid partial matchings
					param = "(?:" + param + ")?(?: +" + param + ")*";
				}
				
				signature += " +(" + param + ")";
			}
			
			if (signature.length() > 0)
				signature = signature.substring(2);
			
			signature += " *";
			
			pattern = Pattern.compile(signature);

		}
		
		private boolean matches(String key) {
			return method.getName().endsWith(key) || method.getName().contains(key + "_");  
		}
		
		public boolean invoke(String key, String text, boolean strict, List<MethodHolder> tails) throws InvocationTargetException, IllegalAccessException, ParseException, HelpNeededException {
			//Last actual parameter, must join the regexes
			
			if (tails == null)
				return invoke(key, text, strict);
			
			if (!matches(key))
				return false;
			
			String tailSignatures = "";
			
			for (MethodHolder t: tails) {
				tailSignatures += "|" + t.signature;
			}
			
			if (tailSignatures.length() > 1)
				tailSignatures = "(" + tailSignatures.substring(1) + ")";
			
			if (strict) {
				Pattern p = Pattern.compile(signature + tailSignatures);

				if (!p.matcher(text).matches()) {
					return false;
				}
				
				parse(p, text, false, tails);
				return true;
			}

			
			Pattern p = Pattern.compile(signature + " +(.*?) +" + tailSignatures);
			
			if (!p.matcher(text).matches())
				return false;
			
			parse(p, text, true, tails);
			
			return true;

		}
		
		public boolean invoke(String key, String text, boolean strict) throws InvocationTargetException, IllegalAccessException, ParseException, HelpNeededException {
			if (!matches(key))
				return false;

			if (strict) {
				if (!pattern.matcher(text).matches()) {
					return false;
				}
				parse(pattern, text, false, null);
				return true;
			}
			
			//is not strict, and has some umatched strings
			
			Pattern p = Pattern.compile(signature + " (.*?)");
			
			if (!p.matcher(text).matches())
				return false;
			
			parse(p, text, true, null);
			
			return true;
		}
		
		private void parse(Pattern pattern, String text, boolean allowUnmatched, List<MethodHolder> tails) throws InvocationTargetException, IllegalAccessException, ParseException, HelpNeededException   {
			Matcher matcher = pattern.matcher(text);
			matcher.matches();
			
			int group = 1; //the first subgroup 
			
			group = invoke(object, method, matcher, group);
			
			if (allowUnmatched) {
				unmatched(matcher.group(group));
				group++;
			}
			
			if (tails == null)
				return;
			
			//no need to test if invocation is happened, because the parameters
			//already have been tested against the regexes of the tails, so
			//one must match for sure!
			
			for (MethodHolder method: tails) {
				if (method.invoke("_", matcher.group(group), true)) {
					break;
				}
			}
		}
		
		private static int invoke(Object object, Method method, Matcher matcher, int group) throws InvocationTargetException, IllegalAccessException  {
			//group is expected to be the index of the group containing the whole signature
			//but the overall expression could be also longer, with other groups following
			
			Vector params = new Vector();
			
			for (Class param: method.getParameterTypes()) {
				addParameter(params, param, matcher.group(group));
				group++;
			}
			
			method.invoke(object, params.toArray());

			return group;
		}
		
		private static void addParameter(Vector params, Class type, String text) {
			//Returns the next available group, after possibly having iterated for an array
			//... I haven't found a better way to do that... This one sucks, but should work

			if (type.isArray()) {
				Vector array = new Vector();
	
				if (type == String[].class) {
					Matcher mat = stringPat.matcher(text);
					while (mat.find()) {
						array.add(dequote(mat.group()));
					}
					params.add(array.toArray(new String[0]));
					
				} else if (type == Float[].class || type == float[].class) {
					Matcher mat = floatPat.matcher(text);
					while (mat.find()) {
						array.add(new Float(mat.group().trim()));
					}
					params.add(array.toArray(new Float[0]));
					
				} else if(type == Double[].class || type == double[].class) {
					Matcher mat = floatPat.matcher(text);
					while (mat.find()) {
						array.add(new Double(mat.group().trim()));
					}
					params.add(array.toArray(new Double[0]));
					
				} else if(type == Integer[].class || type == int[].class) {
					Matcher mat = intPat.matcher(text);
					while (mat.find()) {
						array.add(new Integer(mat.group().trim()));
					}
					params.add(array.toArray(new Integer[0]));
					
				} else if (type == Long[].class || type == long[].class) {
					Matcher mat = intPat.matcher(text);
					while (mat.find()) {
						array.add(new Long(mat.group().trim()));
					}
					params.add(array.toArray(new Long[0]));
					
				} else if(type == Boolean[].class || type == boolean[].class) {
					Matcher mat = booleanPat.matcher(text);
					while (mat.find()) {
						array.add(new Boolean(mat.group().trim()));
					}
					params.add(array.toArray(new Boolean[0]));
					
				} else if(type.getComponentType().isEnum()) {
					Class enumType = type.getComponentType();
					
					Matcher mat = Pattern.compile(enumRegExp(enumType)).matcher(text);
					while(mat.find()) {
						array.add(Enum.valueOf(enumType, mat.group()));
					}
					
					//bah!
					
					Object arr = Array.newInstance(enumType, array.size());
					
					for (int i = 0; i < array.size(); i++) {
						Array.set(arr, i, array.get(i));
					}
					
					params.add(arr);
				}
			} else {					
				if (type == String.class) {
					params.add(dequote(text));
				} else if (type == Float.class || type == float.class) {
					params.add(new Float(text.trim()));
				} else if (type == Double.class || type == double.class) {
					params.add(new Double(text.trim()));
				} else if (type == Integer.class || type == int.class) {
					params.add(new Integer(text.trim()));
				} else if (type == Long.class || type == long.class) {
					params.add(new Long(text.trim()));
				} else if (type == Boolean.class || type == boolean.class) {
					params.add(new Boolean(text.trim()));
				} else if (type.isEnum()) {
					params.add(Enum.valueOf(type, text));
				}
			}
		}
		
		private static String enumRegExp(Class enumClass) {
			String vals = "";
			
			for (Object constant: enumClass.getEnumConstants()) {
				vals += "|" + ((Enum)constant).name();
			}
			
			return "(?:" + vals.substring(1) + ")";
		}
		
		private static String dequote(String text) {
			String s = text.trim();
			if (s.startsWith("'")) {
				s = s.substring(1, s.length() - 1).replaceAll("[\\\\]([\\\\]|')", "$1");
			} else if (s.startsWith("\"")) {
				s = s.substring(1, s.length() - 1).replaceAll("[\\\\]([\\\\]|\")", "$1");				
			}
			return s;
		}

		public Object getObject() {
			return object;
		}
		
		public String getName() {
			return name;
		}
		
		public String getBriefSignature() {
			return sig;
		}
		
		public Method getMethod() {
			return method;
		}
		
		public int compareTo(MethodHolder otherMethod) {
			// this < other if (1) this.len > other.len
			// this > other if (2) this.len < other.len
			// if this.len = other.len (3) lexicographic
			if (objectId < otherMethod.objectId)
				return -1;
			 
			if (objectId > otherMethod.objectId)
				return +1;
			
			//objectIds are ==, are from the same object
			
			if (sig.length() < otherMethod.sig.length())
				return +1;
			
			if (sig.length() > otherMethod.sig.length())
				return -1;
			
			//are of the same length
			
			return sig.compareTo(otherMethod.sig);
		}
	}
	
	
	private static void unmatched(String text) throws ParseException {

		Matcher matcher = stringPat.matcher(text);
		
		while (matcher.find()) {
			boolean matched = false;
			for (Unmatched u: unmatchedManagers) {
				matched = u.unmatched(matcher.group());
				if (matched)
					break;
			}
			
			if (!matched)
				throw new ParseException("Unhandled unmatched string: " + matcher.group());
		}
	}
	
	public static final void parse(String[] mainArgs, Object... modules) throws ParseException, HelpNeededException, Throwable {

		String arg = "";
		
		for (String s: mainArgs) {
			if (s.contains(" ")) {
				//because the java strings between "" are dequoted. But I must keep the spaces!
				arg += "\"" + s.replaceAll("\"", "\\\"") + "\" ";
			} else {
				arg += s + " ";
			}
		}
		
		parse(arg, modules);
	}
	
	public static final void parse(String arguments, Object... modules) throws ParseException, HelpNeededException, Throwable {
		Vector<MethodHolder> methods = new Vector<MethodHolder>();
		Vector<MethodHolder> tails = new Vector<MethodHolder>();

		if (arguments.trim().length() == 0)
			throw new EmptyArgumentListException();
		
		unmatchedManagers.clear();
		help.clear();
		
		for (Object o: modules) {
			Vector<MethodHolder> methodsHelp = new Vector<MethodHolder>();
			
			if (o instanceof Unmatched)
				unmatchedManagers.add((Unmatched)o);
			
			for (Method m: o.getClass().getMethods()) {
				if (!m.getName().startsWith("_"))
					continue;
				
				MethodHolder holder = new MethodHolder(o, m);
				
				methodsHelp.add(holder);
				
				if (m.getName().equals("_")) {
					tails.add(holder);
				} else { 
					methods.add(holder);
				}
			}
			
			//adds the method to the help structure
			
			Collections.sort(methodsHelp); 
			//sort the methods following the priority of signatures
			//Affects only the order of the signatures in the MethodHelp class
			
			for (MethodHolder m: methodsHelp) {
				MethodHelp h = new MethodHelp(o, m.getName());
				
				if (!help.containsKey(h.getUniqueKey())) {
					help.put(h.getUniqueKey(), h);
				}
				
				help.get(h.getUniqueKey()).addSignature(m.getMethod());
			}
			
			if (o instanceof Info) {
				MethodHelp h = new MethodHelp((Info)o);
				
				help.put(h.getUniqueKey(), h);
			}
		}
		
		helpString = moduleHelp(true, modules);
		helpStringWithEmpty = moduleHelp(false, modules);
		
		//setting here the string, the original order of the modules is keeped.
		
		Collections.sort(methods);
		Collections.sort(tails);

		Matcher param = paramPat.matcher(arguments);
		
		while (param.find()) {
			String key = param.group(1).replaceAll("-", "_");
			String params = param.group(2);

			
			//Builds a list of all of the signatures of the method containing the key value in its name
			//this is done because I try to match the signatures firstly of the more restrictive types
			//like float or int, and only then the strings... to do that I create a key with the initials
			//of each type in the method's signature, and simply sort that! Not very correct, but should work.
			
			if (key.equals("_?")) {
				throw new HelpNeededException("-? " + params);
			}
			
			boolean invoked = false;
			
			try {
				if (key.trim().length() > 0) {
					if (!param.hitEnd()) {
						//Tries with a strict matching...
						for (MethodHolder method: methods) {
							if (method.invoke(key, params, true)) {
								invoked = true;
								break;
							}
						}
						
						if (!invoked) {
							//in case of failure, tries with a weak approach
							for (MethodHolder method: methods) {
								if (method.invoke(key, params, false)) {
									invoked = true;
									break;
								}
							}					
						}
					} else {
						//Tries with a strict matching...
						for (MethodHolder method: methods) {
							if (method.invoke(key, params, true, tails)) {
								invoked = true;
								break;
							}
						}
						
						if (!invoked) {
							//in case of failure, tries following a weak approach
							for (MethodHolder method: methods) {
								if (method.invoke(key, params, false, tails)) {
									invoked = true;
									break;
								}
							}
						}
					}
				} else {
					//no parameters. If the input has ended tries to match with tails
					if (param.hitEnd()) {
						for (MethodHolder method: tails) {
							if (method.invoke(key, params, true)) {
								invoked = true;
								break;
							}
						}
					} else {
						//or are used as unmatched, because parameters will follow
						unmatched(params);
					}
				}
			} catch (IllegalAccessException e) {
				//Should never happen, why bother the user with inexistent exceptions?
			} catch (InvocationTargetException e) {
				throw e.getCause();
			}
			
			if (!invoked)
				throw new ParseException("No matching method for " + key.replace((CharSequence)"_", "-") + " " + params);
		}
	}
	
	public static final String getHelp() {
		return helpStringWithEmpty;
	}
	
	public static final String getHelp(boolean hideEmpty) {
		if (hideEmpty)
			return helpString;
		else
			return helpStringWithEmpty;
	}
	
	public static MethodHelp getMethodHelp(Object module, String methodName) {
		MethodHelp m = new MethodHelp(module, methodName);
		
		return help.get(m.getUniqueKey());
	}
	
	public static String moduleHelp(boolean hideEmpty, Object... modules) {
		String s = "";
		
		for(Object o: modules) {
			MethodHelp h = new MethodHelp(o, "");
			
			h = help.get(h.getUniqueKey());
			
			if (h != null) {
				s += h.getHelpText();
				continue;
			}
			
			for (String k: help.keySet()) {
				MethodHelp m = help.get(k);
				if (m.getModule() == o) {
					if (m.getHelpText() != null || !hideEmpty) {
						s += m.getHelp();
					}
				}
			}
		}
		
		return s;
	}
}
