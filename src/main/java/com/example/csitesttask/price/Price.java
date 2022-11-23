package com.example.csitesttask.price;

import java.util.Date;
import java.util.Objects;

public class Price {
    long id;
    String productCode;
    int number;
    int depart;
    Date begin;
    Date end;
    long value;

    public Price(long id, String productCode, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return number == price.number && depart == price.depart && value == price.value && productCode.equals(price.productCode) && begin.equals(price.begin) && end.equals(price.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, number, depart, begin, end, value);
    }

    public boolean isOverlapWith(Price price) {
        return !isRightThan(price) && !isLeftThan(price);
    }

    public boolean isLeftThan(Price price) {
        return this.end.before(price.begin);
    }

    public boolean isRightThan(Price price) {
        return this.begin.after(price.end);
    }

    public boolean isInsideIn(Price price) {
        return (this.begin.after(price.begin) || this.begin.equals(price.begin)) &&
                (this.end.before(price.end) || this.end.equals(price.end));
    }
}
