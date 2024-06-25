# Getting Started

## Adding Source via Maven Dependency.

Add the following snippet to your pom.xml:

### All Modules

```xml

<properties>
    <twitch.java.version>{replace this wit current version number}</twitch.java.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.ryifestudios.twitch</groupId>
        <artifactId>chat</artifactId>
        <version>${twitch.java.version}</version>
    </dependency>
    <dependency>
        <groupId>com.ryifestudios.twitch</groupId>
        <artifactId>authentication</artifactId>
        <version>${twitch.java.version}</version>
    </dependency>
</dependencies>
```

### Only one Module

#### Chat Module

> This module is used to interact with the IRC Server of Twitch

```xml
<dependency>
    <groupId>com.ryifestudios.twitch</groupId>
    <artifactId>chat</artifactId>
    <version>{currentVersion}</version>
</dependency>
```

#### Authentication

> This module is used to get an authToken for your account

```xml
<dependency>
    <groupId>com.ryifestudios.twitch</groupId>
    <artifactId>authentication</artifactId>
    <version>{currentVersion}</version>
</dependency>
```

