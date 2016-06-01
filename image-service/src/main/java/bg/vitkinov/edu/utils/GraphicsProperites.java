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

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

public class GraphicsProperites {

	private Font font = new Font("Arial", Font.PLAIN, 14);
	private Color backColor;
	private Color foreColor = Color.BLACK;
	private Rectangle imageSize;
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Color getBackColor() {
		return backColor;
	}
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	public Color getForeColor() {
		return foreColor;
	}
	public void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}
	public Rectangle getImageSize() {
		return imageSize;
	}
	public void setImageSize(Rectangle imageSize) {
		this.imageSize = imageSize;
	}	
}
