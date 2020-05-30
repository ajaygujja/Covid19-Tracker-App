package com.gujja.ajay.coronovirus;

public class CovidCountry {

    private String mCovidCountry, mCovidCases, mCovidNewCases, mCovidDeath , mCovidCountryImages;

    CovidCountry(String mCovidCountry, String mCovidCases, String mCovidDeath , String mCovidCountryImages, String mCovidNewCases ) {
        this.mCovidCases = mCovidCases;
        this.mCovidCountry = mCovidCountry;
        this.mCovidDeath = mCovidDeath;
        this.mCovidCountryImages = mCovidCountryImages;
        this.mCovidNewCases = mCovidNewCases;

    }


    public String getmCovidNewCases() {
        return mCovidNewCases;
    }

    public String getmCovidCountryImages() {
        return mCovidCountryImages;
    }

    public String getmCovidCountry() {
        return mCovidCountry;
    }

    String getmCovidCases() {
        return mCovidCases;
    }



    String getmCovidDeath() {
        return mCovidDeath;
    }
}
