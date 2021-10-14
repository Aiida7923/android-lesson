package com.websarva.wings.android.menusample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

class MenuThanksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_thanks)

        //リスト画面から渡されたデータを取得。1
        val menuName = intent.getStringExtra("menuName")
        val menuPrice = intent.getStringExtra("menuPrice")

        //定食名と金額を表示させるTextViewを取得。2
        val tvMenuName = findViewById<TextView>(R.id.tvMenuName)
        val tvMenuPrice = findViewById<TextView>(R.id.tvMenuPrice)

        //TextViewに定食名と金額を表示。
        tvMenuName.text = menuName
        tvMenuPrice.text = menuPrice

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //選択されたメニューが［戻る］の場合、アクティビティを終了。
        if (item.itemId == android.R.id.home) {
            finish()
        }
        //親クラスの同名メソッドを呼び出し、その戻り値を返却。
        return super.onOptionsItemSelected(item)
    }

}