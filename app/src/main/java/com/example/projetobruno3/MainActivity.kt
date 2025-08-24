package com.example.projetobruno3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    
    private lateinit var btnCaptureImage: Button
    private lateinit var btnSelectFromGallery: Button
    private lateinit var btnSpeakText: Button
    private lateinit var tvRecognizedText: TextView
    private lateinit var ivSelectedImage: ImageView
    private lateinit var textToSpeech: TextToSpeech
    private var recognizedText = ""
    
    private val cameraPermissionCode = 100
    private val storagePermissionCode = 101
    
    // Launcher para capturar imagem da câmera
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                // Exibir a imagem no ImageView
                ivSelectedImage.setImageBitmap(it)
                ivSelectedImage.visibility = android.view.View.VISIBLE
                processImageForText(it)
            }
        }
    }
    
    // Launcher para selecionar imagem da galeria
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    // Exibir a imagem no ImageView
                    ivSelectedImage.setImageBitmap(bitmap)
                    ivSelectedImage.visibility = android.view.View.VISIBLE
                    processImageForText(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(this, "Erro ao carregar imagem: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Inicializar views
        btnCaptureImage = findViewById(R.id.btnCaptureImage)
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery)
        btnSpeakText = findViewById(R.id.btnSpeakText)
        tvRecognizedText = findViewById(R.id.tvRecognizedText)
        ivSelectedImage = findViewById(R.id.ivSelectedImage)
        
        // Inicializar Text-to-Speech
        textToSpeech = TextToSpeech(this, this)
        
        // Configurar listeners
        btnCaptureImage.setOnClickListener {
            checkCameraPermissionAndCapture()
        }
        
        btnSelectFromGallery.setOnClickListener {
            checkStoragePermissionAndSelectImage()
        }
        
        btnSpeakText.setOnClickListener {
            speakText()
        }
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    private fun checkCameraPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, 
                arrayOf(Manifest.permission.CAMERA), 
                cameraPermissionCode
            )
        } else {
            captureImage()
        }
    }
    
    private fun captureImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }
    
    private fun checkStoragePermissionAndSelectImage() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            selectImageFromGallery()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                storagePermissionCode
            )
        }
    }
    
    private fun selectImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }
    
    private fun processImageForText(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                recognizedText = visionText.text
                if (recognizedText.isNotEmpty()) {
                    tvRecognizedText.text = recognizedText
                    Toast.makeText(this, "Texto reconhecido com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    tvRecognizedText.text = "Nenhum texto foi encontrado na imagem."
                    Toast.makeText(this, "Nenhum texto encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                tvRecognizedText.text = "Erro ao processar imagem: ${e.message}"
                Toast.makeText(this, "Erro ao processar imagem", Toast.LENGTH_SHORT).show()
            }
    }
    
    private fun speakText() {
        if (recognizedText.isNotEmpty()) {
            textToSpeech.speak(
                recognizedText, 
                TextToSpeech.QUEUE_FLUSH, 
                null, 
                null
            )
        } else {
            Toast.makeText(this, "Nenhum texto para ler", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale("pt", "BR"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Idioma português não suportado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Falha ao inicializar Text-to-Speech", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            cameraPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage()
                } else {
                    Toast.makeText(this, "Permissão da câmera negada", Toast.LENGTH_SHORT).show()
                }
            }
            storagePermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromGallery()
                } else {
                    Toast.makeText(this, "Permissão de armazenamento negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}