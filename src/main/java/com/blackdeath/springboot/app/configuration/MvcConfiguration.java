/**
 * 
 */
package com.blackdeath.springboot.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author blackdeath
 *
 */

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

	@Value("${paths.clientes.fotos}")
	private String rutaFotosCliente;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);

		registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + rutaFotosCliente);
	}

}
