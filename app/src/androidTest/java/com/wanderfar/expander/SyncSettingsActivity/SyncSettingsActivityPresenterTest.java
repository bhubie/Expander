package com.wanderfar.expander.SyncSettingsActivity;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.wanderfar.expander.AppSettings.AppSettings;
import com.wanderfar.expander.Application.Expander;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static com.wanderfar.expander.TestHelpers.MacroTestHelpers.initDB;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@PrepareForTest({Expander.class})
public class SyncSettingsActivityPresenterTest {

    private SyncSettingsActivityView syncSettingsActivityView;
    private SyncSettingsActivityPresenterImpl syncSettingsActivityPresenter;
    private AppSettings appSettings;

    @Before
    public void setupData(){
        syncSettingsActivityView = mock(SyncSettingsActivityView.class);
        appSettings = mock(AppSettings.class);
        when(appSettings.isDynamicValuesEnabled()).thenReturn(true);
        syncSettingsActivityPresenter = new SyncSettingsActivityPresenterImpl(syncSettingsActivityView, appSettings);

        initDB(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void enableSyncSettingsFieldsShouldBeCalledIfSyncIsEnabled(){
        when(appSettings.isSyncEnabled()).thenReturn(true);

        syncSettingsActivityPresenter.onResume();

        verify(syncSettingsActivityView, times(1)).setSyncSettingsSwitch(true);
        verify(syncSettingsActivityView, times(1)).enableSyncSettingFields();
        verify(syncSettingsActivityView, never()).disableSyncSettingFields();
    }

    @Test
    public void enableSyncSettingsFieldsShouldNotBeCalledIfSyncIsDisabled(){
        when(appSettings.isSyncEnabled()).thenReturn(false);

        syncSettingsActivityPresenter.onResume();

        verify(syncSettingsActivityView, times(1)).setSyncSettingsSwitch(false);
        verify(syncSettingsActivityView, times(1)).disableSyncSettingFields();
        verify(syncSettingsActivityView, never()).enableSyncSettingFields();
    }

    @Test
    public void syncShouldBeTurnedOffWhenTurnOffSyncIsCalled(){
        syncSettingsActivityPresenter.turnOffSync();

        verify(appSettings, times(1)).setSyncEnabled(false);
        verify(syncSettingsActivityView, times(1)).disableSyncSettingFields();
    }

    @Test
    public void syncShouldBeTurnedOnWhenTurnOnSyncIsCalled(){
        syncSettingsActivityPresenter.turnOnSync();

        verify(appSettings, times(1)).setSyncEnabled(true);
        verify(syncSettingsActivityView, times(1)).enableSyncSettingFields();

    }
}
