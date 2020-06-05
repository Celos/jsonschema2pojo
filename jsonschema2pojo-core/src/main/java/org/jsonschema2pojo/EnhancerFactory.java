/**
 * Copyright © 2010-2020 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsonschema2pojo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EnhancerFactory {
	
	private final GenerationConfig generationConfig;
	
	public EnhancerFactory(GenerationConfig generationConfig) {
		this.generationConfig = generationConfig;
	}
	
	public Enhancer getEnhancer(Class<? extends Enhancer> clazz) {
		
		if (!Enhancer.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("The class name given as a custom enhancer (" + clazz.getName() + ") does not refer to a class that implements " + Enhancer.class.getName());
		}
		
		try {
			try {
				Constructor<? extends Enhancer> constructor = clazz.getConstructor(GenerationConfig.class);
				return constructor.newInstance(generationConfig);
			} catch (NoSuchMethodException e) {
				return clazz.newInstance();
			}
		} catch (InvocationTargetException | InstantiationException e) {
			throw new IllegalArgumentException("Failed to create a custom Enhancer from the given class. An exception was thrown on trying to create a new instance.", e.getCause());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Failed to create a custom Enhancer from the given class. It appears that we do not have access to this class - is both the class and its no-arg constructor marked public?", e);
		}
		
	}
	
	public CompositeEnhancer getEnhancer( Enhancer... annotators ) {
		return new CompositeEnhancer(annotators);
	}
	
}
