package com.takhyungmin.dowadog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.takhyungmin.dowadog.utils.CustomPressedAdoptDialog
import kotlinx.android.synthetic.main.activity_pressed_adopt.*



class PressedAdoptActivity : BaseActivity() {

    private val customShelterDialog: CustomPressedAdoptDialog by lazy {
        CustomPressedAdoptDialog(this@PressedAdoptActivity, "보호소 이름이 들어가야함", "보호소 번호가 들어가야함", cancleBtnListener, confirmBtnListener, "취소", "확인")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressed_adopt)
        init()
    }

    private fun init(){
        btn_back_pressed_adopt_act.clicks().subscribe{
            finish()
        }
        btn_adopt_pressed_adopt_act.clicks().subscribe{
            customShelterDialog.show()
        }
    }

    private var cancleBtnListener = View.OnClickListener {
        customShelterDialog.dismiss()
    }

    private var confirmBtnListener = View.OnClickListener {
        // 전화 번호
        var mNum = "01035680465"
        val tel = "tel:$mNum"
        startActivity(Intent("android.intent.action.DIAL", Uri.parse(tel)))

        customShelterDialog.dismiss()
    }

}
