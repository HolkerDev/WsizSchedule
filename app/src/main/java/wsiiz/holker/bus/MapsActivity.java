package wsiiz.holker.bus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String stationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        stationName = intent.getStringExtra("stationName");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //ul. Ofiar Katynia (kladka piesza)
        //Al. Powst. Warszawy
        //Parking TESCO
        //Kielnarowa
        mMap = googleMap;
        float zoom = 18f;
        // Add a marker in Sydney and move the camera
        LatLng tesco = new LatLng(50.018711, 22.013980);
        LatLng kielnarowa = new LatLng(49.949747, 22.059492);
        LatLng warszawa = new LatLng(50.017654, 22.015634);
        LatLng catynia = new LatLng(50.053082, 21.978444);
        mMap.addMarker(new MarkerOptions().position(tesco).title("TESCO station"));
        mMap.addMarker(new MarkerOptions().position(kielnarowa).title("Kielnarowa"));
        mMap.addMarker(new MarkerOptions().position(warszawa).title("Al. Powst. Warszawy"));
        mMap.addMarker(new MarkerOptions().position(catynia).title("ul. Ofiar Katynia (kladka piesza)"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tesco,zoom));

        switch (stationName) {
            case "Parking TESCO":
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tesco, zoom));
                break;
            case "Kielnarowa":
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kielnarowa, zoom));
                break;
            case "Al. Powst. Warszawy":
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(warszawa, zoom));
                break;
            case "ul. Ofiar Katynia (kladka piesza)":
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(catynia, zoom));
                break;
            case "":
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(catynia, zoom));
                break;
            default:
                break;
        }
    }
}
