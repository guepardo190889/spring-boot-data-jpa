/**
 * 
 */
package com.blackdeath.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author blackdeath
 *
 */
public interface IUploadFileService {

	public Resource cargar(String filename) throws MalformedURLException;

	public String copiar(MultipartFile file) throws IOException;

	public boolean eliminar(String filename);

}
