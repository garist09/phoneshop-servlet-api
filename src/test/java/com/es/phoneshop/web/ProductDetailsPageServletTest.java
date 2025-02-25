package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    private static final String PRODUCT = "product";
    private static final String SGS_2 = "sgs2";
    private static final String SAMSUNG_GALAXY_S_II = "Samsung Galaxy S II";
    private static final int INT200 = 200;
    private static final Currency USD = Currency.getInstance("USD");
    private static final int INT10 = 10;
    private static final String GALAXY_20_S_20_II_JPG = "https://raw.githubusercontent.com/andrewosipenko" +
            "/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg";
    private static final String STRING = "/";
    private static final String NOT_EXIST_PRODUCT_ID = "id";

    private ProductDetailsPageServlet productDetailsPageServlet;
    private ProductDao productDao;
    private Product product;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletConfig config;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private HttpSession session;

    @Before
    public void init() throws ServletException {
        productDao = ArrayListProductDao.getInstance();
        product = new Product.Builder()
                .withCode(SGS_2)
                .withDescription(SAMSUNG_GALAXY_S_II)
                .withPrice(new BigDecimal(INT200))
                .withCurrency(USD)
                .withStock(INT10)
                .withImageUrl(GALAXY_20_S_20_II_JPG)
                .build();
        productDetailsPageServlet = new ProductDetailsPageServlet();
        productDetailsPageServlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void doGetShouldForwardWhenGettingExistingId() throws ServletException, IOException {
        productDao.save(product);
        when(request.getPathInfo()).thenReturn(STRING + product.getId());

        productDetailsPageServlet.doGet(request, response);

        verify(request).setAttribute(PRODUCT, product);
        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = ProductNotFoundException.class)
    public void doGetShouldThrowProductNotFoundExceptionWhenIdIsNotFound() throws ServletException, IOException {
        productDao.save(product);
        when(request.getPathInfo()).thenReturn(STRING + NOT_EXIST_PRODUCT_ID);

        productDetailsPageServlet.doGet(request, response);
    }
}
