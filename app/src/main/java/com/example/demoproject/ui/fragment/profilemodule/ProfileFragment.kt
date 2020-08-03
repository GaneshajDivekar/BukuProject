package com.example.demoproject.ui.fragment.profilemodule

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.demoproject.R
import com.example.demoproject.ShiftApplication
import com.example.demoproject.core.presentation.base.BaseFragment
import com.example.demoproject.data.db.UserListEntity
import com.example.demoproject.databinding.FragmentProfileBinding
import com.example.demoproject.room.DatabaseHelper
import com.example.demoproject.ui.cameraModule.CameraActivity
import com.example.demoproject.utils.ImageHelper
import com.example.demoproject.utils.copyFileOrDirectory
import com.example.demoproject.utils.getImagePath
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import logi.retail.ui.setupstore.bottomsheet.ImageNavigator
import logi.retail.ui.setupstore.bottomsheet.SelectImageBottomSheet
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(), ProfileNavigator,
    ImageNavigator {
    var fragmentProfileBinding: FragmentProfileBinding? = null
    val profileViewModel: ProfileViewModel by viewModel()
    var imageNavigator: ImageNavigator? = null
    val REQUEST_GALLERY_IMAGE = 200
    val REQUEST_CAPTURE_IMAGE = 100
    private var imageFilePath: String? = ""
    private var photoPath: String? = ""
    private val PERMISSION_REQUEST_CODE = 200


    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun getViewModel(): ProfileViewModel {
        return profileViewModel
    }

    override fun setUp(view: View, savedInstanceState: Bundle?) {
        fragmentProfileBinding = getViewDataBinding()
        fragmentProfileBinding!!.profileCallBack = this
        imageNavigator = this
        fragmentProfileBinding!!.imgUserProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var selectionBottomSheet =
                    SelectImageBottomSheet("Product Image", imageNavigator as ProfileFragment)
                selectionBottomSheet.show(
                    activity!!.supportFragmentManager,
                    selectionBottomSheet.tag
                )
            }

        })

    }

    override fun clickOnCreateProfile() {
        var userlistModel = UserListEntity()
        userlistModel.first_name = fragmentProfileBinding!!.edtFirstNameResult.text.toString()
        userlistModel.last_name = fragmentProfileBinding!!.edtLastNameResult.text.toString()
        userlistModel.email = fragmentProfileBinding!!.edtEmailResult.text.toString()
        userlistModel.avatar = photoPath.toString()

        DatabaseHelper.getDatabase(activity!!).interfaceDao().addUsers(userlistModel)
        Toast.makeText(activity!!, "User data added successfully", Toast.LENGTH_SHORT).show()

    }

    override fun clickOnCamera() {


        if (checkPermission()) {
            // customerListEntityLIst.clear()
            callCameraActivity()
        } else {
            requestPermission()
        }
    }

    override fun clickOnGallery() {
        if (checkPermission()) {
            // customerListEntityLIst.clear()
            galleryIntent()
        } else {
            requestPermission()
        }

    }

    private fun galleryIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        photoPickerIntent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(photoPickerIntent, "Select File"),
            REQUEST_GALLERY_IMAGE
        )
    }

    private fun callCameraActivity() {
        val intent = Intent(activity!!, CameraActivity::class.java)
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            onCaptureImageResult(data!!)
        } else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            onSelectFromGalleryResult(data!!)
        } else {
            Toast.makeText(activity!!, "No Image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun onSelectFromGalleryResult(data: Intent) {
        photoPath = (saveGalleryImageToInternalStorage(data.data!!).toString())
        imageFilePath = photoPath as String
        if (photoPath != null) {
            Glide.with(activity!!)
                .load(photoPath)
                .placeholder(R.drawable.ic_dummy_user)
                .into(fragmentProfileBinding!!.imgUserProfile)
        } else {

        }
    }

    private fun onCaptureImageResult(data: Intent) {
        imageFilePath = data.getStringExtra("strFilePath")
        Uri.parse(imageFilePath)
        Glide.with(activity!!)
            .load(imageFilePath)
            .placeholder(R.drawable.ic_dummy_user)
            .into(fragmentProfileBinding!!.imgUserProfile)
        photoPath = imageFilePath
    }


    private fun saveGalleryImageToInternalStorage(imageUri: Uri): File? {
        createFolder()
        var str: String? = ""
        var mypath: File? = null
        if (imageUri.isAbsolute) {
            try {
                str = getImagePath(imageUri, activity!!)
                mypath = ImageHelper.getOutputMediaFile("1")
                copyFileOrDirectory(str, mypath!!.parent, mypath.name)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            System.gc()
        }
        return mypath
    }

    override fun onResume() {
        super.onResume()
        createFolder()
    }

    private fun createFolder() {
        if (!ImageHelper.storageDir.exists()) {
            val created: Boolean = ImageHelper.storageDir.mkdirs()
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            ShiftApplication.applicationContext,
            Manifest.permission.CAMERA
        )
        val result1 = ContextCompat.checkSelfPermission(
            ShiftApplication.applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val result2 = ContextCompat.checkSelfPermission(
            ShiftApplication.applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
        //
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val locationAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted =
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (locationAccepted && cameraAccepted) {

                    Snackbar.make(
                        layoutMobileNo,
                        "Permission Granted, Now you can access camera and gallary",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    Snackbar.make(
                        layoutMobileNo,
                        "Permission Denied, You cannot access Camera.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        /* if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                             showMessageOKCancel("You need to allow access to both the permissions",
                                 DialogInterface.OnClickListener { dialog, which ->
                                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                         requestPermissions(
                                             arrayOf(
                                                 Manifest.permission.READ_CONTACTS,
                                                 Manifest.permission.WRITE_CONTACTS
                                             ),
                                             PERMISSION_REQUEST_CODE
                                         )
                                     }
                                 })
                             return
                         }*/
                    }
                }
            }
        }
    }
}