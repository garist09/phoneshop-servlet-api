package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private Currency usd;

    @Mock
    private Product product;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
        usd = Currency.getInstance("USD");
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct() throws ProductNotFoundException {
        Product newProduct = new Product.Builder().withId(1L).withCode("sgs").withDescription("Samsung Galaxy S").withPrice(new BigDecimal(100)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg").build();
        productDao.save(newProduct);
        assertEquals(newProduct, productDao.getProduct(1L));
    }

    @Test
    public void testSaveNewId() throws ProductNotFoundException {
        int listSize = productDao.findProducts().size();
        Product newProduct = new Product.Builder().withCode("nokia3310").withDescription("Nokia 3310").withPrice(new BigDecimal(70)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg").build();
        productDao.save(newProduct);
        assertNotNull(newProduct.getId());
        assertEquals(listSize + 1, productDao.findProducts().size());
        Product result = productDao.getProduct(newProduct.getId());
        assertEquals(newProduct, result);
        assertEquals("nokia3310", result.getCode());
        assertNotEquals("Siemens C61", result.getDescription());
    }

    @Test
    public void testSaveUsedId() throws ProductNotFoundException {
        productDao.save(new Product.Builder().withId(1L).withCode("simc61").withDescription("Siemens C61").withPrice(new BigDecimal(80)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg").build());
        int listSize = productDao.findProducts().size();
        Product product = new Product.Builder().withId(1L).withCode("xperiaxz").withDescription("Sony Xperia XZ").withPrice(new BigDecimal(120)).withCurrency(usd).withStock(100).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg").build();
        productDao.save(product);
        assertTrue(product.getId() > 0);
        assertEquals(listSize, productDao.findProducts().size());
        Product result = productDao.getProduct(1L);
        assertEquals(product, result);
        assertNotEquals(30, result.getStock());
        assertEquals(usd, result.getCurrency());
    }
    @Test
    public void testSave() {
        MockitoAnnotations.initMocks(this);
        when(product.getId()).thenReturn(null);
        productDao.save(product);
        verify(product, atLeast(1)).getId();
        verify(product).setId(anyLong());
    }

    @Test
    public void testDeleteExistingProduct() throws ProductNotFoundException {
        Product product = new Product.Builder().withId(0L).withCode("palmp").withDescription("Palm Pixi").withPrice(new BigDecimal(170)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg").build();
        productDao.save(product);
        productDao.save(new Product.Builder().withId(1L).withCode("iphone6").withDescription("Apple iPhone 6").withPrice(new BigDecimal(1000)).withCurrency(usd).withStock(30).withImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg").build());
        productDao.delete(0L);
        assertNotEquals(product, productDao.getProduct(1L));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNonExistingProduct() throws ProductNotFoundException {
        productDao.delete(-5L);
    }
}
