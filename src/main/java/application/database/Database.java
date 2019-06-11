package application.database;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Database<T, ID extends Serializable> {

  Optional<T> create(T entity) throws DatabaseOperationException;

  Optional<T> find(ID id) throws DatabaseOperationException;

  boolean exists(ID id) throws DatabaseOperationException;

  Optional<List<T>> findAll() throws DatabaseOperationException;

  void delete(ID id) throws DatabaseOperationException;

}
