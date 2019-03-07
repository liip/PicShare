# PicShare

PicShare is an android lib that allows you to easily add an improved share functionnality to your app

* Let the user choose a picture from the phone's library
* Optionally allow the user to crop the picture
* Optionally add an inlay on the chosen picture
* Optionally display a preview of the cropped, inlaid picture before sharing

## Installation

In your root **build.gradle** in repositories, add jitpack.io like this:

```gradle
allprojects {
    repositories {
        [...]
        maven { url "https://jitpack.io" }
    }
}
```

*Note that this goes under `allprojects` and NOT under `buildscript`*

In your app **build.gradle**, add a dependency to picshare

```gradle
dependencies {
    [...]
    implementation 'com.github.liip:PicShare:1.0.0'
}
```

## Usage

**Optional:** if you want to add your dynammic custom view on top of the chosen image
```kotlin
// Extend InlayViewProvider. Your xml resource with id 'viewResourceId' will be inflated for you and passed in the populate function 
// In this example, we know that the view contains a TextView called textViewDemo and we want to set the date to it
class InlayCustomProvider(viewResourceId: Int) : InlayViewProvider(viewResourceId) {

        private val date: Date = Date()

        override fun populate(view: View, context Context) {
            view.textViewDemo.text = context.getString(R.string.shared_date, date)
        }
    } 
```

**To run:** simply call the `startSharing` function with the desired options

```kotlin
// Import the library
import ch.liip.picshare.sharing.sharing.options.CropOptions
import ch.liip.picshare.sharing.sharing.options.PreviewOptions
import ch.liip.picshare.sharing.startSharing

// Create options for crop
val cropOptions = CropOptions()
                    .setFixedSize(1024, 1024)

// Create options for inlay. Pass your own layout resource.
// If your layout needs to have some values set dynamically, pass a custom InlayViewProvider subclass (see optional code above).
val inlayOptions = InlayOptions(InlayViewProvider(R.layout.inlay))

// Create options for preview
val previewOptions = PreviewOptions()
                    .setTitle(resources.getString(R.string.picshare_app_title))

// Start the sharing with the given context (Usually 'this' to pass the current Activity context.)
// The image will be resized down to fit in the provided maxSizeX and maxSizeY if needed
// You can pass null to any other option (Crop, Inlay, Preview) to completely disable the corresponding step
startSharing(this, 1024, 1024, "my share panel", "my shared image", cropOptions, inlayOptions, previewOptions)
```

**Color options:** You can impact the rendering by adding a resource file called picshare_colors.xml and setting your own colors for the following names

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="text_color">#ff0000</color>
    <color name="primary_color">#00ff00</color>
    <color name="secondary_color">#0000ff</color>
</resources>
```

## Sample

If you checkout the sources, you can run the sample app to test the library

## Dependencies
This library uses [Yalantis/uCrop](https://github.com/Yalantis/uCrop) for cropping. UCrop is distributed under the Apache2 license

## Future work

The ideas below are only possible improvements without any guarantee to ever be implemented. Feel free to write to show interest or suggest ideas

* Add a custom crop functionnality so PicShare stops depending on an external library
* Allow more flexibility in customizing the views
