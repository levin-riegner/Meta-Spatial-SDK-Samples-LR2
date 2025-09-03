// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.model

import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.levinriegner.mediaview.data.gallery.model.MediaModel
import com.meta.levinriegner.mediaview.data.gallery.model.MediaType.IMAGE_2D
import com.meta.levinriegner.mediaview.data.gallery.model.MediaType.IMAGE_360
import com.meta.levinriegner.mediaview.data.gallery.model.MediaType.IMAGE_PANORAMA
import com.meta.levinriegner.mediaview.data.gallery.model.MediaType.VIDEO_2D
import com.meta.levinriegner.mediaview.data.gallery.model.MediaType.VIDEO_360
import com.meta.levinriegner.mediaview.data.gallery.model.MediaType.VIDEO_SPATIAL
import com.meta.spatial.core.Vector3
import com.meta.spatial.runtime.AlphaMode
import com.meta.spatial.runtime.EquirectLayerConfig
import com.meta.spatial.runtime.PanelConfigOptions
import com.meta.spatial.runtime.PanelConfigOptions.Companion.DEFAULT_DPI
import com.meta.spatial.runtime.SceneMaterial
import com.meta.spatial.runtime.SceneMesh
import com.meta.spatial.runtime.SceneTexture
import kotlin.math.min

private const val PIXELS_TO_METERS = 0.0254f / 100f
private const val defaultTextureDp = 1280
private const val maxResolutionInPx = 20 * 1000 * 1000 // 20M, exploratory value

private const val mediaPanelSpacingOffsetDp = 0

// Standardized 360 media dimensions for uniformity
// Based on typical 2D media sizes (1.2f x 0.9f default) and ensuring "head-sized" sphere
private const val STANDARD_360_MINIMIZED_SIZE = 0.7f // Standard size for minimized 360 media
private const val STANDARD_360_MAXIMIZED_RADIUS = 1.6f // Radius large enough to "put your head in it"

private fun normalizeForMaxResolution(width: Int, height: Int): Pair<Int, Int> {
  val currentResolution = width * height
  if (currentResolution > maxResolutionInPx) {
    // Normalize the dimensions to the max resolution
    val reductionRatio = maxResolutionInPx.toFloat() / currentResolution
    val newWidth = width * reductionRatio
    val newHeight = height * reductionRatio
    return Pair(newWidth.toInt(), newHeight.toInt())
  }
  return Pair(width, height)
}

fun MediaModel.calculateDimensionsInMeters(): Pair<Float, Float>? {
  if (width == null || height == null) return null
  val minMetersSize = 1f
  val (width, height) = normalizeForMaxResolution(width, height)
  val widthInMeters = width * PIXELS_TO_METERS
  val heightInMeters = height * PIXELS_TO_METERS
  val offsetInMeters = dpToPx(mediaPanelSpacingOffsetDp) * PIXELS_TO_METERS
  if (widthInMeters >= minMetersSize && heightInMeters >= minMetersSize) {
    return Pair(widthInMeters + offsetInMeters, heightInMeters + offsetInMeters)
  }
  // Normalize the dimensions to the min meters size
  val ratio = widthInMeters / heightInMeters
  val newWidth = if (widthInMeters < minMetersSize) minMetersSize else widthInMeters
  val newHeight = newWidth / ratio
  return Pair(newWidth + offsetInMeters, newHeight + offsetInMeters)
}

fun MediaModel.panelWidthAndHeight(): Pair<Float, Float> {
  return calculateDimensionsInMeters()
      ?: Pair(
          1.2f,
          0.9f,
      )
}

/**
 * Returns standardized dimensions for 360 media to ensure uniformity
 * Uses consistent size regardless of original media dimensions
 */
fun MediaModel.standardized360Dimensions(): Pair<Float, Float> {
  return Pair(STANDARD_360_MINIMIZED_SIZE, STANDARD_360_MINIMIZED_SIZE)
}

fun MediaModel.textureWidthAndHeight(): Pair<Int, Int> {
  if (width != null && height != null) {
    return normalizeForMaxResolution(width, height)
  }
  val (panelWidth, panelHeight) = panelWidthAndHeight()
  return Pair(
      dpToPx(defaultTextureDp) + dpToPx(mediaPanelSpacingOffsetDp),
      dpToPx(
          (defaultTextureDp * (panelHeight / panelWidth)).toInt() +
              dpToPx(mediaPanelSpacingOffsetDp)
      ),
  )
}

fun MediaModel.minimizedPanelConfigOptions(): PanelConfigOptions {
  val (panelWidth, panelHeight) = panelWidthAndHeight()
  val (layoutWidthInPx, layoutHeightInPx) = textureWidthAndHeight()
  return when (mediaType) {
    IMAGE_2D ->
        PanelConfigOptions(
            width = panelWidth,
            height = panelHeight,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            enableLayer = true,
            enableTransparent = false,
            includeGlass = false,
        )

    VIDEO_2D ->
        PanelConfigOptions(
            width = panelWidth,
            height = panelHeight,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            enableLayer = true,
            enableTransparent = false,
            includeGlass = false,
        )

    IMAGE_PANORAMA ->
        PanelConfigOptions(
            width = panelWidth,
            height = panelHeight,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            panelShader = "data/shaders/punch/punch",
            alphaMode = AlphaMode.HOLE_PUNCH,
            includeGlass = false,
            sceneMeshCreator = { texture: SceneTexture ->
              val unlitMaterial =
                  SceneMaterial(texture, AlphaMode.OPAQUE, SceneMaterial.UNLIT_SHADER)
              SceneMesh.singleSidedQuad(panelWidth / 2, panelHeight / 2, unlitMaterial)
            },
        )

    IMAGE_360 ->
        PanelConfigOptions(
            width = STANDARD_360_MINIMIZED_SIZE,
            height = STANDARD_360_MINIMIZED_SIZE,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            layerConfig = EquirectLayerConfig(STANDARD_360_MINIMIZED_SIZE / 2),
            panelShader = "data/shaders/punch/punch",
            alphaMode = AlphaMode.HOLE_PUNCH,
            includeGlass = false,
        )

    VIDEO_360 ->
        PanelConfigOptions(
            width = STANDARD_360_MINIMIZED_SIZE,
            height = STANDARD_360_MINIMIZED_SIZE,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            layerConfig = EquirectLayerConfig(STANDARD_360_MINIMIZED_SIZE / 2),
            panelShader = "data/shaders/punch/punch",
            alphaMode = AlphaMode.HOLE_PUNCH,
            includeGlass = false,
        )

    VIDEO_SPATIAL ->
        PanelConfigOptions(
            width = panelWidth,
            height = panelHeight,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            enableLayer = true,
            enableTransparent = false,
            includeGlass = false,
        )

    null ->
        PanelConfigOptions(
            width = panelWidth,
            height = panelHeight,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            enableLayer = true,
            enableTransparent = false,
            includeGlass = false,
        )
  }
}

fun MediaModel.maximizedPanelConfigOptions(): PanelConfigOptions {
  val (panelWidth, panelHeight) = panelWidthAndHeight()
  val (layoutWidthInPx, layoutHeightInPx) = textureWidthAndHeight()
  return when (mediaType) {
    IMAGE_2D,
    VIDEO_2D,
    VIDEO_SPATIAL,
    null ->
        PanelConfigOptions(
            width = panelWidth * 2,
            height = panelHeight * 2,
            enableLayer = true,
            enableTransparent = false,
            includeGlass = false,
        )

    IMAGE_PANORAMA ->
        PanelConfigOptions(
            fractionOfScreen = 0.1f,
            width = panelWidth * 2,
            height = panelHeight * 2,
            panelShader = "data/shaders/punch/punch",
            alphaMode = AlphaMode.HOLE_PUNCH,
            includeGlass = false,
            sceneMeshCreator = { texture: SceneTexture ->
              val unlitMaterial =
                  SceneMaterial(texture, AlphaMode.OPAQUE, SceneMaterial.UNLIT_SHADER)
              SceneMesh.cylinderSurface(5.0f, 5.0f, 0.7f, unlitMaterial)
            },
        )

    IMAGE_360 ->
        PanelConfigOptions(
            width = STANDARD_360_MINIMIZED_SIZE,
            height = STANDARD_360_MINIMIZED_SIZE,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            layerConfig = EquirectLayerConfig(STANDARD_360_MAXIMIZED_RADIUS),
            panelShader = "data/shaders/punch/punch",
            alphaMode = AlphaMode.HOLE_PUNCH,
            includeGlass = false,
        )

    VIDEO_360 ->
        PanelConfigOptions(
            width = STANDARD_360_MINIMIZED_SIZE,
            height = STANDARD_360_MINIMIZED_SIZE,
            layoutWidthInPx = layoutWidthInPx,
            layoutHeightInPx = layoutHeightInPx,
            layerConfig = EquirectLayerConfig(STANDARD_360_MAXIMIZED_RADIUS),
            panelShader = "data/shaders/punch/punch",
            alphaMode = AlphaMode.HOLE_PUNCH,
            includeGlass = false,
        )
  }
}

fun MediaModel.maximizedBottomCenterPanelVector3(): Vector3 {
  val mediumSpacing = dpToPx(Dimens.medium.value.toInt()) * PIXELS_TO_METERS
  val immersiveMenuHeight = 0.1f
  val (_, panelHeight) = panelWidthAndHeight()
  val immersiveMenuYOffset =
      when (mediaType) {
        IMAGE_2D,
        VIDEO_2D,
        VIDEO_SPATIAL -> -panelHeight / 2 - immersiveMenuHeight / 2 - mediumSpacing

        IMAGE_PANORAMA -> -panelHeight - immersiveMenuHeight / 2 - mediumSpacing
        IMAGE_360,
        VIDEO_360 -> -STANDARD_360_MINIMIZED_SIZE / 2 - immersiveMenuHeight / 2 - mediumSpacing
        null -> -panelHeight
      }
  return Vector3(0.0f, immersiveMenuYOffset, 0.0f)
}

private fun dpToPx(dp: Int): Int {
  return ((dp * DEFAULT_DPI).toFloat() / 160f).toInt()
}
