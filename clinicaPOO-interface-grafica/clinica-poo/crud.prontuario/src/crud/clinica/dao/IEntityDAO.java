package crud.clinica.dao;

import java.sql.SQLException;
import java.util.List;

import crud.clinica.exception.CPFJaExisteException;

public interface IEntityDAO <T> {

	public void create(T t) throws SQLException, CPFJaExisteException;
	
	public T findById(Long id) throws SQLException;
	
	public void delete(T t) throws Exception;
	
	public List<T> findAll() throws SQLException;
	
	public void update(T t) throws SQLException, CPFJaExisteException;
	
//	public T findByCPF(String s);
}
