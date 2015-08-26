package com.tctiez.onthewayhome;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BasePopup;
import com.tctiez.onthewayhome.base.BaseView;
import com.tctiez.onthewayhome.popup.PConfirm;
import com.tctiez.onthewayhome.base.RootView;
import com.tctiez.onthewayhome.service.GPS;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 */
public class VMap extends BaseView implements View.OnClickListener, MapView.MapViewEventListener {
    private final String DAUM_MAP_API_KEY   = "57bd584c3b8fed6f207d610c1d41047f";

    private BaseActivity    mContext        = null;
    private RootView        mParentView     = null;

    private ViewGroup       mMapContainer   = null;
    private MapView         mMapView        = null;

    public VMap(BaseActivity context, RootView parentView) {
        super(context);
        mContext = context;
        mParentView = parentView;

        // setContentsLayout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_map, null, false);
        addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (checkGPSEnabled()) {
            setMapView();
        }
    }

    private void setMapView() {
        mContext.showDarkView(null);
        mMapView = new MapView(mContext);
        mMapView.setDaumMapApiKey(DAUM_MAP_API_KEY);
        mMapView.setMapViewEventListener(this);
        mMapContainer = (ViewGroup) findViewById(R.id.map_container);
        mMapContainer.addView(mMapView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (checkGPSEnabled()) {
            setMapView();
        }
    }

    @Override
    public void initView() {}

    @Override
    public void onClick(View view) {}

    @Override
    public boolean onBackPressed() {
        setTracking(mMapView);
        return true;
    }

    /**
     * MapView가 사용 가능한 상태
     */
    @Override
    public void onMapViewInitialized(MapView mapView) {
        setTracking(mapView);

        mContext.hideDarkView(null);
        mParentView.setMenu(true);
    }

    private void setTracking(MapView mapView) {
        Location location = GPS.getInstance(mContext).getLocation();
        if (location != null) {
            // 중심점 변경 + 줌레벨
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()), 1, true);
        } else {
            // 줌 레벨 변경
            mapView.setZoomLevel(1, true);
        }

        // 트래킹 모드 => 자신의 위치 자동 마커
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {}

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {}

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {}

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        setTracking(mapView);
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {}

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {}

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {}

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {}

    /**
     * checkGPSEnabled
     *
     * @return
     */
    private boolean checkGPSEnabled() {
        boolean isGPSEnabled = GPS.getInstance(mContext).isGPSEnabled();
        if (!isGPSEnabled) {
            mParentView.showPopup(new PConfirm(mContext, mParentView, "GPS 사용을 확인해주세요.", "CANCEL", "OK"), null, new BasePopup.onPopupActionListener() {
                @Override
                public void onPopupAction(int action, Object ret) {
                    switch ((int) ret) {
                        case PConfirm.CONFIRM_LEFT:
                            mParentView.closePopupView();
                            mParentView.showPopup(new PConfirm(mContext, mParentView, "GPS 사용이 불가하여\n앱을 종료합니다!", "OK"), null, new BasePopup.onPopupActionListener() {
                                @Override
                                public void onPopupAction(int action, Object ret) {
                                    switch ((int) ret) {
                                        case PConfirm.CONFIRM_LEFT:
                                            mParentView.closePopupView();
                                            mContext.finish();
                                            break;
                                    }
                                }
                            });
                            break;

                        case PConfirm.CONFIRM_RIGHT:
                            mParentView.closePopupView();
                            mContext.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                            break;
                    }
                }
            });
//            mContext.showPopupView(new PConfirm(mContext, mParentView, "GPS 사용을 확인해주세요.", "CANCEL", "OK").setOnPopupActionListener(new BasePopup.onPopupActionListener() {
//                @Override
//                public void onPopupAction(int action, Object ret) {
//                switch ((int) ret) {
//                    case PConfirm.CONFIRM_LEFT:
//                        mContext.closePopupView(mParentView);
//                        mContext.showPopupView(new PConfirm(mContext, mParentView, "GPS 사용이 불가하여\n앱을 종료합니다!", "OK").setOnPopupActionListener(new BasePopup.onPopupActionListener() {
//                            @Override
//                            public void onPopupAction(int action, Object ret) {
//                                switch ((int) ret) {
//                                    case PConfirm.CONFIRM_LEFT:
//                                    mContext.closePopupView(mParentView);
//                                    mContext.finish();
//                                    break;
//                                }
//                            }
//                        }));
//                        break;
//
//                    case PConfirm.CONFIRM_RIGHT:
//                        mContext.closePopupView(mParentView);
//                        mContext.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
//                        break;
//                }
//                }
//            }));
        }

        return (isGPSEnabled ? true : false);
    }
}
