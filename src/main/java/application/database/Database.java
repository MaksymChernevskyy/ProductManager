package application.database;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Database<T, ID extends Serializable> {

  Optional<T> save(T entity) throws DatabaseOperationException;

  Optional<T> findById(ID id) throws DatabaseOperationException;

  long count() throws DatabaseOperationException;

  boolean existsById(ID id) throws DatabaseOperationException;

  Optional<List<T>> findAll() throws DatabaseOperationException;

  void deleteById(ID id) throws DatabaseOperationException;

  void deleteAll() throws DatabaseOperationException;

}
