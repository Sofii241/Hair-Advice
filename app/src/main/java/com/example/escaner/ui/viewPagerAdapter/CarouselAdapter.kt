package com.example.escaner.ui.viewPagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.escaner.Model.viewPagerImages.CarouselItem
import com.example.escaner.databinding.CarouselBinding

class CarouselAdapter(private val carouselItems: List<CarouselItem>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(val binding: CarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(carouselItem: CarouselItem) {
            binding.imageViewCarousel.setImageResource(carouselItem.image)
            binding.textViewTitle.text = carouselItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = CarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(carouselItems[position])
    }

    override fun getItemCount(): Int = carouselItems.size
}
