package com.doctusoft.smartfactory;

/**
 * 
 * @author dipacs
 */
abstract class Binding {
	
	private final boolean singleton;
	private final BindKey key;
	
	public Binding(BindKey key, boolean singleton) {
		this.key = key;
		this.singleton = singleton;
	}
	
	public Class<?> getSourceType() {
		return getKey().getType();
	}
	
	public boolean isSingleton() {
		return singleton;
	}
	
	public BindKey getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return "Binding [singleton=" + singleton + ", key=" + key + "]";
	}
	
}
