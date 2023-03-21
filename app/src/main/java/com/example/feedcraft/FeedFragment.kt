package com.example.feedcraft

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.File


class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    var intentMainAct: Intent? = null

    private val viewModel: EditViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var galleryList: ArrayList<GalleryModel>
    private lateinit var galleryAdapter: GalleryAdapter

    private var clickedImage: GalleryModel? = null
    private lateinit var directory: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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


            val action = FeedFragmentDirections.actionItemFeedToDeleteDialogFragment()
            findNavController().navigate(action)
        }
        binding.imageViewFeedColorCode.setOnClickListener {
            if (clickedImage?.dominantColor == 0x00000000)
                clickedImage?.dominantColor = clickedImage?.let { it1 -> viewModel.getDominantColor(it1.imageBitmap) }!!
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
        galleryList = ArrayList()

        directory = File(context?.filesDir.toString(), "creation_previews")
        val files = directory.listFiles()

        if(files == null){
            GalleryImagesInvisible()
            return
        }

        GalleryImagesVisible()

        for (file in files) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath) // decode the image file to a Bitmap object
            //val dominantColor = viewModel.getDominantColor(bitmap)
            val dominantColor = 0x00000000
            galleryList.add(GalleryModel(bitmap, dominantColor))
        }

        galleryAdapter = GalleryAdapter(context, galleryList){ position: Int, active: Boolean ->
            Toast.makeText(context, "$position,$active", Toast.LENGTH_SHORT).show()
            clickedImage = galleryList[position]
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




}