package application.controller;

import application.model.Product;
import application.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/products", description = "Available operations for products manager application", tags = {"Products"})
@RestController
@RequestMapping("/products")
public class ProductController {

  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @ApiOperation(
      value = "Returns all products",
      response = Product.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Product.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> getAll() {
    try {
      Optional<List<Product>> optionalProductList = productService.getAllProducts();
      if (optionalProductList.isPresent()) {
        return new ResponseEntity<>(optionalProductList.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ArrayList<Product>(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage("Internal server error while getting products."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @GetMapping("/{id}")
  @ApiOperation(
      value = "Read existing product.",
      response = Product.class)
  @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 12", example = "12", dataType = "Long")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Product.class),
      @ApiResponse(code = 404, message = "Product not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> read(@PathVariable("id") Long id) {
    try {
      Optional<Product> optionalProduct = productService.getProduct(id);
      if (optionalProduct.isPresent()) {
        return new ResponseEntity<>(optionalProduct.get(), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ErrorMessage(String.format("Product not found for passed id: %d", id)), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage(String.format("Internal server error while getting product by id: %d", id)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/byName")
  @ApiOperation(
      value = "Returns all filtered by product name.",
      response = Product.class)
  @ApiImplicitParam(name = "name", value = "Possible letters, numbers and sign '/'  e.g. 'Name'", example = "name")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Product.class),
      @ApiResponse(code = 404, message = "Products not found for passed name.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> find(@RequestParam("name") String name) {
    try {
      Optional<List<Product>> optionalProductList = productService.getAllProducts();
      if (optionalProductList.isPresent()) {
        Optional<Product> optionalProduct = optionalProductList.get()
            .stream()
            .filter(productToFind -> productToFind.getName().getValue().equals(name))
            .findFirst();
        if (optionalProduct.isPresent()) {
          return new ResponseEntity<>(optionalProduct.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorMessage(String.format("Product not found for passed name: %s", name)), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(new ErrorMessage(String.format("Product not found for passed name: %s", name)), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage(String.format("Internal server error while getting product by name: %s", name)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  @ApiOperation(
      value = "Creates new product.",
      response = Product.class)
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Created", response = Product.class),
      @ApiResponse(code = 409, message = "Product already exists", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> create(@RequestBody(required = false) Product product) {
    try{
      if (product.getId() == null || !productService.productExistsById(product.getId())) {
        Optional<Product> addedProduct = productService.createProduct(product);
        HttpHeaders responseHeaders = new HttpHeaders();
        if (addedProduct.isPresent()) {
          responseHeaders.setLocation(URI.create(String.format("/products/%d", addedProduct.get().getId())));
          return new ResponseEntity<>(addedProduct.get(), responseHeaders, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ErrorMessage("Internal server error while adding product."), HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(new ErrorMessage("Product already exist."), HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage("Internal server error while adding product."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  @ApiOperation(
      value = "Update existing product.",
      response = Product.class)
  @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 12", example = "12", dataType = "Long")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Product.class),
      @ApiResponse(code = 404, message = "Product not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody(required = false) Product product) {
    try {
      if (!id.equals(product.getId())) {
        return new ResponseEntity<>(new ErrorMessage(String.format("Product to update has different id than %d.", id)), HttpStatus.BAD_REQUEST);
      }
      if (!productService.productExistsById(id)) {
        return new ResponseEntity<>(new ErrorMessage(String.format("Product with %d id does not exist.", id)), HttpStatus.NOT_FOUND);
      }
      productService.updateProduct(product);
      return new ResponseEntity<>(product, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage("Internal server error while updating product."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  @ApiOperation(
      value = "Removes existing product.",
      response = Product.class)
  @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 12", example = "12", dataType = "Long")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Product.class),
      @ApiResponse(code = 404, message = "Product not found for passed id.", response = ErrorMessage.class),
      @ApiResponse(code = 500, message = "Internal server error.", response = ErrorMessage.class)})
  public ResponseEntity<?> remove(@PathVariable("id") Long id) {
    try {
      Optional<Product> optionalProduct = productService.getProduct(id);
      if (!optionalProduct.isPresent()) {
        return new ResponseEntity<>(new ErrorMessage(String.format("Product with %d id does not exist.", id)), HttpStatus.NOT_FOUND);
      }
      productService.deleteProduct(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage("Internal server error while removing product."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
