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
import io.github.omisie11.coronatracker.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chipGithub.setOnClickListener { openWebUrl(getString(R.string.github_url_omisie11)) }
        binding.chipTwitter.setOnClickListener { openWebUrl(getString(R.string.twitter_url_omisie11)) }
        binding.chipWebsiteOmisie11.setOnClickListener { openWebUrl(getString(R.string.website_url_omisie11)) }
        binding.chipSourceCode.setOnClickListener { openWebUrl(getString(R.string.source_code_url)) }
        binding.chipAttributionApi.setOnClickListener {
            openWebUrl(getString(R.string.api_url))
        }
        binding.chipUsedLibs.setOnClickListener {
            findNavController().navigate(R.id.used_libraries_dest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openWebUrl(urlAddress: String) {
        if (urlAddress.isNotEmpty()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress)))
        }
    }
}
