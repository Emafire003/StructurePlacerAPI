# Potion Recipes API
This simple API provides a simple way to make brewing recipes. If this is all you need, good use this, if you also need to do something else you may look also at nbt-crafting api.
## Setup
Include this library into your `build.gradle` as a dependency
```gradle
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    modImplementation "maven.modrinth:potionrecipes:<version>"
}
```
If you want you can `include `the API in your jar file by adding only the `include` string:
```gradle
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    modImplementation "maven.modrinth:potionrecipes:<version>"
    include "maven.modrinth:potionrecipes:<version>"
}
```
## How to use it

## License
This API is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
