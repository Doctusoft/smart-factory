package com.doctusoft.smartfactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class represents a SmartFactory module. An implementation of this class must register all the providers which is used to inject
 * over SmartFactory.
 * 
 * @author dipacs
 */
public abstract class AbstractModule {
	
	private final HashMap<BindKey, Binding> bindCache = new HashMap<BindKey, Binding>();
	
	protected AbstractModule() {
		
	}
	
	/**
	 * Binds the given type to the given toType. The instantiation will be done by reflection. The given toType
	 * must contain a parameterless public constructor. This bind will be singleton.
	 * 
	 * @param type
	 *      The type which you want to bind.
	 * @param toType
	 *      The type which you want to bind to the 'type'.
     * @param <T>
     *      The base type.      
	 */
	public <T> void bindSingleton(Class<T> type, Class<? extends T> toType) {
		bindSingleton(type, null, toType);
	}
	
	/**
	 * Binds the given type to the given toType. The instantiation will be done by reflection. The given toType
	 * must contain a parameterless public constructor.
	 * 
	 * @param type
	 *           The type which you want to bind.
	 * @param toType
	 *           The type which you want to bind to the 'type'.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindDynamic(Class<T> type, Class<? extends T> toType) {
		bind(type, null, toType, false);
	}
	
	/**
	 * Binds the given type to the given toType. The instantiation will be done by reflection. The given toType
	 * must contain a parameterless public constructor. This bind will be a singleton. This bind is a named bind.
	 * 
	 * @param type
	 *           The type which you want to bind.
	 * @param name
	 *           The name of the bind.
	 * @param toType
	 *           The type which you want to bind to the 'type'.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindSingleton(Class<T> type, String name, Class<? extends T> toType) {
		bind(type, name, toType, true);
	}
    
    /**
	 * Binds the given type to the given toType. The instantiation will be done by reflection. The given toType
	 * must contain a parameterless public constructor. This bind is a named bind.
	 * 
	 * @param type
	 *           The type which you want to bind.
	 * @param name
	 *           The name of the bind.
	 * @param toType
	 *           The type which you want to bind to the 'type'.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindDynamic(Class<T> type, String name, Class<? extends T> toType) {
		bind(type, name, toType, false);
	}
	
	/**
	 * Binds the given type to the given toType. The instantiation will be done by reflection. The given toType
	 * must contain a parameterless public constructor. This bind is a named bind.
	 * 
	 * @param type
	 *           The type which you want to bind.
	 * @param name
	 *           The name of the bind.
	 * @param toType
	 *           The type which you want to bind to the 'type'.
	 * @param singleton
	 *           Indicates if this bind is singleton or not.
     * @param <T>
     *           The base type.
	 */
	private <T> void bind(Class<T> type, String name, Class<? extends T> toType, boolean singleton) {
		if (type == null) {
			throw new NullPointerException("The type parameter can not be null.");
		}
		if (toType == null) {
			throw new NullPointerException("The toType parameter can not be null.");
		}
		TypeBinding binding = new TypeBinding(new BindKey(type, name), toType, singleton);
		addBind(binding, false);
	}
	
	/**
	 * Binds the given type to the given provider. This bind will be singleton.
	 * 
	 * @param type
	 *           The type which you want to bind to the provider.
	 * @param toProvider
	 *           The provider which will be executed to get the instance.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindSingleton(Class<T> type, Provider<? extends T> toProvider) {
		bindSingleton(type, null, toProvider);
	}
	
	/**
	 * Binds the given type to the given provider.
	 * 
	 * @param type
	 *           The type which you want to bind to the provider.
	 * @param toProvider
	 *           The provider which will be executed to get the instance.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindDynamic(Class<T> type, Provider<? extends T> toProvider) {
		bind(type, null, toProvider, false);
	}
	
	/**
	 * Binds the given type to the given provider. This bind will be singleton. This bind will be named.
	 * 
     * @param type
	 *           The type which you want to bind to the provider.
	 * @param name
	 *       The name of the bind.
	 * @param toProvider
	 *           The provider which will be executed to get the instance.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindSingleton(Class<T> type, String name, Provider<? extends T> toProvider) {
		bind(type, name, toProvider, true);
	}
    
    /**
	 * Binds the given type to the given provider. This bind will be named.
	 * 
     * @param type
	 *           The type which you want to bind to the provider.
	 * @param name
	 *       The name of the bind.
	 * @param toProvider
	 *           The provider which will be executed to get the instance.
     * @param <T>
     *           The base type.
	 */
	public <T> void bindDynamic(Class<T> type, String name, Provider<? extends T> toProvider) {
		bind(type, name, toProvider, false);
	}
	
	/**
	 * Binds the given type to the given provider. This bind will be named.
	 * 
	 * @param type
	 *           The type which you want to bind to the provider.
	 * @name
	 *       The name of the bind.
	 * @param toProvider
	 *           The provider which will be executed to get the instance.
	 * @param singleton
	 *           Indicates if this bind is singleton or not.
     * @param <T>
     *           The base type.
	 */
	private <T> void bind(Class<T> type, String name, Provider<? extends T> toProvider, boolean singleton) {
		if (type == null) {
			throw new NullPointerException("The type parameter can not be null.");
		}
		if (toProvider == null) {
			throw new NullPointerException("The toProvider parameter can not be null.");
		}
		ProviderBinding binding = new ProviderBinding(new BindKey(type, name), toProvider, singleton);
		addBind(binding, false);
	}
	
	void addBind(Binding binding, boolean merge) {
		bindCache.put(binding.getKey(), binding);
	}
	
	protected void merge(SmartFactory injectorFactory, AbstractModule module) {
		module.init(injectorFactory);
		Collection<Binding> bindings = module.getBindings();
		for (Binding binding : bindings) {
			addBind(binding, true);
		}
	}
	
	Binding getBind(Class<?> type) {
		return bindCache.get(new BindKey(type));
	}
	
	Binding getBind(Class<?> type, String name) {
		return bindCache.get(new BindKey(type, name));
	}
	
	Collection<Binding> getBindings() {
		return Collections.unmodifiableCollection(bindCache.values());
	}
	
	/**
	 * This method is called when an injector wants to use this module. The implementation of this method must register
	 * all the types which the SmartFactory can instantiate using the bind(...) methods.
	 * 
	 * @param injector
	 */
	protected abstract void init(SmartFactory injector);
	
}
