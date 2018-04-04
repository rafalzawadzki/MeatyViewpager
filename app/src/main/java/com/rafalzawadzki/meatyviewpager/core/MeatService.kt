package com.rafalzawadzki.meatyviewpager.core

import com.rafalzawadzki.meatyviewpager.core.model.Meat


class MeatService {

    fun getMeats(): List<Meat> = arrayListOf(
            Meat("https://baconmockup.com/300/500/bacon2/"),
            Meat("https://baconmockup.com/300/500/bacon/"),
            Meat("https://baconmockup.com/300/500/bacon-eggs/"),
            Meat("https://baconmockup.com/300/500/beef/"),
            Meat("https://baconmockup.com/300/500/drumstick/"),
            Meat("https://baconmockup.com/300/500/drumstick2/"),
            Meat("https://baconmockup.com/300/500/corned-beef/"),
            Meat("https://baconmockup.com/300/500/brisket/"),
            Meat("https://baconmockup.com/300/500/flank-steak/"),
            Meat("https://baconmockup.com/300/500/hamburger/"),
            Meat("https://baconmockup.com/300/500/pork-ribs/"),
            Meat("https://baconmockup.com/300/500/sausage/")
    )

}
