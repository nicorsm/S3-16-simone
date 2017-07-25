package app.simone.multiplayer.view

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import app.simone.R
import app.simone.shared.main.FullscreenBaseGameActivity
import app.simone.shared.utils.Constants

class MultiplayerTypeActivity : FullscreenBaseGameActivity() {

    override fun backTransition() {
        overridePendingTransition(R.anim.slide_up,R.anim.slide_up_existing)
    }

    override fun setSubclassContentView() {
        setContentView(R.layout.activity_multiplayer_type)
        mContentView = findViewById(R.id.multiplayer_fullscreen_content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nearbyFab = this.findViewById(R.id.multiplayer_fab_nearby) as FloatingActionButton
        nearbyFab.setOnClickListener {
            openActivity(NearbyGameActivity::class.java, Constants.COLOR_EXTRA, R.id.RED, R.anim.right_in, R.anim.left_out)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }
}
