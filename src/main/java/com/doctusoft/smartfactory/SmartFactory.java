package com.doctusoft.smartfactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 
 * @author dipacs
 */
public final class SmartFactory {
	
	private static final Logger log = Logger.getLogger(SmartFactory.class.getName());
	
	private static final Map<AbstractModule, SmartFactory> factoryCache = new HashMap<AbstractModule, SmartFactory>();
	
	/**
	 * Returns a SmartFactory instance which uses the given module. The returned instance is cached. The equality check is done on the
	 * module parameter using reference equality check.
	 * 
	 * @param module
	 *           The modul which you want to get the SmartFactory.
	 * @return
	 *         The new or cached SmartFactory instance.
	 */
	public static SmartFactory getFactory(AbstractModule module) {
		if (module == null) {
			throw new NullPointerException("The module parameter can not be null.");
		}
		
		SmartFactory res = factoryCache.get(module);
		if (res == null) {
			synchronized (factoryCache) {
				res = factoryCache.get(module);
				if (res == null) {
					res = new SmartFactory(module);
					factoryCache.put(module, res);
				}
			}
		}
		return res;
	}
	
	/**
	 * Returns a new instance of SmartFactory which contains the modules given in the parameter.
	 * 
	 * @param module
	 *           The main module.
	 * @param otherModules
	 *           Other modules which will be merged with this module.
	 * 
	 * @return
	 *         A new SmartFactory instance which contains all the given modules.
	 */
	public static SmartFactory getFactory(AbstractModule module, AbstractModule... otherModules) {
		SmartFactory res = new SmartFactory(module);
		for (AbstractModule otherModule : otherModules) {
			res.merge(otherModule);
		}
		return res;
	}
	
	private final HashMap<BindKey, Object> instanceCache = new HashMap<BindKey, Object>();
	private final HashMap<BindKey, Provider<?>> providerCache = new HashMap<BindKey, Provider<?>>();
	
	private final AbstractModule module;
	
	private SmartFactory(AbstractModule module) {
		this.module = module;
		this.module.init(this);
	}
	
	/**
	 * Clears the instance and the provider cache.
	 */
	public void clearCache() {
		instanceCache.clear();
		providerCache.clear();
	}
	
	/**
	 * Returns a new or a cached instance of the given type.
	 * 
	 * @param type
	 *           The type which you want to get an instance.
	 * 
	 * @return
	 *         A new or cached instance of the given type.
	 */
	public <T> T getInstance(Class<T> type) {
		return getInstance(type, null);
	}
	
	/**
	 * Returns a new or cached named instance of the given type.
	 * 
     * @param type
	 *           The type which you want to instantiate.
	 * @param name
	 *           The name of the bind.
	 * @return
	 *         The new or cached named instance of the given type.
	 */
	public <T> T getInstance(Class<T> type, String name) {
		if (type == null) {
			throw new NullPointerException("The type parameter can not be null.");
		}
		Object res = instanceCache.get(new BindKey(type, name));
		if (res == null) {
			synchronized (instanceCache) {
				res = instanceCache.get(new BindKey(type, name));
				if (res == null) {
					res = getInstanceInner(type, name);
				}
			}
		}
		return (T) res;
	}
	
	private Object getInstanceInner(Class<?> type, String name) {
		Binding binding = module.getBind(type, name);
		if (binding != null) {
			if (binding instanceof TypeBinding) {
				TypeBinding typeBinding = (TypeBinding) binding;
				Object res = getInstance(typeBinding.getTargetType());
				if (typeBinding.isSingleton()) {
					instanceCache.put(new BindKey(type, name), res);
				}
				return res;
			} else if (binding instanceof ProviderBinding) {
				ProviderBinding providerBinding = (ProviderBinding) binding;
				Provider<?> provider = providerBinding.getProvider();
				Object res = provider.get();
				if (providerBinding.isSingleton()) {
					instanceCache.put(new BindKey(type, name), res);
				}
				if (res instanceof Injectable) {
					((Injectable) res).initInjectedFields();
				}
				return res;
			} else {
				throw new IllegalArgumentException("Unknown binding type.");
			}
		} else {
			if (type.isInterface()) {
				throw new IllegalArgumentException("Cant find binding for interface: " + type.getName());
			}
			if (Modifier.isAbstract(type.getModifiers())) {
				throw new IllegalArgumentException("Cant find binding for abstract class: " + type.getName());
			}
			
			Constructor<?> constructor = null;
			try {
				constructor = type.getConstructor();
			} catch (NoSuchMethodException ex) {
				throw new IllegalArgumentException("Can't find the default constructor of type: " + type.getName());
			} catch (SecurityException ex) {
				throw new IllegalArgumentException("Can't find the default constructor of type: " + type.getName());
			}
			
			Object res;
			try {
				res = constructor.newInstance();
			} catch (InstantiationException ex) {
				// can't happen
				throw new RuntimeException(ex);
			} catch (IllegalAccessException ex) {
				throw new IllegalArgumentException("Can't access the constructor of the given type: " + type.getName());
			} catch (IllegalArgumentException ex) {
				// can't happen
				throw new RuntimeException(ex);
			} catch (InvocationTargetException ex) {
				throw new RuntimeException("An exception was thrown from the constructor of type: " + type.getName(), ex);
			}
			
			if (res instanceof Injectable) {
				((Injectable) res).initInjectedFields();
			}
			
			return res;
		}
	}
	
	/**
	 * Returns a cached Provider instance which can be used to get instances of the given type.
	 * 
	 * @param type
	 *           The type which the provider instantiate.
	 * @return
	 *         The cached Provider instance which can be used to get instances of the given type.
	 */
	public <T> Provider<T> getProvider(final Class<T> type) {
		return getProvider(type, null);
	}
	
	/**
	 * Returns a cached Provider instance which can be used to get the named instances of the given type.
	 * 
	 * @param type
	 *           The type which the provider instantiate.
     * @param name
     *          The name of the binding.
	 * @return
	 *         The cached Provider instance which can be used to get the named instances of the given type.
	 */
	public <T> Provider<T> getProvider(final Class<T> type, String name) {
		Provider<?> provider = providerCache.get(new BindKey(type, name));
		if (provider == null) {
			synchronized (providerCache) {
				provider = providerCache.get(new BindKey(type, name));
				if (provider == null) {
					provider = new Provider<T>() {
						
						@Override
						public T get() {
							return SmartFactory.this.getInstance(type);
						}
					};
					providerCache.put(new BindKey(type, name), provider);
				}
			}
		}
		return (Provider<T>) provider;
	}
	
	/**
	 * Merges the given module inside this module. If the given module contains bindings for an already registered type, than this method
	 * overrides that type.
	 * 
	 * @param module
	 *           The module which will be merged into this injector.
	 */
	public void merge(AbstractModule module) {
		this.module.merge(this, module);
	}

    /**
     * Returns the module which this factory relays on.
     * 
     * @return 
     * The module which this factory relays on.
     */
    public AbstractModule getModule() {
        return module;
    }
	
}
