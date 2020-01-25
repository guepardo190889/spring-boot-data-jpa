package com.blackdeath.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz para el manejo de los archivos de fotos
 * 
 * @author blackdeath
 *
 */
public interface IUploadImageService {

	/**
	 * Busca un archivo por su nombre y lo devuelve como Resource
	 * 
	 * @param filename
	 * @return
	 * @throws MalformedURLException
	 */
	public Resource getAsResource(String filename) throws MalformedURLException;

	/**
	 * Genera un nombre único para una foto y la guarda
	 * 
	 * @param file
	 * @return el nombre único de la foto guardada
	 * @throws IOException
	 */
	public String saveWithUniqueName(MultipartFile file) throws IOException;

	/**
	 * Elimina una foto
	 * 
	 * @param filename
	 * @return
	 */
	public boolean delete(String filename);

	/**
	 * Elimina recursivamente el directorio donde se guardan las fotos
	 * 
	 * @throws IOException
	 */
	public void deleteUploadsDirectory() throws IOException;

	/**
	 * Crea el directorio donde se guardan las fotos
	 * 
	 * @throws IOException
	 */
	public void createUploadsDirectory() throws IOException;
}
