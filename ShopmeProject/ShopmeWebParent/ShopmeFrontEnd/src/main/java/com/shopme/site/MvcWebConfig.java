package com.shopme.site;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcWebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exportDirectory("../category-images",registry);
		exportDirectory("../brand-logos",registry);
		exportDirectory("../product-images",registry);
		exportDirectory("../site-logo",registry);
	}
	
	private void exportDirectory(String pathPattern,ResourceHandlerRegistry registry) {
		Path path=Paths.get(pathPattern);
		String absolutePath=path.toFile().getAbsolutePath();
		String dirName=pathPattern.replace("../", "")+"/**";
		registry.addResourceHandler(dirName).addResourceLocations("file:/" + absolutePath + "/");
	}

}
