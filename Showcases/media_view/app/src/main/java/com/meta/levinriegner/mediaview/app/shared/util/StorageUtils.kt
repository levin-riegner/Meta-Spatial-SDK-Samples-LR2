// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.util

import android.content.Context
import android.os.Environment
import android.os.StatFs
import java.io.File

class StorageUtils(private val context: Context) {
    
    /**
     * Get available storage space in bytes
     */
    fun getAvailableStorageSpace(): Long {
        val externalStorage = Environment.getExternalStorageDirectory()
        val statFs = StatFs(externalStorage.path)
        val availableBlocks = statFs.availableBlocksLong
        val blockSize = statFs.blockSizeLong
        return availableBlocks * blockSize
    }
    
    /**
     * Get total storage space in bytes
     */
    fun getTotalStorageSpace(): Long {
        val externalStorage = Environment.getExternalStorageDirectory()
        val statFs = StatFs(externalStorage.path)
        val totalBlocks = statFs.blockCountLong
        val blockSize = statFs.blockSizeLong
        return totalBlocks * blockSize
    }
    
    /**
     * Format bytes to human readable string (e.g., "1.5 GB")
     */
    fun formatStorageSize(bytes: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var unitIndex = 0
        
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }
        
        return when {
            unitIndex == 0 -> "${size.toLong()} ${units[unitIndex]}"
            size < 10 -> "%.1f %s".format(size, units[unitIndex])
            else -> "%.0f %s".format(size, units[unitIndex])
        }
    }
    
    /**
     * Get available storage space formatted as string
     */
    fun getAvailableStorageSpaceFormatted(): String {
        val availableBytes = getAvailableStorageSpace()
        return formatStorageSize(availableBytes)
    }
    
    /**
     * Check if there's enough storage space for the given size
     */
    fun hasEnoughStorageSpace(requiredBytes: Long): Boolean {
        val availableBytes = getAvailableStorageSpace()
        return availableBytes >= requiredBytes
    }
}
