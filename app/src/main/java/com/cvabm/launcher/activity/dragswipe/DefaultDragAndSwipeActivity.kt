package com.cvabm.launcher.activity.dragswipe

import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CacheDiskUtils
import com.blankj.utilcode.util.GsonUtils
import com.cvabm.launcher.activity.dragswipe.adapter.DragAndSwipeAdapter
import com.cvabm.launcher.base.BaseViewBindingActivity
import com.chad.baserecyclerviewadapterhelper.databinding.ActivityUniversalRecyclerBinding
import com.chad.library.adapter.base.dragswipe.QuickDragAndSwipe
import com.chad.library.adapter.base.dragswipe.listener.OnItemDragListener
import com.chad.library.adapter.base.dragswipe.listener.OnItemSwipeListener
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import net.sourceforge.pinyin4j.PinyinHelper
import org.json.JSONArray
import org.json.JSONObject


/**
 * 默认实现拖动与侧滑效果
 * Drag and Drag effects are implemented by default
 */
class DefaultDragAndSwipeActivity : BaseViewBindingActivity<ActivityUniversalRecyclerBinding>() {

    private val mAdapter: DragAndSwipeAdapter = DragAndSwipeAdapter()
    var mListData: List<AppBean>? = null
    var mListSearched: List<AppBean>? = null
    private val quickDragAndSwipe = QuickDragAndSwipe()
        .setDragMoveFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun initBinding(): ActivityUniversalRecyclerBinding =
        ActivityUniversalRecyclerBinding.inflate(layoutInflater)

    var pd: ProgressDialog? = null
    fun showLoading() {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading..")
        pd!!.show()
    }

    private fun dismissLoading() {
        pd!!.dismiss()

    }

    private var subHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val obj = msg.obj as String
            if (obj.isNullOrEmpty()) viewBinding.delete.visibility =
                View.GONE else viewBinding.delete.visibility = View.VISIBLE
            val filter = mListData?.filter {
                if (it.pinyin.isNullOrEmpty()) false else it.pinyin!!.contains(obj)
            } ?: emptyList()
            if (!filter.isNullOrEmpty()) {
                mListSearched = filter
                mAdapter.submitList(mListSearched)
            }
        }
    }


    fun handInputMessage(input: String) {
        subHandler.removeMessages(0)
        val message: Message = subHandler.obtainMessage()
        message.obj = input
        message.what = 0
        subHandler.sendMessageDelayed(message, 200)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.rv.layoutManager = GridLayoutManager(this, 7)
        viewBinding.rv.adapter = mAdapter
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                handInputMessage(s.toString());
            }
        }
        viewBinding.et.addTextChangedListener(textWatcher)
        viewBinding.delete.setOnTouchListener { v, event ->
            viewBinding.et.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        viewBinding.delete2.setOnTouchListener { v, event ->
            viewBinding.et.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        val mData = generateData()
        mListData = mData
        mAdapter.submitList(mData)
        // 拖拽监听
        val listener: OnItemDragListener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                Log.d(TAG, "drag start")
                val holder = viewHolder as QuickViewHolder? ?: return
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.WHITE
                val endColor = Color.rgb(245, 245, 245)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation: ValueAnimator ->
                        holder.itemView.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
                    v.start()
                }
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder,
                from: Int,
                target: RecyclerView.ViewHolder,
                to: Int
            ) {
                Log.d(
                    TAG,
                    "move from: " + source.bindingAdapterPosition + " to: " + target.bindingAdapterPosition
                )
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "drag end")
                val holder = viewHolder as QuickViewHolder
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.rgb(245, 245, 245)
                val endColor = Color.WHITE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation: ValueAnimator ->
                        holder.itemView.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
                    v.start()
                }

                mAdapter.items.forEach {
                    Log.d(
                        TAG,
                        "-------->> w 顺序 ${it} "
                    )
                }

            }
        }
        val swipeListener: OnItemSwipeListener = object : OnItemSwipeListener {
            override fun onItemSwipeStart(
                viewHolder: RecyclerView.ViewHolder?,
                bindingAdapterPosition: Int
            ) {
                Log.d(TAG, "onItemSwipeStart")
            }

            override fun onItemSwipeEnd(
                viewHolder: RecyclerView.ViewHolder,
                bindingAdapterPosition: Int
            ) {
                Log.d(TAG, "onItemSwipeEnd")
            }

            override fun onItemSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int,
                bindingAdapterPosition: Int
            ) {
                Log.d(TAG, "onItemSwiped")
            }

            override fun onItemSwipeMoving(
                canvas: Canvas,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {
                Log.d(TAG, "onItemSwipeMoving")
            }
        }

        // 滑动事件
        quickDragAndSwipe.attachToRecyclerView(viewBinding.rv)
            .setDataCallback(mAdapter)
            .setItemDragListener(listener)
            .setItemSwipeListener(swipeListener)

        // 点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            launchApp(adapter.getItem(position)?.pkgName)
        }
    }

    private fun launchApp(packageName: String?) {
        val launchIntentForPackage =
            packageName?.let { packageManager.getLaunchIntentForPackage(it) }
        launchIntentForPackage?.let { startActivity(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateData(): List<AppBean> {
        val cacheCount = CacheDiskUtils.getInstance().cacheCount
        val data = ArrayList<AppBean>()
        if (cacheCount > 0) {
            val jsonObj: JSONObject = CacheDiskUtils.getInstance().getJSONObject("list")
            val jsonArray = jsonObj.getJSONArray("array")
            for (i in 0 until jsonArray.length()) {
                val appBean = GsonUtils.fromJson(jsonArray.get(i).toString(), AppBean::class.java)
                val drawable = appBean.pkgName?.let { CacheDiskUtils.getInstance().getDrawable(it) }
                appBean.drawable = drawable
                data.add(appBean)
            }
        } else {
            showLoading()
            val appsInfo = AppUtils.getAppsInfo()
            val names = mutableSetOf<String>()
            val jsonObject1 = JSONObject()
            val jsonArray = JSONArray()
            for (app in appsInfo) {
                app.packageName?.let { packageManager.getLaunchIntentForPackage(it) }
                    ?: continue
                names.add(app.packageName)
                val appBean = AppBean()
                appBean.name = app.name
                appBean.pkgName = app.packageName
                appBean.drawable = app.icon
                val chineseInitial = getChineseFirstLetter(app.name)
                val pinyin4j = Pinyin4jUtil.getPinyin(app.name, true)
                val firstSpellPinYin1 = Pinyin4jUtil.getFirstSpellPinYin(app.name, false)
                var pinyin = app.name + ","
                if (!chineseInitial.isNullOrEmpty()) {
                    pinyin += "$chineseInitial,"
                }
                pinyin += "$firstSpellPinYin1,"
                pinyin4j.forEach { pinyin += "$it," }
                appBean.pinyin = pinyin

                val jsonObject = JSONObject()
                jsonObject.put("name", app.name)
                jsonObject.put("pkgName", app.packageName)
                jsonObject.put("pinyin", pinyin)
                jsonArray.put(jsonObject)
                CacheDiskUtils.getInstance().put(app.packageName, app.icon)
                data.add(appBean)
            }
            jsonObject1.put("array", jsonArray)
            CacheDiskUtils.getInstance().put("list", jsonObject1)
            dismissLoading()
        }
        return data
    }

    private fun getAppList() {
        val pm = packageManager
        // Return a List of all packages that are installed on the device.
        val packages = pm.getInstalledPackages(0)
        println("getAppList " + packages.size)
        for (packageInfo in packages) {
            println("getAppList, packageInfo=" + packageInfo.packageName)
        }
    }

    fun getChineseFirstLetter(chinese: String): String {
        val sb = StringBuilder()

        for (i in 0 until chinese.length) {
            val c = chinese[i]
            val pyArray = PinyinHelper.toHanyuPinyinStringArray(c)

            if (pyArray != null && pyArray.isNotEmpty()) {
                val py = pyArray[0]
                val firstLetter = py[0].toLowerCase()
                sb.append(firstLetter)
            } else {
                sb.append(c)
            }
        }

        return sb.toString()
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    class AppBean {
        var name: String? = null
        var pkgName: String? = null
        var drawable: Drawable? = null
        var pinyin: String? = null
    }

    companion object {
        private const val TAG = "ljg"
    }
}