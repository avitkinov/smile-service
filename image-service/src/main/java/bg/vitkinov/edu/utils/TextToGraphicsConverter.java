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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class TextToGraphicsConverter implements Converter<String, BufferedImage> {

	private GraphicsProperites properites;
	
	public TextToGraphicsConverter(GraphicsProperites properites) {
		this.properites = properites;
	}
	
	@Override
	public BufferedImage convert(String text) {
		String[] lines = text.split("\n");
		Rectangle textBounds = properites.getImageSize() == null ? getBounds(lines, properites.getFont()) : properites.getImageSize();

		BufferedImage img = new BufferedImage((int)textBounds.getWidth(), (int)textBounds.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(properites.getFont());
        
        if (properites.getBackColor() != null) {
        	g2d.setPaint ( properites.getBackColor() );
        	g2d.fillRect ( 0, 0, (int)textBounds.getWidth(), (int)textBounds.getHeight());
        }
        
    	FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(properites.getForeColor());	
        
        for (int i = 0; i < lines.length;) {
        	String line = lines[i];
        	int y =  ++i *  fm.getAscent();
        	g2d.drawLine(0, y, (int) textBounds.getWidth(), y);
			g2d.drawString(line, 0, y);
		}
        
        g2d.dispose();
        
        return img;
	}
	
	private Rectangle getBounds(String[] lines, Font font) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = 0;
        int lineHeight = 0;
        for (String line : lines) {
       		width = Math.max(width, fm.stringWidth(line));
        	lineHeight += fm.getHeight();
		}
//        int height = lines.length * fm.getHeight();
        g2d.dispose();
        
		return new Rectangle(width + 2, lineHeight);
	}
	
	public static void main(String[] args) throws IOException {
		GraphicsProperites properites2 = new GraphicsProperites();
		properites2.setBackColor(Color.YELLOW);
		properites2.setFont(new Font("Arial", Font.PLAIN, 30));
		TextToGraphicsConverter converter = new TextToGraphicsConverter(properites2);
		BufferedImage convert = converter.convert(new String(Base64.getDecoder().decode("0JDRgdC/0LDRgNGD0YUg0Lgg0J3QtdCy0Lg=")));

		File outputfile = new File("image.png");
		ImageIO.write(convert, "png", outputfile);
	}
}
