import java.util.ArrayList;
import java.util.List;

public class StocksPortfolio {
    private String name;
    private IStockMarket stockMarket;
    private List<Stock> stocks = new ArrayList<Stock>();

    public StocksPortfolio(IStockMarket stockMarket) {
        this.stockMarket = stockMarket;
    }

    public IStockMarket getMarketService(){
        return this.stockMarket;
    }


    public String getName(){
        return this.name;
    }

    public void setName(String _name){
        this.name = _name;
    }

    public double getTotalValue(){
        double total = 0;
        for(Stock s : this.stocks){
            total += (stockMarket.getPrice(s.getName())*s.getQuantity());
        }

        return total;
    }

    public void addStock(Stock newStock){
        this.stocks.add(newStock);
    }
}
