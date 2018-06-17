# react-native-fit-text

## Getting started

`$ npm install react-native-fit-text --save`

### Automatic installation

`$ react-native link react-native-fit-text`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.rnfittext.RNFitTextPackage;` to the imports at the top of the file
  - Add `new RNFitTextPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-fit-text'
  	project(':react-native-fit-text').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-fit-text/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-fit-text')
  	```


## Usage
```javascript
import RNFitText from 'react-native-fit-text';


```
  
## Author

* **PrivateOmega** [RNFitText](https://github.com/privateOmega/RNFitText)