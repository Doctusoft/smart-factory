package com.doctusoft.smartfactory;

/**
 * 
 * @author dipacs
 */
final class ProviderBinding extends Binding {
	
	private final Provider<?> provider;
	
	public ProviderBinding(BindKey key, Provider<?> provider, boolean singleton) {
		super(key, singleton);
		this.provider = provider;
	}
	
	public Provider<?> getProvider() {
		return provider;
	}
	
	@Override
	public String toString() {
		return "ProviderBinding [provider=" + provider + ", toString()=" + super.toString() + "]";
	}
	
}
