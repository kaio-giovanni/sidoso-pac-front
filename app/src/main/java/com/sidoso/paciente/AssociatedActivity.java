package com.sidoso.paciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sidoso.paciente.model.Associados;

public class AssociatedActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private GoogleMap mMap;
    private LatLng location;
    private Associados associado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associated);

        Intent intent = getIntent();

        associado = new Associados();
        associado.setName(intent.getStringExtra("aName"));
        associado.setType(intent.getStringExtra("aType"));
        associado.setLat(intent.getDoubleExtra("aLat", 0));
        associado.setLng(intent.getDoubleExtra("aLng", 0));
        associado.setEmail(intent.getStringExtra("aEmail"));
        associado.setPhone_main(intent.getStringExtra("aPhoneMain"));
        associado.setPhone_secondary(intent.getStringExtra("aPhoneSecondary"));

        toolbar = findViewById(R.id.tollbarAssociated);
        toolbar.setTitle(associado.getName());
        toolbar.setSubtitle(associado.getType());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        location = new LatLng(associado.getLat(), associado.getLng());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMap.addMarker(new MarkerOptions()
                .position(this.location)
                .title(this.associado.getName()));

        CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.location, 15));

    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;
        private final TextView textView;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.associated_map_info_window, null);
            textView = mWindow.findViewById(R.id.textMapInfo);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            textView.setText(associado.toString());
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            textView.setText(associado.toString());
            return mWindow;
        }
    }
}
