package com.example.feedcraft

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedcraft.adapters.GalleryAdapter
import com.example.feedcraft.databinding.FragmentFeedBinding
import com.example.feedcraft.repository.PreferenceDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    //private val binding get() = _binding!!
    private val viewModel: FeedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var galleryList: ArrayList<GalleryModel> = ArrayList()
    private lateinit var galleryAdapter: GalleryAdapter

    private var clickedImage: GalleryModel? = null
    private var clickedImages: MutableList<GalleryModel?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EditButtonsInvisible()

        viewModel.deletePicture.observe(viewLifecycleOwner){deletePicture ->
            if(!deletePicture)
                return@observe
            val uriToDelete = clickedImage?.uri

            if (uriToDelete != null) {
                val fileToDelete = uriToDelete.path?.let { it -> File(it) }
                if ((fileToDelete != null) && fileToDelete.exists())
                    fileToDelete.delete()
            }

            val prefDataStore = PreferenceDataStore.getInstance(requireContext())
            lifecycleScope.launch {
                prefDataStore.getGsonImageData().collect { json ->
                    val objectArray = Gson().fromJson(json, Array<ImageData>::class.java)

                    val idToRemove = viewModel.extractFileName(uriToDelete.toString())

                    val updatedObjectArray = objectArray.filter { it.IdCreation != idToRemove }

                    prefDataStore.setGsonImageData(Gson().toJson(updatedObjectArray))

                    /*GalleryAdapter.itemSelected = null
                    GalleryAdapter.oneIsSelected = false*/
                    //galleryAdapter.galleryListBool.removeLast()
                    //galleryAdapter.galleryListBool.fill(false)
                    refreshRecyclerView()

                }
            }
            EditButtonsInvisible()
            clickedImages?.removeAt(clickedImages!!.indexOf(clickedImage))
            clickedImage = null
        }

        binding.imageViewFeedDelete.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToDeleteDialogFragment()
            findNavController().navigate(action)
        }

        binding.imageViewFeedColorCode.setOnClickListener {
            if (clickedImage?.dominantColor == 0x00000000) {
                val color = viewModel.getDominantColor(clickedImage!!.imageBitmap)
                clickedImage?.dominantColor = color
            }
            else
                clickedImage?.dominantColor = 0x00000000

            galleryAdapter.notifyDataSetChanged()
            EditButtonsInvisible()
            clickedImages?.removeAt(clickedImages!!.indexOf(clickedImage))
            clickedImage = null
        }

        binding.imageViewFeedEdit.setOnClickListener{
            UIApplication.imageUri = clickedImage?.uriOrg
            startActivity(Intent(requireContext(), EditActivity::class.java).putExtra("CameraOrGallery", "Gallery"))
        }

        binding.imageViewButtonPlus.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToFeedDialogFragment()
            findNavController().navigate(action)
        }

        UIApplication.saveCompleted.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Toast.makeText(context, "Picture has been saved!", Toast.LENGTH_SHORT).show()
                refreshRecyclerView()
            }
        }
    }

    private fun init(){
        recyclerView = binding.RecyclerViewGallery
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            val tempGalleryList = viewModel.getGalleryList(requireContext())

            if(tempGalleryList == null)
                GalleryImagesInvisible()
            else{
                GalleryImagesVisible()
                galleryList = tempGalleryList
            }

            clickedImages = MutableList(galleryList.size){null}

            galleryAdapter = GalleryAdapter(context, galleryList){ position: Int ->
                Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
                val clickedImg = galleryList[position]

                if (clickedImages?.contains(clickedImg) == true) {
                    clickedImages!!.removeAt(clickedImages!!.indexOf(clickedImg))
                } else {
                    clickedImages?.add(position,clickedImg)
                }
                clickedImage =  clickedImages?.find { it != null }
                val countNonNulls = clickedImages?.count { it != null }
                if(countNonNulls == 1) {
                    EditButtonsVisible()
                }
                else {
                    EditButtonsInvisible()

                }
            }

            recyclerView.adapter = galleryAdapter
        }

    }

    private fun GalleryImagesVisible() {
        binding.imageViewGirl1.visibility = View.INVISIBLE
        binding.RecyclerViewGallery.visibility = View.VISIBLE
        binding.textViewFeed.visibility = View.INVISIBLE
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

    }
    
    private fun refreshRecyclerView(){
        lifecycleScope.launch {
            viewModel.getGalleryList(requireContext())?.let {
                galleryAdapter.galleryListChanged(it)
                galleryList = it
                galleryAdapter.notifyDataSetChanged()
                UIApplication.galleryListChanged = false
            }
        }
    }
}