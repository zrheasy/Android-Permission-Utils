package com.zrh.permission

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zrh.permission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStorage.setOnClickListener {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        binding.btnMicroPhone.setOnClickListener {
            requestPermission(Manifest.permission.RECORD_AUDIO)
        }
        binding.btnCamera.setOnClickListener {
            requestPermission(Manifest.permission.CAMERA)
        }
        binding.btnLocation.setOnClickListener {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        binding.btnCalendar.setOnClickListener {
            requestPermission(Manifest.permission.WRITE_CALENDAR)
        }
        binding.btnContact.setOnClickListener {
            requestPermission(Manifest.permission.READ_CONTACTS)
        }
        binding.btnSMS.setOnClickListener {
            requestPermission(Manifest.permission.SEND_SMS)
        }
        binding.btnPhone.setOnClickListener {
            requestPermission(Manifest.permission.CALL_PHONE)
        }
    }

    private fun requestPermission(permission: String) {
        PermissionUtils.requestPermissions(this, arrayOf(permission)) { _, granted ->
            val msg = if (granted) "已获取「${permission}」权限" else "未获取「${permission}」权限"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}