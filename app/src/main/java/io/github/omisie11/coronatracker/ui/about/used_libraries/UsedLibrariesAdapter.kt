package io.github.omisie11.coronatracker.ui.about.used_libraries

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.omisie11.coronatracker.databinding.UsedLibrariesRecyclerItemBinding

class UsedLibrariesAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<UsedLibrariesAdapter.ViewHolder>() {

    private var librariesList: List<UsedLibrary> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = UsedLibrariesRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (librariesList.isNotEmpty()) holder.bind(librariesList[position], itemClickListener)
    }

    override fun getItemCount(): Int = librariesList.size

    inner class ViewHolder(private val itemBinding: UsedLibrariesRecyclerItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val libLicenseTextView: TextView = itemBinding.textLibLicense
        private val libNameTextView: TextView = itemBinding.textLibName
        private val libDescTextView: TextView = itemBinding.textLibDesc

        fun bind(library: UsedLibrary, itemClickListener: OnItemClickListener) {
            libLicenseTextView.text = library.license
            libNameTextView.text = library.name
            libDescTextView.text = library.description

            itemBinding.root.setOnClickListener {
                if (adapterPosition != -1) itemClickListener.onItemClicked(library.repositoryUrl)
            }
        }
    }

    fun setData(data: List<UsedLibrary>) {
        librariesList = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(usedLibraryRepoUrl: String)
    }
}
