package com.airongomes.compassouol.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.airongomes.compassouol.R
import com.airongomes.compassouol.detail.DetailViewModel
import com.airongomes.compassouol.network.EventProperty
import com.airongomes.compassouol.overview.OverviewViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

@BindingAdapter("imageAdapter")
fun ImageView.imageAdapter(imageUrl: String?) {
    imageUrl?.let {
        visibility = View.VISIBLE
        val imageUri = imageUrl.toUri().buildUpon().scheme("http").build()
        Glide.with(context)
            .load(imageUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.animation_loading)
                .error(R.drawable.ic_broken))
            .into(this)
    }
}

@BindingAdapter("dateAdapter")
fun TextView.dateAdapter(date: Long?) {
    date?.let {
        val calendar = Calendar.getInstance()
        calendar.time = Date(it)
        val textString = "Data de publicação: ${formatDateTime(calendar, context)}"
        text = textString

    }
}

@BindingAdapter("priceAdapter")
fun TextView.priceAdapter(price: Double?) {
    price?.let {
        val textString = "VALOR: R$${String.format("%.2f", price)}"
        text = textString
    }
}


@BindingAdapter("statusError")
fun statusError(view: View, status: OverviewViewModel.Status?) {
    status?.let {
        when(it){
            OverviewViewModel.Status.ERROR -> {
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("statusDone")
fun statusDone(view: View, status: OverviewViewModel.Status?) {
    status?.let {
        when(it){
            OverviewViewModel.Status.DONE -> {
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }
}


@BindingAdapter("statusAdapter")
fun ImageView.statusAdapter(status: OverviewViewModel.Status?) {
    status?.let {
        when(it){
            OverviewViewModel.Status.LOADING -> {
                this.visibility = View.VISIBLE
                setImageResource(R.drawable.animation_loading)

            }
            OverviewViewModel.Status.ERROR -> {
                this.visibility = View.VISIBLE
                setImageResource(R.drawable.ic_wifi_off)
            }
            else -> {
                this.visibility = View.GONE
            }
        }
    }
}


@BindingAdapter("statusErrorDetail")
fun statusErrorDetail(view: View, status: DetailViewModel.Status?) {
    status?.let {
        when(it){
            DetailViewModel.Status.ERROR -> {
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("statusDoneDetail")
fun statusDoneDetail(view: View, status: DetailViewModel.Status?) {
    status?.let {
        when(it){
            DetailViewModel.Status.DONE -> {
                view.visibility = View.VISIBLE
            }
            else -> {
                view.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("statusAdapterDetail")
fun ImageView.statusAdapterDetail(status: DetailViewModel.Status?) {
    status?.let {
        when(it){
            DetailViewModel.Status.LOADING -> {
                this.visibility = View.VISIBLE
                setImageResource(R.drawable.animation_loading)
            }
            DetailViewModel.Status.ERROR -> {
                this.visibility = View.VISIBLE
                setImageResource(R.drawable.ic_wifi_off)
            }
            else -> {
                this.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("enableLocation")
fun enableLocation(view: View, event: EventProperty?) {
    event?.let {
        if(event.latitude != null && event.longitude != null) {
            view.isEnabled = true
        }
    }
}
