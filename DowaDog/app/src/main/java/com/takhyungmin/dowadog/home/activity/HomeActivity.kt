package com.takhyungmin.dowadog.home.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.clicks
import com.takhyungmin.dowadog.CustomHomeDailogActivty
import com.takhyungmin.dowadog.R
import com.takhyungmin.dowadog.adopt.fragment.AdoptAnimalFindFragment
import com.takhyungmin.dowadog.community.CommunityFragment
import com.takhyungmin.dowadog.contents.fragment.ContentsFragment
import com.takhyungmin.dowadog.home.HomeObject
import com.takhyungmin.dowadog.home.fragment.HomeFragment
import com.takhyungmin.dowadog.home.model.get.GetDuplicateResponse
import com.takhyungmin.dowadog.home.model.get.GetUserInfoData
import com.takhyungmin.dowadog.introduce.IntroduceActivity
import com.takhyungmin.dowadog.login.LoginActivity
import com.takhyungmin.dowadog.mypage.MypageActivity
import com.takhyungmin.dowadog.presenter.activity.HomeActivityPresenter
import com.takhyungmin.dowadog.search.SearchActivity
import com.takhyungmin.dowadog.utils.ApplicationData
import com.takhyungmin.dowadog.utils.CustomTypeSpan
import com.takhyungmin.dowadog.utils.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.navi_home.*
import org.jetbrains.anko.startActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var homeActivityPresenter : HomeActivityPresenter
    var clickedText : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initPresenter()
        homeActivityPresenter.initView()
        init()
        //homeActivityPresenter.requestData()
        setBinding()
        textSizeChange(text_navi_home)

        if(SharedPreferenceController.getFirstPopUpFlag(this@HomeActivity) == 0){
            startActivity<CustomHomeDailogActivty>()
            SharedPreferenceController.setFirstPopUpFlag(this@HomeActivity, 1)
        }




    }

    fun init(){
        if(ApplicationData.loginState){
            layout_navi_not_login.visibility = View.GONE
            layout_navi_after_login.visibility = View.VISIBLE
            requestUserInfo()
        }else{
            layout_navi_not_login.visibility = View.VISIBLE
            layout_navi_after_login.visibility = View.GONE
        }
    }

    fun requestUserInfo(){
        homeActivityPresenter.requestUserInfo()
    }

    fun responseUserInfo(userData : GetUserInfoData){
        tv_navi_name.text = userData.name + "님,"
        Glide.with(this).load(userData.thumbnailImg).into(img_navi_profile)
    }

    fun responseData(data : GetDuplicateResponse){
        Log.v("data", data.message)
    }

    fun initPresenter(){
        homeActivityPresenter = HomeActivityPresenter()
        homeActivityPresenter.view = this
        HomeObject.homeActivityPresenter = homeActivityPresenter
    }

    fun addFragment(fragment : Fragment){
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.add(R.id.frame_home, fragment)
        transaction.commitNow()
    }

    fun replaceFragment(fragment : Fragment){
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.replace(R.id.frame_home, fragment)
        transaction.commitNow()
    }

    fun setBinding(){
//        btn_home_drawer.clicks().subscribe {
//            if (!drawer_home.isDrawerOpen(Gravity.START))
//                drawer_home.openDrawer(Gravity.START)
//        }

        btn_navi_login.clicks().subscribe {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        btn_home_drawer.clicks().subscribe {
            if (!drawer_home.isDrawerOpen(Gravity.START))
                drawer_home.openDrawer(Gravity.START)
        }


        btn_navi_home.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }
            homeActivityPresenter.replaceFragment(HomeFragment())
            layout_home_base.elevation = 1f
            btn_home_search.visibility = View.GONE
            btn_home_mypage.visibility = View.VISIBLE
            tv_home_title.text = "기다릴개"
            textSizeChange(text_navi_home)
        }

        btn_navi_contents.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }
            homeActivityPresenter.replaceFragment(ContentsFragment())
            layout_home_base.elevation = 0f
            btn_home_mypage.visibility = View.GONE
            btn_home_search.visibility = View.GONE
            textSizeChange(text_navi_contents)
            tv_home_title.text = "컨텐츠"

        }

        btn_navi_community.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }
            homeActivityPresenter.replaceFragment(CommunityFragment())
            layout_home_base.elevation = 0f
            btn_home_mypage.visibility = View.GONE
            btn_home_search.visibility = View.GONE
            textSizeChange(text_navi_community)
            tv_home_title.text = "커뮤니티"
        }

        btn_navi_adopt.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }

            layout_home_base.elevation = 1f
            btn_home_mypage.visibility = View.GONE
            btn_home_search.visibility = View.GONE
            homeActivityPresenter.replaceFragment(AdoptAnimalFindFragment())
            textSizeChange(text_navi_adopt)
            tv_home_title.text = "입양 하기"
            btn_home_search.visibility = View.VISIBLE
        }

        btn_navi_mypage.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }

            startActivity(Intent(this, MypageActivity::class.java))

        }

        btn_home_mypage.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }

            startActivity(Intent(this, MypageActivity::class.java))
        }

        btn_navi_introduce.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 400)
            }

            startActivity(Intent(this, IntroduceActivity::class.java))
        }

        btn_home_search.clicks().subscribe {
            if (drawer_home.isDrawerOpen(Gravity.START)){
                Handler().postDelayed(Runnable {
                    //communityFragmentPresenter.nextPage(currentPage, itemCount)
                    drawer_home.closeDrawer(Gravity.START)
                }, 500)
            }

            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    fun textSizeChange(textView : TextView){
        val font1 = Typeface.createFromAsset(assets, "nanum_square_extra_bold.ttf")
        val font2 = Typeface.createFromAsset(assets, "nanum_square_light.ttf")

        if(clickedText != null){
            val ssb2 = SpannableStringBuilder(clickedText!!.text.toString())
            ssb2.setSpan(CustomTypeSpan("", font2), 0, clickedText!!.text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            clickedText!!.text = ssb2

        }
        val ssb1 = SpannableStringBuilder(textView.text.toString())

        ssb1.setSpan(CustomTypeSpan("", font1), 0, textView.text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)

        textView.text = ssb1
        clickedText = textView
    }

    fun clickedAdoptBtn(){
        textSizeChange(text_navi_adopt)
    }

    fun adjustDim(percent : Float){
        layout_home_dim.alpha = percent
    }

    fun toNew(){
        btn_home_mypage.visibility = View.GONE
        btn_home_search.visibility = View.VISIBLE
    }
}
