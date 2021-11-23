package com.websarva.wings.android.servicesample

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import java.io.IOException
import java.lang.IllegalArgumentException

class SoundManagerService : Service() {
    /**
     * メディアプレーヤーフィールド。
     */
    private var _player: MediaPlayer? = null

    override fun onCreate() {
        //フィールドのメディアプレーヤーオブジェクトを生成。
        _player = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //音声ファイルのURI文字列を作成。
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.mountain_stream}"
        //音声ファイルのURI文字列を元にURIオブジェクトを生成
        val mediaFileUri = Uri.parse(mediaFileUriStr)
        try {
            //メディアプレーヤーに音声ファイルを指定。
            _player?.setDataSource(applicationContext, mediaFileUri)
            //非同期でのメディア再生準備が完了した際のリスナを設定。
            _player?.setOnPreparedListener(PlayerPreparedListenner())
            //メディア再生が終了した際のリスナを設定。
            _player?.setOnCompletionListener(PlayerCompletionListener())
            //非同期でメディア再生を準備
            _player?.prepareAsync()
        }
        catch (ex: IllegalArgumentException) {
            Log.e("ServiceSample", "メディアプレーヤー準備時の例外発生", ex)
        }
        catch (ex: IOException) {
            Log.e("ServiceSample", "メディアプレーヤー準備時の例外発生", ex)
        }
        //定数を返す
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        //フィールドのプレーヤーがnullじゃなかったら
        _player?.let {
            //プレーヤーが再生中なら
            if(it.isPlaying) {
                //プレーヤーを停止。
                it.stop()
            }
            //プレーヤーを解放
            it.release()
            //プレーヤー用フィールドをnullに
            _player = null
        }
    }

    /**
     * メディア再生が完了時のリスナクラス。
     */
    private inner class PlayerPreparedListenner: MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            //メディアを再生。
            mp.start()
        }
    }

    /**
     * メディア再生が終了したときのリスナクラス。
     */
    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer) {
            //自分自身を終了
            stopSelf()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}