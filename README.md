# artifactsizes
Plugin that displays the file size of artifacts in your Gradle project.

## Usage:
```
./gradlew artifactSizes
```

artifactsizes is a parent Gradle task and depends on all generated tasks for each configuration you have in your Gradle project.

For example if you have a configuration called "debugCompileClasspath" then the plugin will create a task called "artifactSizesDebugCompileClasspath"

Example of output:
```
:app:artifactSizesDebugCompileClasspath
980.84 KB   appcompat-v7-26.1.0.aar
621.41 KB   support-compat-26.1.0.aar
304.53 KB   support-media-compat-26.1.0.aar
227.63 KB   support-core-ui-26.1.0.aar
160.75 KB   support-fragment-26.1.0.aar
85.1 KB     support-core-utils-26.1.0.aar
34.33 KB    animated-vector-drawable-26.1.0.aar
30.74 KB    support-vector-drawable-26.1.0.aar
24.13 KB    support-annotations-26.1.0.jar
12.07 KB    common-1.0.0.jar
10.89 KB    common-1.0.0.jar
9.37 KB     runtime-1.0.0.aar
3.01 KB     support-v4-26.1.0.aar
```

## Setup:
First you need to download this repo and publish the artifact to mavenLocal:
```
git clone git@github.com:daverix/artifactsizes.git
cd artifactsizes
./gradlew build publishToMavenLocal
```

Then in your own project, open the build.gradle containing the buildScript closure, add mavenLocal as a repository and add the plugin's classpath:
```
buildscript {
    repositories {
        // ...
        mavenLocal()
    }

    dependencies {
        // ...
        classpath 'net.daverix.artifactsizes:plugin:0.1'
    }
}
```

In your application project, apply the plugin:
```
// other apply plugins here...
apply plugin: 'net.daverix.artifactsizes'
```

## License:
   Copyright 2017 David Laurell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
