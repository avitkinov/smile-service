//package bg.vitkinov.edu.model;
//
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient("image-service")
//public interface ImageServiceClient {
//
//	@RequestMapping(value = "/img", method = {RequestMethod.POST}, produces = {MediaType.IMAGE_JPEG_VALUE})
//	public byte[] convertToInlineTextImage(@RequestHeader(value = "Accept") String acceptType,
//										   @RequestParam(value = "text") String text,
//										   @RequestParam(value = "base64", required = false, defaultValue = "false") boolean base64,
//										   @RequestParam(value = "font", required = false, defaultValue = "Arial-14") String font,
//										   @RequestParam(value = "foreColor", required = false, defaultValue = "black") String foreColor,
//										   @RequestParam(value = "backColor", required = false) String backColor);
//}
