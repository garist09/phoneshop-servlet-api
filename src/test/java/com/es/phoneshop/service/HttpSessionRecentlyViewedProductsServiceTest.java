package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.IdNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import com.es.phoneshop.service.impl.HttpSessionRecentlyViewedProductsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionRecentlyViewedProductsServiceTest {
    public static final int NUMBER_OF_INVOCATIONS = 2;
    public static final String PRODUCT_ID = "3";

    private RecentlyViewedProductsService httpSessionRecentlyViewedProductsService;
    private ProductDao productDao;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private Product product;

    @Before
    public void setup() {
        httpSessionRecentlyViewedProductsService = HttpSessionRecentlyViewedProductsServiceImpl.getInstance();
        productDao = ArrayListProductDao.getInstance();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void getRecentlyViewedProductsShouldSetAttributeWhenAttributeIsNull() {
        when(session.getAttribute(anyString())).thenReturn(null);

        RecentlyViewedProducts recentlyViewedProducts = httpSessionRecentlyViewedProductsService.getRecentlyViewedProducts(request);

        verify(session).getAttribute(anyString());
        verify(session).setAttribute(anyString(), any());
        assertNotNull(recentlyViewedProducts);
    }

    @Test
    public void getRecentlyViewedProductsShouldGetAndReturnExistRecentlyViewedProductsWhenAttributeNotNull() {
        RecentlyViewedProducts recentlyViewedProducts = new RecentlyViewedProducts();
        when(session.getAttribute(anyString())).thenReturn(recentlyViewedProducts);

        RecentlyViewedProducts resultRecentlyViewedProducts = httpSessionRecentlyViewedProductsService.getRecentlyViewedProducts(request);

        verify(session, times(NUMBER_OF_INVOCATIONS)).getAttribute(anyString());
        assertNotNull(resultRecentlyViewedProducts);
        assertEquals(recentlyViewedProducts, resultRecentlyViewedProducts);
    }

    @Test(expected = IdNotFoundException.class)
    public void addProductShouldThrowIdNotFoundExceptionWhenProductIdIsNull() {
        httpSessionRecentlyViewedProductsService.addProduct(request, null);
    }

    @Test
    public void addProductShouldAddProductToRecentlyViewedProductsWhenAllParametersAreCorrect() {
        RecentlyViewedProducts recentlyViewedProducts = new RecentlyViewedProducts();
        when(product.getId()).thenReturn(PRODUCT_ID);
        when(session.getAttribute(anyString())).thenReturn(recentlyViewedProducts);
        productDao.save(product);

        httpSessionRecentlyViewedProductsService.addProduct(request, PRODUCT_ID);

        List<Product> productList = httpSessionRecentlyViewedProductsService.getRecentlyViewedProducts(request).getProducts();
        assertTrue(productList.contains(product));
    }
}
