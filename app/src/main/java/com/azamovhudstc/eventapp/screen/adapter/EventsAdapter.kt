package com.azamovhudstc.eventapp.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.eventapp.R
import com.azamovhudstc.eventapp.data.local.model.EventData
import kotlinx.android.synthetic.main.item_event.view.*

class EventsAdapter : ListAdapter<EventData, EventsAdapter.Holder>(EventsAdapterDiffUtils) {

    private var onEventStateChangeListener: ((eventId: Int, eventState: Boolean) -> Unit)? = null

    private object EventsAdapterDiffUtils : DiffUtil.ItemCallback<EventData>() {
        override fun areItemsTheSame(oldItem: EventData, newItem: EventData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: EventData, newItem: EventData): Boolean =
            oldItem == newItem
    }

    inner class Holder(view:View) :
        RecyclerView.ViewHolder(view) {

        init {
            itemView.container.setOnClickListener {
                val data = getItem(absoluteAdapterPosition)
                val state = !data.actionState
                data.actionState = state
                onEventStateChangeListener?.invoke(data.id, state)
                itemView.statusEvent.isChecked = state
            }
            itemView.statusEvent.setOnClickListener {
                val data = getItem(absoluteAdapterPosition)
                val state = !data.actionState
                data.actionState = state
                onEventStateChangeListener?.invoke(data.id, state)
                itemView.statusEvent.isChecked = state
            }
        }

        fun bind() = with(getItem(absoluteAdapterPosition)) {
            itemView.apply {
                imageEvent.setImageResource(actionIcon)
                textEvent.text = itemView.resources.getString(actionName)
                statusEvent.isChecked = actionState
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_event,parent,false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    fun setOnEventStateChangeListener(block: (eventId: Int, eventState: Boolean) -> Unit) {
        onEventStateChangeListener = block
    }

}