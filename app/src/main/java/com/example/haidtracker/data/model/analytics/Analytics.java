package com.example.haidtracker.data.model.analytics;

import com.google.gson.annotations.SerializedName;

public class Analytics {

    @SerializedName("averageCycleLength")
    private Double averageCycleLength;

    @SerializedName("lastCycleLength")
    private Integer lastCycleLength;

    @SerializedName("nextPredictedDate")
    private String nextPredictedDate;

    @SerializedName("totalCycles")
    private Integer totalCycles;

    @SerializedName("shortestCycle")
    private Integer shortestCycle;

    @SerializedName("longestCycle")
    private Integer longestCycle;

    @SerializedName("regularityScore")
    private Double regularityScore;

    @SerializedName("lastPeriodDate")
    private String lastPeriodDate;

    // Constructors
    public Analytics() {}

    public Analytics(Double averageCycleLength, Integer lastCycleLength, String nextPredictedDate, 
                    Integer totalCycles, Integer shortestCycle, Integer longestCycle, 
                    Double regularityScore, String lastPeriodDate) {
        this.averageCycleLength = averageCycleLength;
        this.lastCycleLength = lastCycleLength;
        this.nextPredictedDate = nextPredictedDate;
        this.totalCycles = totalCycles;
        this.shortestCycle = shortestCycle;
        this.longestCycle = longestCycle;
        this.regularityScore = regularityScore;
        this.lastPeriodDate = lastPeriodDate;
    }

    // Getters and setters
    public Double getAverageCycleLength() {
        return averageCycleLength;
    }

    public void setAverageCycleLength(Double averageCycleLength) {
        this.averageCycleLength = averageCycleLength;
    }

    public Integer getLastCycleLength() {
        return lastCycleLength;
    }

    public void setLastCycleLength(Integer lastCycleLength) {
        this.lastCycleLength = lastCycleLength;
    }

    public String getNextPredictedDate() {
        return nextPredictedDate;
    }

    public void setNextPredictedDate(String nextPredictedDate) {
        this.nextPredictedDate = nextPredictedDate;
    }

    public Integer getTotalCycles() {
        return totalCycles;
    }

    public void setTotalCycles(Integer totalCycles) {
        this.totalCycles = totalCycles;
    }

    public Integer getShortestCycle() {
        return shortestCycle;
    }

    public void setShortestCycle(Integer shortestCycle) {
        this.shortestCycle = shortestCycle;
    }

    public Integer getLongestCycle() {
        return longestCycle;
    }

    public void setLongestCycle(Integer longestCycle) {
        this.longestCycle = longestCycle;
    }

    public Double getRegularityScore() {
        return regularityScore;
    }

    public void setRegularityScore(Double regularityScore) {
        this.regularityScore = regularityScore;
    }

    public String getLastPeriodDate() {
        return lastPeriodDate;
    }

    public void setLastPeriodDate(String lastPeriodDate) {
        this.lastPeriodDate = lastPeriodDate;
    }

    @Override
    public String toString() {
        return "Analytics{" +
                "averageCycleLength=" + averageCycleLength +
                ", lastCycleLength=" + lastCycleLength +
                ", nextPredictedDate='" + nextPredictedDate + '\'' +
                ", totalCycles=" + totalCycles +
                ", shortestCycle=" + shortestCycle +
                ", longestCycle=" + longestCycle +
                ", regularityScore=" + regularityScore +
                ", lastPeriodDate='" + lastPeriodDate + '\'' +
                '}';
    }
}