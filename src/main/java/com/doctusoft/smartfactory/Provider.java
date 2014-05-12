/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.doctusoft.smartfactory;

/**
 *
 * @author dipacs
 * @param <T> The result type of this provider.
 */
public interface Provider<T> {
    /**
     * Returns the value of this provider.
     * 
     * @return The value of this provider.
     */
    public T get();
    
}
