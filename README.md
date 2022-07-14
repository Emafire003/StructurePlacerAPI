# Structure Placer API
This simple API provides a simple way to spawn a structure in your world one-time, instead of putting it in the world generation. It could be useful for an item that creates a portal, or someother kind of structure. Or to create LuckyBlocks, who knows! 
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
    modImplementation "maven.modrinth:structureplacerapi:<version>"
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
    modImplementation "maven.modrinth:structureplacerapi:<version>"
    include "maven.modrinth:structureplacerapi:<version>"
}
```

## How to use it
### How to create the structure?
You have to create an NBT structure using minecraft's StructureBlocks, as demonstrated in this video: https://www.youtube.com/watch?v=umhuRXinD3o

### Where to put the file I got?
You will need to place the *structure.nbt* file insde your mod's data folder, like this: `data/yourmodid/structures`

### Creating a new placer object
In order to place the structure, you will need to create a placer object first, this is done simply by creating a new `StructurePlacerAPI` object. It has quite a few parameters to play with:
```java
StructurePlacerAPI(ServerWorld world, Identifier templateName, BlockPos blockPos, BlockMirror mirror, BlockRotation rotation, boolean ignoreEntities, float integrity, BlockPos offset)
```
### The parameters
- The first parameter **`world`** is the wolrd in which we are going to place a structure. You could get it from an entity, or whatever.
- The second parameter **`templateName`** is an Identifier made up by the MOD_ID of your mod and the structure's name
- The third parameter **`blockPos`** is the block position where your structure is going to spawn. You can get it from an entity coordinates for example
- The fourth, **`mirror`** is the first of the optional parameters. With this you can mirror your structure using the `BlockMirror` class. No need to create another object just type BlockMirror.#stuff
- The fifth, **`rotation`** is like the previuous one, and you will be able to rotate your structure using the `BlockRotation` class. No need to create another object just type BlockMirror.#stuff
- The **`ignoreEntities`** parameter if set to true won't spawn entities included inside the structure file. If set to false, it will instead spawn them
- The **`integrity`** parameter is pretty intresting, since it is a float value between 0f and 1f which will deteremine how much the structure will be run-down. If set to a value below 1f some of the blocks will be removed upon generation
- Finally, the **`offset`** parameter is another BlockPos that could be useful to reposition the structure under an entity's feet or something.

### And finally... the hard part.
Now, you have to actually place the structure. Pay attention, it's a very tricky bit. You have just created a `StructurePlacerAPI placer = new StructurePlacerAPI(things up there)`, and to spawn it you will have to do this:
```java
placer.loadStructure();
```
Yeah ok it's not that difficult. You just have to run that tho, not the `.place` method directly, since the load will also check for the existance of said structure before spawning it.

### Example
An example of this could be the one you find insde the [LightWithin](https://github.com/Emafire003/LightWithin) mod(whihc btw, you should check out): 
```java
StructurePlacer placer = new StructurePlacer((ServerWorld) caster.getWorld(), new Identifier(MOD_ID, "frost_light"), caster.getBlockPos(), BlockMirror.NONE, BlockRotation.CLOCKWISE_90, true, 1.0f, new BlockPos(-4, -3, -3));
placer.loadStructure();
```
### Warning!
IMPORTANT! MAKE SURE YOU ARE ON THE SERVER THREAD!

```java
if(!world.isClient){
//run stuff
}
```

## Support me
If you would like to offer me a coffee, here you go.

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/S6S88307C)
Alternativly, you can support me by supporting you using a 25% off on a server for you and your friend using this code down here
[![bisecthosting](https://www.bisecthosting.com/partners/custom-banners/e9c85d2a-cafa-4e2f-98bf-4f62bd9e951c.png)](https://www.bisecthosting.com/LightDev)

## License
This API is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
