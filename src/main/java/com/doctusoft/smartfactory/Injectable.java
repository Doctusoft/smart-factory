package com.doctusoft.smartfactory;

/**
 * This interface represents an injectable type. If an injected type is implements this interface, than after the
 * instantiation, the initInjectedFields() method will be called.
 * 
 * @author dipacs
 * 
 */
public interface Injectable {
	
	/**
	 * This method is called when an instance of this interface is created. The implementor can inject dependencies in this
	 * method to prevent circular dependency.
	 */
	public void initInjectedFields();
	
}
