package com.wanderfar.expander.MainActivity;


import com.wanderfar.expander.AppSettings.AppSettings;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainActivityPresenterTest {

    private MainActivityView mMainActivityView;
    private MainActivityPresenterImpl mMainActivityPresenter;
    private AppSettings mAppSettings;

    @Before
    public void setupData(){
        mMainActivityView = mock(MainActivityView.class);
        mAppSettings = mock(AppSettings.class);
        mMainActivityPresenter = new MainActivityPresenterImpl(mMainActivityView, mAppSettings);

    }

    @Test
    public void shouldCallShowAccessibilityServiceNotEnabledMessageWhenAccessibilityServiceIsTurnedOff(){
        when(mAppSettings.isAccessibilityServiceEnabled()).thenReturn(false);
        mMainActivityPresenter.onResume();
        verify(mMainActivityView, times(1)).showAccessibilityServiceNotEnabledMessage();
    }

    @Test
    public void shouldNotCallShowAccessibilityServiceNotEnabledMessageWhenAccessibilityServiceIsTurnedOn(){
        when(mAppSettings.isAccessibilityServiceEnabled()).thenReturn(true);
        mMainActivityPresenter.onResume();
        verify(mMainActivityView, never()).showAccessibilityServiceNotEnabledMessage();
    }

    @Test
    public void shouldCallShowApplicationIntroductionIfFirstTimeApplicationIsBeingLaunched(){
        when(mAppSettings.isApplicationFirstStart()).thenReturn(true);
        mMainActivityPresenter.onCreate();
        verify(mMainActivityView, times(1)).launchApplicationIntroductionActivity();
        verify(mAppSettings, times(1)).setApplicationFirstStart(false);
    }

    @Test
    public void shouldNotCallShowApplicationIntroductionIfItIsNotTheFirstTimeApplicationHasBeenLaunched(){
        when(mAppSettings.isApplicationFirstStart()).thenReturn(false);
        mMainActivityPresenter.onCreate();
        verify(mMainActivityView, never()).launchApplicationIntroductionActivity();
        verify(mAppSettings, never()).setApplicationFirstStart(false);
    }

}
