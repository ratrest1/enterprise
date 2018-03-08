package utils;

import author.Author;
import javafx.collections.ObservableList;

public abstract class GatewayBase {

	/**
	 * 		Read from a gateway
	 * @return the list of objects read.
	 */
	public abstract ObservableList<Object> read();
	
	/**
	 * 		delete from a gateway
	 * @param arg the object being deleted.
	 */
	public abstract void delete(Object arg);
}
