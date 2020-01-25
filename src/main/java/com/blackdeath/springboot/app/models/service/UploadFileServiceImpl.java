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
 * Lógica de negocio para manejo de carga, copia y eliminación de fotos
 * 
 * @author blackdeath
 *
 */
@Service
public class UploadFileServiceImpl implements IUploadFileService {

	@Value("${paths.clientes.fotos}")
	private String rutaFotosCliente;

	private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

	/**
	 * Busca un archivo por su nombre y lo devuelve como Resource
	 * 
	 */
	@Override
	public Resource cargar(String filename) throws MalformedURLException {
		Path rutaAbsolutaFoto = getRuta(filename);
		log.info("rutaAbsolutaFoto: " + rutaAbsolutaFoto);

		Resource recurso = null;

		recurso = new UrlResource(rutaAbsolutaFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("No se pudo cargar el archivo: " + rutaAbsolutaFoto);
		}

		return recurso;
	}

	@Override
	public String copiar(MultipartFile file) throws IOException {
		String nombreUnicoFoto = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		Path rutaAbsolutaFotoNueva = getRuta(nombreUnicoFoto);

		Files.copy(file.getInputStream(), rutaAbsolutaFotoNueva);

		log.info("rutaFoto: " + rutaAbsolutaFotoNueva);

		return nombreUnicoFoto;
	}

	@Override
	public boolean eliminar(String filename) {
		Path rutaAbsolutaFoto = getRuta(filename);

		File file = rutaAbsolutaFoto.toFile();

		if (file.exists() && file.canRead()) {
			return file.delete();
		}
		return false;
	}

	public Path getRuta(String filename) {
		return Paths.get(rutaFotosCliente).resolve(filename).toAbsolutePath();
	}

	@Override
	public void borrarTodo() throws IOException {
		FileSystemUtils.deleteRecursively(Paths.get(rutaFotosCliente));
	}

	@Override
	public void init() throws IOException {
		Files.createDirectories(Paths.get(rutaFotosCliente));
	}

}
