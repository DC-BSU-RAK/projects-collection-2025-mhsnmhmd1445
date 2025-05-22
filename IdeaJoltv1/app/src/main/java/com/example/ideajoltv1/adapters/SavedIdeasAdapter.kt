package com.example.ideajoltv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ideajoltv1.R
import com.example.ideajoltv1.api.models.Idea
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SavedIdeasAdapter(
    private val onIdeaClicked: (Idea) -> Unit,
    private val onDeleteClicked: (Idea) -> Unit,
    private val onEditNotesClicked: (Idea) -> Unit
) : ListAdapter<Idea, SavedIdeasAdapter.IdeaViewHolder>(IdeaDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_idea, parent, false)
        return IdeaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTopic: TextView = itemView.findViewById(R.id.tv_topic)
        private val tvMood: TextView = itemView.findViewById(R.id.tv_mood)
        private val tvProjectType: TextView = itemView.findViewById(R.id.tv_project_type)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvNotes: TextView = itemView.findViewById(R.id.tv_notes)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
        private val btnEditNotes: ImageButton = itemView.findViewById(R.id.btn_edit_notes)

        fun bind(idea: Idea) {
            tvTopic.text = idea.topic
            tvMood.text = idea.mood
            tvProjectType.text = idea.projectType

            // Format date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
            tvDate.text = dateFormat.format(Date(idea.timestamp))

            // Set notes or hide if empty
            if (idea.notes.isNotEmpty()) {
                tvNotes.visibility = View.VISIBLE
                tvNotes.text = idea.notes
            } else {
                tvNotes.visibility = View.GONE
            }

            // Set click listeners
            itemView.setOnClickListener { onIdeaClicked(idea) }
            btnDelete.setOnClickListener { onDeleteClicked(idea) }
            btnEditNotes.setOnClickListener { onEditNotesClicked(idea) }
        }
    }

    object IdeaDiffCallback : DiffUtil.ItemCallback<Idea>() {
        override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean {
            return oldItem == newItem
        }
    }
}