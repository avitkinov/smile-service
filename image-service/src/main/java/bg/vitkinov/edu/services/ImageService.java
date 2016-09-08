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
package bg.vitkinov.edu.services;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bg.vitkinov.edu.model.Service;
import bg.vitkinov.edu.utils.Converter;
import bg.vitkinov.edu.utils.GraphicsProperites;
import bg.vitkinov.edu.utils.GraphicsType;
import bg.vitkinov.edu.utils.TextToGraphicsConverter;

/**
 * @author Asparuh Vitkinov
 */
@RestController
@RequestMapping(value = "/")
public class ImageService {
	
	private Logger logger = Logger.getLogger(getClass().getName());

	@RequestMapping(method = RequestMethod.GET, produces= { MediaType.APPLICATION_JSON_VALUE } )
    public Service getClichedMessage() {
    	return new Service(this.getClass().getSimpleName(), "ready");
    }
	
	@RequestMapping(value = "/img", method = {RequestMethod.POST}, produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public byte[] convertToInlineTextImage(@RequestHeader(value="Accept") String acceptType,
										   @RequestParam String text,
										   @RequestParam(required = false, defaultValue = "false") String base64,
										   @RequestParam(required = false, defaultValue = "Arial-14") String font,
										   @RequestParam(required = false, defaultValue = "black") String foreColor,
										   @RequestParam(required = false) String backColor) {
		logger.info(acceptType);
		logger.info("Text: " + text);
		return convert(Boolean.valueOf(base64) ? new String(Base64.getDecoder().decode(text)) :	text,
				new TextToGraphicsConverter(createGraphicsProperties(font, foreColor, backColor)), 
				GraphicsType.value(acceptType));
    }

	private GraphicsProperites createGraphicsProperties(String font, String foreColor, String backColor) {
		GraphicsProperites prop = new GraphicsProperites();
        prop.setFont(createFont(font));
        prop.setBackColor(getColor(backColor));
        prop.setForeColor(getColor(foreColor));
		return prop;
	}
	
	private Color getColor(String name) {
		try {
		    return (Color)Color.class.getField(name).get(null);
		} catch (Exception e) {
		    return null; // Not defined
		}
	}
		
	private Font createFont(String font) {
		int lastIndex = font.lastIndexOf('-');
		return new Font(font.substring(0, lastIndex), Font.PLAIN, Integer.valueOf(font.substring(lastIndex + 1)));
	}

	private byte[] convert(String text, Converter<String, BufferedImage> converter, GraphicsType type) {
		BufferedImage image = converter.convert(text);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
        	ImageIO.write(image, type.name().toLowerCase(), baos);
        	return baos.toByteArray();
        } catch (IOException e) {
        	return null;
		}
        
	}
}
