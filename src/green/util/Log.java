package green.util;

import java.io.IOException;
import java.io.OutputStream;

public class Log {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static OutputStream output;
	private static boolean disabled = true;
	
	public Log() {
		disabled = true;
	}
	public static void print(String message) {
		if(!isDisabled()) {
			try {
				getOutput().write(message.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.print(message);
		}
	}
	
	public static void printLine(String message) {
		if(!isDisabled()) {
			print(message + LINE_SEPARATOR);
		} else {
			System.out.println(message);
		}
			
	}
	
	
	public static void setOutput(OutputStream _output) {
		output = _output;
	}
	
	public static OutputStream getOutput() {
		if(output == null) {
			setOutput(System.out);
		}
		return output;
	}
	
	public static void setDisabled(boolean _disabled) {
		disabled = _disabled;
	}
	
	public static boolean isDisabled() {
		return disabled;
	}
	
	public static void disable() {
		setDisabled(true);
	}
	
	public static void enable() {
		setDisabled(false);
	}
}
