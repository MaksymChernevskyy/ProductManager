package application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.database.DatabaseOperationException;
import application.database.ProductDatabase;
import application.generators.ProductGenerator;
import application.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductDatabase productDatabase;

  @InjectMocks
  private ProductService productService;

  @Test
  void shouldFindAllProducts() throws DatabaseOperationException, ServiceOperationException {
    //Given
    List<Product> expectedProduct = new ArrayList<>();
    Product randomProduct1 = ProductGenerator.getRandomProduct();
    Product randomProduct2 = ProductGenerator.getRandomProduct();
    expectedProduct.add(randomProduct1);
    expectedProduct.add(randomProduct2);
    when(productDatabase.findAll()).thenReturn(Optional.of(expectedProduct));

    //When
    Optional<List<Product>> actualProducts = productService.getAllProducts();

    //Then
    assertTrue(actualProducts.isPresent());
    assertEquals(expectedProduct, actualProducts.get());
    verify(productDatabase).findAll();
  }

  @Test
  void shouldFindProduct() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Optional<Product> expectedProduct = Optional.of(ProductGenerator.getRandomProduct());
    Long id = expectedProduct.get().getId();
    when(productDatabase.findById(id)).thenReturn(expectedProduct);

    //When
    Optional<Product> actualProducts = productService.getProduct(id);

    //Then
    assertEquals(expectedProduct, actualProducts);
    verify(productDatabase).findById(id);
  }

  @Test
  void shouldCreateProduct() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Product productToCreate = ProductGenerator.getRandomProduct();
    Product expectedProduct = ProductGenerator.getRandomProduct();
    when(productDatabase.save(productToCreate)).thenReturn(Optional.of(expectedProduct));
    when(productDatabase.existsById(productToCreate.getId())).thenReturn(false);

    //When
    Optional<Product> actualProduct = productService.createProduct(productToCreate);

    //Then
    assertTrue(actualProduct.isPresent());
    assertEquals(expectedProduct, actualProduct.get());
    verify(productDatabase).save(productToCreate);
    verify(productDatabase).existsById(productToCreate.getId());
  }

  @Test
  void shouldUpdateProduct() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Product product = ProductGenerator.getRandomProduct();
    when(productDatabase.save(product)).thenReturn(Optional.of(product));
    when(productDatabase.existsById(product.getId())).thenReturn(true);

    //When
    productService.updateProduct(product);

    //Then
    verify(productDatabase).save(product);
    verify(productDatabase).existsById(product.getId());
  }

  @Test
  void shouldDeleteProduct() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 3448L;
    when(productDatabase.existsById(id)).thenReturn(true);
    doNothing().when(productDatabase).deleteById(id);

    //When
    productService.deleteProduct(id);

    //Then
    verify(productDatabase).existsById(id);
    verify(productDatabase).deleteById(id);
  }

  @Test
  void shouldReturnTrueWhenExist() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 3448L;
    when(productDatabase.existsById(id)).thenReturn(true);

    //When
    productService.productExistsById(id);

    //Then
    verify(productDatabase).existsById(id);
  }

  @Test
  void shouldReturnFalseWhenNot() throws DatabaseOperationException, ServiceOperationException {
    //Given
    Long id = 3448L;
    when(productDatabase.existsById(id)).thenReturn(false);

    //When
    productService.productExistsById(id);

    //Then
    verify(productDatabase).existsById(id);
  }

  @Test
  void findProductMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productService.getProduct(null));
  }

  @Test
  void createProductMethodShouldThrowIllegalArgumentExceptionForNullAsProduct() {
    assertThrows(IllegalArgumentException.class, () -> productService.createProduct(null));
  }

  @Test
  void updateProductMethodShouldThrowIllegalArgumentExceptionForNullAsProduct() {
    assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(null));
  }

  @Test
  void deleteProductMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(null));
  }

  @Test
  void productExistsMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> productService.productExistsById(null));
  }

  @Test
  void findAllProductsMethodShouldThrowProductServiceOperationExceptionWhenWhenAnErrorOccurDuringExecutionFindingProductsInDatabase() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(productDatabase).findAll();

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.getAllProducts());
  }

  @Test
  void findProductMethodShouldThrowProductServiceOperationExceptionWhenWhenAnErrorOccurDuringExecutionFindingProductsInDatabase() throws DatabaseOperationException {
    //Given
    doThrow(DatabaseOperationException.class).when(productDatabase).findById(1L);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.getProduct(1L));
  }

  @Test
  void createProductMethodShouldThrowProductServiceOperationExceptionWhenProductAlreadyExistsInDatabase() throws DatabaseOperationException {
    //Given
    Product product= ProductGenerator.getRandomProduct();
    when(productDatabase.existsById(product.getId())).thenReturn(true);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.createProduct(product));
    verify(productDatabase, never()).save(product);
  }

  @Test
  void updateProductMethodShouldThrowProductServiceOperationExceptionWhenAnErrorOccurDuringExecutionFindingProductsInDatabase() throws DatabaseOperationException {
    //Given
    Product product = ProductGenerator.getRandomProduct();
    when(productDatabase.existsById(product.getId())).thenReturn(true);
    doThrow(DatabaseOperationException.class).when(productDatabase).save(product);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.updateProduct(product));
  }

  @Test
  void updateProductMethodShouldThrowProductServiceOperationExceptionWhenProductDoesNotExist() throws DatabaseOperationException {
    //Given
    Product product = ProductGenerator.getRandomProduct();
    when(productDatabase.existsById(product.getId())).thenReturn(false);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.updateProduct(product));
  }

  @Test
  void deleteProducMethodShouldThrowProductServiceOperationExceptionWhenWhenAnErrorOccurDuringExecutionFindingProductsFromDatabase() throws DatabaseOperationException {
    //Given
    when(productDatabase.existsById(1L)).thenReturn(true);
    doThrow(DatabaseOperationException.class).when(productDatabase).deleteById(1L);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.deleteProduct(1L));
  }

  @Test
  void deleteProductMethodShouldThrowProductServiceOperationExceptionWhenProductDosesNotExist() throws DatabaseOperationException {
    //Given
    Product product = ProductGenerator.getRandomProduct();
    when(productDatabase.existsById(product.getId())).thenReturn(false);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.deleteProduct(product.getId()));
  }

  @Test
  void productExistsMethodShouldThrowProductServiceOperationExceptionWhenWhenAnErrorOccurDuringExecutionFindingProductInDatabase() throws DatabaseOperationException {
    //Given
    long id = 1L;
    doThrow(DatabaseOperationException.class).when(productDatabase).existsById(id);

    //Then
    assertThrows(ServiceOperationException.class, () -> productService.productExistsById(id));
  }
}