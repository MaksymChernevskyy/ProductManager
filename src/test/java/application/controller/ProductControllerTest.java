package application.controller;

import static org.mockito.Mockito.when;

import application.configuration.ApplicationConfiguration;
import application.generators.ProductGenerator;
import application.model.Product;
import application.service.ProductService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import application.service.ServiceOperationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {

  private final String urlAddressTemplate = "/products";

  @Autowired
  private ObjectMapper mapper = ApplicationConfiguration.getObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @Test
  void shouldFindAllProducts() throws Exception {
    //Given
    List<Product> expectedProduct = Arrays.asList(ProductGenerator.getRandomProduct(), ProductGenerator.getRandomProduct());
    when(productService.getAllProducts()).thenReturn(Optional.of(expectedProduct));

    //When
    MvcResult result = mockMvc
        .perform(get(urlAddressTemplate)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    List<Product> actualProduct = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>() {
    });

    //Then
    assertEquals(HttpStatus.OK.value(), actualHttpStatus);
    assertNotNull(actualProduct);
    assertEquals(expectedProduct, actualProduct);
    verify(productService).getAllProducts();
  }

  @Test
  void shouldReturnEmptyListOfProductsWhenThereAreNoProductsInTheDatabase() throws Exception {
    //Given
    when(productService.getAllProducts()).thenReturn(Optional.empty());

    //When
    MvcResult result = mockMvc
        .perform(get(urlAddressTemplate)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    List<Product> actualProduct = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>() {
    });

    //Then
    assertEquals(HttpStatus.OK.value(), actualHttpStatus);
    assertNotNull(actualProduct);
    assertEquals(new ArrayList<Product>(), actualProduct);
    verify(productService).getAllProducts();
  }

  @Test
  void shouldReturnInternalServerErrorDuringFindingAllProductsWhenSomethingWentWrongOnServer() throws Exception {
    //Given
    doThrow(ServiceOperationException.class).when(productService).getAllProducts();
    ErrorMessage expectedResponse = new ErrorMessage("Internal server error while getting products.");

    //When
    MvcResult result = mockMvc
        .perform(get(urlAddressTemplate)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    ErrorMessage actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ErrorMessage.class);

    //Then
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualHttpStatus);
    assertNotNull(actualResponse);
    assertEquals(expectedResponse, actualResponse);
    verify(productService).getAllProducts();
  }

  @Test
  void shouldFindProduct() throws Exception {
    //Given
    Product expectedProduct = ProductGenerator.getRandomProduct();
    Long id = expectedProduct.getId();
    when(productService.getProduct(id)).thenReturn(Optional.of(expectedProduct));

    //When
    MvcResult result = mockMvc
        .perform(get(String.format("%s/%d", urlAddressTemplate, id))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    Product actualProduct = mapper.readValue(result.getResponse().getContentAsString(), Product.class);

    //Then
    assertEquals(HttpStatus.OK.value(), actualHttpStatus);
    assertNotNull(actualProduct);
    assertEquals(expectedProduct, actualProduct);
    verify(productService).getProduct(id);
  }

  @Test
  void shouldFindProductByName() throws Exception {
    //Given
    Product expectedProduct = ProductGenerator.getRandomProduct();
    List<Product> productList = Collections.singletonList(expectedProduct);
    String name = expectedProduct.getName().getValue();
    when(productService.getAllProducts()).thenReturn(Optional.of(productList));

    //When
    MvcResult result = mockMvc
        .perform(get(String.format("%s/byName", urlAddressTemplate))
            .param("name", name)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    Product actualProduct = mapper.readValue(result.getResponse().getContentAsString(), Product.class);

    //Then
    assertEquals(HttpStatus.OK.value(), actualHttpStatus);
    assertNotNull(actualProduct);
    assertEquals(expectedProduct, actualProduct);
    verify(productService).getAllProducts();
  }

  @Test
  void shouldCreateProduct() throws Exception {
    //Given
    Product expectedProduct = ProductGenerator.getRandomProduct();
    Long id = expectedProduct.getId();
    when(productService.productExistsById(id)).thenReturn(false);
    when(productService.createProduct(expectedProduct)).thenReturn(Optional.of(expectedProduct));

    //When
    MvcResult result = mockMvc
        .perform(post(urlAddressTemplate)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(mapper.writeValueAsString(expectedProduct))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    Product actualProduct = mapper.readValue(result.getResponse().getContentAsString(), Product.class);
    String actualLocationHeader = result.getResponse().getHeader("location");

    //Then
    assertEquals(HttpStatus.CREATED.value(), actualHttpStatus);
    assertEquals(String.format("/products/%s", id), actualLocationHeader);
    assertNotNull(actualProduct);
    assertEquals(expectedProduct, actualProduct);
    verify(productService).productExistsById(id);
    verify(productService).createProduct(expectedProduct);
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    //Given
    Product productToUpdate = ProductGenerator.getRandomProduct();
    Long id = productToUpdate.getId();
    when(productService.productExistsById(id)).thenReturn(true);

    //When
    MvcResult result = mockMvc
        .perform(put(String.format("%s/%d", urlAddressTemplate, id))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(mapper.writeValueAsString(productToUpdate))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();
    Product actualProduct = mapper.readValue(result.getResponse().getContentAsString(), Product.class);

    //Then
    assertEquals(HttpStatus.OK.value(), actualHttpStatus);
    assertNotNull(actualProduct);
    assertEquals(productToUpdate, actualProduct);
    verify(productService).productExistsById(id);
    verify(productService).updateProduct(productToUpdate);
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    //Given
    Product product = ProductGenerator.getRandomProduct();
    Long id = product.getId();
    when(productService.getProduct(id)).thenReturn(Optional.of(product));
    doNothing().when(productService).deleteProduct(id);

    //When
    MvcResult result = mockMvc
        .perform(delete(String.format("%s/%d", urlAddressTemplate, id))
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andReturn();
    int actualHttpStatus = result.getResponse().getStatus();

    //Then
    assertEquals(HttpStatus.OK.value(), actualHttpStatus);
    verify(productService).getProduct(id);
    verify(productService).deleteProduct(id);
  }
}