package com.blackdeath.springboot.app.models.dao;

import java.util.List;

import com.blackdeath.springboot.app.models.entity.Cliente;

/**
 * @author blackdeath
 *
 */
public interface IClienteDao {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
}
