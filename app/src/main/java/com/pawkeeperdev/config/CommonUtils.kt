package com.pawkeeperdev.config

//noinspection ExifInterface
//noinspection ExifInterface
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.pawkeeperdev.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale


object CommonUtils {

    var thisIsDashBoard = false
    const val TAG = "RoomDataBase"


    fun addLogE(tag: String, msg: String) {
        Log.e(tag, msg)
    }


    @SuppressLint("RestrictedApi", "InflateParams")
    fun showCustomToast(view: View, msg: String?, isSuccess: Boolean) {
        /**For custom toast style */
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = inflater.inflate(R.layout.toast_layout, null)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = msg
        if (isSuccess) {
            layout.setBackgroundColor(view.context.getColor(R.color.s_color))
        } else {
            layout.setBackgroundColor(view.context.getColor(R.color.e_color))
        }

        val toast = Toast(view.context)
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }


    fun calculateAge(birthDateString: String): Int {
        val formatter = DateTimeFormatter.ofPattern("dd / MMM / yyyy", Locale.ENGLISH)
        val birthDate = LocalDate.parse(birthDateString.trim(), formatter)
        val currentDate = LocalDate.now()
        return ChronoUnit.YEARS.between(birthDate, currentDate).toInt()
    }


    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun showCameraDialog(
        context: Context,
        btnCameraClick: (() -> Unit)? = null,
        btnGalleryClick: (() -> Unit)? = null
    ) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(context)
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.custome_picker_option, null)
        val btnGallery = dialogView.findViewById<AppCompatImageView>(R.id.btnGallery)
        val btnCamera = dialogView.findViewById<AppCompatImageView>(R.id.btnCamera)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancel)


        builder.setView(dialogView)
        builder.setCancelable(true)


        alertDialog = builder.create()
        alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnCamera.setOnClickListener {
            btnCameraClick?.invoke()
            alertDialog.dismiss()
        }

        btnGallery.setOnClickListener {
            btnGalleryClick?.invoke()
            alertDialog.dismiss()
        }

        btnCancel.setOnClickListener {
            alertDialog?.dismiss()
        }


        alertDialog?.show()
    }


    fun changeDateFormat(format: String, cal: Date): String? {
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)
        return simpleDateFormat.format(cal)
    }


    fun getIconList(): List<Int> {
        val pets: List<Int> =
            listOf(
                R.mipmap.ic_cata,
                R.mipmap.ic_catb,
                R.mipmap.ic_catc,
                R.mipmap.ic_doga,
                R.mipmap.ic_dogb,
                R.mipmap.ic_dogc,
                R.mipmap.ic_dogd
            )
        return pets
    }

    /**
     * Convert a Bitmap to a URI.
     *
     * @param context The context to access the file system and FileProvider.
     * @param bitmap The Bitmap to convert.
     * @return The URI of the saved image file, or null if conversion fails.
     */
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val fileName = "image_${System.currentTimeMillis()}.png"
        val file = File(context.filesDir, fileName)
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun correctImageOrientation(context: Context, uri: Uri): Uri? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val exif = ExifInterface(inputStream)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }
        val correctedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        val outputStream = context.openFileOutput("corrected_image.jpg", Context.MODE_PRIVATE)
        correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return Uri.fromFile(File(context.filesDir, "corrected_image.jpg"))
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun resizeAndConvertToByteArray(uri: Uri, context: Context): ByteArray {
        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 600, true)
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun goToAppSettings(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.packageName, null)
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }


    fun drawableToBitmap(context: Context, drawableId: Int): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            // Create a Bitmap from the Drawable
            val bitmap = Bitmap.createBitmap(
                drawable?.intrinsicWidth ?: 0,
                drawable?.intrinsicHeight ?: 0,
                Bitmap.Config.ARGB_8888
            )
            val canvas = android.graphics.Canvas(bitmap)
            drawable?.setBounds(0, 0, canvas.width, canvas.height)
            drawable?.draw(canvas)
            bitmap
        }
    }


    fun checkPermission(requestPermissions: ActivityResultLauncher<Array<String>>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    fun goToSetting(v: View) {
        showCustomToast(v, "Permission denied", false)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", v.context.packageName, null)
        intent.data = uri
        v.context.startActivity(intent)
    }


    /**
     * Calculates the time difference between the given date string and the current date and time.
     *
     * @param dateString The date string in format "yyyy-MM-dd HH:mm:ss"
     * @return A string representing the difference in hours and minutes.
     */
    fun calculateTimeDifference(dateString: String): String {
        // Define a formatter for the space-separated format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parse the date string into a LocalDateTime object
        val apiDate = LocalDateTime.parse(dateString, formatter)

        // Get the current date and time
        val currentDate = LocalDateTime.now()

        // Calculate the total difference in minutes
        val totalMinutesDifference = ChronoUnit.MINUTES.between(apiDate, currentDate)

        // Convert total minutes into hours and minutes
        val hoursDifference = totalMinutesDifference / 60
        val minutesDifference = totalMinutesDifference % 60

        // Return the formatted result
        return "Time difference: $hoursDifference hours and $minutesDifference minutes"
    }

}