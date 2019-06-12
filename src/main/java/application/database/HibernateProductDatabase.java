package application.database;

import application.model.Product;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "application.database", havingValue = "hibernate")
@Repository
public class HibernateProductDatabase implements ProductDatabase {

  private HibernateProductRepository hibernateProductRepository;

  @Autowired
  public HibernateProductDatabase(HibernateProductRepository hibernateProductRepository) {
    this.hibernateProductRepository = hibernateProductRepository;
  }
  @Override
  public Optional<Product> save(Product product) throws DatabaseOperationException {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    try {
      return Optional.of(hibernateProductRepository.save(product));
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("An error while saving product.", e);
    }
  }

  @Override
  public Optional<Product> findById(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      return hibernateProductRepository.findById(id);
    } catch (NoSuchElementException e) {
      throw new DatabaseOperationException("An error while searching for product.", e);
    }
  }

  @Override
  public long count() throws DatabaseOperationException {
    try {
      return hibernateProductRepository.count();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("An error while counting products.", e);
    }
  }

  @Override
  public boolean existsById(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      return hibernateProductRepository.existsById(id);
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("An error while searching for product.", e);
    }
  }

  @Override
  public Optional<List<Product>> findAll() throws DatabaseOperationException {
    try {
      return Optional.of(hibernateProductRepository.findAll());
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("An error while searching for products.", e);
    }
  }

  @Override
  public void deleteById(Long id) throws DatabaseOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }
    try {
      hibernateProductRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new DatabaseOperationException("There was no product in database.", e);
    }
  }

  @Override
  public void deleteAll() throws DatabaseOperationException {
    try {
      hibernateProductRepository.deleteAll();
    } catch (NonTransientDataAccessException e) {
      throw new DatabaseOperationException("An error while deleting all products.", e);
    }
  }
}
