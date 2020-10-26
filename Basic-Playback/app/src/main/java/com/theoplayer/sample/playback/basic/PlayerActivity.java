package com.theoplayer.sample.playback.basic;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.theoplayer.android.api.event.player.PlayerEventTypes;
import com.theoplayer.android.api.event.player.VolumeChangeEvent;
import com.theoplayer.android.api.player.Player;
import com.theoplayer.android.api.source.SourceDescription;
import com.theoplayer.android.api.source.TypedSource;
import com.theoplayer.sample.playback.basic.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private ActivityPlayerBinding viewBinding;
    private Player theoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TheoTheme_Base);
        super.onCreate(savedInstanceState);

        // Inflating view and obtaining an instance of the binding class.
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);

        // Gathering THEO objects references.
        theoPlayer = viewBinding.theoPlayerView.getPlayer();

        // Configuring action bar.
        setSupportActionBar(viewBinding.toolbarLayout.toolbar);

        // Configuring THEOplayer playback with default parameters.
        configureTHEOplayer();
    }

    private void configureTHEOplayer() {
        // Coupling the orientation of the device with the fullscreen state.
        // The player will go fullscreen when the device is rotated to landscape
        // and will also exit fullscreen when the device is rotated back to portrait.
        viewBinding.theoPlayerView.getSettings().setFullScreenOrientationCoupled(true);

        // Creating a TypedSource builder that defines the location of a single stream source.
        TypedSource.Builder typedSource = TypedSource.Builder
                .typedSource(getString(R.string.defaultSourceUrl));

        // Creating a SourceDescription builder that contains the settings to be applied as a new
        // THEOplayer source.
        SourceDescription.Builder sourceDescription = SourceDescription.Builder
                .sourceDescription(typedSource.build())
                .poster(getString(R.string.defaultPosterUrl));

        // Configuring THEOplayer with defined SourceDescription object.
        theoPlayer.setSource(sourceDescription.build());

        // Adding listeners to THEOplayer basic playback events.
        theoPlayer.addEventListener(PlayerEventTypes.PLAY, event -> Log.i(TAG, "Event: PLAY"));
        theoPlayer.addEventListener(PlayerEventTypes.PLAYING, event -> Log.i(TAG, "Event: PLAYING"));
        theoPlayer.addEventListener(PlayerEventTypes.PAUSE, event -> Log.i(TAG, "Event: PAUSE"));
        theoPlayer.addEventListener(PlayerEventTypes.ENDED, event -> Log.i(TAG, "Event: ENDED"));
        theoPlayer.addEventListener(PlayerEventTypes.ERROR, event -> Log.i(TAG, "Event: ERROR, error=" + event.getError()));
        theoPlayer.addEventListener(PlayerEventTypes.VOLUMECHANGE, event -> logVolumeEvent(event));
    }

    private void logVolumeEvent(VolumeChangeEvent event) {
        Log.i(TAG, "Event: VOLUMECHANGE, volume=" + event.getVolume());
        Log.i(TAG, "                     isMuted=" + theoPlayer.isMuted());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem muteItem = menu.add(0, 100, 0, "(UN)MUTE");
        muteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 100) {
            theoPlayer.setMuted(!theoPlayer.isMuted());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // In order to work properly and in sync with the activity lifecycle changes (e.g. device
    // is rotated, new activity is started or app is moved to background) we need to call
    // the "onResume", "onPause" and "onDestroy" methods of the THEOplayerView when the matching
    // activity methods are called.

    @Override
    protected void onPause() {
        super.onPause();
        viewBinding.theoPlayerView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewBinding.theoPlayerView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinding.theoPlayerView.onDestroy();
    }

}
