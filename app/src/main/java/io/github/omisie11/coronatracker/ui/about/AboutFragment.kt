package io.github.omisie11.coronatracker.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.omisie11.coronatracker.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chip_github.setOnClickListener { openWebUrl(getString(R.string.github_url_omisie11)) }
        chip_twitter.setOnClickListener { openWebUrl(getString(R.string.twitter_url_omisie11)) }
        chip_website_omisie11.setOnClickListener { openWebUrl(getString(R.string.website_url_omisie11)) }
        chip_source_code.setOnClickListener { openWebUrl(getString(R.string.source_code_url)) }

        chip_attribution_api.setOnClickListener {
            openWebUrl(getString(R.string.api_url))
        }
        chip_used_libs.setOnClickListener { findNavController().navigate(R.id.used_libraries_dest) }
    }

    private fun openWebUrl(urlAddress: String) {
        if (urlAddress.isNotEmpty()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress)))
        }
    }
}
