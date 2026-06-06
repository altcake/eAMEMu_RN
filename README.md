# eAMEMu (React Native)
An Android app that emulates an e-amusement pass using HCE-F (Host Card Emulation Type-F).

## Download

You can download it [here](https://github.com/altcake/eAMEMu_RN/releases/latest).

## Requirements

* Android 7.0+
* NFC
* HCE-F

While most NFC-enabled devices support HCE-F, some devices do not support it despite having NFC hardware. On such devices, a message indicating a lack of HCE-F support will appear when launching the app.

## Notes

For Samsung devices, it is recommended to set the default NFC setting (Settings > Connections > NFC and contactless payments > ⋯ button at the top right > Default NFC method) to "Auto-select" or "Android OS."

Recognition performance may drop significantly when using a case. If the card is not recognized, please remove the case and try again.

## Special Thanks to
* [@dogelition_man](https://github.com/ledoge) (Provided card number conversion code)
* [Juchan Roh](https://github.com/juchan1220) (Created original project)