package com.rafalzawadzki.meatyviewpager.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import com.rafalzawadzki.meatyviewpager.R
import com.rafalzawadzki.meatyviewpager.core.MeatService
import com.rafalzawadzki.meatyviewpager.core.model.Meat
import com.rafalzawadzki.meatyviewpager.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MeatAdapter()
    private val meats = MeatService().getMeats()
    private var reenterState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setExitSharedElementCallback(exitSharedElementCallback)
        setUI()
    }

    private fun setUI() {
        list.adapter = adapter
        adapter.clickListener = this::onMeatClicked
        adapter.setItems(meats)
    }

    private fun onMeatClicked(meat: Meat, clickedView: View) {
        val transitionName = ViewCompat.getTransitionName(clickedView)
        val intent = DetailActivity.newIntent(this, meat)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, clickedView, transitionName)
        startActivity(intent, options.toBundle())
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        reenterState = Bundle(data.extras)
        reenterState?.let {
            val startingPosition = it.getInt(EXTRA_STARTING_MEAT_POSITION)
            val currentPosition = it.getInt(EXTRA_CURRENT_MEAT_POSITION)
            if (startingPosition != currentPosition) list.scrollToPosition(currentPosition)
            ActivityCompat.postponeEnterTransition(this)

            scheduleStartPostponedTransition(list)
        }
    }

    private val exitSharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            reenterState?.let {
                val startingPosition = it.getInt(EXTRA_STARTING_MEAT_POSITION)
                val currentPosition = it.getInt(EXTRA_CURRENT_MEAT_POSITION)
                if (startingPosition != currentPosition) {
                    val newTransitionName = meats[currentPosition].transitionName
                    val newSharedElement = list.findViewWithTag<View>(newTransitionName)
                    newSharedElement?.let {
                        names.clear()
                        names.add(newTransitionName)
                        sharedElements.clear()
                        sharedElements[newTransitionName] = newSharedElement
                    }
                }
                reenterState = null
            }
        }
    }

    private fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }

    companion object {
        const val EXTRA_STARTING_MEAT_POSITION = "extra_starting_meat_position"
        const val EXTRA_CURRENT_MEAT_POSITION = "extra_current_meat_position"
    }
}
