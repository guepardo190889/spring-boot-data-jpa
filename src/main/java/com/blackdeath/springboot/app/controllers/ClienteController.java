package com.blackdeath.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blackdeath.springboot.app.models.entity.Cliente;
import com.blackdeath.springboot.app.models.service.IClienteService;
import com.blackdeath.springboot.app.models.service.IUploadImageService;
import com.blackdeath.springboot.app.util.PageRender;

/**
 * @author blackdeath
 *
 */
@SessionAttributes("cliente")
@Controller
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadImageService uploadImageService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Alta de Cliente");

		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, @RequestParam("file") MultipartFile foto, BindingResult result,
			Model model, SessionStatus status, RedirectAttributes flash) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Alta de Cliente");
			return "form";
		}

		if (!foto.isEmpty()) {
			if (cliente.getId() != null && cliente.getFoto() != null && !cliente.getFoto().isEmpty()
					&& cliente.getFoto().length() > 0) {
				if (uploadImageService.delete(cliente.getFoto())) {
					flash.addFlashAttribute("info", "Foto '" + cliente.getFoto() + "' eliminada con éxito");
				}
			}

			String nombreUnicoFoto = null;

			try {
				nombreUnicoFoto = uploadImageService.saveWithUniqueName(foto);

				flash.addFlashAttribute("info", "Foto '" + nombreUnicoFoto + "' subida con éxito");

				cliente.setFoto(nombreUnicoFoto);
			} catch (IOException ioe) {
				flash.addFlashAttribute("error", "Foto '" + nombreUnicoFoto + "' no pudo ser subida");
				ioe.printStackTrace();
			}
		}

		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito" : "Cliente guardado con éxito";

		clienteService.save(cliente);
		status.setComplete();

		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:/listar";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);

			if (cliente == null) {
				flash.addFlashAttribute("error", "El Id del Cliente no existe");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El Id del Cliente no puede ser cero");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");

		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");

			if (uploadImageService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto '" + cliente.getFoto() + "' eliminada con éxito");
			}

		}

		return "redirect:/listar";
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			flash.addAttribute("error", "El Id del Cliente no existe");
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle del cliente: " + cliente.getNombreConApellido());

		return "ver";
	}

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;

		try {
			recurso = uploadImageService.getAsResource(filename);
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

}
