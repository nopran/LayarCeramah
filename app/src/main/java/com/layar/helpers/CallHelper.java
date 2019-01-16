package com.layar.helpers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

/**
 * Created by melvin on 26/08/2016.
 * Gets the permission from user to make a call
 */
public class CallHelper {

    FragmentActivity activity;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 45;

    //phone request code
    int PhoneClosedRequestCode = 0;
    String number;

    //translation
    String explanationTitle = "Call Permission";
    String explanationDesc = "The app would like to open the phone.";
    String explanationOK = "OK";
    String explanationCancel = "CANCEL";


    //GETTERS and SETTERS for translation
    public void setExplanationTitle(String explanationTitle) {
        this.explanationTitle = explanationTitle;
    }

    public void setExplanationDesc(String explanationDesc) {
        this.explanationDesc = explanationDesc;
    }

    public void setExplanationOK(String explanationOK) {
        this.explanationOK = explanationOK;
    }

    public void setExplanationCancel(String explanationCancel) {
        this.explanationCancel = explanationCancel;
    }

    public CallHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    public void call(int PhoneClosedRequestCode, String number) {
        this.PhoneClosedRequestCode = PhoneClosedRequestCode;
        this.number = number;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {

                // Show an expanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user sees the explanation, try again to request the permission.
                Alert alert = new Alert();
                alert.DisplayText(explanationTitle, explanationDesc, explanationOK, explanationCancel, activity);
                alert.show(activity.getSupportFragmentManager(), explanationTitle);
                alert.setPositiveButtonListener(new Alert.PositiveButtonListener() {
                    @Override
                    public void onPositiveButton(String input) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                });
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            callDirect(PhoneClosedRequestCode, number);
        }
    }

    public void callDirect(int requestCode, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        activity.startActivityForResult(intent, requestCode);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the contacts-related task you need to do.
                    callDirect(PhoneClosedRequestCode, number);

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
