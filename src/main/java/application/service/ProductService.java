package application.service;

import application.database.DatabaseOperationException;
import application.database.ProductDatabase;
import application.model.Product;
import java.util.List;
import java.util.Optional;

public class ProductService {

  private ProductDatabase productDatabase;

  public ProductService(ProductDatabase productDatabase) {
    this.productDatabase = productDatabase;
  }

  public Optional<Product> createProduct(Product product) throws ServiceOperationException {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null.");
    }
    try {
      Long id = product.getId();
      if (id != null && productDatabase.exists(id)) {
        throw new ServiceOperationException(String.format("Product with id %s already exists", id));
      }
      return productDatabase.create(product);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error while adding product.", e);
    }
  }

  public Optional<List<Product>> getAllProducts() throws ServiceOperationException {
    try {
      return productDatabase.findAll();
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error while getting all products", e);
    }
  }

  public Optional<Product> getProduct(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      return productDatabase.find(id);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error while getting product.", e);
    }
  }

  public void updateProduct(Product product) throws ServiceOperationException {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null.");
    }
    try {
      Long id = product.getId();
      if (id == null || !productDatabase.exists(id)) {
        throw new ServiceOperationException(String.format("Product with id %s does not exist", id));
      }
      productDatabase.create(product);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error while updating product.", e);
    }
  }

  public void deleteProduct(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      if (!productDatabase.exists(id)) {
        throw new ServiceOperationException(String.format("Product with id %s does not exist", id));
      }
      productDatabase.delete(id);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error while deleting product.", e);
    }
  }

  public boolean productExistsById(Long id) throws ServiceOperationException {
    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null.");
    }
    try {
      return productDatabase.exists(id);
    } catch (DatabaseOperationException e) {
      throw new ServiceOperationException("An error while checking if product exist.", e);
    }
  }
}
