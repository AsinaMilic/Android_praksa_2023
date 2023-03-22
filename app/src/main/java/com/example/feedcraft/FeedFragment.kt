package com.example.feedcraft

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedcraft.Adapter.GalleryAdapter
import com.example.feedcraft.databinding.FragmentFeedBinding
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream


class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    var intentMainAct: Intent? = null

    private val viewModel: FeedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var galleryList: ArrayList<GalleryModel>? = null
    private lateinit var galleryAdapter: GalleryAdapter

    private var clickedImage: GalleryModel? = null
    private lateinit var directory: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EditButtonsInvisible()

        binding.imageViewFeedDelete.setOnClickListener{
            val files = directory.listFiles()
            val uriToDelete = clickedImage?.uri

            if (uriToDelete != null) {
                val fileToDelete = uriToDelete.path?.let { it1 -> File(it1) }
                if ((fileToDelete != null) && fileToDelete.exists())
                        fileToDelete.delete()
            }

            val action = FeedFragmentDirections.actionItemFeedToDeleteDialogFragment()
            findNavController().navigate(action)
        }

        binding.imageViewFeedColorCode.setOnClickListener {
            if (clickedImage?.dominantColor == 0x00000000) {
                clickedImage?.dominantColor = clickedImage?.let { it1 -> viewModel.getDominantColor(it1.imageBitmap) }!!
                Toast.makeText(context, "Color code is set, please double-click the picture again ❤", Toast.LENGTH_SHORT).show()
            }
            else
                clickedImage?.dominantColor = 0x00000000
            galleryAdapter.notifyDataSetChanged()
        }

        binding.imageViewFeedEdit.setOnClickListener{
            startActivity(Intent(requireActivity(), EditActivity::class.java))
        }

        binding.imageViewButtonPlus.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToFeedDialogFragment()
            findNavController().navigate(action)
        }
    }

    private fun init(){
        recyclerView = binding.RecyclerViewGallery
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        galleryList = viewModel.getGalleryList(requireContext())!!

        if(galleryList == null){
            GalleryImagesInvisible()
            return
        }
        GalleryImagesVisible()

        galleryAdapter = GalleryAdapter(context, galleryList!!){ position: Int, active: Boolean ->
            Toast.makeText(context, "$position,$active", Toast.LENGTH_SHORT).show()
            clickedImage = galleryList!![position]
            if(active)
                EditButtonsVisible()
            else {
                EditButtonsInvisible()
                clickedImage = null
            }
        }

        recyclerView.adapter = galleryAdapter
    }

    private fun GalleryImagesVisible() {
        binding.imageViewGirl1.visibility = View.INVISIBLE
        binding.RecyclerViewGallery.visibility = View.VISIBLE
       // binding.textViewFeed.visibility = View.INVISIBLE //mozda
    }
    private fun GalleryImagesInvisible() {
        binding.imageViewGirl1.visibility = View.VISIBLE
        binding.textViewFeed.visibility = View.VISIBLE
        binding.imageViewFeedEdit.visibility = View.INVISIBLE
        binding.imageViewFeedColorCode.visibility = View.INVISIBLE
        binding.imageViewFeedDelete.visibility = View.INVISIBLE
        binding.textViewEdit.visibility = View.INVISIBLE
        binding.textViewColorCode.visibility = View.INVISIBLE
        binding.textViewDelete.visibility = View.INVISIBLE
        binding.RecyclerViewGallery.visibility = View.INVISIBLE
    }
    private fun EditButtonsVisible(){

        binding.imageViewButtonPlus.visibility = View.INVISIBLE
        binding.textViewFeed.visibility = View.INVISIBLE
        binding.imageViewFeedEdit.visibility = View.VISIBLE
        binding.imageViewFeedColorCode.visibility = View.VISIBLE
        binding.imageViewFeedDelete.visibility = View.VISIBLE
        binding.textViewEdit.visibility = View.VISIBLE
        binding.textViewColorCode.visibility = View.VISIBLE
        binding.textViewDelete.visibility = View.VISIBLE
    }

    private fun EditButtonsInvisible(){

        binding.imageViewButtonPlus.visibility = View.VISIBLE
        binding.textViewFeed.visibility = View.INVISIBLE
        binding.imageViewFeedEdit.visibility = View.INVISIBLE
        binding.imageViewFeedColorCode.visibility = View.INVISIBLE
        binding.imageViewFeedDelete.visibility = View.INVISIBLE
        binding.textViewEdit.visibility = View.INVISIBLE
        binding.textViewColorCode.visibility = View.INVISIBLE
        binding.textViewDelete.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        if(UIApplication.galleryListChanged) {
            viewModel.getGalleryList(requireContext())?.let {
                galleryAdapter.galleryListChanged(it)
                galleryAdapter.notifyItemInserted(0)
                UIApplication.galleryListChanged = false
            }

        }
    }

}