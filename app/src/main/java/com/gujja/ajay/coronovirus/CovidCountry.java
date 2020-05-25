package com.gujja.ajay.coronovirus;

class CovidCountry {

    private String mCovidCountry, mCovidCases;

    CovidCountry(String mCovidCountry, String mCovidCases) {
        this.mCovidCases = mCovidCases;
        this.mCovidCountry = mCovidCountry;
    }

    String getmCovidCountry() {
        return mCovidCountry;
    }

    String getmCovidCases() {
        return mCovidCases;
    }
}
