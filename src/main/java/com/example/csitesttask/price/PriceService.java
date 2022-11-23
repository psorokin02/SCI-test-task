package com.example.csitesttask.price;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceService {

    public Collection<Price> joinPrices(
            Collection<Price> currentPrices,
            Collection<Price> newPrices
    ) {
        Map<String, List<Price>> groupedCurrentPrices = groupPrices(currentPrices);
        Map<String, List<Price>> groupedNewPrices = groupPrices(newPrices);

        for(String k: groupedNewPrices.keySet()) {
            if(!groupedCurrentPrices.containsKey(k))
                groupedCurrentPrices.put(k, new ArrayList<>());
            for(Price newPrice: groupedNewPrices.get(k)) {
                addNewPrice(groupedCurrentPrices.get(k), newPrice);
            }
        }

        return groupedCurrentPrices.values().stream()
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    private Map<String, List<Price>> groupPrices(Collection<Price> prices) {
        return prices.stream()
                .collect(Collectors.groupingBy(price -> price.productCode + price.depart + price.number));
    }

    private void addNewPrice(List<Price> prices, Price newPrice) {
        List<Price> pricesToDelete = new ArrayList<>();
        List<Price> pricesToAdd = new ArrayList<>();
        pricesToAdd.add(newPrice);

        for (Price currentPrice : prices) {
            if(!currentPrice.isOverlapWith(newPrice))
                continue;
            if(currentPrice.isInsideIn(newPrice)) {
                pricesToDelete.add(currentPrice);
            } else if(newPrice.value == currentPrice.value) {
                pricesToDelete.add(currentPrice);
                newPrice.begin = (newPrice.begin.before(currentPrice.begin)) ? newPrice.begin : currentPrice.begin;
                newPrice.end = (newPrice.end.after(currentPrice.end)) ? newPrice.end : currentPrice.end;
            } else if(newPrice.isInsideIn(currentPrice)) {
                pricesToAdd.add(new Price(0, currentPrice.productCode, currentPrice.number, currentPrice.depart, newPrice.end, currentPrice.end, currentPrice.value));
                currentPrice.end = newPrice.begin;
            } else if(currentPrice.begin.before(newPrice.begin))
                currentPrice.end = newPrice.begin;
            else
                currentPrice.begin = newPrice.end;
        }

        for(Price price: prices)
            if(price.end.equals(price.begin))
                pricesToDelete.add(price);

        prices.removeAll(pricesToDelete);
        prices.addAll(pricesToAdd);
    }

}