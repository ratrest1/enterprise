package utils;

import author.Author;
import javafx.collections.ObservableList;

public abstract class GatewayBase {

	public abstract ObservableList<Object> read();
	
	public abstract void delete(Object arg);
}
