/**
 * 
 */
package com.blackdeath.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.blackdeath.springboot.app.controllers.ClienteController;

/**
 * Implementación para el manejo de fotos en el FileSystem del sistema operativo
 * 
 * @author blackdeath
 *
 */
@Service
public class UploadImageServiceImpl implements IUploadImageService {

	private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

	@Value("${paths.clientes.fotos}")
	private String rootPath;

	@Override
	public Resource getAsResource(String filename) throws MalformedURLException {
		Path rutaAbsolutaFoto = getRutaAbsoluta(filename);

		Resource recurso = null;

		recurso = new UrlResource(rutaAbsolutaFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("No se pudo cargar el archivo: " + rutaAbsolutaFoto);
		}

		return recurso;
	}

	@Override
	public String saveWithUniqueName(MultipartFile file) throws IOException {
		String nombreUnicoFoto = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		Path rutaAbsolutaFotoNueva = getRutaAbsoluta(nombreUnicoFoto);

		Files.copy(file.getInputStream(), rutaAbsolutaFotoNueva);

		log.info("rutaFoto: " + rutaAbsolutaFotoNueva);

		return nombreUnicoFoto;
	}

	@Override
	public boolean delete(String filename) {
		Path rutaAbsolutaFoto = getRutaAbsoluta(filename);

		File file = rutaAbsolutaFoto.toFile();

		if (file.exists() && file.canRead()) {
			return file.delete();
		}
		return false;
	}

	@Override
	public void deleteUploadsDirectory() throws IOException {
		FileSystemUtils.deleteRecursively(Paths.get(rootPath));
	}

	@Override
	public void createUploadsDirectory() throws IOException {
		Files.createDirectories(Paths.get(rootPath));
	}

	public Path getRutaAbsoluta(String filename) {
		return Paths.get(rootPath).resolve(filename).toAbsolutePath();
	}

}
