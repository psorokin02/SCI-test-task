package com.example.csitesttask.price;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceServiceTest {

    private final PriceService priceService;

    public PriceServiceTest() {
        this.priceService = new PriceService();
    }

    @Test
    void testJoinPriceWhenPricesNotOverlap() {
        var currentPrices = List.of(
                new Price(1, "product-1", 1, 1, new Date(1), new Date(2), 10),
                new Price(2, "product-2", 1, 1, new Date(1), new Date(2), 10)
        );
        var newPrices = List.of(
                new Price(3, "product-1", 2, 1, new Date(1), new Date(2), 10),
                new Price(4, "product-1", 1, 2, new Date(1), new Date(2), 10),
                new Price(5, "product-1", 2, 2, new Date(1), new Date(2), 10),
                new Price(6, "product-2", 1, 1, new Date(10), new Date(20), 10)
        );
        var result = priceService.joinPrices(currentPrices, newPrices);

        assertEquals(currentPrices.size() + newPrices.size(), result.size(),
                "Count of prices must be equal because they are not overlapping");
        assertThat(result).containsAll(currentPrices);
        assertThat(result).containsAll(newPrices);
    }

    @Test
    void testJoinPriceWhenPricesOverlapWithSameValue() {
        var currentPrices = List.of(
                new Price(1, "product-1", 1, 1, new Date(1), new Date(5), 10),
                new Price(2, "product-1", 1, 1, new Date(6), new Date(10), 10),
                new Price(3, "product-1", 1, 1, new Date(15), new Date(20), 10),
                new Price(4, "product-2", 2, 2, new Date(1), new Date(5), 20),
                new Price(5, "product-2", 2, 2, new Date(6), new Date(10), 20),
                new Price(6, "product-2", 2, 2, new Date(15), new Date(20), 20)
        );
        var newPrices = List.of(
                new Price(7, "product-1", 1, 1, new Date(1), new Date(25), 10),
                new Price(8, "product-2", 2, 2, new Date(3), new Date(11), 20),
                new Price(9, "product-2", 2, 2, new Date(16), new Date(19), 20)
        );
        var expected = List.of(
                new Price(10, "product-1", 1, 1, new Date(1), new Date(25), 10),
                new Price(11, "product-2", 2, 2, new Date(1), new Date(11), 20),
                new Price(12, "product-2", 2, 2, new Date(15), new Date(20), 20)
        );
        var result = priceService.joinPrices(currentPrices, newPrices);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    void testJoinPriceWhenPricesOverlapWithDifferentValue() {
        var currentPrices = List.of(
                new Price(1, "product-1", 1, 1, new Date(1), new Date(5), 10),
                new Price(2, "product-1", 1, 1, new Date(6), new Date(10), 10),
                new Price(3, "product-1", 1, 1, new Date(15), new Date(20), 10),
                new Price(4, "product-2", 2, 2, new Date(1), new Date(5), 20),
                new Price(5, "product-2", 2, 2, new Date(6), new Date(10), 20),
                new Price(6, "product-2", 2, 2, new Date(15), new Date(20), 20),
                new Price(6, "product-3", 3, 3, new Date(10), new Date(20), 30)
        );
        var newPrices = List.of(
                new Price(7, "product-1", 1, 1, new Date(4), new Date(7), 110),
                new Price(7, "product-1", 1, 1, new Date(18), new Date(25), 110),
                new Price(8, "product-2", 2, 2, new Date(3), new Date(18), 220),
                new Price(6, "product-3", 3, 3, new Date(1), new Date(30), 330)
        );
        var expected = List.of(
                new Price(1, "product-1", 1, 1, new Date(1), new Date(4), 10),
                new Price(1, "product-1", 1, 1, new Date(4), new Date(7), 110),
                new Price(1, "product-1", 1, 1, new Date(7), new Date(10), 10),
                new Price(1, "product-1", 1, 1, new Date(15), new Date(18), 10),
                new Price(1, "product-1", 1, 1, new Date(18), new Date(25), 110),
                new Price(8, "product-2", 2, 2, new Date(1), new Date(3), 20),
                new Price(8, "product-2", 2, 2, new Date(3), new Date(18), 220),
                new Price(8, "product-2", 2, 2, new Date(18), new Date(20), 20),
                new Price(6, "product-3", 3, 3, new Date(1), new Date(30), 330)
        );
        var result = priceService.joinPrices(currentPrices, newPrices);
        assertThat(result).hasSameElementsAs(expected);
    }

}