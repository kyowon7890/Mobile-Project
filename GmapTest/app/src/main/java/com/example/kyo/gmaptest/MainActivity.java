package com.example.kyo.gmaptest;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private GoogleMap map;
    private ArrayList<String> pasingTag;
    private ArrayList<String> result;
    TextView textView, textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intentStart = new Intent(getBaseContext(), startActivity.class);
        //startActivity(intentStart);
        setContentView(R.layout.activity_main);

        // 지도 객체 참조
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // 위치 확인하여 위치 표시 시작
        startLocationService();

        pasingTag.add("aview");
        pasingTag.add("etc");
        GetXMLTask xml = new GetXMLTask("http://openapi.nfrdi.re.kr/openapi/service/OceanProblemService/getOceanproblemRedTideOccurrenceInfo?ServiceKey=vqCctjHc8aHialzUcJeaByJFsS9EyGqYxo%2BNmQ7MeSkvdvtXyqcAZncJ%2BU1m3450KzJAVMfWnuObURx0dVHeRA%3D%3D&rdate=20020828"
                                        , "item", pasingTag);
        /**
        result = xml.getPasingData();
        textView = (TextView) findViewById(R.id.redtite1);
        textView2 = (TextView) findViewById(R.id.redtite2);
        textView.setText(result.get(0));
        textView2.setText(result.get(1));*/
    }

    @Override
    public void onResume() {
        super.onResume();

        // 내 위치 자동 표시 enable
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 내 위치 자동 표시 disable
        map.setMyLocationEnabled(false);
    }

    /**
     * 현재 위치 확인을 위해 정의한 메소드
     */
    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 리스너 객체 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        // GPS 기반 위치 요청
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

        // 네트워크 기반 위치 요청
        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                gpsListener);

    }

    /**
     * 리스너 정의
     */
    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인되었을 때 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSLocationService", msg);

            // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출
            showCurrentLocation(latitude, longitude);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    /**
     * 현재 위치의 지도를 보여주기 위해 정의한 메소드
     *
     * @param latitude
     * @param longitude
     */
    private void showCurrentLocation(Double latitude, Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        // 지도 유형 설정. 지형도인 경우에는 GoogleMap.MAP_TYPE_TERRAIN, 위성 지도인 경우에는 GoogleMap.MAP_TYPE_SATELLITE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }



    private void showAllBankItems(Double latitude, Double longitude, String Name) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(latitude, longitude));
        marker.title(Name);
        marker.draggable(true);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bank));

        map.addMarker(marker);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
