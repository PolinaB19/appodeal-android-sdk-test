# Test Assignment: Appodeal Android SDK Integration

## Objective

Create a minimal Android application that integrates the Appodeal SDK and demonstrates all of the following ad formats in Test Mode:

- Banner
- Interstitial
- Rewarded Video
- Native Ads

## Functional requirements

For every format, the application must demonstrate that it:

- is included in SDK initialization;
- loads successfully;
- displays successfully;
- reports the relevant SDK callbacks correctly.

The application must log all callback methods exposed by the SDK interfaces for the four formats, including success, failure, display, click, expiration, close, and reward events where applicable.

The implementation must:

- enable Test Mode before SDK initialization;
- log successful initialization and any initialization errors;
- make load and display operations visible in Logcat;
- check availability before attempting to display an ad;
- keep the Appodeal application key out of committed source and submitted logs;
- use only test creatives during QA.

## QA documentation

Provide:

- a screenshot or screen recording demonstrating Banner;
- a screenshot or screen recording demonstrating Interstitial;
- a screenshot or screen recording demonstrating Rewarded Video;
- a screenshot or screen recording demonstrating Native Ads;
- console logs showing successful SDK initialization;
- console logs showing loading and display callbacks for all four formats;
- a brief explanation of the implementation.

## Troubleshooting report

Describe:

- problems encountered;
- how each problem was investigated;
- which official documentation was used;
- which AI tools helped;
- which AI answers or assumptions were incorrect or incomplete and how they were corrected.

## Acceptance criteria

The assignment is complete when an installable debug APK and its source can be built with documented instructions, all four genuine Appodeal Test Mode formats are visibly demonstrated, successful initialization and runtime callbacks are captured, and any unverified results or remaining uncertainties are explicitly identified rather than inferred.
