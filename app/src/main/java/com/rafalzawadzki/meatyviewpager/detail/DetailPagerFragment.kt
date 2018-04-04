package com.rafalzawadzki.meatyviewpager.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafalzawadzki.meatyviewpager.R
import com.rafalzawadzki.meatyviewpager.core.model.Meat
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_meat.*


class DetailPagerFragment : Fragment() {

    private lateinit var meat: Meat
    lateinit var sharedElement: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.meat_transition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_detail_meat, container, false)
        meat = arguments.get(EXTRA_MEAT) as Meat
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {
        ViewCompat.setTransitionName(imgBackground, meat.transitionName)
        setImageForMeatWithTransition(meat)
        sharedElement = imgBackground
    }

    private fun setImageForMeatWithTransition(meat: Meat) {
        Picasso.with(context)
                .load(meat.url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .noFade()
                .fit()
                .into(imgBackground,
                        object : Callback {
                            override fun onSuccess() = startTransition()
                            override fun onError() = startTransition()
                        })
    }

    fun startTransition() {
        (activity as DetailActivity).scheduleStartPostponedTransition(imgBackground)
    }

    companion object {

        private const val EXTRA_MEAT = "extra_meat"

        fun newInstance(meat: Meat): DetailPagerFragment {
            val fragment = DetailPagerFragment()
            val args = Bundle()
            args.apply {
                putSerializable(EXTRA_MEAT, meat)
            }
            fragment.arguments = args
            return fragment
        }
    }
}