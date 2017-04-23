# android-radiobutton-extended

<a href='https://play.google.com/store/apps/details?id=jp.s64.android.radiobuttonextended.example&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height="60" /></a>

An example project / library of custom RadioButton widgets.

<img src="assets/screenshot_1.png" width="250" /> <img src="assets/screenshot_2.png" width="250" />

This is contains below components:

- CompoundFrameLayout
- RadioFrameLayout
- RadioGroupedAdapter

## Usages

Add following lines to your buildscripts.

```groovy
buildscript {
    ext {
        radiobutton_extended_version = '0.1.0'
    }
}
```

```groovy
repositories {
    maven {
        url "http://dl.bintray.com/s64/maven"
    }
}

dependencies {
    compile("jp.s64.android.radiobuttonextended:core:${radiobutton_extended_version}") {
        exclude module: 'support-annotations'
    }
    
    compile("jp.s64.android.radiobuttonextended:recycler:${radiobutton_extended_version}") {
        exclude module: 'support-annotations'
        exclude module: 'recyclerview-v7'
    }
}
```
