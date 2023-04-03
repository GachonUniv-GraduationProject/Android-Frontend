package com.example.graduationproject.trend;

import java.util.ArrayList;
import java.util.List;

public class TrendData {
    private String name;
    private int amount;

    private List<TrendData> childTrendList = null;

    public TrendData(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public void addChildTrend(TrendData trendData) {
        if(childTrendList == null)
            childTrendList = new ArrayList<>();

        childTrendList.add(trendData);
    }

    public TrendData getChild(int index) {
        return childTrendList.get(index);
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
