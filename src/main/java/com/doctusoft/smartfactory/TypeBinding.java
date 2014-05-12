package com.doctusoft.smartfactory;

/**
 * 
 * @author dipacs
 */
final class TypeBinding extends Binding {
	
	private final Class<?> targetType;
	
	public TypeBinding(BindKey key, Class<?> targetType, boolean singleton) {
		super(key, singleton);
		this.targetType = targetType;
	}
	
	public Class<?> getTargetType() {
		return targetType;
	}
	
	@Override
	public String toString() {
		return "TypeBinding [targetType=" + targetType + ", toString()=" + super.toString() + "]";
	}
	
}
