package application.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.generators.ProductGenerator;
import application.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.NonTransientDataAccessException;

@ExtendWith(MockitoExtension.class)
class HibernateProductDatabaseTest {

  @Mock
  private HibernateProductRepository hibernateProductRepository;

  private ProductDatabase productDatabase;

  @BeforeEach
  void setup() {
    productDatabase = new HibernateProductDatabase(hibernateProductRepository);
  }

  @Test
  void shouldSaveProduct() throws DatabaseOperationException {
    //given
    Product product1 = ProductGenerator.getRandomProduct();
    Product product2 = ProductGenerator.getRandomProduct();
    when(hibernateProductRepository.save(product1)).thenReturn(product2);

    //when
    Optional<Product> savedProduct = productDatabase.save(product1);

    //then
    assertTrue(savedProduct.isPresent());
    assertEquals(product2, savedProduct.get());
    verify(hibernateProductRepository).save(product1);
  }

  @Test
  void shouldFindOneProduct() throws DatabaseOperationException {
    //given
    Product product = (ProductGenerator.getRandomProduct());
    Long id = product.getId();
    when(hibernateProductRepository.findById(id)).thenReturn(Optional.ofNullable(product));

    //When
    Optional<Product> actualProduct = productDatabase.findById(id);

    //Then
    assertTrue(actualProduct.isPresent());
    assertEquals(product, actualProduct.get());
    verify(hibernateProductRepository).findById(id);
  }

  @Test
  void shouldReturnTrueIfProductExistsInDatabase() throws DatabaseOperationException {
    //given
    Product product = ProductGenerator.getRandomProduct();
    when(hibernateProductRepository.existsById(product.getId())).thenReturn(true);

    //when
    boolean isProductExist = productDatabase.existsById(product.getId());

    //then
    Assert.assertTrue(isProductExist);
    verify(hibernateProductRepository).existsById(product.getId());
  }

  @Test
  void shouldReturnFalseIfProductExistsInDatabase() throws DatabaseOperationException {
    //given
    when(hibernateProductRepository.existsById(1L)).thenReturn(false);

    //when
    boolean isProductExist = productDatabase.existsById(1L);

    //then
    Assert.assertFalse(isProductExist);
    verify(hibernateProductRepository).existsById(1L);
  }

  @Test
  void shouldFindAllProducts() throws DatabaseOperationException {
    //given
    List<Product> expectedProduct = new ArrayList<>();
    expectedProduct.add(ProductGenerator.getRandomProduct());
    expectedProduct.add(ProductGenerator.getRandomProduct());
    when(hibernateProductRepository.findAll()).thenReturn(expectedProduct);

    //When
    Optional<List<Product>> actualProduct = productDatabase.findAll();

    //Then
    assertTrue(actualProduct.isPresent());
    assertEquals(expectedProduct, actualProduct.get());
    verify(hibernateProductRepository).findAll();
  }

  @Test
  void shouldDeleteProduct() throws DatabaseOperationException {
    //given
    Long id = 1L;
    doNothing().when(hibernateProductRepository).deleteById(id);

    //when
    productDatabase.deleteById(id);

    //then
    verify(hibernateProductRepository).deleteById(id);
  }

  @Test
  void shouldDeleteAllProducts() throws DatabaseOperationException {
    //given
    doNothing().when(hibernateProductRepository).deleteAll();

    //when
    productDatabase.deleteAll();

    //then
    verify(hibernateProductRepository).deleteAll();
  }

  @Test
  void shouldReturnCountOdProducts() throws DatabaseOperationException {
    //given
    long numberOfProducts = 3L;
    when(hibernateProductRepository.count()).thenReturn(numberOfProducts);

    //when
    long actualNumberOfProducts = productDatabase.count();

    //then
    Assert.assertEquals(numberOfProducts, actualNumberOfProducts);
    verify(hibernateProductRepository).count();
  }

  @Test
  void saveProductMethodShouldThrowExceptionWhenProductIsNull() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.save(null));
  }

  @Test
  void findByIdMethodShouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.findById(null));
  }

  @Test
  void existByIdMethodShouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.existsById(null));
  }

  @Test
  void deleteByIdMethodShouldThrowExceptionWhenIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> productDatabase.existsById(null));
  }

  @Test
  void deleteProductByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    Long id = 1L;
    EmptyResultDataAccessException mockedException = Mockito.mock(EmptyResultDataAccessException.class);
    doThrow(mockedException).when(hibernateProductRepository).deleteById(id);

    //Then
    assertThrows(DatabaseOperationException.class, () -> productDatabase.deleteById(id));
    verify(hibernateProductRepository).deleteById(id);
  }

  @Test
  void findProductByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    Long id = 1L;
    NoSuchElementException mockedException = Mockito.mock(NoSuchElementException.class);
    doThrow(mockedException).when(hibernateProductRepository).findById(id);

    //Then
    assertThrows(DatabaseOperationException.class, () -> productDatabase.findById(id));
    verify(hibernateProductRepository).findById(id);
  }

  @Test
  void existProductByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    Long id = 1L;
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(hibernateProductRepository).existsById(id);

    //Then
    assertThrows(DatabaseOperationException.class, () -> productDatabase.existsById(id));
    verify(hibernateProductRepository).existsById(id);
  }

  @Test
  void findAllProductByIdMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(hibernateProductRepository).findAll();

    //Then
    assertThrows(DatabaseOperationException.class, () -> productDatabase.findAll());
    verify(hibernateProductRepository).findAll();
  }

  @Test
  void deleteAllMethodShouldThrowExceptionWhenAnErrorOccurDuringExecution() {
    //Given
    NonTransientDataAccessException mockedException = Mockito.mock(NonTransientDataAccessException.class);
    doThrow(mockedException).when(hibernateProductRepository).deleteAll();

    //Then
    assertThrows(DatabaseOperationException.class, () -> productDatabase.deleteAll());
    verify(hibernateProductRepository).deleteAll();
  }

}