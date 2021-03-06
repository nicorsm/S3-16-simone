package app.simone.scores.google;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.leaderboard.Leaderboards;

import app.simone.shared.utils.Constants;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * ClassicLeaderboardCallback class.
 * @author Michele Sapignoli
 */

public class ClassicLeaderboardCallback implements ResultCallback<Leaderboards.SubmitScoreResult> {

    /**
     Scored pushed online.
     Update of the value NEED_TO_SYNC on SharedPreferences: there's no need to sync.
     */
    @Override
    public void onResult(@NonNull Leaderboards.SubmitScoreResult res) {
        if (res.getStatus().getStatusCode() == 0) {
            // data sent successfully to server.
            // display toast.
            Log.d("##ClassicLeaderboardCal","SCORE SENT");
            final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
            pref.edit().putInt(Constants.NEED_TO_SYNC_CLASSIC, 0).apply();
        }
    }
}
