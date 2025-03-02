package application.database;

import application.model.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "application.database", havingValue = "in-memory")
@Repository
public class InMemoryProductsDatabase implements ProductDatabase {
  private Map<Long, Product> products = Collections.synchronizedMap(new HashMap<>());
  private AtomicLong counter = new AtomicLong();
  private final Object lock = new Object();


  @Override
  public Optional<Product> save(Product product) throws DatabaseOperationException {
    synchronized (lock) {
      if (product == null) {
        throw new IllegalArgumentException("Product cannot be null");
      }
      if (isProductExist(product.getId())) {
        products.put(product.getId(), product);
        return Optional.of(product);
      }
      long id = counter.incrementAndGet();
      product.setId(id);
      products.put(id, product);
      return Optional.of(product);
    }
  }

  @Override
  public Optional<Product> findById(Long id) throws DatabaseOperationException {
    synchronized (lock) {
      if (id == null) {
        throw new IllegalArgumentException("Id cannot be null");
      }
      return Optional.ofNullable(products.get(id));
    }
  }

  @Override
  public long count() throws DatabaseOperationException {
    synchronized (lock) {
      return products.size();
    }
  }

  @Override
  public boolean existsById(Long id) {
    synchronized (lock) {
      if (id == null) {
        throw new IllegalArgumentException("Id cannot be null");
      }
      return isProductExist(id);
    }
  }

  @Override
  public Optional<List<Product>> findAll() throws DatabaseOperationException {
    synchronized (lock) {
      return Optional.of(new ArrayList<>(products.values()));
    }
  }

  @Override
  public void deleteById(Long id) throws DatabaseOperationException {
    synchronized (lock) {
      if (id == null) {
        throw new IllegalArgumentException("Id cannot be null");
      }
      if (!isProductExist(id)) {
        throw new DatabaseOperationException("Product does not exist");
      }
      products.remove(id);
    }
  }

  @Override
  public void deleteAll() throws DatabaseOperationException {
    synchronized (lock) {
      products.clear();
    }
  }

  private boolean isProductExist(long id) {
    return products.containsKey(id);
  }
}
