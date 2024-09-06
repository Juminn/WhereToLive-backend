package com.enm.whereToLive.Data.dabang;

public class Rental {
    private int deposit;
    private int monthlyRent;

    public Rental(int deposit, int monthlyRent) {
        this.deposit = deposit;
        this.monthlyRent = monthlyRent;
    }

    public int getDeposit() {
        return deposit;
    }

    public int getMonthlyRent() {
        return monthlyRent;
    }

    /**
     * 월세 평가 계산
     * @return 계산된 월세 가치
     */
    public double evaluateMonthlyRent() {
        final int LEASE_CONVERSION_CONSTANT = 5;

        return (deposit * LEASE_CONVERSION_CONSTANT / 100.0 / 12) + monthlyRent;
    }
}