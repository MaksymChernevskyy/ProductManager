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
  void shouldCreateProduct() throws DatabaseOperationException {
    //given
    Product productToCreate = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.create(productToCreate);

    //when
    assertTrue(createdProduct.isPresent());
    Optional<Product> productFromDatabase = productDatabase.find(createdProduct.get().getId());

    //then
    assertNotNull(productFromDatabase);
    assertEquals(productFromDatabase, createdProduct);
  }

  @Test
  void shouldUpdateProduct() throws DatabaseOperationException {
    //given
    Product productToCreating = ProductGenerator.getRandomProduct();
    Optional<Product> productToUpdate = productDatabase.create(productToCreating);
    assertTrue(productToUpdate.isPresent());
    assertEquals(productToUpdate, productDatabase.find(productToUpdate.get().getId()));
    productToUpdate.get().setId(11L);


    //when
    Optional<Product> updatedProduct = productDatabase.create(productToUpdate.get());
    assertTrue(updatedProduct.isPresent());
    Optional<Product> productFromDatabase = productDatabase.find(updatedProduct.get().getId());

    //then
    assertNotNull(productFromDatabase);
    assertEquals(productToUpdate, updatedProduct);
  }

  @Test
  void shouldFindOneProduct() throws DatabaseOperationException {
    //given
    Product productToCreating = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.create(productToCreating);

    //when
    assertTrue(createdProduct.isPresent());
    Optional<Product> productFromDatabase = productDatabase.find(createdProduct.get().getId());

    //then
    assertNotNull(productFromDatabase);
    assertEquals(productFromDatabase, createdProduct);
  }

  @Test
  void shouldReturnTrueIfProductExistsInDatabase() throws DatabaseOperationException {
    //given
    Product productToCreate = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct = productDatabase.create(productToCreate);

    //when
    assertTrue(createdProduct.isPresent());
    boolean isProductExist = productDatabase.exists(createdProduct.get().getId());

    //then
    assertTrue(isProductExist);
  }

  @Test
  void shouldReturnFalseIfProductNotExistsInDatabase() throws DatabaseOperationException {
    //when
    boolean isProductExist = productDatabase.exists(1L);

    //then
    assertFalse(isProductExist);
  }

  @Test
  void shouldFindAllProducts() throws DatabaseOperationException {
    //given
    List<Product> expectedProduct = new ArrayList<>();
    Product productToCreate1 = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct1 = productDatabase.create(productToCreate1);
    assertTrue(createdProduct1.isPresent());
    expectedProduct.add(createdProduct1.get());
    Product productToCreate2 = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct2 = productDatabase.create(productToCreate2);
    assertTrue(createdProduct2.isPresent());
    expectedProduct.add(createdProduct2.get());
    Product productToCreate3 = ProductGenerator.getRandomProduct();
    Optional<Product> createdProduct3 = productDatabase.create(productToCreate3);
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
    Optional<Product> createdProduct = productDatabase.create(productToCreate);
    assertTrue(createdProduct.isPresent());
    assertTrue(productDatabase.exists(createdProduct.get().getId()));

    //when
    productDatabase.delete(createdProduct.get().getId());
    boolean isProductExists = productDatabase.exists(createdProduct.get().getId());

    //then
    assertFalse(isProductExists);
  }

  @Test
  void createMethodShouldThrowExceptionForNullIAsProduct() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.create(null));
  }

  @Test
  void findMethodShouldThrowExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.find(null));
  }

  @Test
  void deleteMethodShouldThrowExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.delete(null));
  }

  @Test
  void existsMethodShouldThrowExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.exists(null));
  }

  @Test
  void findMethodShouldThrowExceptionForNegativeNumberAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.find(-1L));
  }

  @Test
  void deleteMethodShouldThrowExceptionForNegativeNumberAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.delete(-1L));
  }

  @Test
  void existsMethodShouldThrowExceptionForNegativeNumberAsId() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.exists(-1L));
  }
}