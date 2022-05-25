package com.example.contest.Utils.noUse.reGeocode;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

public class ReGeocode {

    public static void getReGeocode(Context context) {
        GeocodeSearch geocoderSearch = null;
        try {
            geocoderSearch = new GeocodeSearch(context);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    Log.e("result id=", "" + i);
//                    regeocodeResult.getRegeocodeAddress();


                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {


                }
            });
        } catch (
                AMapException e) {
            e.printStackTrace();
        }


        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(30.750, 103.932), 1000, GeocodeSearch.EXTENSIONS_ALL);

        geocoderSearch.getFromLocationAsyn(query);

    }


}
