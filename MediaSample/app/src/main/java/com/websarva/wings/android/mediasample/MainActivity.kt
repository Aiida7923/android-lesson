package com.websarva.wings.android.mediasample

import android.media.MediaParser
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import java.io.IOException
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    /**
     * メディアプレーヤーフィールド。
     */
    private var _player:MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //フィールドのメディアプレーヤーオブジェクトを生成。
        _player = MediaPlayer()
        //音声ファイルのURI文字列を作成。
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.mountain_stream}"
        //音声ファイルのURI文字列を元にURIオブジェクトを生成。
        val mediaFileUri = Uri.parse(mediaFileUriStr)
        try{
            //メディアプレーヤーに音声ファイルを指定。
            _player?.setDataSource(applicationContext, mediaFileUri)
            //非同期でのメディア再生準備が完了した際のリスナを設定。
            _player?.setOnPreparedListener(PlayerPreparedListener())
            //メディア再生が終了した際のリスナを設定。
            _player?.setOnCompletionListener(PlayerCompletionListener())
            //非同期でメディア再生を準備。
            _player?.prepareAsync()
        }
        catch (ex: IllegalArgumentException){
            Log.e("MediaSample", "メディアプレーヤー準備時の例外発生", ex)
        }
        catch (ex: IOException){
            Log.e("MediaSample", "メディアプレーヤー準備時の例外発生", ex)
        }

        //スイッチを取得。
        val loopSwitch = findViewById<Switch>(R.id.swLoop)
        //スイッチにリスナを設定。
        loopSwitch.setOnCheckedChangeListener(LoopSwitchChangedListener())
    }

    override fun onDestroy() {
        //親クラスのメソッド呼び出し
        super.onDestroy()
        //フィールドのプレーヤーがnullじゃなかったら
        _player?.let{
            //プレーヤーが再生中なら･･･
            if (it.isPlaying){
                //プレーヤーを停止
                it.stop()
            }
            //プレーヤーを解放。
            it.release()
            //プレーヤーフィールドをnullに
            _player = null
        }
    }

    /**
     * 再生ボタンタップ時の処理メソッド。
     */
    fun onPlayButtonClick(view: View){
        //フィールドのプレーヤーがnullじゃなかったら
        _player?.let {
            //再生ボタンを取得。
            val btPlay = findViewById<Button>(R.id.btPlay)
            //プレーヤーが再生中だったら…
            if(it.isPlaying){
                //プレーヤーを一時停止。
                it.pause()
                //再生ボタンのラベルを「再生」に設定。
                btPlay.setText(R.string.bt_play_play)
            }
            //プレーヤーが再生中じゃなかったら…
            else {
                //プレーヤーを再生。
                it.start()
                //再生ボタンのラベルを「一時停止」に設定。
                btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    /**
     * 戻るボタンタップ時の処理メソッド。
     */
    fun onBackButtonClick(view: View){
        //再生位置を先頭に変更
        _player?.seekTo(0)
    }

    /**
     * 進むボタンタップ時の処理メソッド。
     */
    fun onForwardButtonClick(view: View) {
        //フィールドのプレーヤーがnullじゃなかったら
        _player?.let {
            //再生中のメディアファイルの長さを取得。
            val duration = it.duration
            //再生位置を終端に変更
            it.seekTo(duration)
            //再生中でないなら
            if (!it.isPlaying) {
                //再生を開始。
                it.start()
            }
        }
    }

    /**
     * プレーヤーの再生準備が整った時のリスナクラス。
     */
    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            //各ボタンをタップ可能に設定。
            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.isEnabled = true
            val btBack = findViewById<Button>(R.id.btBack)
            btBack.isEnabled = true
            val btForward = findViewById<Button>(R.id.btForward)
            btForward.isEnabled = true
        }
    }

    /**
     * 再生が終了したときのリスナクラス。
     */
    private inner class PlayerCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer) {
            //フィールドのプレーヤーがnullじゃなかったら
            _player?.let {
                //ループが設定されていないならば
                if(!it.isLooping){
                    //再生ボタンのラベルを「再生」に設定。
                    val btPlay = findViewById<Button>(R.id.btPlay)
                    btPlay.setText(R.string.bt_play_play)
                }
            }

        }
    }

    /**
     * リピート再生スイッチの切替時のリスナクラス。
     */
    private inner class LoopSwitchChangedListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            //ループするかどうかを設定。
            _player?.isLooping = isChecked
        }
    }


    }