package com.lock.smartlocker.ui.face_list.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.UserLockerModel
import com.lock.smartlocker.databinding.ItemFaceBinding
import com.lock.smartlocker.ui.face_list.FaceListViewModel
import com.xwray.groupie.databinding.BindableItem

class FaceItem(
    private val context: Context,
    private val model: UserLockerModel,
    private val viewModel: FaceListViewModel

) : BindableItem<ItemFaceBinding>() {

    override fun getLayout() = R.layout.item_face

    override fun bind(viewBinding: ItemFaceBinding, position: Int) {
        viewBinding.userLocker = model
        // Decode Base64 string to byte array
        val decodedString = Base64.decode(model.faceBase64, Base64.DEFAULT)

        // Convert byte array to Bitmap
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        // Use Glide to display the Bitmap in the ImageView
        val requestOptions = RequestOptions()
            .transform(CircleCrop(), Rotate(90))
        Glide.with(context)
            .load(decodedByte)
            .apply(requestOptions)
            .into(viewBinding.ivAvatar)
        viewBinding.btnRemove.setOnClickListener {
            viewModel.removeFace(model)
        }
    }
}