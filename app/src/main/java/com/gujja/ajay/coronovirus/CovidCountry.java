package com.gujja.ajay.coronovirus;

class CovidCountry {

    private String mCovidCountry, mCovidCases, mCovidRecovered, mCovidActive, mCovidDeath;

    CovidCountry(String mCovidCountry, String mCovidCases, /*String mCovidRecovered, String mCovidActive,*/ String mCovidDeath) {
        this.mCovidCases = mCovidCases;
        this.mCovidCountry = mCovidCountry;
        this.mCovidDeath = mCovidDeath;
//        this.mCovidRecovered = mCovidRecovered;
//        this.mCovidActive = mCovidActive;
    }

    String getmCovidCountry() {
        return mCovidCountry;
    }

    String getmCovidCases() {
        return mCovidCases;
    }

    String getmCovidRecovered() {
        return mCovidRecovered;
    }

    String getmCovidActive() {
        return mCovidActive;
    }

    String getmCovidDeath() {
        return mCovidDeath;
    }
}
