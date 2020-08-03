package com.example.demoproject.ui.cameraModule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import com.example.demoproject.R

import com.example.demoproject.core.presentation.base.BaseActivity
import com.example.demoproject.databinding.ActivityCameraBinding
import com.example.demoproject.ui.cameraModule.CameraNavigator
import com.example.demoproject.ui.cameraModule.CameraViewModel
import com.example.demoproject.utils.ImageHelper.getOutputMediaFile
import com.example.demoproject.utils.ImageHelper.storageDir
import com.example.demoproject.utils.SessionManger
import com.example.demoproject.utils.SessionManger.Companion.PREF_FILE_NAME
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException

class CameraActivity : BaseActivity<ActivityCameraBinding, CameraViewModel>(), CameraNavigator {
    var activityCameraBinding: ActivityCameraBinding? = null
    val cameraViewModel: CameraViewModel by viewModel()
    var lensFacing = CameraX.LensFacing.FRONT
    var imageFilePath: String? = null
    var photoFile: File? = null
    private var lastClickTime: Long = 0
    private var flahmode = FlashMode.OFF

    var sessionManger: SessionManger? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_camera

    }

    override fun getViewModel(): CameraViewModel {
        return cameraViewModel
    }

    override fun setUp(savedInstanceState: Bundle?) {
        activityCameraBinding = getViewDataBinding()
        activityCameraBinding?.cameraCallback = this
        sessionManger = SessionManger(this@CameraActivity, PREF_FILE_NAME)

        lensFacing = if (sessionManger!!.isFront()) {
            CameraX.LensFacing.FRONT
        } else {
            CameraX.LensFacing.BACK
        }
        flahmode = if (sessionManger!!.isFlash()) {
            activityCameraBinding!!.flash.setImageResource(R.drawable.ic_flash_on_black_36dp)
            FlashMode.ON
        } else {
            activityCameraBinding!!.flash.setImageResource(R.drawable.ic_flash_off_black_36dp)
            FlashMode.OFF
        }
        startCamera()
    }


    override fun clickOnBack() {
        onBackPressed()
    }

    @SuppressLint("RestrictedApi")
    override fun clickOnFlash() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 300) {
            return
        } else {
            try {
                CameraX.unbindAll()
                if (flahmode == FlashMode.ON) {
                    activityCameraBinding!!.flash.setImageResource(R.drawable.ic_flash_off_black_36dp)
                    flahmode = FlashMode.OFF
                    sessionManger?.isFlash() == false
                } else {
                    sessionManger?.isFlash() == true
                    activityCameraBinding!!.flash.setImageResource(R.drawable.ic_flash_on_black_36dp)
                    flahmode = FlashMode.ON
                }
                Handler().postDelayed({
                    try {
                        activityCameraBinding!!.imgCapture.isEnabled = true
                        CameraX.getCameraWithLensFacing(lensFacing)
                        bindCameraUseCases()
                    } catch (e: Exception) {
                        Log.e("show", "camera method Exception==" + e.message)
                    }
                }, 300)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun clickOnLenseSwitch() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 300) {
            return
        } else {
            try {
                CameraX.unbindAll()
                if (lensFacing == CameraX.LensFacing.FRONT) {
                    lensFacing = CameraX.LensFacing.BACK
                    sessionManger!!.isFront() == false
                } else {
                    lensFacing = CameraX.LensFacing.FRONT
                    sessionManger!!.isFront() == true
                }
                Handler().postDelayed({
                    try {
                        activityCameraBinding!!.imgCapture.isEnabled = true
                        CameraX.getCameraWithLensFacing(lensFacing)
                        bindCameraUseCases()
                    } catch (e: Exception) {
                        Log.e("show", "camera method Exception==" + e.message)
                    }
                }, 300)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }

    fun getImageOrientation(imagePath: String?): Int {
        var rotate = 0
        try {
            val exif = ExifInterface(imagePath)
            val orientation =
                exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: IOException) {
            Log.e(
                "show",
                "ChecklistActivity getImageOrientation exception==" + e.message
            )
            e.printStackTrace()
        }
        return rotate
    }

    private fun createFolder() {
        if (!storageDir.exists()) {
            val created: Boolean = storageDir.mkdirs()
        }
    }

    override fun onResume() {
        super.onResume()
        createFolder()
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun bindCameraUseCases() {
        CameraX.unbindAll()
        val aspectRatio = Rational(
            activityCameraBinding!!.viewFinder.width,
            activityCameraBinding!!.viewFinder.height
        )
        val screen = Size(
            activityCameraBinding!!.viewFinder.width,
            activityCameraBinding!!.viewFinder.height
        ) //size of the screen
        val pConfig =
            PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen)
                .setLensFacing(lensFacing).build()
        val preview = Preview(pConfig)
        preview.onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener { output ->

            val parent = activityCameraBinding!!.viewFinder.parent as ViewGroup
            parent.removeView(activityCameraBinding!!.viewFinder)
            parent.addView(activityCameraBinding!!.viewFinder, 0)
            activityCameraBinding!!.viewFinder.surfaceTexture = output.surfaceTexture
            updateTransform()
        }
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            .setTargetRotation(Surface.ROTATION_0)
            .setLensFacing(lensFacing)
            .setFlashMode(flahmode)
            .setTargetResolution(Size(720, 1000))
            .build()
        val imgCap = ImageCapture(imageCaptureConfig)
        activityCameraBinding!!.imgCapture.setOnClickListener { v: View? ->
            activityCameraBinding!!.imgCapture.isEnabled = false
            activityCameraBinding!!.imgCapture.setImageResource(R.drawable.ic_circle_with_check_symbol)
            //Storage Path
            photoFile = getOutputMediaFile("1")
            imageFilePath = photoFile!!.getAbsoluteFile().toString()
            imgCap.takePicture(photoFile, object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    var strFilePath: String? = ""
                    strFilePath = imageFilePath
                    //         strFilePath = saveToInternalStorage(file);
                    //CommonUtils.compressImage(this@CameraActivity, strFilePath)
                    val returnIntent = Intent()
                    returnIntent.putExtra("strFilePath", strFilePath)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }

                override fun onError(
                    useCaseError: ImageCapture.UseCaseError,
                    message: String,
                    cause: Throwable?
                ) {
                    val msg = "Pic capture failed : $message"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    cause?.printStackTrace()
                }
            })
        }
        //bind to lifecycle:
        CameraX.bindToLifecycle(this, preview, imgCap)
    }

    private fun updateTransform() {
        val mx = Matrix()
        val w = activityCameraBinding!!.viewFinder.measuredWidth.toFloat()
        val h = activityCameraBinding!!.viewFinder.measuredHeight.toFloat()
        val cX = w / 2f
        val cY = h / 2f
        val rotationDgr: Int
        val rotation = activityCameraBinding!!.viewFinder.rotation.toInt()
        rotationDgr = when (rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        mx.postRotate(rotationDgr.toFloat(), cX, cY)
        activityCameraBinding!!.viewFinder.setTransform(mx)
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    private fun startCamera() {
        bindCameraUseCases()
        //clickOnLenseSwitch();
    }





}