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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bg.vitkinov.edu.utils.Converter;
import bg.vitkinov.edu.utils.GraphicsProperites;
import bg.vitkinov.edu.utils.GraphicsType;
import bg.vitkinov.edu.utils.TextToGraphicsConverter;

@RestController
@RequestMapping(value = "/")
public class ImageService {
	
	private Logger logger = Logger.getLogger(getClass().getName());

	@RequestMapping(value = "/{text}", method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public byte[] convertToInlineTextImage(@RequestHeader(value="Accept") String acceptType,
										   @PathVariable String text,
										   @RequestParam(required = false, defaultValue = "false") String base64,
										   @RequestParam(required = false, defaultValue = "Arial-14") String font,
										   @RequestParam(required = false, defaultValue = "black") String foreColor,
										   @RequestParam(required = false) String backColor) {
		logger.info(acceptType);
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
