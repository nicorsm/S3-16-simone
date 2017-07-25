package app.simone.multiplayer.view

import akka.actor.ActorRef
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.TextView
import app.simone.R
import app.simone.shared.application.App
import app.simone.shared.main.FullscreenBaseGameActivity
import app.simone.shared.utils.AnimationHandler
import app.simone.shared.utils.AudioPlayer
import app.simone.shared.utils.Constants
import app.simone.shared.utils.Utilities
import app.simone.singleplayer.messages.GuessColorMsg
import app.simone.singleplayer.messages.NextColorMsg
import app.simone.singleplayer.model.SColor

class NearbyGameActivity : FullscreenBaseGameActivity() {

    private var playerBlinking: Boolean = true
    private var scoreTextView : TextView? = null

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {

            when (msg.what) {
                Constants.CPU_TURN -> {
                    val score = msg.arg2 + 1/*Score*/

                    if (playerBlinking) {
                        scoreTextView?.text = score.toString()
                        //finalScore = currentScore
                        scoreTextView?.startAnimation(AnimationHandler.getGameButtonAnimation())
                        /*
                        if (App.getGoogleApiHelper().googleApiClient.isConnected) {
                            Games.setViewForPopups(App.getGoogleApiHelper().googleApiClient, mContentView)
                            AchievementHelper.checkAchievement(currentScore, chosenMode)
                        }*/
                    }
                    playerBlinking = false
                    if (/*arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor*/msg.arg1 != 0) {
                        blinkDelayed(msg)
                    }
                }
                Constants.PLAYER_TURN -> {
                    if (!playerBlinking) {
                        scoreTextView?.text = Constants.TURN_PLAYER
                        scoreTextView?.startAnimation(AnimationHandler.getGameButtonAnimation())
                        /*if (chosenMode == Constants.HARD_MODE) {
                            swapButtonPositions()
                        }*/
                    }
                    playerBlinking = true
                    if (/*arg1 = 0 when PLAYER_TURN_MSG comes from GameViewActor*/msg.arg1 != 0) {
                        blinkDelayed(msg)
                    }
                }
                /*Constants.WHATTASHAMEYOULOST_MSG -> {
                    finalScore = msg.arg1
                    if (App.getGoogleApiHelper().isConnected) {
                        Games.Leaderboards.submitScoreImmediate(App.getGoogleApiHelper().googleApiClient, if (chosenMode == Constants.CLASSIC_MODE) Constants.LEADERBOARD_CLASSIC_ID else Constants.LEADERBOARD_HARD_ID, finalScore.toLong())
                                .setResultCallback(LeaderboardCallback())
                    } else {
                        //TODO WRITE PENDING SCORE SU DB
                    }

                    tapToBegin = true
                    simoneTextView.setText(Constants.PLAY_AGAIN)
                    simoneTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")))
                    gameFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")))
                    sendMsgToOtherPlayer()
                    simoneTextView.startAnimation(AnimationHandler.getGameButtonAnimation())
                }*/
            }

        }
    }

    override fun backTransition() {
        overridePendingTransition(R.anim.left_in,R.anim.right_out)
    }

    override fun setSubclassContentView() {
        setContentView(R.layout.activity_nearby_game)
        mContentView = findViewById(R.id.nearby_fullscreen_content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = this.intent

        if(intent.hasExtra(Constants.COLOR_EXTRA)) {
            val chosenColor = SColor.fromInt(intent.getIntExtra(Constants.COLOR_EXTRA, 0))
            val btn = findViewById(R.id.nearby_button) as Button
            btn.setBackgroundResource(chosenColor.colorId)
        }

        val msg = Message()
        msg.what = Constants.CPU_TURN // Tempo?
        msg.arg1 = R.id.GREEN //ID  colore
        this.blinkDelayed(msg)

    }

    private fun blinkDelayed(msg: Message) {
        // Message from ViewActor or this activity itself, handling the blinking
        val b = findViewById(R.id.nearby_button) as Button
        b.alpha = 0.4f
        val color = SColor.fromInt(msg.arg1)

        if(color != null) {
            AudioPlayer().play(applicationContext, color.soundId)
            val m = Message()
            m.what = msg.what
            m.arg1 = msg.arg1
            vHandler.sendMessageDelayed(m, Constants.STD_DELAY_BTN_TIME.toLong())
        }
    }

    private val vHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val b = findViewById(R.id.nearby_button) as Button
            b.alpha = 1f

            when (msg.what) {
                Constants.CPU_TURN -> Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().actorSystem)
                        .tell(NextColorMsg(), ActorRef.noSender())
                Constants.PLAYER_TURN -> Utilities.getActorByName(Constants.PATH_ACTOR + Constants.GAMEVIEW_ACTOR_NAME, App.getInstance().actorSystem)
                        .tell(GuessColorMsg(SColor.fromInt(msg.arg1)), ActorRef.noSender())
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)


            val button = findViewById(R.id.nearby_button) as Button

            button.setOnClickListener {
                if (playerBlinking) {
                    val m = Message()
                    //m.arg1 = color.buttonId
                    m.what = Constants.PLAYER_TURN
                    handler.sendMessage(m)
                }
            }
    }
}
