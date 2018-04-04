package com.rafalzawadzki.meatyviewpager.core.model

import java.io.Serializable


data class Meat (val url: String,
                 val transitionName: String = "item_$url"): Serializable