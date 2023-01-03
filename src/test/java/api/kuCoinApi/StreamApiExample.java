package api.kuCoinApi;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

public class StreamApiExample {

    public List<TickerData> getTickers() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.kucoin.com/api/v1/market/allTickers")
                .then().log().body()
                .extract().jsonPath().getList("data.ticker", TickerData.class);
    }

    @Test
    public void checkCrypto() {

        List<TickerData> usdTickers = getTickers().stream()
                .filter(x -> x.getSymbol().endsWith("USDT"))
                .collect(toList());

        assertTrue(usdTickers.stream().allMatch(x -> x.getSymbol().endsWith("USDT")));
    }

    // Sorting by higher rising the value of the currency per day
    @Test
    public void sortingCrypto() {

        List<Double> mostRisedCurrencies = getTickers().stream()
                .filter(x -> x.getSymbol().endsWith("USDT"))
                .map(x -> Double.parseDouble(x.getChangeRate()))
                .sorted(Double::compareTo)
                .collect(toList());

        assertTrue(mostRisedCurrencies.get(0) < mostRisedCurrencies.get(mostRisedCurrencies.size() - 1));

    }

    // Sorting by higher rising the value of the currency per day V2
    @Test
    public void sortingCryptoV2() {

        List<TickerData> highToLow = getTickers().stream()
                .filter(x -> x.getSymbol().endsWith("USDT"))
                .sorted(new Comparator<TickerData>() {
                    @Override
                    public int compare(TickerData o1, TickerData o2) {
                        return o2.getChangeRate().compareTo(o1.getChangeRate());
                    }
                })
                .limit(10)
                .collect(toList());

        assertTrue(Double.parseDouble(highToLow.get(0).getAveragePrice()) > Double.parseDouble(highToLow.get(highToLow.size() - 1).getAveragePrice()));
        assertEquals(highToLow.get(0).getSymbol(), "OUSD-USDT");
    }

    @Test
    public void sortLowToHigh(){
        List<TickerData> lowToHigh= getTickers().stream()
                .filter(x -> x.getSymbol().endsWith("USDT"))
                .sorted(new TickerComparatorLow())
                .limit(10)
                .collect(toList());
    }
}
