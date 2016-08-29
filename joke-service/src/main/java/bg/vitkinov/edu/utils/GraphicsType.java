/*
 * Copyright 2016 the original author or authors.
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
package bg.vitkinov.edu.utils;

/**
 * @author Asparuh Vitkinov
 */
public enum GraphicsType {
	
	PNG("image/png"), GIF("image/gif"), JPG("image/jepg");
	
	private String conentType;
	
	private GraphicsType(String contentType) {
		this.conentType = contentType;
	}
	
	public String getConentType() {
		return conentType;
	}
	
	public static GraphicsType value(String contentType) {
		for (GraphicsType type : values()) {
			if (type.getConentType().equals(contentType)) {
				return type;
			}
		}
		
		return PNG;
	}
	
}
