package com.hgm.loginkeyboard.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hgm.loginkeyboard.R

/**
 * @author：  HGM
 * @date：  2023-06-17 14:44
 */
class LoginKeyboard : LinearLayout, View.OnClickListener {

      constructor(context: Context) : this(
            context,
            null
      )

      constructor(context: Context, attrs: AttributeSet?) : this(
            context,
            attrs,
            0
      )

      //统一入口后，用this来调用，确保它进入第三个方法
      constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
      ) {
            //获取相关属性
            //初始化
            val view =
                  LayoutInflater.from(context).inflate(R.layout.view_login_keyboard, this, false)
            addView(view)

            initView()
      }

      private fun initView() {
            findViewById<TextView>(R.id.num_1).setOnClickListener(this)
            findViewById<TextView>(R.id.num_2).setOnClickListener(this)
            findViewById<TextView>(R.id.num_3).setOnClickListener(this)
            findViewById<TextView>(R.id.num_4).setOnClickListener(this)
            findViewById<TextView>(R.id.num_5).setOnClickListener(this)
            findViewById<TextView>(R.id.num_6).setOnClickListener(this)
            findViewById<TextView>(R.id.num_7).setOnClickListener(this)
            findViewById<TextView>(R.id.num_8).setOnClickListener(this)
            findViewById<TextView>(R.id.num_9).setOnClickListener(this)
            findViewById<TextView>(R.id.num_0).setOnClickListener(this)
            findViewById<ImageView>(R.id.num_back).setOnClickListener(this)
      }

      override fun onClick(v: View?) {
            val viewId = v?.id
            if (onLoginKeypadPressListener == null) {
                  return
            }
            if (viewId == R.id.num_back) {
                  //走删除键
                  onLoginKeypadPressListener?.onBackPress()
            } else {
                  //走0-9数字键
                  val text =(v as TextView).text.toString()
                  onLoginKeypadPressListener?.onNumberPress(text.toInt())
            }

      }


      /**
       * 暴露功能接口
       */
      interface OnLoginKeypadPressListener {
            fun onNumberPress(number: Int)
            fun onBackPress()
      }

      private var onLoginKeypadPressListener: OnLoginKeypadPressListener? = null

      fun setOnLoginKeypadPressListener(listener: OnLoginKeypadPressListener) {
            onLoginKeypadPressListener = listener
      }
}