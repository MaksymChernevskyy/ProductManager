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
  public Optional<Product> create(Product product) throws DatabaseOperationException {
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
  public Optional<Product> find(Long id) throws DatabaseOperationException {
    synchronized (lock) {
      if (id == null) {
        throw new IllegalArgumentException("Id cannot be null");
      }
      if (id < 0) {
        throw new IllegalArgumentException("Id cannot be lower than zero");
      }
      return Optional.ofNullable(products.get(id));
    }
  }

  @Override
  public boolean exists(Long id) {
    synchronized (lock) {
      if (id == null) {
        throw new IllegalArgumentException("Id cannot be null");
      }
      if (id < 0) {
        throw new IllegalArgumentException("Id cannot be lower than zero");
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
  public void delete(Long id) throws DatabaseOperationException {
    synchronized (lock) {
      if (id == null) {
        throw new IllegalArgumentException("Id cannot be null");
      }
      if (id < 0) {
        throw new IllegalArgumentException("Id cannot be lower than zero");
      }
      if (!isProductExist(id)) {
        throw new DatabaseOperationException("Product does not exist");
      }
      products.remove(id);
    }
  }

  private boolean isProductExist(long id) {
    return products.containsKey(id);
  }
}
