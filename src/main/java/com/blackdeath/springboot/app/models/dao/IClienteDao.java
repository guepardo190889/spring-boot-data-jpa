package com.blackdeath.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.blackdeath.springboot.app.models.entity.Cliente;

/**
 * @author blackdeath
 *
 */
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
