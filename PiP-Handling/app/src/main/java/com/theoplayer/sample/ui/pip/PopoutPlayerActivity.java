package com.theoplayer.sample.ui.pip;

import android.app.PictureInPictureParams;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.widget.Toast;

import com.theoplayer.android.api.fullscreen.FullScreenActivity;

public class PopoutPlayerActivity extends FullScreenActivity {

    private static boolean SUPPORTS_PIP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

    @Override
    protected void onUserLeaveHint() {
        tryEnterPictureInPictureMode();
    }

    private void tryEnterPictureInPictureMode() {
        if (SUPPORTS_PIP) {
            enterPictureInPictureMode(new PictureInPictureParams.Builder().build());
        } else {
            SpannableString toastMessage = SpannableString.valueOf(getString(R.string.pipNotSupported));
            toastMessage.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, toastMessage.length(), 0);
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }
    }

}
