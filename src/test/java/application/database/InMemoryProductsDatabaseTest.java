package application.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.generators.ProductGenerator;
import application.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryProductsDatabaseTest {

  private ProductDatabase productDatabase;

  @BeforeEach
  void setup() {
    productDatabase = new InMemoryProductsDatabase();
  }

  @Test
  void shouldSaveProduct() throws DatabaseOperationException {
    //given
    Product productToCreate = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.save(productToCreate);

    //when
    assertTrue(createdProduct.isPresent());
    Optional<Product> productFromDatabase = productDatabase.findById(createdProduct.get().getId());

    //then
    assertNotNull(productFromDatabase);
    assertEquals(productFromDatabase, createdProduct);
  }

  @Test
  void shouldUpdateProduct() throws DatabaseOperationException {
    //given
    Product productToCreating = ProductGenerator.getRandomProduct();
    Optional<Product> productToUpdate = productDatabase.save(productToCreating);
    assertTrue(productToUpdate.isPresent());
    assertEquals(productToUpdate, productDatabase.findById(productToUpdate.get().getId()));
    productToUpdate.get().setId(11L);


    //when
    Optional<Product> updatedProduct = productDatabase.save(productToUpdate.get());
    assertTrue(updatedProduct.isPresent());
    Optional<Product> productFromDatabase = productDatabase.findById(updatedProduct.get().getId());

    //then
    assertNotNull(productFromDatabase);
    assertEquals(productToUpdate, updatedProduct);
  }

  @Test
  void shouldFindOneProduct() throws DatabaseOperationException {
    //given
    Product productToCreating = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.save(productToCreating);

    //when
    assertTrue(createdProduct.isPresent());
    Optional<Product> productFromDatabase = productDatabase.findById(createdProduct.get().getId());

    //then
    assertNotNull(productFromDatabase);
    assertEquals(productFromDatabase, createdProduct);
  }

  @Test
  void shouldReturnTrueIfProductExistsInDatabase() throws DatabaseOperationException {
    //given
    Product productToCreate = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.save(productToCreate);

    //when
    assertTrue(createdProduct.isPresent());
    boolean isProductExist = productDatabase.existsById(createdProduct.get().getId());

    //then
    assertTrue(isProductExist);
  }

  @Test
  void shouldReturnFalseIfProductNotExistsInDatabase() throws DatabaseOperationException {
    //when
    boolean isProductExist = productDatabase.existsById(1L);

    //then
    assertFalse(isProductExist);
  }

  @Test
  void shouldFindAllProducts() throws DatabaseOperationException {
    //given
    List<Product> expectedProduct = new ArrayList<>();
    Product productToCreate1 = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct1 = productDatabase.save(productToCreate1);
    assertTrue(createdProduct1.isPresent());
    expectedProduct.add(createdProduct1.get());
    Product productToCreate2 = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct2 = productDatabase.save(productToCreate2);
    assertTrue(createdProduct2.isPresent());
    expectedProduct.add(createdProduct2.get());
    Product productToCreate3 = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct3 = productDatabase.save(productToCreate3);
    assertTrue(createdProduct3.isPresent());
    expectedProduct.add(createdProduct3.get());

    //when
    Optional<List<Product>> productFromDatabase = productDatabase.findAll();

    //then
    assertNotNull(productFromDatabase);
    assertTrue(productFromDatabase.isPresent());
    assertEquals(expectedProduct, productFromDatabase.get());
  }

  @Test
  void shouldDeleteProduct() throws DatabaseOperationException {
    //given
    Product productToCreate = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.save(productToCreate);
    assertTrue(createdProduct.isPresent());
    assertTrue(productDatabase.existsById(createdProduct.get().getId()));

    //when
    productDatabase.deleteById(createdProduct.get().getId());
    boolean isProductExists = productDatabase.existsById(createdProduct.get().getId());

    //then
    assertFalse(isProductExists);
  }

  @Test
  void shouldDeleteAllProducts() throws DatabaseOperationException {
    //given
    Product productToSave1 = ProductGenerator.getRandomProduct();
    Optional<Product> savedProduct1 = productDatabase.save(productToSave1);
    Product productToSave2 = ProductGenerator.getRandomProduct();
    Optional<Product> savedProduct = productDatabase.save(productToSave2);
    List<Product> productInDatabase = new ArrayList<>();
    assertTrue(savedProduct1.isPresent());
    productInDatabase.add(savedProduct1.get());
    assertTrue(savedProduct.isPresent());
    productInDatabase.add(savedProduct.get());
    assertEquals(productInDatabase.size(), productDatabase.count());

    //when
    productDatabase.deleteAll();
    long numberOfProducts = productDatabase.count();

    //then
    assertEquals(0, numberOfProducts);
  }

  @Test
  void shouldReturnCountOfProducts() throws DatabaseOperationException {
    //given
    List<Product> products = new ArrayList<>();
    Product productToSave1 = ProductGenerator.getRandomProduct();
    Optional<Product> savedProduct = productDatabase.save(productToSave1);
    assertTrue(savedProduct.isPresent());
    products.add(savedProduct.get());
    Product productToSave2 = ProductGenerator.getRandomProduct();
    Optional<Product> savedProduct2 = productDatabase.save(productToSave2);
    assertTrue(savedProduct2.isPresent());
    products.add(savedProduct2.get());
    Product productToSave3 = ProductGenerator.getRandomProduct();
    Optional<Product> savedProduct3 = productDatabase.save(productToSave3);
    assertTrue(savedProduct3.isPresent());
    products.add(savedProduct3.get());

    //when
    long numberOfProducts = productDatabase.count();

    //then
    assertEquals(products.size(), numberOfProducts);
  }

  @Test
  void saveMethodShouldThrowExceptionForNullIAsProduct() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.save(null));
  }

  @Test
  void findMethodShouldThrowExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.findById(null));
  }

  @Test
  void deleteMethodShouldThrowExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.deleteById(null));
  }

  @Test
  void existsMethodShouldThrowExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.existsById(null));
  }
}
