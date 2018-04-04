package com.rafalzawadzki.meatyviewpager.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewTreeObserver
import com.rafalzawadzki.meatyviewpager.R
import com.rafalzawadzki.meatyviewpager.core.MeatService
import com.rafalzawadzki.meatyviewpager.core.Util.mapRange
import com.rafalzawadzki.meatyviewpager.core.model.Meat
import com.rafalzawadzki.meatyviewpager.list.MainActivity.Companion.EXTRA_CURRENT_MEAT_POSITION
import com.rafalzawadzki.meatyviewpager.list.MainActivity.Companion.EXTRA_STARTING_MEAT_POSITION
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private var detailPagerAdapter = DetailPagerAdapter(supportFragmentManager)
    private var meats = MeatService().getMeats()
    private var startingPosition: Int = 0
    private val pageTransformer = ZoomPageTransformer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        postponeEnterTransition()
        setEnterSharedElementCallback(enterSharedElementCallback)

        val transition = TransitionInflater.from(this).inflateTransition(R.transition.meat_transition)
        window.sharedElementEnterTransition = transition
        window.sharedElementExitTransition = transition
        window.sharedElementReturnTransition = transition
        window.sharedElementReenterTransition = transition

        setUI()

        intent.extras?.let {
            val meat = it.getSerializable(EXTRA_MEAT) as Meat
            val index = meats.indexOfFirst { it == meat }
            startingPosition = if (index > 0) index else 0
            pager.currentItem = startingPosition
        }
    }

    private fun setUI() {
        pager.adapter = detailPagerAdapter
        pager.setPageTransformer(true, pageTransformer)
        detailPagerAdapter.setItems(meats)
        containerDraggable.closeAction = this::closeActivity
        containerDraggable.dragAction = this::onContainerDrag
    }

    private val enterSharedElementCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
            val sharedElement = detailPagerAdapter.currentDetailFragment?.sharedElement
            sharedElement?.let {
                names.clear()
                names.add(sharedElement.transitionName)
                sharedElements.clear()
                sharedElements[sharedElement.transitionName] = sharedElement
            }
        }
    }

    fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                sharedElement.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun onBackPressed() {
        closeActivity()
    }

    private fun closeActivity() {
        val result = Intent()
        result.apply {
            putExtra(EXTRA_STARTING_MEAT_POSITION, startingPosition)
            putExtra(EXTRA_CURRENT_MEAT_POSITION, pager.currentItem)
        }
        setResult(Activity.RESULT_OK, result)
        super.finishAfterTransition()
    }

    private fun onContainerDrag(x: Float, y: Float) {
        val dragRangeY = 1000f // 1000 is an arbitrary number
        if (y > dragRangeY) return

        val mappedValue = mapRange(0f.rangeTo(dragRangeY), 0f.rangeTo(0.5f), Math.abs(y))
        detailPagerAdapter.currentDetailFragment?.view?.let {
            pageTransformer.makeSmall(mappedValue, it)
        }
    }

    companion object {

        const val EXTRA_MEAT = "extra_meat"

        fun newIntent(context: Context, meat: Meat): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.apply {
                putExtra(EXTRA_MEAT, meat)
            }
            return intent
        }

    }
}
