package com.es.phoneshop.web;

import org.apache.commons.lang3.StringUtils;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    public static final int WANTED_NUMBER_OF_INVOCATIONS = 3;
    public static final String CORRECT_PARAMETER = "a";
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ServletConfig config;

    @Mock
    private HttpSession session;

    public static final String ORDER_ATTRIBUTE = "order";
    private static final String CART_ITEM_LIST_ATTRIBUTE = "cartItemList";
    private static final String TOTAL_PRICE_ATTRIBUTE = "totalPrice";
    private static final String DELIVERY_COST_ATTRIBUTE = "deliveryCost";
    private static final String DELIVERY_DATES_ATTRIBUTE = "deliveryDates";
    private static final String ORDER_LIST_ATTRIBUTE = "orderList";

    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void doGetShouldAlwaysForward() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq(ORDER_ATTRIBUTE), any());
        verify(request).setAttribute(eq(CART_ITEM_LIST_ATTRIBUTE), any());
        verify(request).setAttribute(eq(TOTAL_PRICE_ATTRIBUTE), any());
        verify(request).setAttribute(eq(DELIVERY_COST_ATTRIBUTE), any());
        verify(request).setAttribute(eq(DELIVERY_DATES_ATTRIBUTE), any());
    }

    @Test
    public void doPostShouldAlwaysForwardWhenAllParametersNotExist() throws ServletException, IOException {
        when(request.getParameter(any())).thenReturn(StringUtils.EMPTY);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPostShouldSetAttributesAndSendRedirectWhenAllParametersExist() throws ServletException, IOException {
        when(request.getParameter(any())).thenReturn(CORRECT_PARAMETER);

        servlet.doPost(request, response);

        verify(session, times(WANTED_NUMBER_OF_INVOCATIONS)).setAttribute(eq(ORDER_LIST_ATTRIBUTE), any());
        verify(response).sendRedirect(anyString());
    }
}
