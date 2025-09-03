// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.data.gallery.repository

import android.content.ContentValues
import android.net.Uri
import com.meta.levinriegner.mediaview.data.di.IoDispatcher
import com.meta.levinriegner.mediaview.data.gallery.model.MediaFilter
import com.meta.levinriegner.mediaview.data.gallery.model.MediaModel
import com.meta.levinriegner.mediaview.data.gallery.model.MediaSortBy
import com.meta.levinriegner.mediaview.data.gallery.model.StorageType
import com.meta.levinriegner.mediaview.data.gallery.service.DeviceGalleryService
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class GalleryRepository
@Inject
constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val galleryService: DeviceGalleryService,
) {

  suspend fun getMedia(
      filter: MediaFilter,
      sortBy: MediaSortBy,
  ): List<MediaModel> =
      withContext(dispatcher) { galleryService.getMedia(filter, sortBy).map { it.mapToDomain() } }

  suspend fun createMediaFile(
      displayFileName: String?,
      mimeType: String?,
      relativeSubPath: String?,
      storageType: StorageType,
  ): Pair<ContentValues, Uri?> =
      withContext(dispatcher) {
        galleryService.createMediaFile(displayFileName, mimeType, relativeSubPath, storageType)
      }

  suspend fun writeMediaFile(
      uri: Uri,
      onWrite: (FileOutputStream) -> Unit,
  ) = withContext(dispatcher) { galleryService.writeMediaFile(uri, onWrite) }

  suspend fun setMediaFileReady(contentValues: ContentValues, uri: Uri) =
      withContext(dispatcher) { galleryService.setMediaFileReady(contentValues, uri) }

  fun setMediaFileDeleted(uri: Uri) = galleryService.setMediaFileDeleted(uri)

  suspend fun deleteMedia(mediaId: Long) = withContext(dispatcher) {
    // Get the media item directly from the service without filtering
    val mediaItem = galleryService.getMediaById(mediaId)
    
    mediaItem?.let { item ->
      // Delete the media file
      galleryService.setMediaFileDeleted(item.uri)
      Timber.i("Deleted media with ID: $mediaId")
    } ?: run {
      Timber.w("Media item with ID $mediaId not found")
    }
  }

  suspend fun deleteSampleMedia(exceptRelativePath: String? = null) =
      withContext(dispatcher) { galleryService.deleteSampleMedia(exceptRelativePath) }

  suspend fun deleteSampleMediaSubFolder(relativePath: String) =
      withContext(dispatcher) { galleryService.deleteSampleMediaSubFolder(relativePath) }

  // Saves a cropped media file to the device gallery using JPEG format
  suspend fun saveCroppedMediaFile(mediaModel: MediaModel, onWrite: (FileOutputStream) -> Unit) =
      withContext(dispatcher) { galleryService.saveCroppedMediaFile(mediaModel, onWrite) }

  // Check if sample media exists by counting sample media files
  suspend fun hasSampleMedia(): Boolean =
      withContext(dispatcher) {
        val sampleMedia = galleryService.getMedia(MediaFilter.SAMPLE_MEDIA, MediaSortBy.DateDesc)
        sampleMedia.isNotEmpty()
      }
}
