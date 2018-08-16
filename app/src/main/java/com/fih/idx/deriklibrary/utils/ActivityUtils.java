package com.fih.idx.deriklibrary.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by derik on 17-12-7.
 */


/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * Add fragment to activity
     *
     * @param fragmentManager android.support.v4.app.fragmentManager
     * @param fragment        android.support.v4.app.fragment that will be added to activity
     * @param frameId         container id in activity
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Add fragment to activity
     *
     * @param fragmentManager android.app.fragmentManager
     * @param fragment        android.app.fragment that will be added to activity
     * @param frameId         container id in activity
     */
    public static void addFragmentToActivity(@NonNull android.app.FragmentManager fragmentManager,
                                             @NonNull android.app.Fragment fragment, int frameId) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Replace fragment in activity
     *
     * @param fragmentManager android.support.v4.app.fragmentManager
     * @param fragment        android.support.v4.app.fragment that will be replaced in activity
     * @param frameId         container id in activity
     */
    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Replace fragment in activity
     *
     * @param fragmentManager android.app.fragmentManager
     * @param fragment        android.app.fragment that will be replaced in activity
     * @param frameId         container id in activity
     */
    public static void replaceFragmentInActivity(@NonNull android.app.FragmentManager fragmentManager,
                                                 @NonNull android.app.Fragment fragment, int frameId) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Remove fragment from activity
     *
     * @param fragmentManager android.support.v4.app.fragmentManager
     * @param fragment        android.support.v4.app.fragment that will be removed from activity
     */
    public static void removeFragmentFromActivity(@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Remove fragment from activity
     *
     * @param fragmentManager android.app.fragmentManager
     * @param fragment        android.app.fragment that will be removed from activity
     */
    public static void removeFragmentFromActivity(@NonNull android.app.FragmentManager fragmentManager,
                                                  @NonNull android.app.Fragment fragment) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Hide fragment from activity
     *
     * @param fragmentManager android.support.v4.app.fragmentManager
     * @param fragment        android.support.v4.app.fragment that will be hided from activity
     */
    public static void hideFragmentFromActivity(@NonNull FragmentManager fragmentManager,
                                                @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Hide fragment from activity
     *
     * @param fragmentManager android.app.fragmentManager
     * @param fragment        android.app.fragment that will be hided from activity
     */
    public static void hideFragmentFromActivity(@NonNull android.app.FragmentManager fragmentManager,
                                                @NonNull android.app.Fragment fragment) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Show fragment from activity
     *
     * @param fragmentManager android.support.v4.app.fragmentManager
     * @param fragment        android.support.v4.app.fragment that will be showed from activity
     */
    public static void showFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Show fragment from activity
     *
     * @param fragmentManager android.app.fragmentManager
     * @param fragment        android.app.fragment that will be removed from activity
     */
    public static void showFragmentInActivity(@NonNull android.app.FragmentManager fragmentManager,
                                              @NonNull android.app.Fragment fragment) {
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
}
