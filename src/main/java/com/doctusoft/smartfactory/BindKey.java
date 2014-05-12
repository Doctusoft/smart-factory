package com.doctusoft.smartfactory;

import java.util.Objects;

/**
 * 
 * @author dipacs
 */
final class BindKey {
	
	private final Class<?> type;
	private final String name;
	
	public BindKey(Class<?> type) {
		this(type, null);
	}
	
	public BindKey(Class<?> type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		int hash = 5;
		if (this.type != null) {
			hash = 59 * hash + this.type.hashCode();
		} else {
			hash = 59 * hash;
		}
		if (this.name != null) {
			hash = 59 * hash + this.name.hashCode();
		} else {
			hash = 59 * hash;
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BindKey other = (BindKey) obj;
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "BindKey [type=" + type + ", name=" + name + "]";
	}
	
}
