package bg.vitkinov.edu.utils;

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
