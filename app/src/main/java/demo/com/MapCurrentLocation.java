package demo.com;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;

import im.delight.android.location.SimpleLocation;

public class MapCurrentLocation extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private SimpleLocation location;
    private boolean isCheckPermisstion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION).doOnNext(this::accept).subscribe();

    }



    private void accept(Boolean granted) {
        if (granted) {
            runTimePermissions();
            isCheckPermisstion = true;
        } else {
            Toast.makeText(getApplicationContext(), "Permission has been denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void runTimePermissions() {
        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Toast.makeText(getApplicationContext(), " "+location.getLatitude() +"--"+ location.getLongitude(),Toast.LENGTH_SHORT).show();

        LatLng latLng= new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }




    @Override
    protected void onResume() {
        super.onResume();
        if(isCheckPermisstion) {
            location.beginUpdates();
        }
    }

    @Override
    protected void onPause() {
        if(isCheckPermisstion) {
            location.endUpdates();
        }
        super.onPause();
    }

}