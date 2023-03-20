package com.example.feedcraft

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var galleryList: ArrayList<GalleryModel>
    private lateinit var galleryAdapter: GalleryAdapter

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

        binding.imageViewFeedDelete.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToDeleteDialogFragment()
            findNavController().navigate(action)
        }
        binding.imageViewFeedColorCode.setOnClickListener {
            //sta sa ovim
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

        val directory = File(context?.filesDir.toString(), "creation_previews")
        val files = directory.listFiles()

        if(files == null){
            GalleryImagesInvisible()
            return
        }

        GalleryImagesVisible()

        for (file in files) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath) // decode the image file to a Bitmap object
            val dominantColor = getDominantColor(bitmap)
            galleryList.add(GalleryModel(bitmap, dominantColor))
        }

        galleryAdapter = GalleryAdapter(galleryList){ position: Int, active: Boolean ->
            val clickedImage = galleryList[position]
            Toast.makeText(context, position.toString()+","+active.toString(), Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = galleryAdapter
    }

    private fun GalleryImagesVisible() {
        binding.imageViewGirl1.visibility = View.INVISIBLE
        binding.textViewFeed.visibility = View.INVISIBLE
        binding.imageViewFeedEdit.visibility = View.INVISIBLE
        binding.imageViewFeedColorCode.visibility = View.INVISIBLE
        binding.imageViewFeedDelete.visibility = View.INVISIBLE
        binding.textViewEdit.visibility = View.INVISIBLE
        binding.textViewColorCode.visibility = View.INVISIBLE
        binding.textViewDelete.visibility = View.INVISIBLE

        binding.RecyclerViewGallery.visibility = View.VISIBLE
    }
    private fun GalleryImagesInvisible() {
        binding.imageViewGirl1.visibility = View.VISIBLE
        binding.textViewFeed.visibility = View.VISIBLE
        binding.imageViewFeedEdit.visibility = View.VISIBLE
        binding.imageViewFeedColorCode.visibility = View.VISIBLE
        binding.imageViewFeedDelete.visibility = View.VISIBLE
        binding.textViewEdit.visibility = View.VISIBLE
        binding.textViewColorCode.visibility = View.VISIBLE
        binding.textViewDelete.visibility = View.VISIBLE

        binding.RecyclerViewGallery.visibility = View.INVISIBLE
    }

    private fun getDominantColor(bitmap: Bitmap?): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val dominantColor = newBitmap.getPixel(0, 0)
        newBitmap.recycle() //idk why this exactly
        return dominantColor //Color.red(???)
    }


}