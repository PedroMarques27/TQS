import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.NotExtensible;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {
    @Mock
    IStockMarket stockMarket2;

    @InjectMocks
    StocksPortfolio portfolio2;

    @Test
    public void getTotalValue(){
        IStockMarket stockMarket = Mockito.mock(IStockMarket.class);
        StocksPortfolio portfolio = new StocksPortfolio(stockMarket);

        when(stockMarket.getPrice("EBAY")).thenReturn(4.0);
        when(stockMarket.getPrice("MSFT")).thenReturn(3.1);


        portfolio.addStock(new Stock("EBAY",1));
        portfolio.addStock(new Stock("MSFT",3));
        double result = portfolio.getTotalValue();
        assertEquals(13.3, result);
        assertThat(result, is(13.3));
        verify(stockMarket, Mockito.times(2)).getPrice(anyString());
    }

}