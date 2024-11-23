<p align="center">
<a href="https://discord.gg/jEtwguzZ4R"><img alt="" src="https://img.shields.io/badge/Discord-Channel-blue" height="20"/></a>
<a href="https://www.youtube.com/@YourTradeMaster"><img alt="" src="https://img.shields.io/badge/Youtube-Channel-db2e73" height="20"/></a>
<a href=""><img alt="" src="https://img.shields.io/github/commit-activity/t/drkhodakarami/JiEnergy" height="20"/></a>
<a href=""><img alt="" src="https://img.shields.io/github/last-commit/drkhodakarami/JiEnergy" height="20"/></a>
<a href=""><img alt="" src="https://img.shields.io/github/downloads/drkhodakarami/JiEnergy/total" height="20"/></a>
<a href=""><img alt="" src="https://img.shields.io/github/license/drkhodakarami/JiEnergy" height="20"/></a>
<a href=""><img alt="" src="https://img.shields.io/badge/Maintained-YES-31ad31" height="20"/></a>
	</p>

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)](#thelibrary)

## ➤ What is this library?

This is a simple yet useful library that provides wrappers for energy storages. The helper methods will significantly help you when working with
energy API provided by tech reborn development team. Remember that you need to add the dependency for tech reborn energy API in your mod
side by side with this library.

This library is using the energy API provided by tech reborn team. Because of this dependency, you need to add this dependency to your mod side by side
the library.

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)](#dependency)

## ➤ Depending Your Mod

This library is using repsy for maven publishing system. Follow the guideline bellow to make dependency for your mod.

### ➤ Latest Version: 1.1.1+MC_1.21.3

First you need to add the maven to your `build.gradle` repositories:

```Maven Repository
repositories 
{
	mavenCentral()
    maven { url 'https://repo.repsy.io/mvn/jiraiyah/jilibs' }
	maven {
        url "https://cursemaven.com"
        content {
            includeGroup "curse.maven"
        }
    }
}
```

then you need to add the dependency into your dependency section in `build.gradle`

```dependencies
dependencies 
{
    modImplementation include("jiraiyah.energy:jienergy:${jienergy_version}")
	// Energy API Handling Dependency <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    include modApi("teamreborn:energy:${project.energy_version}") {
        exclude(group: "net.fabricmc.fabric-api")
    }
}
```

Finally, we need to add an entry inside `gradle.properties` using the version from the maven repository. The version should look like 
x.x.x+MC_x.x.x for sub versions of Minecraft, and for the main versions of the game it should be like x.x.x+MC_x.x

Look into [MAVEN REPOSITORY](https://repo.repsy.io/mvn/jiraiyah/jilibs/jiraiyah/energy/) for the proper versioning.
Look into [ENERGY API MAVEN REPOSITORY](https://maven.fabricmc.net/teamreborn/energy/) for the proper versioning.

```gradle.properties
jienergy_version=x.x.x+MC_x.x.x

# Dependencies
# Energy API is being used for energy handling in library
energy_version=y.y.y
```

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)](#contributors)

## ➤ Contributors

| [<img alt="Alireza Khodakarami" src="https://avatars.githubusercontent.com/u/77685668?v=4" width="100">](https://www.youtube.com/@YourTradeMaster) |
|:--------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                          [Alireza Khodakarami](https://www.youtube.com/@YourTradeMaster)                                           |
|                                          [khodakarami_dr@outlook.com](mailto:khodakarami_dr@outlook.com)                                           |
|                                                                        🔥🔧                                                                        |

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)](#license)

## ➤ License

Licensed under [MIT](https://opensource.org/licenses/MIT).

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)](#table-of-contents)

## ➤ Table of Contents

* [➤ The Library Info](#-thelibrary)
* [➤ Depending to Library](#-dependency)
* [➤ Contributors](#-contributors)
* [➤ License](#-license)