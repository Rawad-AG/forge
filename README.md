# Forge

Forge is a **modular, plugin-based CLI system for Java development**.
It provides a single execution engine that delegates all functionality to plugins, allowing developers to automate repetitive tasks and extend behavior without bloating the core.

Forge is designed to stay small. All real work is done by plugins.

---

## Getting Started

Forge is distributed as a **single fat JAR**.

### Build from source

```bash
git clone https://github.com/Rawad-AG/forge.git
cd forge
mvn clean package
```

After building, the main engine JAR will be located at: `forge/forge-engine/target/forge-engine.jar`

Place this file somewhere easy to remember, because you will run it frequently:

```bash
java -jar /path/to/forge-engine.jar
```

> Tip:
> Create a shell alias for convenience:
> alias forge='java -jar /path/to/forge-engine.jar'

---

## Plugins

Forge is **entirely plugin-driven**.

- Every command is implemented as a fat JAR plugin
- The engine itself does not contain commands
- The engine discovers plugins and delegates execution to them

By default, Forge looks for plugins in: `~/.forge/plugins/`

Any JAR placed in this directory is treated as a plugin.

In practice, you only need to manually install the Plugins Manager plugin.
All other plugins can then be downloaded and managed through Forge itself.

---

## Available Plugins

### 1. Plugins Manager

A lightweight package manager for Forge plugins.

Responsibilities:

- Download plugins
- Remove plugins
- Manage installed plugin versions
- update installed plugins
- pull literally anything hosted in the public forge repo like libraries, templates, ...etc

This is the first plugin you should install.

---

### 2. Make

A template-based code generator, inspired by Laravel’s artisan make.

Use it to generate:

- Classes
- Services
- Controllers
- Any boilerplate defined by your templates

The plugin focuses on speed and consistency rather than framework-specific assumptions.

---

### 3. Archetype

A Forge-native alternative to Maven archetypes.

Archetype allows you to define reusable templates that can be instantiated using user-provided variables.

Custom Archetype Structure:

~/.forge/archetypes/templates/.test
├── ctx.properties
└── template
└── your-template/

`ctx.properties:`
Defines the variables Forge will prompt the user to enter.

`template/your-template/:`
This directory is copied when the archetype command is executed, with variables resolved.

---

### 4. Requester

A Forge-native offline alternative to Postman.

Requester is a CLI-based HttpClient that enables you to send requests in a similar way to `postman`.

you can even create and manage collections, environments variables, and all of that in easy, configurable way.

---

## Design Philosophy

- Forge is engine + plugins
- Plugins are isolated, versioned, and replaceable
- The engine stays minimal and stable
- Behavior lives entirely in plugins

If a command exists, it exists because a plugin provides it.

---

[Email me](mailto:rawadaboughanem0@gmail.com)
