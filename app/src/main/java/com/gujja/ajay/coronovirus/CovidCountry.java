package com.gujja.ajay.coronovirus;

class CovidCountry {

    private String mCovidCountry, mCovidCases, mCovidRecovered, mCovidActive, mCovidDeath , mCovidCountryImages;

    CovidCountry(String mCovidCountry, String mCovidCases, String mCovidDeath , String mCovidCountryImages ) {
        this.mCovidCases = mCovidCases;
        this.mCovidCountry = mCovidCountry;
        this.mCovidDeath = mCovidDeath;
        this.mCovidCountryImages = mCovidCountryImages;

    }

    public String getmCovidCountryImages() {
        return mCovidCountryImages;
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
