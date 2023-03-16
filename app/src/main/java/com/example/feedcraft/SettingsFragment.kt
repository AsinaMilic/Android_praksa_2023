package com.example.feedcraft

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.feedcraft.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ourPackage = resources.getString(R.string.play_store_package_id)
        val ourDev = resources.getString(R.string.play_store_dev)
        val ourPrivacyLink = resources.getString(R.string.privacy_link)
        val feedbackMessage = resources.getString(R.string.feedback_message)
        val mail = resources.getString(R.string.mail_of_company)

        binding.ViewMoreApps.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$ourPackage")
                setPackage("com.android.vending")
            }
            startActivity(intent)
        }

        binding.ViewRate.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/search?q=$ourDev")
                setPackage("com.android.vending")
            }
            try {
                startActivity(intent)
            }catch(activityNotFoundEx: ActivityNotFoundException){
                val text = "something bad happened! :("
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        }

        binding.ViewPrivacy.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(ourPrivacyLink)
            }
            try {
                startActivity(intent)
            }catch(activityNotFoundEx: ActivityNotFoundException){
                val text = "something bad happened! :("
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        }

        binding.ViewRecommend.setOnClickListener{
            
            val imageUri = getUriFromDrawableResId( requireContext(), R.drawable.logo)
            
            val appLink = "https://play.google.com/store/apps/details?id=$ourPackage"
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, appLink)
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, feedbackMessage)
            try {
                startActivity(shareIntent)
            }catch(activityNotFoundEx: ActivityNotFoundException){
                val text = "something bad happened! :("
                val duration = Toast.LENGTH_SHORT
                Toast.makeText(context, text, duration).show()
            }

        }

        binding.ViewFeedback.setOnClickListener {
            val subject = "FeedCraft - Version Code: ${BuildConfig.VERSION_CODE}, Version Name: ${BuildConfig.VERSION_NAME}, Device: ${Build.MODEL}, Android Version: ${Build.VERSION.SDK_INT}"
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                //data = Uri.parse("mailto:$mail")
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_EMAIL,arrayOf(mail))
                putExtra(Intent.EXTRA_TEXT, "Down below write your feedback message:")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Handle the case when no email app is available on the user's device.
                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }

        //To set the initial position of the Switch based on the current theme
        binding.switch1.isChecked = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
    fun getUriFromDrawableResId(context: Context, drawableResId: Int): Uri {
        val builder: StringBuilder = StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .append("://")
            .append(context.getResources().getResourcePackageName(drawableResId))
            .append("/")
            .append(context.getResources().getResourceTypeName(drawableResId))
            .append("/")
            .append(context.getResources().getResourceEntryName(drawableResId));
        return Uri.parse(builder.toString());
    }

}