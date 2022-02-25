package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductIdNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeast;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {
    private static final String USD = "USD";
    private static final String PRODUCT_ID = "L1";
    private static final String PRODUCT_CODE = "sgs";
    private static final String PRODUCT_DESCRIPTION = "Samsung Galaxy S";
    private static final String ANOTHER_PRODUCT_DESCRIPTION = "Siemens C61";
    private static final String PRODUCT_IMAGE_URL = "https://raw.githubusercontent.com/andrewosipenko" +
            "/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private static final int MIN_NUMBER_OF_INVOCATIONS = 1;
    private static final int PRODUCT_STOCK = 100;
    private static final int PRODUCT_PRICE = 30;

    private ProductDao productDao;
    private Product product;
    private static final Currency USD_CURRENCY = Currency.getInstance(USD);

    @Mock
    private Product mockProduct;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        product = new Product.Builder()
                .withId(PRODUCT_ID).
                withCode(PRODUCT_CODE).
                withDescription(PRODUCT_DESCRIPTION).
                withPrice(new BigDecimal(PRODUCT_PRICE))
                .withCurrency(USD_CURRENCY)
                .withStock(PRODUCT_STOCK)
                .withImageUrl(PRODUCT_IMAGE_URL)
                .build();
    }

    @After
    public void clear() {
        productDao.deleteAllProducts();
    }

    @Test
    public void productListShouldBeEmpty() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void getProductShouldReturnSavedProduct() throws ProductNotFoundException, ProductIdNotFoundException {
        productDao.save(product);
        assertEquals(product, productDao.getProduct(PRODUCT_ID));
    }

    @Test
    public void productsShouldEqualsWhenSavingProduct() throws ProductNotFoundException, ProductIdNotFoundException {
        productDao.save(product);
        Product result = productDao.getProduct(product.getId());
        assertEquals(product, result);
    }

    @Test
    public void getCodeShouldEquals() throws ProductNotFoundException, ProductIdNotFoundException {
        productDao.save(product);
        Product result = productDao.getProduct(product.getId());
        assertEquals(PRODUCT_CODE, result.getCode());
    }

    @Test
    public void getDescriptionShouldNotEquals() throws ProductNotFoundException, ProductIdNotFoundException {
        productDao.save(product);
        Product result = productDao.getProduct(product.getId());
        assertNotEquals(ANOTHER_PRODUCT_DESCRIPTION, result.getDescription());
    }

    @Test
    public void shouldEqualsWhenComparingListSize() {
        int listSize = productDao.findProducts().size();
        productDao.save(product);
        assertEquals(listSize + 1, productDao.findProducts().size());
    }

    @Test
    public void getIdShouldReturnNotNull() {
        Product newProduct = new Product.Builder()
                .withCode(PRODUCT_CODE)
                .withDescription(PRODUCT_DESCRIPTION)
                .withPrice(new BigDecimal(PRODUCT_PRICE))
                .withCurrency(USD_CURRENCY)
                .withStock(PRODUCT_STOCK)
                .withImageUrl(PRODUCT_IMAGE_URL)
                .build();
        assertNotNull(newProduct.getId());
    }

    @Test
    public void productsAndSizesShouldEqualsWhenSavingProductWithTheSameId()
            throws ProductNotFoundException, ProductIdNotFoundException {
        productDao.save(product);
        int listSize = productDao.findProducts().size();
        productDao.save(product);
        assertEquals(listSize, productDao.findProducts().size());
        assertEquals(product, productDao.getProduct(PRODUCT_ID));
    }

    @Test
    public void productDaoShouldCallGetIdWhenMockProductHasBeenSaved() {
        MockitoAnnotations.initMocks(this);
        when(mockProduct.getId()).thenReturn(PRODUCT_ID);

        productDao.save(mockProduct);
        productDao.getProduct(PRODUCT_ID);

        verify(mockProduct, atLeast(MIN_NUMBER_OF_INVOCATIONS)).getId();
    }

    @Test(expected = ProductNotFoundException.class)
    public void getProductShouldThrowProductNotFoundExceptionWhenHasBeenDeletedCorrectly()
            throws ProductNotFoundException, ProductIdNotFoundException {
        Product product = new Product.Builder()
                .withId(PRODUCT_ID)
                .withCode(PRODUCT_CODE)
                .withDescription(PRODUCT_DESCRIPTION)
                .withPrice(new BigDecimal(PRODUCT_PRICE))
                .withCurrency(USD_CURRENCY)
                .withStock(PRODUCT_STOCK)
                .withImageUrl(PRODUCT_IMAGE_URL)
                .build();
        productDao.save(product);
        assertEquals(product, productDao.getProduct(PRODUCT_ID));

        productDao.delete(PRODUCT_ID);

        productDao.getProduct(PRODUCT_ID);
    }

    @Test(expected = ProductNotFoundException.class)
    public void deleteShouldThrowProductNotFoundExceptionWhenProductIsNonExist()
            throws ProductNotFoundException, ProductIdNotFoundException {
        productDao.delete(PRODUCT_ID);
    }
}
