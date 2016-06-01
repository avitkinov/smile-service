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
