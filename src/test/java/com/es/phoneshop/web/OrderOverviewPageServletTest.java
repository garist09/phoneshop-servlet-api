package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderIdNotFoundException;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    private static final String CORRECT_ORDER_ID = "3";

    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

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

    @Mock
    private Order order;

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
    }

    @Test
    public void doPostShouldAlwaysForwardWhenOrderIdIsCorrect() throws ServletException, IOException {
        when(request.getParameter(any())).thenReturn(CORRECT_ORDER_ID);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test(expected = OrderIdNotFoundException.class)
    public void doGetShouldThrowIdNotFoundExceptionWhenIdIsNotFound() throws ServletException, IOException {
        when(request.getParameter(any())).thenReturn(null);

        servlet.doPost(request, response);
    }

    @Test(expected = OrderNotFoundException.class)
    public void doPostShouldRedirect23() throws ServletException, IOException {
        when(request.getParameter(any())).thenReturn(CORRECT_ORDER_ID);
        when(order.getId()).thenReturn(CORRECT_ORDER_ID);
        orderDao.addOrder(request, order);

        servlet.doPost(request, response);

        orderDao.getOrder(request, order.getId());
    }
}
