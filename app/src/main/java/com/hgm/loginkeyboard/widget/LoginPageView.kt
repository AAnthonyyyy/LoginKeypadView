package com.hgm.loginkeyboard.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.hgm.loginkeyboard.R
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


/**
 * @author：  HGM
 * @date：  2023-06-17 16:33
 */
class LoginPageView : FrameLayout {
      companion object {
            const val SIZE_VERIFY_CODE_DEFAULT = 4
      }

      private var mainColor = 0
      private var verifyCodeSize = SIZE_VERIFY_CODE_DEFAULT
      private lateinit var checkBox: CheckBox
      private lateinit var etPhone: EditText
      private lateinit var etCode: EditText
      private lateinit var btnCode: View
      private lateinit var btnLogin: View
      private lateinit var loginKeypad: LoginKeyboard
      private var isPhoneNumOK = false
      private var isVerifyCodeOK = false
      private var isAgreementOK = false
      private val TAG = "LoginPageView"

      @Throws(PatternSyntaxException::class)
      fun isChinaPhoneLegal(str: String?): Boolean {
            val regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$"
            val p: Pattern = Pattern.compile(regExp)
            val m: Matcher = p.matcher(str)
            return m.matches()
      }


      constructor(context: Context) : this(
            context, null
      )

      constructor(context: Context, attrs: AttributeSet?) : this(
            context, attrs, 0
      )

      //统一入口后，用this来调用，确保它进入第三个方法
      constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context, attrs, defStyleAttr
      ) {
            //获取属性
            initAttrs(context, attrs)
            //初始化控件
            initView()
            //监听事件
            initEvent()
      }


      private fun initEvent() {
            loginKeypad.setOnLoginKeypadPressListener(object :
                  LoginKeyboard.OnLoginKeypadPressListener {
                  override fun onNumberPress(number: Int) {
                        //点击数字
                        getFocusView()?.let {
                              //获取焦点的输入框后在文本末尾插入
                              it.text.insert(it.selectionEnd, number.toString())
                        }
                  }

                  override fun onBackPress() {
                        //点击删除
                        getFocusView()?.let {
                              it.selectionEnd.let { index ->
                                    if (index > 0) {
                                          it.text.delete(index - 1, index)
                                    }
                              }
                        }
                  }

            })

            etPhone.addTextChangedListener {
                  object : TextWatcher {
                        override fun beforeTextChanged(
                              s: CharSequence?,
                              start: Int,
                              count: Int,
                              after: Int
                        ) {
                              TODO("Not yet implemented")
                        }

                        override fun onTextChanged(
                              s: CharSequence?,
                              start: Int,
                              before: Int,
                              count: Int
                        ) {
                              val phoneNum = s.toString()
                              val isMatch = isChinaPhoneLegal(phoneNum)
                              isPhoneNumOK = phoneNum.length == 11 && isMatch
                              updateAllBtnState()
                        }

                        override fun afterTextChanged(s: Editable?) {
                              TODO("Not yet implemented")
                        }
                  }
            }

            etCode.addTextChangedListener {
                  object : TextWatcher {
                        override fun beforeTextChanged(
                              s: CharSequence?,
                              start: Int,
                              count: Int,
                              after: Int
                        ) {
                              TODO("Not yet implemented")
                        }

                        override fun onTextChanged(
                              s: CharSequence?,
                              start: Int,
                              before: Int,
                              count: Int
                        ) {
                              isVerifyCodeOK = s?.length == 4
                              updateAllBtnState()
                        }

                        override fun afterTextChanged(s: Editable?) {
                              TODO("Not yet implemented")
                        }

                  }
            }

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                  isAgreementOK = isChecked
                  updateAllBtnState()
            }

            btnLogin.setOnClickListener {
                  //todo:登录

            }

            btnCode.setOnClickListener {
                  onLoginPageActionListener?.onGetCodeClick(etPhone.text.toString().trim())
            }
      }

      private fun initView() {
            LayoutInflater.from(context).inflate(R.layout.view_login_page, this, false).let {
                  addView(it)
            }
            checkBox = findViewById(R.id.cb_confirm)
            etPhone = findViewById(R.id.et_phone)
            etCode = findViewById(R.id.et_code)
            btnCode = findViewById(R.id.tv_code)
            btnLogin = findViewById(R.id.btn_login)
            loginKeypad = findViewById(R.id.login_keypad)
            //禁用软键盘
            etPhone.showSoftInputOnFocus = false
            etCode.showSoftInputOnFocus = false
            //设置checkBox的颜色
            if (mainColor != -1) {
                  checkBox.setTextColor(mainColor)
            }
            //设置验证码的规定长度
            etCode.filters = Array<InputFilter>(1) {
                  InputFilter.LengthFilter(verifyCodeSize)
            }
            //初始值
            updateAllBtnState()
      }

      private fun initAttrs(
            context: Context, attrs: AttributeSet?
      ) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView)
            with(a) {
                  mainColor = getColor(R.styleable.LoginPageView_mainColor, -1)
                  verifyCodeSize =
                        getInt(R.styleable.LoginPageView_verifyCodeSize, SIZE_VERIFY_CODE_DEFAULT)
            }
      }


      private fun getFocusView(): EditText? {
            val view = findFocus()
            if (view is EditText) {
                  return view
            }
            return null
      }

      private fun updateAllBtnState() {
            btnCode.isEnabled = isPhoneNumOK
            btnLogin.isEnabled = isPhoneNumOK && isAgreementOK && isVerifyCodeOK
      }


      /**
       * 定义功能接口
       */
      interface OnLoginPageActionListener {
            fun onGetCodeClick(phone: String)
            fun onConfirmClick(isConfirm: Boolean)
            fun onLoginClick(phone: String, code: String)
      }

      private var onLoginPageActionListener: OnLoginPageActionListener? = null
      fun setOnLoginPageActionListener(listener: OnLoginPageActionListener) {
            onLoginPageActionListener = listener
      }

}